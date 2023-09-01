package tonals.denpamake

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
        addBaseStyle(scene)
        setIcons(primaryStage)
        primaryStage.scene = scene
        primaryStage.show()
    }

    fun setIcons(stage: Stage) {
        stage.icons.setAll(appIcons)
    }

    fun addBaseStyle(scene: Scene) {
        scene.stylesheets += "style/main.css"
    }
}