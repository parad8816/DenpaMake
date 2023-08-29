package denpamodding.denpamake.model

import denpamodding.denpamake.util.NULL_CHAR
import java.nio.ByteBuffer
import java.time.Duration
import java.time.LocalDateTime

class DenpaMan(override val id: Int, override var type: DenpaMemberType?) : DenpaMember() {
    override var date: LocalDateTime = DATE_TIME_FROM

    override var name: String = ""
    override var memo: String = ""
    override var owner: String = ""

    var antenna: DenpaAntenna = DenpaAntenna()
    var shape: DenpaShape = DenpaShape.ROUND
    var face: DenpaFace = DenpaFace()
    var design: DenpaDesign = DenpaDesign()

    var body: DenpaBody = DenpaBody(0, 0)
    var personality: DenpaPersonality = DenpaPersonality.KIND

    var hp: Short = 0
    var ap: Short = 0

    var happiness: Short = 0
    var attribute: DenpaAttribute = DenpaAttribute()

    var generation: Byte = 0
    var maxGeneration: Byte = 0

    var canBirth: Boolean = true
    var fightCountToGrow: Byte = 0

    override var level: Short = 1
    var exp: Int = 0
    var monster: Short = 0

    var fruitRemain: Byte = 0
    var fruitEffects: Array<DenpaFruitEffect> = arrayOf(DenpaFruitEffect(), DenpaFruitEffect())
    var equips: Array<DenpaEquip> = arrayOf(DenpaEquip(), DenpaEquip())

    constructor(buffer: ByteBuffer?, id: Int, type: DenpaMemberType?) : this(id, type) {
        if (buffer != null) {
            buffer.position(8)
            val seconds = buffer.getInt().toUInt()
            date = DATE_TIME_FROM
                .plusSeconds(seconds.toLong())

            buffer.getLong()

            val nameChars = mutableListOf<Char>()
            repeat(NAME_LENGTH) {
                nameChars += buffer.getChar()
            }

            name = String(
                nameChars.takeWhile { it != Char.NULL_CHAR }.toCharArray()
            )

            val memoChars = mutableListOf<Char>()
            repeat(MEMO_LENGTH) {
                memoChars += buffer.getChar()
            }

            memo = String(
                memoChars.takeWhile { it != Char.NULL_CHAR }.toCharArray()
            )

            val ownerChars = mutableListOf<Char>()
            repeat(OWNER_LENGTH) {
                ownerChars += buffer.getChar()
            }

            owner = String(
                ownerChars.takeWhile { it != Char.NULL_CHAR }.toCharArray()
            )

            buffer.getShort()

            antenna.appearance = buffer.getShort()
            shape = DenpaShape.of(buffer.get())
            face.contour = buffer.get()
            face.hair = buffer.get()
            face.eyebrow = buffer.get()
            face.eye = buffer.get()
            face.nose = buffer.get()
            face.mouth = buffer.get()
            face.cheek = buffer.get()

            buffer.get()

            body = DenpaBody.fromByte(buffer.get())

            buffer.getShort()

            design.pattern = DenpaPattern.of(buffer.get())
            design.primaryColor = DenpaColor.of(buffer.get())
            design.secondaryColor = DenpaColor.of(buffer.get())
            design.skinColor = buffer.get()
            design.hairColor = buffer.get()
            design.paintColor = DenpaColor.paintOf(buffer.get())
            design.reflectance = buffer.get()

            personality = DenpaPersonality.of(buffer.get())
            hp = buffer.getShort()
            ap = buffer.getShort()

            buffer.getInt()

            val state = buffer.getShort().toInt()
            happiness = (state and 0x0FFF).toShort()
            attribute = DenpaAttribute.fromByte(((state ushr 12) and 0x0F).toByte())

            generation = buffer.get()
            maxGeneration = buffer.get()

            val birth = buffer.get().toInt()
            canBirth = (birth and 0b1) == 1
            fightCountToGrow = (birth ushr 2).toByte()

            // 「ふれあい」における状態などを含むバイト列
            repeat(15) {
                buffer.get()
            }

            antenna.family = buffer.get()
            antenna.level = buffer.get()

            level = buffer.getShort()
            exp = buffer.getInt()
            monster = buffer.getShort()

            buffer.get()

            fruitRemain = buffer.get()

            repeat(2) { i ->
                fruitEffects[i].hp = buffer.get()
                fruitEffects[i].ap = buffer.get()
                fruitEffects[i].attack = buffer.get()
                fruitEffects[i].defense = buffer.get()
                fruitEffects[i].speed = buffer.get()
                fruitEffects[i].evade = buffer.get()
            }

            repeat(2) { i ->
                equips[i].headGear = buffer.getInt()
                equips[i].armGear = buffer.getInt()
                equips[i].legGear = buffer.getInt()
                equips[i].backGear = buffer.getInt()
                equips[i].body = buffer.getInt()
                equips[i].faceGear = buffer.getInt()

                buffer.getLong()
            }
        }
    }

