package denpamodding.denpamake

import denpamodding.denpamake.model.DenpaData
import denpamodding.denpamake.util.exceptionAlert
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.File

class HomePane(private val stage: Stage) : BorderPane() {
    private val title = DenpaMakeTitle()
    private val dataProperty: ObjectProperty<DenpaData?> = SimpleObjectProperty(null)
    private val fileProperty: ObjectProperty<File?> = SimpleObjectProperty(null)

    init {
        updateTitle()
        stylesheets += "style/home.css"

        dataProperty.addListener { _, _, newValue ->
            title.prefix = ""
            updateTitle()

            if (newValue == null) {
                center = Pane()
            }
            else {
                try {
                    val dataPane = DenpaDataPane(newValue)
                    center = dataPane

                    newValue.dirtyProperty.addListener { _, _, dirty ->
                        title.prefix = if (dirty) "*" else ""
                        updateTitle()
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    exceptionAlert(e).showAndWait()
                }
            }
        }

        fileProperty.addListener { _, _, newValue ->
            title.fileName = newValue?.name
            updateTitle()

            if (newValue != null) {
                if (!newValue.canWrite()) {
                    val alert = Alert(Alert.AlertType.WARNING).apply {
                        title = "警告"
                        headerText = "読み取り専用ファイル"

                        dialogPane.content = VBox().apply {
                            children += Label().apply {
                                isWrapText = true
                                text = "このファイルは読み取り専用です。\n" +
                                        "データの保存はできませんのでご注意ください。"
                            }
                        }
                    }
                    alert.showAndWait()
                }
            }
        }

        stage.setOnCloseRequest {
            val data = dataProperty.get()
            if (data != null) {
                discardConfirmationAlert(
                    data,
                    {},
                    { it.consume() }
                )
            }
        }

        top = HBox().apply {
            styleClass += "header"

            children += MenuBar().apply {
                menus += Menu("ファイル").apply {
                    items += MenuItem("開く...").apply {
                        setOnAction {
                            fun chooseFile() {
                                val chooser = FileChooser().apply {
                                    title = "セーブデータを選択"
                                    extensionFilters += ExtensionFilter("セーブデータ (*.bin)", "*.bin")
                                }
                                val file = chooser.showOpenDialog(stage)
                                if (file != null) {
                                    fileProperty.set(file)
                                    loadDataFromFile(file)
                                }
                            }

                            val data = dataProperty.get()
                            if (data == null) {
                                chooseFile()
                            }
                            else {
                                discardConfirmationAlert(
                                    data,
                                    { chooseFile() },
                                    {}
                                )
                            }
                        }
                    }

                    items += MenuItem("閉じる").apply {
                        isDisable = true

                        dataProperty.addListener { _, _, newValue ->
                            isDisable = newValue == null
                        }

                        setOnAction {
                            val data = dataProperty.get()!!
                            discardConfirmationAlert(
                                data,
                                {
                                    dataProperty.set(null)
                                    fileProperty.set(null)
                                },
                                {}
                            )
                        }
                    }

                    items += MenuItem("上書き保存").apply {
                        isDisable = true

                        dataProperty.addListener { _, _, newValue ->
                            isDisable = newValue == null
                        }

                        setOnAction {
                            val data = dataProperty.get()!!
                            if (data.isReadOnly) {
                                readOnlyAlert().showAndWait()
                            }
                            else {
                                try {
                                    val bytes = data.updateData()
                                    val file = fileProperty.get()!!
                                    file.writeBytes(bytes)
                                }
                                catch (e: Exception) {
                                    e.printStackTrace()
                                    exceptionAlert(e).showAndWait()
                                }
                            }
                        }
                    }

                    items += MenuItem("名前を付けて保存...").apply {
                        isDisable = true

                        dataProperty.addListener { _, _, newValue ->
                            isDisable = newValue == null
                        }

                        setOnAction {
                            try {
                                val chooser = FileChooser().apply {
                                    title = "セーブデータを出力"
                                    extensionFilters += ExtensionFilter("セーブデータ (*.bin)", "*.bin")
                                }
                                val file = chooser.showSaveDialog(stage)
                                if (file != null) {
                                    val data = dataProperty.get()!!
                                    val bytes = data.updateData()
                                    file.writeBytes(bytes)
                                    fileProperty.set(file)
                                }
                            }
                            catch (e: Exception) {
                                e.printStackTrace()
                                exceptionAlert(e).showAndWait()
                            }
                        }
                    }

                    items += SeparatorMenuItem()

                    items += MenuItem("終了").apply {
                        setOnAction {
                            val data = dataProperty.get()
                            if (data == null) {
                                stage.close()
                            }
                            else {
                                discardConfirmationAlert(
                                    data,
                                    { stage.close() },
                                    {}
                                )
                            }
                        }
                    }
                }
            }

            children += MenuBar().apply {
                menus += Menu("ヘルプ").apply {
                    items += MenuItem("このアプリについて").apply {
                        setOnAction {
                            showAboutStage()
                        }
                    }
                }
            }
        }
    }

    private fun showAboutStage() {
        val stage = Stage()
        val scene = Scene(AboutPane(stage), 400.0, 300.0)
        scene.stylesheets += "style/main.css"
        stage.scene = scene
        stage.isResizable = false
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.show()
    }

    private fun updateTitle() {
        stage.title = title.toString()
    }

    private fun loadDataFromFile(file: File) {
        try {
            val data = DenpaData(file)
            dataProperty.set(data)
        }
        catch (e: Exception) {
            e.printStackTrace()
            exceptionAlert(e).showAndWait()
        }
    }

    private fun readOnlyAlert(): Alert {
        return Alert(Alert.AlertType.ERROR).apply {
            headerText = "読み取り専用ファイル"
            dialogPane.content = VBox().apply {
                children += Label().apply {
                    isWrapText = true
                    text = "読み取り専用であるため、このファイルにデータを\n" +
                            "上書きすることはできません。"
                }
            }
        }
    }

    private fun discardConfirmationAlert(
        data: DenpaData,
        onProceed: () -> Unit,
        onCancel: () -> Unit
    ) {
        if (data.isDirty) {
            val alert = Alert(Alert.AlertType.WARNING).apply {
                buttonTypes.clear()
                buttonTypes.addAll(
                    ButtonType.YES,
                    ButtonType.NO,
                    ButtonType.CANCEL
                )

                headerText = "データは変更されました"
                contentText = "${data.file.name} への変更を上書き保存しますか?"
            }
            val answer = alert.showAndWait()

            when (answer.get()) {
                ButtonType.YES -> {
                    if (data.isReadOnly) {
                        readOnlyAlert().showAndWait()
                    }
                    else {
                        try {
                            val bytes = data.updateData()
                            val file = fileProperty.get()!!
                            file.writeBytes(bytes)
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                            exceptionAlert(e).showAndWait()
                        }
                    }
                    onProceed()
                }
                ButtonType.NO -> onProceed()
                else -> onCancel()
            }
        }
        else {
            onProceed()
        }
    }

    private class DenpaMakeTitle {
        companion object {
            private const val APP_NAME = "DenpaMake"
        }

        var prefix: String = ""
        var fileName: String? = null

        override fun toString(): String {
            return prefix +
                    APP_NAME +
                    (if (fileName == null) ""
                    else " - [${fileName}]")
        }
    }
}