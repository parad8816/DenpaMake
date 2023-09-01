package tonals.denpamake.util

import javafx.scene.control.TextArea
import javafx.scene.control.TextFormatter

class LimitedTextArea(text: String, private var limit: Int) : TextArea(text) {
    init {
        textFormatter = TextFormatter<String> { change ->
            if (change.controlNewText.length > limit) {
                null
            }
            else {
                change
            }
        }
    }
}