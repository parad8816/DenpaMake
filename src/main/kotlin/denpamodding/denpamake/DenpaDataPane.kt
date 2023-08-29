package denpamodding.denpamake

import denpamodding.denpamake.model.*
import denpamodding.denpamake.util.Event
import denpamodding.denpamake.util.IntSpinner
import denpamodding.denpamake.util.LimitedTextField
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.util.Callback

class DenpaDataPane(data: DenpaData) : BorderPane() {
    private val dirtyEvent = Event<Boolean>()

    private val segmentProperty: ObjectProperty<MemberSegment> = SimpleObjectProperty(MemberSegment.PARTY)
    private var members = data.memberCollection.partyMembers
    private var index: Int = 0

    private val memberUpdateEvent = Event<Unit>()
    private val memberChangeEvent = Event<Unit>()
    private val boxBoundReviewEvent = Event<Unit>()

    private val displayTipsEvent = Event<String>()

    fun displayTips(text: String) {
        displayTipsEvent(text)
    }

    fun closeTips() {
        displayTipsEvent("")
    }

    init {
        stylesheets += "style/denpaData.css"

        data.dirtyProperty.addListener { _, _, newValue ->
            dirtyEvent(newValue)
        }

        data.dirtyProperty.addListener { _, _, newValue ->
            if (newValue) dirtyEvent(true)
        }

        top = BorderPane().apply {
            styleClass += "header-data"

            left = HBox().apply {
                styleClass += "header-segment-container"

                children += ComboBox<MemberSegment>().apply {
                    items.addAll(
                        MemberSegment.PARTY,
                        MemberSegment.COLOSSEUM,
                        MemberSegment.BOX
                    )

                    val listCellSupplier = {
                        object : ListCell<MemberSegment>() {
                            override fun updateItem(item: MemberSegment?, empty: Boolean) {
                                super.updateItem(item, empty)
                                this.text = item?.text ?: ""
                            }
                        }
                    }

                    cellFactory = Callback { listCellSupplier() }
                    buttonCell = listCellSupplier()
                    value = MemberSegment.PARTY

                    valueProperty().addListener { _, _, newValue ->
                        if (newValue != null) {
                            members = when(newValue) {
                                MemberSegment.PARTY -> data.memberCollection.partyMembers
                                MemberSegment.COLOSSEUM -> data.memberCollection.colosseumMembers
                                MemberSegment.BOX -> data.memberCollection.boxMembers
                            }

                            segmentProperty.set(newValue)
                        }
                    }
                }
            }

            center = HBox().apply {
                styleClass += "header-category-container"

                children += ComboBox<MemberCategory>().apply {
                    var isMemberChange = false

                    items.addAll(
                        MemberCategory.DENPA_MAN,
                        MemberCategory.SUPPORTER,
                        MemberCategory.NONE
                    )

                    val listCellSupplier = {
                        object : ListCell<MemberCategory>() {
                            override fun updateItem(item: MemberCategory?, empty: Boolean) {
                                super.updateItem(item, empty)
                                this.text = item?.text ?: ""
                            }
                        }
                    }

                    cellFactory = Callback { listCellSupplier() }
                    buttonCell = listCellSupplier()
                    value = MemberCategory.DENPA_MAN

                    valueProperty().addListener { _, oldValue, newValue ->
                        if (!isMemberChange) {
                            val member = members[index]
                            val oldMember = member.value
                            member.history[oldValue] = oldMember
                            if (oldMember != null) member.remarkableValue = oldMember.id

                            // 履歴に残ったデータがあれば読み込み、なければ新しく作る
                            member.value = if (member.history.containsKey(newValue)) {
                                member.history[newValue]
                            }
                            else {
                                // IDをコピー
                                val remarkableValue = member.remarkableValue
                                val id = if (remarkableValue != null) {
                                    remarkableValue
                                }
                                else {
                                    val segment = segmentProperty.get()
                                    when (segment) {
                                        MemberSegment.COLOSSEUM -> DenpaMemberCollection.colosseumId(index)
                                        else -> data.memberCollection.newId
                                    }
                                }
                                println(id)

                                when (newValue) {
                                    MemberCategory.DENPA_MAN -> DenpaMan(id, DenpaMemberType.NORMAL)
                                    MemberCategory.SUPPORTER -> DenpaSupporter(id)
                                    else -> null
                                }
                            }

                            member.isDirty = true
                            memberUpdateEvent(Unit)
                        }
                    }

                    memberChangeEvent += {
                        val member = members[index]

                        val newValue = when (member.value) {
                            is DenpaMan -> MemberCategory.DENPA_MAN
                            is DenpaSupporter -> MemberCategory.SUPPORTER
                            else -> MemberCategory.NONE
                        }

                        isMemberChange = true
                        value = newValue
                        isMemberChange = false
                    }
                }
            }

            right = HBox().apply {
                styleClass += "index-container"

                children += IntSpinner(1, members.size, 1).apply {
                    styleClass += "short-spinner"
                    isEditable = true

                    valueProperty().addListener { _, _, newValue ->
                        index = newValue.toInt() - 1
                        memberChangeEvent(Unit)
                    }

                    memberChangeEvent += {
                        editor.text = (index + 1).toString()
                    }

                    segmentProperty.addListener { _, _, _ ->
                        max = members.size
                    }
                }

                children += Label("人目")
            }
        }

        left = VBox().apply {
            styleClass += "left-button-container"

            children += Button("<").apply {
                styleClass += "move-button"

                setOnAction {
                    val next = index - 1
                    if (next in members.indices) {
                        index = next
                        memberChangeEvent(Unit)
                    }
                }

                memberChangeEvent += {
                    val next = index - 1
                    isDisable = next !in members.indices
                }
            }
        }

        right = BorderPane().apply {
            left = VBox().apply {
                styleClass += "box-list-view-container"
                val roomCountChangeEvent = Event<Unit>()

                children += VBox().apply {
                    children += Label("ボックス (クリックしてジャンプ)")

                    children += ListView<Int>().apply {
                        styleClass += "box-list-view"
                        isDisable = true

                        val observableListSupplier = {
                            val range = 0 until data.box.roomCount
                            FXCollections.observableList(range.toList())
                        }

                        val applyNamesToView = {
                            items = observableListSupplier()
                        }

                        applyNamesToView()
                        cellFactory = Callback {
                            object : ListCell<Int>() {
                                override fun updateItem(item: Int?, empty: Boolean) {
                                    super.updateItem(item, empty)

                                    if (!empty && item != null) {
                                        val roomNameProp = data.box.roomNames[item]
                                        val roomNameChangeEvent = Event<String>()

                                        graphic = BorderPane().apply {
                                            left = HBox().apply {
                                                styleClass += "box-list-cell-name-container"
                                                children += Label(roomNameProp.value).apply {
                                                    roomNameChangeEvent += {
                                                        this.text = it
                                                        roomNameProp.value = it
                                                        data.box.isDirty = true
                                                    }
                                                }
                                            }

                                            right = HBox().apply {
                                                children += Button().apply {
                                                    styleClass += "box-list-cell-button"

                                                    setOnAction {
                                                        val renameStage = Stage()
                                                        val renamePane = VBox().apply {
                                                            stylesheets += "style/rename.css"
                                                            styleClass += "body"

                                                            children += Label("部屋の名前：")
                                                            children += LimitedTextField(roomNameProp.value, 12).apply {
                                                                setOnAction {
                                                                    val newText = this.text
                                                                    roomNameProp.value = newText
                                                                    roomNameChangeEvent(newText)
                                                                    renameStage.close()
                                                                }
                                                            }
                                                        }

                                                        renameStage.title = "部屋の名前を変更"
                                                        renameStage.scene = Scene(renamePane, 300.0, 100.0)
                                                        renameStage.initModality(Modality.APPLICATION_MODAL)
                                                        renameStage.isResizable = false
                                                        renameStage.show()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        graphic = null
                                    }
                                }
                            }
                        }

                        roomCountChangeEvent += {
                            boxBoundReviewEvent(Unit)
                            selectionModel.clearSelection()
                            applyNamesToView()
                        }

                        selectionModel.selectedItemProperty().addListener { _, _, newValue ->
                            if (newValue != null) {
                                val newIndex = DenpaBox.ROOM_CAPACITY * newValue
                                index = newIndex
                                memberChangeEvent(Unit)
                            }
                        }

                        segmentProperty.addListener { _, _, newValue ->
                            isDisable = newValue != MemberSegment.BOX
                            selectionModel.clearSelection()
                        }
                    }
                }

                children += VBox().apply {
                    children += Label("部屋の数")

                    children += IntSpinner(0, DenpaBox.ROOM_COUNT, data.box.roomCount).apply {
                        styleClass += "box-list-spinner"
                        isEditable = true
                        isDisable = true

                        valueProperty().addListener { _, _, newValue ->
                            data.box.isDirty = true
                            data.box.roomCount = newValue.toInt()
                            roomCountChangeEvent(Unit)
                        }

                        segmentProperty.addListener { _, _, newValue ->
                            isDisable = newValue != MemberSegment.BOX
                        }

                        focusedProperty().addListener { _, _, newValue ->
                            if (newValue) displayTips("電波人間ボックスにおける部屋数。最大70です。")
                        }
                    }
                }
            }

            right = VBox().apply {
                styleClass += "right-button-container"

                children += Button(">").apply {
                    styleClass += "move-button"

                    setOnAction {
                        val next = index + 1
                        if (next in members.indices) {
                            index = next
                            memberChangeEvent(Unit)
                        }
                    }

                    memberChangeEvent += {
                        val next = index + 1
                        isDisable = next !in members.indices
                    }
                }
            }
        }

        bottom = BorderPane().apply {
            styleClass += "footer"

            left = HBox().apply {
                styleClass += "footer-location-container"

                val locationLabel = Label("")
                val warningLabel = Label("※ボックス外※ このデータは無視されます。").apply {
                    styleClass += "footer-text-warn"
                    isVisible = false
                }

                var isWorking = false

                segmentProperty.addListener { _, _, newValue ->
                    isWorking = newValue == MemberSegment.BOX
                }

                memberChangeEvent += {
                    if (isWorking) {
                        boxBoundReviewEvent(Unit)
                    }
                    else {
                        locationLabel.text = ""
                        warningLabel.isVisible = false
                    }
                }

                boxBoundReviewEvent += {
                    val roomIndex = index.div(DenpaBox.ROOM_CAPACITY)

                    if (roomIndex in 0 until data.box.roomCount) {
                        val order = index.mod(DenpaBox.ROOM_CAPACITY) + 1
                        locationLabel.text = "@${data.box.roomNames[roomIndex].value} → ${order}人目"
                        warningLabel.isVisible = false
                    }
                    else {
                        locationLabel.text = ""
                        warningLabel.isVisible = true
                    }
                }

                children.addAll(locationLabel, warningLabel)
            }

            right = HBox().apply {
                styleClass += "footer-tips-container"

                children += Label("").apply {
                    displayTipsEvent += {
                        text = it
                    }
                }
            }
        }

        val changeMainPane: () -> Unit = {
            center = if (index in members.indices) {
                val member = members[index]

                when (member.value) {
                    is DenpaMan -> DenpaManPane(member, this)
                    is DenpaSupporter -> SupporterPane(member, this)
                    else -> NonePane()
                }
            }
            else {
                null
            }
        }

        segmentProperty.addListener { _, _, _ ->
            index = 0
            memberChangeEvent(Unit)
        }

        memberUpdateEvent += { changeMainPane() }
        memberChangeEvent += { changeMainPane() }

        memberChangeEvent(Unit)
    }

    private enum class MemberSegment(val text: String) {
        PARTY("パーティー"),
        COLOSSEUM("コロシアム"),
        BOX("電波人間ボックス"),
    }

    private enum class MemberCategory(val text: String) {
        NONE("なし"),
        DENPA_MAN("電波人間"),
        SUPPORTER("サポーター"),
    }
}