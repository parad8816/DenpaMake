package denpamodding.denpamake.util

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty

class Property<T>(initialValue: T) {
    val valueProperty: ObjectProperty<T> = SimpleObjectProperty(initialValue)
    var value: T
        get() = valueProperty.get()
        set(value) = valueProperty.set(value)
}