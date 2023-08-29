package denpamodding.denpamake.util

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty

class ModificationTracker<T>(initialValue: T) : Modifiable {
    var value: T

    override val dirtyProperty: BooleanProperty = SimpleBooleanProperty(false)
    override var isDirty: Boolean
        get() = dirtyProperty.get()
        set(value) = dirtyProperty.set(value)

    val history = mutableMapOf<Any, T>()
    var remarkableValue: Int? = null

    init {
        value = initialValue
    }
}