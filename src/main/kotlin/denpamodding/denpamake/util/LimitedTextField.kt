package denpamodding.denpamake.util

import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter

class LimitedTextField(text: String, private var limit: Int) : TextField(text) {
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