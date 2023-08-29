package denpamodding.denpamake.model

class DenpaDesign {
    var pattern: PatternType = PatternType.NONE
    var primaryColor: DenpaColor = DenpaColor.NONE
    var secondaryColor: DenpaColor = DenpaColor.NONE
    var skinColor: Byte = 0
    var hairColor: Byte = 0
    var paintColor: DenpaColor = DenpaColor.NONE
    var reflectance: Byte = 0

    enum class PatternType(val value: Byte, val text: String) {
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

            fun of(value: Byte): PatternType {
                return map[value] ?: NONE
            }
        }
    }
}