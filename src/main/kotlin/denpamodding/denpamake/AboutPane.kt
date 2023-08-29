package denpamodding.denpamake

import javafx.geometry.Orientation
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

class AboutPane(app: DenpaMake) : BorderPane() {
    init {
        stylesheets += "style/about.css"

        left = VBox().apply {
            styleClass += "left-container"

            children += ImageView("image/author.png").apply {
                isPreserveRatio = true
                this.fitWidth = 150.0
            }

            children += Label("作者: Tonal1ty")
        }

        center = VBox().apply {
            styleClass += "body"

            children += Label("DenpaMake").apply {
                styleClass += "text-header"
            }

            children += Label("バージョン: ${DenpaMake.VERSION}")

            children += Separator(Orientation.HORIZONTAL)
            children += Label("DenpaMakeは、「電波人間のRPG FREE!」のセーブデータを編集し、" +
                    "自由に電波人間をカスタマイズできるアプリケーションです。"
            ).apply {
                isWrapText = true
            }

            children += Label("2023/3/28に本ソフトウェアのオンラインサービスが終了し、" +
                        "今までのように楽しめなくなってしまいました。" +
                        "そこで、私が予てから思い描いた、「自分の好きな電波人間ができるツール」を作ってみました。" +
                        "これを使えば、理想の電波人間があなたの手の中に！"
            ).apply {
                isWrapText = true
            }

            children += Label("電波人間のRPGはジニアス・ソノリティ株式会社の登録商標です。"
            ).apply {
                isWrapText = true
            }

            children += Separator(Orientation.HORIZONTAL)
            children += HBox().apply {
                children += Label("- ")

                children += Hyperlink("ソースコード").apply {
                    setOnAction {
                        app.hostServices.showDocument(DenpaMake.GITHUB)
                    }
                }
            }
        }
    }
}