package denpamodding.denpamake.model

import denpamodding.denpamake.util.Modifiable
import denpamodding.denpamake.util.NULL_CHAR
import denpamodding.denpamake.util.Property
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DenpaBox(source: ByteBuffer) : Modifiable {
    companion object {
        private const val DATA_HEAD = 0xA7163
        const val DATA_SIZE = 1681
        const val NAME_LENGTH = 12
        const val ROOM_COUNT = 70
        const val ROOM_CAPACITY = 15
    }

    private val buffer: ByteBuffer
    var roomCount: Int
    val roomNames: List<Property<String>>

    override val dirtyProperty: BooleanProperty = SimpleBooleanProperty(false)
    override var isDirty: Boolean
        get() = dirtyProperty.get()
        set(value) = dirtyProperty.set(value)

    init {
        buffer = source.slice(DATA_HEAD, DATA_SIZE)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        buffer.position(0)

        val count = buffer.get().toUByte().toInt()
        roomCount = count.div(2)

        val list = mutableListOf<String>()
        repeat(ROOM_COUNT) {
            val chars = mutableListOf<Char>()
            repeat(NAME_LENGTH) {
                chars += buffer.getChar()
            }

            val string = String(
                chars.takeWhile { it != Char.NULL_CHAR }.toCharArray()
            )
            list += string
        }

        roomNames = list.map {
            val property = Property(it)
            property
        }
    }

    fun updateData() {
        if (isDirty) {
            buffer.position(0)
            val count = roomCount * 2
            buffer.put(count.toByte())

            for (name in roomNames.map { it.value }) {
                for (c in name.padEnd(NAME_LENGTH, Char.NULL_CHAR)) {
                    buffer.putChar(c)
                }
            }

            isDirty = false
        }
    }
}