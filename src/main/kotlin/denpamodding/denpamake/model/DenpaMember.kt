package denpamodding.denpamake.model

import java.nio.ByteBuffer
import java.time.LocalDateTime

abstract class DenpaMember {
    companion object {
        const val NAME_LENGTH = 12
        const val MEMO_LENGTH = 16
        const val OWNER_LENGTH = 10

        const val DATA_SIZE: Int = 252
        val DATE_TIME_FROM: LocalDateTime = LocalDateTime.of(1999, 1, 1, 0, 0, 0, 0)
        val NULL_DATA = ByteArray(DATA_SIZE) { 0 }
    }

    abstract val id: Int
    abstract val type: DenpaMemberType?
    abstract val date: LocalDateTime
    abstract val name: String
    abstract val memo: String
    abstract val owner: String
    abstract var level: Short

    abstract fun updateData(buffer: ByteBuffer)
}