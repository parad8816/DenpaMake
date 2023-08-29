package denpamodding.denpamake

import denpamodding.denpamake.model.*
import denpamodding.denpamake.util.*
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.util.Callback
import javafx.util.StringConverter
import java.math.BigInteger
import kotlin.math.roundToInt

class DenpaManPane(private val tracker: ModificationTracker<DenpaMember?>, private val dataPane: DenpaDataPane) : BorderPane() {
    companion object {
        private fun enumToCssClass(colorType: DenpaColor): String {
            return when (colorType) {
                DenpaColor.BLACK -> "black"
                DenpaColor.BLACK_DEEP -> "black-deep"
                DenpaColor.BLACK_PALE -> "black-pale"

                DenpaColor.RED -> "red"
                DenpaColor.RED_DEEP -> "red-deep"
                DenpaColor.RED_PALE -> "red-pale"

                DenpaColor.LIGHTBLUE -> "lightblue"
                DenpaColor.LIGHTBLUE_DEEP -> "lightblue-deep"
                DenpaColor.LIGHTBLUE_PALE -> "lightblue-pale"

                DenpaColor.GREEN -> "green"
                DenpaColor.GREEN_DEEP -> "green-deep"
                DenpaColor.GREEN_PALE -> "green-pale"

                DenpaColor.ORANGE -> "orange"
                DenpaColor.ORANGE_DEEP -> "orange-deep"
                DenpaColor.ORANGE_PALE -> "orange-pale"

                DenpaColor.YELLOW -> "yellow"
                DenpaColor.YELLOW_DEEP -> "yellow-deep"
                DenpaColor.YELLOW_PALE -> "yellow-pale"

                DenpaColor.BLUE -> "blue"
                DenpaColor.BLUE_DEEP -> "blue-deep"
                DenpaColor.BLUE_PALE -> "blue-pale"

                DenpaColor.WHITE -> "white"
                DenpaColor.WHITE_DEEP -> "white-deep"
                DenpaColor.WHITE_PALE -> "white-pale"

                DenpaColor.PURPLE -> "purple"
                DenpaColor.PURPLE_DEEP -> "purple-deep"
                DenpaColor.PURPLE_PALE -> "purple-pale"

                DenpaColor.PINK -> "pink"
                DenpaColor.PINK_DEEP -> "pink-deep"
                DenpaColor.PINK_PALE -> "pink-pale"

                DenpaColor.GOLD -> "gold"
                DenpaColor.GOLD_DEEP -> "gold-deep"
                DenpaColor.GOLD_PALE -> "gold-pale"

                DenpaColor.SILVER -> "silver"
                DenpaColor.SILVER_DEEP -> "silver-deep"
                DenpaColor.SILVER_PALE -> "silver-pale"

                else -> "none"
            }
        }
    }

    private val man: DenpaMan
        get() = tracker.value as DenpaMan

    private fun setDirty() {
        tracker.isDirty = true
    }

