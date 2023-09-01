package tonals.denpamake.model

class DenpaBody(var height: Int, var waist: Int) {
    companion object {
        fun fromByte(value: Byte): DenpaBody {
            return DenpaBody(
                height = value.div(5),
                waist = value.rem(5)
            )
        }
    }

    fun toByte(): Byte {
        val value = (height * 5) + waist
        return value.toByte()
    }
}