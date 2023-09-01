package tonals.denpamake.model

enum class DenpaColor(val value: Byte, val text: String, private val isNone: Boolean = false) {
    NONE(
        (0x00).toByte(),
        "なし",
        isNone = true
    ),
    NONE_DEEP(
        (0x01).toByte(),
        "なし",
        isNone = true
    ),
    NONE_PALE(
        (0x02).toByte(),
        "なし",
        isNone = true
    ),
    BLACK(
        (0x03).toByte(),
        "くろ"
    ),
    BLACK_DEEP(
        (0x04).toByte(),
        "こいくろ"
    ),
    BLACK_PALE(
        (0x05).toByte(),
        "うすくろ"
    ),
    RED(
        (0x06).toByte(),
        "あか"
    ),
    RED_DEEP(
        (0x07).toByte(),
        "こいあか"
    ),
    RED_PALE(
        (0x08).toByte(),
        "うすあか"
    ),
    LIGHTBLUE(
        (0x09).toByte(),
        "みずいろ"
    ),
    LIGHTBLUE_DEEP(
        (0x0A).toByte(),
        "こいみずいろ"
    ),
    LIGHTBLUE_PALE(
        (0x0B).toByte(),
        "うすみずいろ"
    ),
    GREEN(
        (0x0C).toByte(),
        "みどり"
    ),
    GREEN_DEEP(
        (0x0D).toByte(),
        "こいみどり"
    ),
    GREEN_PALE(
        (0x0E).toByte(),
        "うすみどり"
    ),
    ORANGE(
        (0x0F).toByte(),
        "だいだい"
    ),
    ORANGE_DEEP(
        (0x10).toByte(),
        "こいだいだい"
    ),
    ORANGE_PALE(
        (0x11).toByte(),
        "うすだいだい"
    ),
    YELLOW(
        (0x12).toByte(),
        "きいろ"
    ),
    YELLOW_DEEP(
        (0x13).toByte(),
        "こいきいろ"
    ),
    YELLOW_PALE(
        (0x14).toByte(),
        "うすきいろ"
    ),
    BLUE(
        (0x15).toByte(),
        "あお"
    ),
    BLUE_DEEP(
        (0x16).toByte(),
        "こいあお"
    ),
    BLUE_PALE(
        (0x17).toByte(),
        "うすあお"
    ),
    WHITE(
        (0x18).toByte(),
        "しろ"
    ),
    WHITE_DEEP(
        (0x19).toByte(),
        "こいしろ"
    ),
    WHITE_PALE(
        (0x1A).toByte(),
        "うすしろ"
    ),
    PURPLE(
        (0x1B).toByte(),
        "むらさき"
    ),
    PURPLE_DEEP(
        (0x1C).toByte(),
        "こいむらさき"
    ),
    PURPLE_PALE(
        (0x1D).toByte(),
        "うすむらさき"
    ),
    PINK(
        (0x1E).toByte(),
        "もも"
    ),
    PINK_DEEP(
        (0x1F).toByte(),
        "こいもも"
    ),
    PINK_PALE(
        (0x20).toByte(),
        "うすもも"
    ),
    GOLD(
        (0x21).toByte(),
        "きん"
    ),
    GOLD_DEEP(
        (0x22).toByte(),
        "こいきん"
    ),
    GOLD_PALE(
        (0x23).toByte(),
        "うすきん"
    ),
    SILVER(
        (0x24).toByte(),
        "ぎん"
    ),
    SILVER_DEEP(
        (0x25).toByte(),
        "こいぎん"
    ),
    SILVER_PALE(
        (0x26).toByte(),
        "うすぎん"
    ),;

    val paintValue: Byte
        get() {
            if (isNone) {
                return (0).toByte()
            }
            val newValue = 1 + (value * 2)
            return newValue.toByte()
        }

    companion object {
        private val map = values().associateBy { it.value }

        fun of(value: Byte): DenpaColor {
            return map[value] ?: NONE
        }

        fun paintOf(value: Byte): DenpaColor {
            val newValue = value.div(2)
            return of(newValue.toByte())
        }
    }
}