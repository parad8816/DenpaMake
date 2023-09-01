package tonals.denpamake.model

import java.nio.ByteBuffer

object DenpaMemberReader {
    fun getMember(buffer: ByteBuffer): DenpaMember? {
        val id = buffer.getInt()

        // データなし
        if (id == 0) {
            return null
        }

        buffer.getShort()

        val type = buffer.get()
        buffer.get()

        // サポーターかどうか判定
        return when(type) {
            DenpaMemberType.SUPPORTER.value -> DenpaSupporter(buffer, id)
            else -> DenpaMan(buffer, id, DenpaMemberType.of(type))
        }
    }
}