package tonals.denpamake.model

import tonals.denpamake.util.Modifiable
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import java.io.File
import java.nio.ByteBuffer

class DenpaData(val file: File) : Modifiable {
    private val bytes: ByteArray = file.readBytes()
    val memberCollection: DenpaMemberCollection
    val box: DenpaBox

    val isReadOnly
        get() = !file.canWrite()

    override val dirtyProperty: BooleanProperty = SimpleBooleanProperty(false)
    override var isDirty: Boolean
        get() = dirtyProperty.get()
        set(value) = dirtyProperty.set(value)

    init {
        val source = ByteBuffer.wrap(bytes)
        memberCollection = DenpaMemberCollection(source)
        box = DenpaBox(source)

        dirtyProperty.bind(memberCollection.dirtyProperty
            .or(box.dirtyProperty)
        )
    }

    fun updateData(): ByteArray {
        if (isDirty) {
            memberCollection.updateData()
            box.updateData()
        }
        return bytes
    }
}