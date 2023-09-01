package tonals.denpamake.model

enum class DenpaPersonality(val value: Byte, val text: String) {
    KIND(
        (0x0).toByte(),
        "優しい"
    ),
    THOUGHTFUL(
        (0x1).toByte(),
        "思慮深い"
    ),
    NAUGHTY(
        (0x2).toByte(),
        "やんちゃ"
    ),
    CHILD(
        (0x3).toByte(),
        "子ども"
    ),
    LADY(
        (0x4).toByte(),
        "レディ"
    ),
    GENTLEMAN(
        (0x5).toByte(),
        "紳士"
    ),
    ELITE(
        (0x6).toByte(),
        "エリート"
    ),
    MAIN_ACTOR(
        (0x32).toByte(),
        "主人公"
    ),;

    companion object {
        private val map = values().associateBy { it.value }

        fun of(value: Byte): DenpaPersonality {
            return map[value] ?: KIND
        }
    }
}