package tonals.denpamake.util

import javafx.beans.property.BooleanProperty

interface Modifiable {
    val dirtyProperty: BooleanProperty
    val isDirty: Boolean
}