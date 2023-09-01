package tonals.denpamake.model

enum class DenpaMemberType(val value: Byte) {
    NORMAL(
        (0x04).toByte()
    ),
    INITIAL(
        (0x84).toByte()
    ),
    SUPPORTER(
        (0x94).toByte()
    ),
    BIRTH1(
        (0xA4).toByte()
    ),
    SPECIAL(
        (0xB4).toByte()
    ),
    BIRTH2(
        (0xC4).toByte()
    ),;

    companion object {
        private val map = values().associateBy { it.value }

        fun of(value: Byte): DenpaMemberType? {
            return map[value]
        }
    }
}