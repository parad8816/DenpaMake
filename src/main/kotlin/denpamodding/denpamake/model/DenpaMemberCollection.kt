package denpamodding.denpamake.model

import denpamodding.denpamake.util.Modifiable
import denpamodding.denpamake.util.ModificationTracker
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.stream.Stream

class DenpaMemberCollection(source: ByteBuffer) : Modifiable {
    companion object {
        private const val DATA_HEAD = 0x2588
        const val AMOUNT_PARTY = 8
        const val AMOUNT_COLOSSEUM = 8
        const val AMOUNT_BOX = 1050

        fun colosseumId(index: Int): Int {
            return (0xFFB43481).toInt() + index
        }
    }

    private val partyBuffers: Map<UniqueKey<Int, ByteBuffer>, ModificationTracker<DenpaMember?>>
    private val colosseumBuffers: Map<UniqueKey<Int, ByteBuffer>, ModificationTracker<DenpaMember?>>
    private val boxBuffers: Map<UniqueKey<Int, ByteBuffer>, ModificationTracker<DenpaMember?>>

    val partyMembers: List<ModificationTracker<DenpaMember?>>
        get() = partyBuffers.values.toList()

    val colosseumMembers: List<ModificationTracker<DenpaMember?>>
        get() = colosseumBuffers.values.toList()

    val boxMembers: List<ModificationTracker<DenpaMember?>>
        get() = boxBuffers.values.toList()

    private val usedIds: List<Int>
        get() = (partyMembers + boxMembers).mapNotNull { it.value?.id }

    // 新しいユニークなIDを得る
    val newId: Int
        get() = usedIds
            .ifEmpty { listOf(0) }
            .reduce { biggest, next ->
                if (biggest < next) next else biggest
            }.inc()

    override val dirtyProperty: BooleanProperty = SimpleBooleanProperty(false)
    override var isDirty: Boolean
        get() = dirtyProperty.get()
        set(value) = dirtyProperty.set(value)

    init {
        fun sliceData(index: Int): ByteBuffer {
            val size = DenpaMember.DATA_SIZE
            val pos = DATA_HEAD + (size * index)
            val buf = source.slice(pos, size)
            buf.position(0)
            buf.order(ByteOrder.LITTLE_ENDIAN)
            return buf
        }

        var index = 0
        val streamSupplier = {
            Stream.generate {
                val buf = sliceData(index)
                val uniqueKey = UniqueKey(index, buf)
                index++
                uniqueKey
            }
        }

        val makeBufferMap = { size: Long ->
            streamSupplier()
                .limit(size)
                .toList()
                .associateWith {
                    val member = DenpaMemberReader.getMember(it.value)
                    val tracker = ModificationTracker(member)
                    tracker.dirtyProperty.addListener { _, _, newValue ->
                        if (newValue) isDirty = true
                    }
                    tracker
                }
        }

        partyBuffers = makeBufferMap(AMOUNT_PARTY.toLong())
        colosseumBuffers = makeBufferMap(AMOUNT_COLOSSEUM.toLong())
        boxBuffers = makeBufferMap(AMOUNT_BOX.toLong())
    }

    fun updateData() {
        for (pair in partyBuffers + colosseumBuffers + boxBuffers) {
            val buffer = (pair.key).value
            val tracker = pair.value

            if (tracker.isDirty) {
                when (val member = tracker.value) {
                    null -> {
                        buffer.position(0)
                        buffer.put(DenpaMember.NULL_DATA)
                    }
                    else -> {
                        member.updateData(buffer)
                    }
                }

                tracker.isDirty = false
            }
        }

        isDirty = false
    }

    private data class UniqueKey<I, T>(val id: I, val value: T)
}