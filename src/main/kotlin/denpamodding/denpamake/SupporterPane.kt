package denpamodding.denpamake

import denpamodding.denpamake.model.DenpaMember
import denpamodding.denpamake.model.DenpaSupporter
import denpamodding.denpamake.util.Event
import denpamodding.denpamake.util.IntSpinner
import denpamodding.denpamake.util.LimitedTextField
import denpamodding.denpamake.util.ModificationTracker
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.util.Callback

class SupporterPane(private val tracker: ModificationTracker<DenpaMember?>, dataPane: DenpaDataPane) : BorderPane() {
    private val supporter: DenpaSupporter
        get() = tracker.value as DenpaSupporter

    private fun setDirty() {
        tracker.isDirty = true
    }

    private val idChangeEvent = Event<Unit>()

    init {
        stylesheets += "style/supporter.css"
        stylesheets += "style/member.css"

        left = VBox().apply {
            styleClass += "right-border"
            styleClass += "container"

            children += VBox().apply {
                children += Label("名前")
                children += LimitedTextField(supporter.name, DenpaMember.NAME_LENGTH).apply {
                    styleClass += "mid-text-field"
                    isDisable = true

                    idChangeEvent += { text = supporter.name }
                }
            }

            children += VBox().apply {
                children += Label("入手者")
                children += LimitedTextField(supporter.owner, DenpaMember.OWNER_LENGTH).apply {
                    styleClass += "mid-text-field"

                    textProperty().addListener { _, _, newValue ->
                        supporter.owner = newValue
                        setDirty()
                    }

                    focusedProperty().addListener { _, _, newValue ->
                        if (newValue) dataPane.displayTips("入手した際に設定されていた3DSのユーザー名です。")
                        else dataPane.closeTips()
                    }
                }
            }

            children += VBox().apply {
                children += Label("入居日")
                children += DatePicker(supporter.date.toLocalDate()).apply {
                    styleClass += "mid-text-field"

                    valueProperty().addListener { _, _, newValue ->
                        supporter.date = newValue.atStartOfDay()
                        setDirty()
                    }
                }
            }
        }

        right = VBox().apply {
            styleClass += "left-border"
            styleClass += "container"

            children += VBox().apply {
                children += Label("種類")

                children += ComboBox<DenpaSupporter.SupporterType>().apply {
                    styleClass += "mid-combo-box"
                    styleClass += "type-combo-box"
                    isDisable = true

                    items.addAll(
                        DenpaSupporter.SupporterType.DWARF,
                        DenpaSupporter.SupporterType.FAIRY,
                        DenpaSupporter.SupporterType.MONSTER,
                    )

                    val listCellSupplier = {
                        object : ListCell<DenpaSupporter.SupporterType>() {
                            override fun updateItem(item: DenpaSupporter.SupporterType?, empty: Boolean) {
                                super.updateItem(item, empty)
                                this.text = item?.text ?: ""
                            }
                        }
                    }

                    cellFactory = Callback { listCellSupplier() }
                    buttonCell = listCellSupplier()
                    value = supporter.supporterType

                    idChangeEvent += { value = supporter.supporterType }
                }
            }

            children += VBox().apply {
                children += Label("スキル")

                children += ListView<String>().apply {
                    styleClass += "skill-list-view"

                    val applySkillsToView = {
                        val skills = supporter.skills
                        items = FXCollections.observableList(skills)
                    }

                    applySkillsToView()
                    idChangeEvent += { applySkillsToView() }
                }
            }
        }

        center = BorderPane().apply {
            top = VBox().apply {
                styleClass += "container"

                children += HBox().apply {
                    styleClass += "sequence"

                    children += VBox().apply {
                        children += Label("ID")

                        children += IntSpinner(
                            min = Short.MIN_VALUE.toInt(),
                            max = Short.MAX_VALUE.toInt(),
                            initialValue = supporter.supporterId.toInt()
                        ).apply {
                            styleClass += "mid-spinner"
                            isEditable = true

                            valueProperty().addListener { _, _, newValue ->
                                supporter.supporterId = newValue.toShort()
                                idChangeEvent(Unit)
                                setDirty()
                            }

                            focusedProperty().addListener { _, _, newValue ->
                                if (newValue) dataPane.displayTips("各サポーターに対応する値。デフォルトの「カネコ」は使用不可です。")
                                else dataPane.closeTips()
                            }
                        }
                    }
                }

                children += HBox().apply {
                    styleClass += "sequence"

                    children += VBox().apply {
                        children += Label("Lv.")

                        children += IntSpinner(
                            min = Short.MIN_VALUE.toInt(),
                            max = Short.MAX_VALUE.toInt(),
                            initialValue = supporter.level.toInt()
                        ).apply {
                            styleClass += "mid-spinner"
                            isEditable = true

                            valueProperty().addListener { _, _, newValue ->
                                supporter.level = newValue.toShort()
                                setDirty()
                            }

                            focusedProperty().addListener { _, _, newValue ->
                                if (newValue) dataPane.displayTips("注意: レベルが0に設定されたデータは無視されてしまいます。別の値を設定しましょう。")
                                else dataPane.closeTips()
                            }
                        }
                    }
                }
            }
        }
    }
}