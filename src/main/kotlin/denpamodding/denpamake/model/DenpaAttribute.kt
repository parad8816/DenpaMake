package denpamodding.denpamake.model

class DenpaAttribute {
    companion object {
        private const val VIRTUE = 0b0100
        private const val VICE = 0b1000

        fun fromByte(value: Byte): DenpaAttribute {
            val valueInt = value.toInt()
            val attr = DenpaAttribute()
            attr.isVirtue = (valueInt and VIRTUE) == VIRTUE
            attr.isVice = (valueInt and VICE) == VICE

            return attr
        }
    }

    var isVirtue: Boolean = false
    var isVice: Boolean = false

    fun toByte(): Byte {
        var value = 0
        if (isVirtue) value += VIRTUE
        if (isVice) value += VICE

        return value.toByte()
    }

    override fun toString(): String {
        val attrs = mutableListOf<String>()
        if (isVirtue) attrs.add("ぜん")
        if (isVice) attrs.add("あく")

        return if (attrs.isEmpty()) {
            "なし"
        }
        else {
            attrs.joinToString(", ")
        }
    }
}