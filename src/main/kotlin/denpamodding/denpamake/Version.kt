package denpamodding.denpamake

data class Version(val major: Int, val minor: Int, val patch: Int, val suffix: String? = null) : Comparable<Version> {
    val value: Int = (major shl 16) + (minor shl 8) + patch

    override fun compareTo(other: Version): Int {
        return value.compareTo(other.value)
    }

    override fun toString(): String {
        return "$major.$minor.$patch${
            if (suffix == null) ""
            else " - $suffix"
        }"
    }
}