    init {
        stylesheets += "style/denpaMan.css"
        stylesheets += "style/member.css"

        left = VBox().apply {
            styleClass += "right-border"
            styleClass += "container"

            children += VBox().apply {
                children += Label("名前")
                children += LimitedTextField(man.name, DenpaMember.NAME_LENGTH).apply {
                    styleClass += "mid-text-field"

                    textProperty().addListener { _, _, newValue ->
                        man.name = newValue
                        setDirty()
                    }
                }
            }

            children += VBox().apply {
                children += Label("入手者")
                children += LimitedTextField(man.owner, DenpaMember.OWNER_LENGTH).apply {
                    styleClass += "mid-text-field"

                    textProperty().addListener { _, _, newValue ->
                        man.owner = newValue
                        setDirty()
                    }

                    focusedProperty().addListener { _, _, newValue ->
                        if (newValue) dataPane.displayTips("入手した際に設定されていた3DSのユーザー名です。")
                        else dataPane.closeTips()
                    }
                }
            }

            children += VBox().apply {
                children += Label("メモ")
                children += LimitedTextArea(man.memo, DenpaMember.MEMO_LENGTH).apply {
                    styleClass += "memo-text-area"
                    isWrapText = true

                    textProperty().addListener { _, _, newValue ->
                        man.memo = newValue
                        setDirty()
                    }
                }
            }

            children += VBox().apply {
                children += Label("入居日")
                children += DatePicker(man.date.toLocalDate()).apply {
                    styleClass += "mid-text-field"

                    valueProperty().addListener { _, _, newValue ->
                        man.date = newValue.atStartOfDay()
                        setDirty()
                    }
                }
            }

            children += VBox().apply {
                children += Label("入手経路")
                children += ComboBox<Acquisition>().apply {
                    styleClass += "mid-combo-box"

                    items.addAll(
                        Acquisition.INITIAL,
                        Acquisition.NORMAL,
                        Acquisition.SPECIAL,
                        Acquisition.BIRTH1,
                        Acquisition.BIRTH2,
                        Acquisition.UNKNOWN,
                    )

                    val listCellSupplier = {
                        object : ListCell<Acquisition>() {
                            override fun updateItem(item: Acquisition?, empty: Boolean) {
                                super.updateItem(item, empty)
                                this.text = item?.text ?: ""
                            }
                        }
                    }

                    cellFactory = Callback { listCellSupplier() }
                    buttonCell = listCellSupplier()

                    value = when(man.type) {
                        DenpaMemberType.NORMAL -> Acquisition.NORMAL
                        DenpaMemberType.INITIAL -> Acquisition.INITIAL
                        DenpaMemberType.SPECIAL -> Acquisition.SPECIAL
                        DenpaMemberType.BIRTH1 -> Acquisition.BIRTH1
                        DenpaMemberType.BIRTH2 -> Acquisition.BIRTH2
                        else -> Acquisition.UNKNOWN
                    }

                    valueProperty().addListener { _, _, newValue ->
                        man.type = when(newValue) {
                            Acquisition.NORMAL -> DenpaMemberType.NORMAL
                            Acquisition.INITIAL -> DenpaMemberType.INITIAL
                            Acquisition.SPECIAL -> DenpaMemberType.SPECIAL
                            Acquisition.BIRTH1 -> DenpaMemberType.BIRTH1
                            Acquisition.BIRTH2 -> DenpaMemberType.BIRTH2
                            else -> null
                        }
                        setDirty()
                    }

                    focusedProperty().addListener { _, _, newValue ->
                        if (newValue) dataPane.displayTips("「出生」に対応する2つの値の違いは、まだ分かっていません...")
                        else dataPane.closeTips()
                    }
                }
            }
        }

        right = VBox().apply {
            styleClass += "left-border"
            styleClass += "container"

            children += HBox().apply {
                children += CheckBox("ぜん").apply {
                    isSelected = man.attribute.isVirtue

                    selectedProperty().addListener { _, _, newValue ->
                        man.attribute.isVirtue = newValue
                        setDirty()
                    }
                }
            }

            children += HBox().apply {
                children += CheckBox("あく").apply {
                    isSelected = man.attribute.isVice

                    selectedProperty().addListener { _, _, newValue ->
                        man.attribute.isVice = newValue
                        setDirty()
                    }
                }
            }

            children += HBox().apply {
                children += CheckBox("出生可能").apply {
                    isSelected = man.canBirth

                    selectedProperty().addListener { _, _, newValue ->
                        man.canBirth = newValue
                        setDirty()
                    }
                }
            }

            children += VBox().apply {
                children += Label("成長に必要なバトル数")

                children += IntSpinner(
                    min = 0,
                    max = 0b111111,
                    initialValue = man.fightCountToGrow.toInt()
                ).apply {
                    isEditable = true

                    valueProperty().addListener { _, _, newValue ->
                        if (newValue != null) {
                            man.fightCountToGrow = newValue.toByte()
                            setDirty()
                        }
                    }

                    focusedProperty().addListener { _, _, newValue ->
                        if (newValue) dataPane.displayTips("この値を1以上にすると、自動的に子どもの電波人間になります。")
                        else dataPane.closeTips()
                    }
                }
            }

            children += VBox().apply {
                children += Label("幸福度")

                children += IntSpinner(
                    min = 0,
                    max = 0x0FFF,
                    initialValue = man.happiness.toInt()
                ).apply {
                    isEditable = true

                    valueProperty().addListener { _, _, newValue ->
                        if (newValue != null) {
                            man.happiness = newValue.toShort()
                            setDirty()
                        }
                    }
                }
            }

            children += VBox().apply {
                children += Label("フルーツ残り使用数")

                children += IntSpinner(
                    min = 0,
                    max = 0xFF,
                    initialValue = man.fruitRemain.toUByte().toInt()
                ).apply {
                    isEditable = true

                    valueProperty().addListener { _, _, newValue ->
                        if (newValue != null) {
                            man.fruitRemain = newValue.toByte()
                            setDirty()
                        }
                    }
                }
            }
        }

        center = BorderPane().apply {
            top = VBox().apply {
                children += VBox().apply {
                    styleClass += "bottom-border"
                    styleClass += "container"

                    children += HBox().apply {
                        styleClass += "sequence"

                        children += VBox().apply {
                            children += Label("Lv.")

                            children += IntSpinner(
                                min = Short.MIN_VALUE.toInt(),
                                max = Short.MAX_VALUE.toInt(),
                                initialValue = man.level.toInt()
                            ).apply {
                                styleClass += "mid-spinner"
                                isEditable = true

                                valueProperty().addListener { _, _, newValue ->
                                    man.level = newValue.toShort()
                                    setDirty()
                                }

                                focusedProperty().addListener { _, _, newValue ->
                                    if (newValue) dataPane.displayTips("注意: レベルが0に設定されたデータは無視されてしまいます。別の値を設定しましょう。")
                                    else dataPane.closeTips()
                                }
                            }
                        }

                        children += VBox().apply {
                            children += Label("世代")
                            children += IntSpinner(
                                min = 1,
                                max = 256,
                                initialValue = man.generation.toUByte().toInt() + 1
                            ).apply {
                                styleClass += "short-spinner"
                                isEditable = true

                                valueProperty().addListener { _, _, newValue ->
                                    man.generation = (newValue.toInt() - 1).toByte()
                                    setDirty()
                                }

                                focusedProperty().addListener { _, _, newValue ->
                                    if (newValue) dataPane.displayTips("この値を10以上にしても、最大レベルは9代目の500と同じです。")
                                    else dataPane.closeTips()
                                }
                            }
                        }

                        children += VBox().apply {
                            children += Label("最大世代")
                            children += IntSpinner(
                                min = 1,
                                max = 256,
                                initialValue = man.maxGeneration.toUByte().toInt() + 1
                            ).apply {
                                styleClass += "short-spinner"
                                isEditable = true

                                valueProperty().addListener { _, _, newValue ->
                                    man.maxGeneration = (newValue.toInt() - 1).toByte()
                                    setDirty()
                                }

                                focusedProperty().addListener { _, _, newValue ->
                                    if (newValue) dataPane.displayTips("この値を10以上にしても、精霊のほこらで10代目以上に転生することはできません。")
                                    else dataPane.closeTips()
                                }
                            }
                        }
                    }

                    children += HBox().apply {
                        styleClass += "sequence"
                        children += VBox().apply {
                            children += Label("経験値")
                            children += IntSpinner(
                                min = Int.MIN_VALUE,
                                max = Int.MAX_VALUE,
                                initialValue = man.exp
                            ).apply {
                                isEditable = true

                                valueProperty().addListener { _, _, newValue ->
                                    man.exp = newValue.toInt()
                                    setDirty()
                                }
                            }
                        }
                    }
                }

                children += VBox().apply {
                    styleClass += "container"
                    styleClass += "bottom-border"

                    children += HBox().apply {
                        styleClass += "sequence"

                        children += VBox().apply {
                            children += Label("柄")
                            children += ComboBox<DenpaDesign.PatternType>().apply {
                                items.addAll(DenpaDesign.PatternType.values())

                                val listCellSupplier = {
                                    object : ListCell<DenpaDesign.PatternType>() {
                                        override fun updateItem(item: DenpaDesign.PatternType?, empty: Boolean) {
                                            super.updateItem(item, empty)
                                            this.text = item?.text ?: ""
                                        }
                                    }
                                }

                                cellFactory = Callback { listCellSupplier() }
                                buttonCell = listCellSupplier()
                                value = man.design.pattern

                                valueProperty().addListener { _, _, newValue ->
                                    man.design.pattern = newValue
                                    setDirty()
                                }

                                focusedProperty().addListener { _, _, newValue ->
                                    if (newValue) dataPane.displayTips("命名は https://wikiwiki.jp/youtonfree/ を引用致しました。")
                                    else dataPane.closeTips()
                                }
                            }
                        }

                        children += VBox().apply {
                            children += Label("頭の形")
                            children += ComboBox<DenpaShape>().apply {
                                items.addAll(DenpaShape.values())

                                val listCellSupplier = {
                                    object : ListCell<DenpaShape>() {
                                        override fun updateItem(item: DenpaShape?, empty: Boolean) {
                                            super.updateItem(item, empty)
                                            this.text = item?.text ?: ""
                                        }
                                    }
                                }

                                cellFactory = Callback { listCellSupplier() }
                                buttonCell = listCellSupplier()
                                value = man.shape

                                valueProperty().addListener { _, _, newValue ->
                                    man.shape = newValue
                                    setDirty()
                                }

                                focusedProperty().addListener { _, _, newValue ->
                                    if (newValue) dataPane.displayTips("命名は https://wikiwiki.jp/youtonfree/ を引用致しました。")
                                    else dataPane.closeTips()
                                }
                            }
                        }

                        children += VBox().apply {
                            children += Label("体型")
                            children += openButton("体型の設定").apply {
                                setOnAction {
                                    val bodyStage = Stage()
                                    val bodyPane = VBox().apply {
                                        stylesheets += "style/bodySettings.css"
                                        styleClass += "body"

                                        children += VBox().apply {
                                            children += Label("身長")
                                            children += IntSlider(0, 4, man.body.height).apply {
                                                isShowTickLabels = true
                                                isShowTickMarks = true
                                                blockIncrement = 1.0
                                                majorTickUnit = 1.0
                                                minorTickCount = 0

                                                labelFormatter = object : StringConverter<Double>() {
                                                    override fun toString(`object`: Double): String {
                                                        return when (`object`.roundToInt()) {
                                                            intMin -> "高"
                                                            intMax -> "低"
                                                            else -> ""
                                                        }
                                                    }

                                                    override fun fromString(string: String): Double {
                                                        return 0.0
                                                    }
                                                }

                                                intValueProperty.addListener { _, _, newValue ->
                                                    man.body.height = newValue
                                                    setDirty()
                                                }
                                            }
                                        }

                                        children += VBox().apply {
                                            children += Label("太さ")
                                            children += IntSlider(0, 4, man.body.waist).apply {
                                                isShowTickLabels = true
                                                isShowTickMarks = true
                                                blockIncrement = 1.0
                                                majorTickUnit = 1.0
                                                minorTickCount = 0

                                                labelFormatter = object : StringConverter<Double>() {
                                                    override fun toString(`object`: Double): String {
                                                        return when (`object`.roundToInt()) {
                                                            intMin -> "細"
                                                            intMax -> "太"
                                                            else -> ""
                                                        }
                                                    }

                                                    override fun fromString(string: String): Double {
                                                        return 0.0
                                                    }
                                                }

                                                intValueProperty.addListener { _, _, newValue ->
                                                    man.body.waist = newValue
                                                    setDirty()
                                                }
                                            }
                                        }
                                    }

                                    bodyStage.title = "体型の設定"
                                    bodyStage.scene = Scene(bodyPane, 300.0, 200.0)
                                    bodyStage.initModality(Modality.APPLICATION_MODAL)
                                    bodyStage.isResizable = false
                                    bodyStage.show()
                                }
                            }
                        }
                    }
                }
            }

            center = BorderPane().apply {
                left = HBox().apply {
                    children += HBox().apply {
                        styleClass += "container"
                        styleClass += "right-border"

                        children += VBox().apply {
                            this.stylesheets += "style/color.css"
                            styleClass += "sequence"

                            fun colorComboBox(modifier: Map<DenpaColor, (DenpaColor) -> Node> = mapOf()): ComboBox<DenpaColor> {
                                return ComboBox<DenpaColor>().apply {
                                    items.addAll(DenpaColor.values())

                                    val listCellSupplier = {
                                        object : ListCell<DenpaColor>() {
                                            override fun updateItem(item: DenpaColor?, empty: Boolean) {
                                                super.updateItem(item, empty)

                                                if (!empty && item != null) {
                                                    graphic = if (modifier.containsKey(item)) {
                                                        val nodeSupplier = modifier[item]!!
                                                        nodeSupplier(item)
                                                    }
                                                    else {
                                                        val string = item.text
                                                        HBox().apply {
                                                            styleClass += "color-list-cell-container"

                                                            children += Rectangle(10.0, 10.0).apply {
                                                                styleClass += "color-sample"
                                                                styleClass += enumToCssClass(item)
                                                            }

                                                            children += Label(string)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    cellFactory = Callback { listCellSupplier() }
                                    buttonCell = listCellSupplier()
                                }
                            }

                            children += VBox().apply {
                                children += Label("第一色")
                                children += colorComboBox().apply {
                                    value = man.design.primaryColor

                                    valueProperty().addListener { _, _, newValue ->
                                        man.design.primaryColor = newValue
                                        setDirty()
                                    }
                                }
                            }

                            children += VBox().apply {
                                children += Label("第二色")
                                children += colorComboBox(
                                    mapOf(
                                        DenpaColor.NONE_PALE to { _ ->
                                            HBox().apply {
                                                styleClass += "color-list-cell-container"

                                                children += Rectangle(10.0, 10.0).apply {
                                                    styleClass += "color-sample"
                                                    styleClass += "aura"
                                                }

                                                children += Label("オーラ")
                                            }
                                        }
                                    )
                                ).apply {
                                    value = man.design.secondaryColor

                                    valueProperty().addListener { _, _, newValue ->
                                        man.design.secondaryColor = newValue
                                        setDirty()
                                    }

                                    focusedProperty().addListener { _, _, newValue ->
                                        if (newValue) dataPane.displayTips("注意: オーラを纏うには柄が「単色」である必要があります。")
                                        else dataPane.closeTips()
                                    }
                                }
                            }

                            children += VBox().apply {
                                children += Label("ペイントの色")
                                children += colorComboBox().apply {
                                    items.removeAll(
                                        DenpaColor.NONE_DEEP,
                                        DenpaColor.NONE_PALE,
                                    )
                                    value = man.design.paintColor

                                    valueProperty().addListener { _, _, newValue ->
                                        man.design.paintColor = newValue
                                        setDirty()
                                    }
                                }
                            }
                        }
                    }

                    children += HBox().apply {
                        styleClass += "container"
                        styleClass += "right-border"

                        children += VBox().apply {
                            styleClass += "sequence"

                            children += VBox().apply {
                                children += Label("アンテナ")
                                children += openButton("アンテナ設定").apply {
                                    setOnAction {
                                        notImplementedAlert().showAndWait()
                                    }
                                }
                            }

                            children += VBox().apply {
                                children += Label("顔")
                                children += openButton("顔の設定").apply {
                                    setOnAction {
                                        notImplementedAlert().showAndWait()
                                    }
                                }
                            }

                            children += VBox().apply {
                                children += Label("そうび")
                                children += openButton("そうびの設定").apply {
                                    setOnAction {
                                        notImplementedAlert().showAndWait()
                                    }
                                }
                            }
                        }
                    }

                    children += HBox().apply {
                        styleClass += "container"

                        children += VBox().apply {
                            styleClass += "sequence"

                            children += VBox().apply {
                                children += Label("性格")
                                children += ComboBox<DenpaPersonality>().apply {
                                    items.addAll(DenpaPersonality.values())

                                    val listCellSupplier = {
                                        object : ListCell<DenpaPersonality>() {
                                            override fun updateItem(item: DenpaPersonality?, empty: Boolean) {
                                                super.updateItem(item, empty)

                                                if (!empty && item != null) {
                                                    this.text = item.text
                                                }
                                            }
                                        }
                                    }

                                    cellFactory = Callback { listCellSupplier() }
                                    buttonCell = listCellSupplier()
                                    value = man.personality

                                    valueProperty().addListener { _, _, newValue ->
                                        man.personality = newValue
                                        setDirty()
                                    }

                                    focusedProperty().addListener { _, _, newValue ->
                                        if (newValue) dataPane.displayTips("「主人公」に設定すると、「さよなら」ができなくなります。")
                                        else dataPane.closeTips()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        bottom = VBox().apply {
            styleClass += "fruit-grid-container"

            val fruitGrid = GridPane().apply {
                styleClass += "fruit-grid"
            }

            val fruitEffectSpinner = { initialValue: Int, setter: (BigInteger) -> Unit ->
                IntSpinner(0, 0xFF, initialValue).apply {
                    styleClass += "short-spinner"
                    isEditable = true

                    valueProperty().addListener { _, _, newValue ->
                        setter(newValue)
                        setDirty()
                    }
                }
            }

            fruitGrid.add(Label("ドーピング"), 0, 1)
            fruitGrid.add(Label("特殊ドピ"), 0, 2)

            fruitGrid.add(Label("HP"), 1, 0)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[0].hp.toUByte().toInt()) { v -> man.fruitEffects[0].hp = v.toByte() }, 1, 1)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[1].hp.toUByte().toInt()) { v -> man.fruitEffects[1].hp = v.toByte() }, 1, 2)

            fruitGrid.add(Label("AP"), 2, 0)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[0].ap.toUByte().toInt()) { v -> man.fruitEffects[0].ap = v.toByte() }, 2, 1)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[1].ap.toUByte().toInt()) { v -> man.fruitEffects[1].ap = v.toByte() }, 2, 2)

            fruitGrid.add(Label("こうげき"), 3, 0)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[0].attack.toUByte().toInt()) { v -> man.fruitEffects[0].attack = v.toByte() }, 3, 1)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[1].attack.toUByte().toInt()) { v -> man.fruitEffects[1].attack = v.toByte() }, 3, 2)

            fruitGrid.add(Label("ぼうぎょ"), 4, 0)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[0].defense.toUByte().toInt()) { v -> man.fruitEffects[0].defense = v.toByte() }, 4, 1)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[1].defense.toUByte().toInt()) { v -> man.fruitEffects[1].defense = v.toByte() }, 4, 2)

