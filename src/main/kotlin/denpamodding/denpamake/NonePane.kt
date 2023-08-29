package denpamodding.denpamake

import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox

class NonePane : BorderPane() {
    init {
        stylesheets += "style/none.css"

        center = VBox().apply {
            styleClass += "body"

            children += Label("データがありません").apply {
                styleClass += "none-text"
            }

            children += Label("上のメニューから新しく作成できます。").apply {
                styleClass += "none-text"
            }
        }
    }
}