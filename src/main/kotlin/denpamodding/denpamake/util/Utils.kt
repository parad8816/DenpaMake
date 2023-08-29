package denpamodding.denpamake.util

import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox

val Char.Companion.NULL_CHAR: Char
    get() = '\u0000'

fun notImplementedAlert(): Alert {
    return Alert(Alert.AlertType.INFORMATION).apply {
        headerText = "未実装です"
        contentText = "申し訳ありませんが、この機能はまだ実装されていません。\n" +
                "次回バージョンのリリースをお待ちください。"
    }
}

fun exceptionAlert(e: Exception): Alert {
    return Alert(Alert.AlertType.ERROR).apply {
        headerText = "エラーが発生しました。"
        dialogPane.content = VBox().apply {
            children += ScrollPane().apply {
                vbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED
                hbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED
                maxWidth = 400.0
                maxHeight = 300.0

                content = Label().apply {
                    isWrapText = true
                    text = e.toString() + "\n" + e.stackTrace.joinToString("\n")
                }
            }
        }
    }
}
