package denpamodding.denpamake

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class DenpaMake : Application() {
    companion object {
        val VERSION: Version = Version(1, 0, 0)
    }

    override fun start(primaryStage: Stage) {
        val scene = Scene(HomePane(primaryStage), 1030.0, 560.0)
        scene.stylesheets += "style/main.css"
        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main() {
    Application.launch(DenpaMake::class.java)
}