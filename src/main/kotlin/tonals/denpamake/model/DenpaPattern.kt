package tonals.denpamake.model

enum class DenpaPattern(val value: Byte, val text: String) {
    NONE(
        (0x0).toByte(),
        "単色"
    ),
    BORDER(
        (0x1).toByte(),
        "ボーダー"
    ),
    STRIPE(
        (0x2).toByte(),
        "ストライプ"
    ),
    TWO_TONE(
        (0x3).toByte(),
        "ツートン"
    ),
    PASTEL(
        (0x4).toByte(),
        "水玉"
    ),
    ANIMAL(
        (0x5).toByte(),
        "アニマル"
    ),
    MARBLE(
        (0x6).toByte(),
        "マーブル"
    ),
    TIGER(
        (0x7).toByte(),
        "虎"
    ),
    BRICK(
        (0x8).toByte(),
        "レンガ"
    ),
    DIAMOND(
        (0x9).toByte(),
        "ダイヤ"
    ),;

    companion object {
        private val map = values().associateBy { it.value }

        fun of(value: Byte): DenpaPattern {
            return map[value] ?: NONE
        }
    }
}