    override fun updateData(buffer: ByteBuffer) {
        buffer.position(0)
        buffer.putInt(id)
        buffer.putShort(2)
        buffer.put(type?.value ?: 0)

        buffer.get()

        val deltaDate = Duration.between(DATE_TIME_FROM, date)
        val seconds = deltaDate.toSeconds()
        buffer.putInt(seconds.toInt())

        buffer.getLong()

        for (c in name.padEnd(NAME_LENGTH, Char.NULL_CHAR)) {
            buffer.putChar(c)
        }

        for (c in memo.padEnd(MEMO_LENGTH, Char.NULL_CHAR)) {
            buffer.putChar(c)
        }

        for (c in owner.padEnd(OWNER_LENGTH, Char.NULL_CHAR)) {
            buffer.putChar(c)
        }

        buffer.getShort()

        buffer.putShort(antenna.appearance)
        buffer.put(shape.value)
        buffer.put(face.contour)
        buffer.put(face.hair)
        buffer.put(face.eyebrow)
        buffer.put(face.eye)
        buffer.put(face.nose)
        buffer.put(face.mouth)
        buffer.put(face.cheek)

        buffer.get()

        buffer.put(body.toByte())

        buffer.getShort()

        buffer.put(design.pattern.value)
        buffer.put(design.primaryColor.value)
        buffer.put(design.secondaryColor.value)
        buffer.put(design.skinColor)
        buffer.put(design.hairColor)
        buffer.put(design.paintColor.paintValue)

        buffer.put(design.reflectance)

        buffer.put(personality.value)
        buffer.putShort(hp)
        buffer.putShort(ap)

        buffer.getInt()

        val state = happiness.toInt() + (attribute.toByte().toInt() shl 12)
        buffer.putShort(state.toShort())

        buffer.put(generation)
        buffer.put(maxGeneration)

        val birth = (fightCountToGrow.toInt() shl 2) + (if (canBirth) 1 else 0)
        buffer.put(birth.toByte())

        repeat(15) {
            buffer.get()
        }

        buffer.put(antenna.family)
        buffer.put(antenna.level)

        buffer.putShort(level)
        buffer.putInt(exp)
        buffer.putShort(monster)

        buffer.get()

        buffer.put(fruitRemain)

        repeat(2) { i ->
            buffer.put(fruitEffects[i].hp)
            buffer.put(fruitEffects[i].ap)
            buffer.put(fruitEffects[i].attack)
            buffer.put(fruitEffects[i].defense)
            buffer.put(fruitEffects[i].speed)
            buffer.put(fruitEffects[i].evade)
        }

        repeat(2) { i ->
            buffer.putInt(equips[i].headGear)
            buffer.putInt(equips[i].armGear)
            buffer.putInt(equips[i].legGear)
            buffer.putInt(equips[i].backGear)
            buffer.putInt(equips[i].body)
            buffer.putInt(equips[i].faceGear)

            buffer.getLong()
        }
    }

    override fun toString(): String {
        return "${name}${
            if (personality == DenpaPersonality.MAIN_ACTOR) " (主人公)"
            else ""
        }\n" +
                (if (memo.isEmpty()) ""
                else "メモ: ${memo}\n") +
                "Lv. ${level}\n" +
                "${generation + 1}代目 (${maxGeneration + 1}代目まで転生可能)"
    }
}