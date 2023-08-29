package denpamodding.denpamake

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class DenpaMake : Application() {
    companion object {
        val VERSION: Version = Version(1, 0, 0)
        const val GITHUB: String = "https://github.com/parad8816/DenpaMake"
        private val appIcons by lazy { listOf(AppIcon.icon32, AppIcon.icon64) }
    }

    override fun start(primaryStage: Stage) {
        val scene = Scene(HomePane(this, primaryStage), 1030.0, 560.0)
        primaryStage.scene = scene
        primaryStage.show()
        addBaseStyle(scene)
    }

    fun addBaseStyle(scene: Scene) {
        (scene.window as? Stage?)?.icons?.setAll(appIcons)
        scene.stylesheets += "style/main.css"
    }
}

fun main() {
    Application.launch(DenpaMake::class.java)
}