            fruitGrid.add(Label("すばやさ"), 5, 0)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[0].speed.toUByte().toInt()) { v -> man.fruitEffects[0].speed = v.toByte() }, 5, 1)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[1].speed.toUByte().toInt()){ v -> man.fruitEffects[1].speed = v.toByte() }, 5, 2)

            fruitGrid.add(Label("かいひ"), 6, 0)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[0].evade.toUByte().toInt()){ v -> man.fruitEffects[0].evade = v.toByte() }, 6, 1)
            fruitGrid.add(fruitEffectSpinner(man.fruitEffects[1].evade.toUByte().toInt()){ v -> man.fruitEffects[1].evade = v.toByte() }, 6, 2)

            children += fruitGrid
        }
    }

    private fun openButton(text: String): Button {
        return Button().apply {
            styleClass += "open-button"

            graphic = BorderPane().apply {
                left = VBox().apply {
                    styleClass += "open-button-text-container"
                    children += Label(text)
                }
                right = VBox().apply {
                    styleClass += "open-button-image-container"
                    children += ImageView("image/open-in-new.png").apply {
                        isPreserveRatio = true
                        this.fitHeight = 15.0
                    }
                }
            }
        }
    }

    private enum class Acquisition(val text: String) {
        UNKNOWN("不明"),
        INITIAL("最初から持っている"),
        NORMAL("ノーマルキャッチ"),
        SPECIAL("特別なキャッチ"),
        BIRTH1("出生1"),
        BIRTH2("出生2"),
    }
}