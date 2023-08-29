package denpamodding.denpamake

import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox

class NoDataPane : BorderPane() {
    init {
        stylesheets += "style/noData.css"

        center = VBox().apply {
            styleClass += "body"

            children += Label("「ファイル → 開く...」 から").apply {
                styleClass += "no-data-text"
            }

            children += Label("セーブデータを選択してください。").apply {
                styleClass += "no-data-text"
            }
        }
    }
}