package tonals.denpamake.util

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Slider
import kotlin.math.roundToInt

class IntSlider(min: Int, max: Int, initialValue: Int) : Slider(min.toDouble(), max.toDouble(), initialValue.toDouble()) {
    val intValueProperty: ObjectProperty<Int> = SimpleObjectProperty(0)

    var intMin: Int
        get() = min.roundToInt()
        set(value) {
            min = value.toDouble()
        }

    var intMax: Int
        get() = max.roundToInt()
        set(value) {
            max = value.toDouble()
        }

    init {
        valueProperty().addListener { _, _, newValue ->
            val newValueInt = newValue.toDouble().roundToInt()
            intValueProperty.set(newValueInt)
            value = newValueInt.toDouble()
        }
    }
}