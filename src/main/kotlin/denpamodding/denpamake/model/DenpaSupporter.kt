package denpamodding.denpamake.model

import denpamodding.denpamake.util.NULL_CHAR
import java.nio.ByteBuffer
import java.time.Duration
import java.time.LocalDateTime

class DenpaSupporter(override val id: Int) : DenpaMember() {
    enum class SupporterType(val text: String) {
        DWARF("地底人"),
        FAIRY("妖精"),
        MONSTER("モンスター"),
    }

    private data class SupporterInfo(
        val name: String,
        val type: SupporterType,
        val skills: List<String>
    )

    companion object {
        private fun supporterInfoOf(id: Short): SupporterInfo {
            return when(id.toInt()) {

                // 地底人

                0x1 -> SupporterInfo(
                    "クレイ",
                    SupporterType.DWARF,
                    listOf("おいしそうなかおり")
                )

                0x2 -> SupporterInfo(
                    "フェール",
                    SupporterType.DWARF,
                    listOf("ぼうはんたいさく")
                )

                0x3 -> SupporterInfo(
                    "カルボー",
                    SupporterType.DWARF,
                    listOf("ガードブレイカー")
                )

                0x4 -> SupporterInfo(
                    "アシエ",
                    SupporterType.DWARF,
                    listOf("おそいがかち")
                )

                0x5 -> SupporterInfo(
                    "オロ",
                    SupporterType.DWARF,
                    listOf("せんせいアップ")
                )

                0x6 -> SupporterInfo(
                    "ぺルラ",
                    SupporterType.DWARF,
                    listOf("ききかんりりょく")
                )

                0x7 -> SupporterInfo(
                    "ゴルド",
                    SupporterType.DWARF,
                    listOf("アゲアゲスピード")
                )

                0x8 -> SupporterInfo(
                    "スフェール",
                    SupporterType.DWARF,
                    listOf("セクシーオーラ")
                )

                0x9 -> SupporterInfo(
                    "ロカ",
                    SupporterType.DWARF,
                    listOf("かいひりつダウン")
                )

                0xa -> SupporterInfo(
                    "ジェマ",
                    SupporterType.DWARF,
                    listOf("かいひりつアップ")
                )

                0xb -> SupporterInfo(
                    "ノーチェ",
                    SupporterType.DWARF,
                    listOf("にげそびれたらワンモア")
                )

                0xc -> SupporterInfo(
                    "イリス",
                    SupporterType.DWARF,
                    listOf("にげるたつじん")
                )

                0xd -> SupporterInfo(
                    "ブロート",
                    SupporterType.DWARF,
                    listOf("まいターンHPかいふく")
                )

                0xe -> SupporterInfo(
                    "フィオ",
                    SupporterType.DWARF,
                    listOf("まいターンAPかいふく")
                )

                0xf -> SupporterInfo(
                    "ロトス",
                    SupporterType.DWARF,
                    listOf("ばんのうぐすり")
                )

                0x10 -> SupporterInfo(
                    "パルマ",
                    SupporterType.DWARF,
                    listOf("もういっかいがんばれ")
                )

                0x11 -> SupporterInfo(
                    "ガース",
                    SupporterType.DWARF,
                    listOf("とくぎふうじふうじ")
                )

                0x12 -> SupporterInfo(
                    "ピエトラ",
                    SupporterType.DWARF,
                    listOf("がっぽりカネもらう")
                )

                0x13 -> SupporterInfo(
                    "ジェッソ",
                    SupporterType.DWARF,
                    listOf("ムキムキガード")
                )

                0x14 -> SupporterInfo(
                    "モリオン",
                    SupporterType.DWARF,
                    listOf("はんしゃこうげきむこう")
                )

                0x15 -> SupporterInfo(
                    "プロン",
                    SupporterType.DWARF,
                    listOf("おたからおとせ")
                )

                0x16 -> SupporterInfo(
                    "バルバ",
                    SupporterType.DWARF,
                    listOf("レアおとせ")
                )

                0x17 -> SupporterInfo(
                    "ローゾ",
                    SupporterType.DWARF,
                    listOf("げきレアおとせ")
                )

                0x18 -> SupporterInfo(
                    "ラタ",
                    SupporterType.DWARF,
                    listOf("くすねる")
                )

                0x19 -> SupporterInfo(
                    "ラウァ",
                    SupporterType.DWARF,
                    listOf("よびだしきんし")
                )

                0x1a -> SupporterInfo(
                    "トラカ",
                    SupporterType.DWARF,
                    listOf("かんていのこころえ")
                )

                0x1b -> SupporterInfo(
                    "ユクサ",
                    SupporterType.DWARF,
                    listOf("けいけんちおおめ")
                )

                0x1c -> SupporterInfo(
                    "グラブル",
                    SupporterType.DWARF,
                    listOf("あたためてあげる")
                )

                0x1d -> SupporterInfo(
                    "アルシ",
                    SupporterType.DWARF,
                    listOf("おこしてあげる")
                )

                0x1e -> SupporterInfo(
                    "スラッジ",
                    SupporterType.DWARF,
                    listOf("マヒさせない")
                )

                0x1f -> SupporterInfo(
                    "ブライ",
                    SupporterType.DWARF,
                    listOf("とつぜんししない")
                )

                0x20 -> SupporterInfo(
                    "ハウエル",
                    SupporterType.DWARF,
                    listOf("かじばのばかじから")
                )

                0x21 -> SupporterInfo(
                    "セレン",
                    SupporterType.DWARF,
                    listOf("かいふくのたつじん")
                )

                0x22 -> SupporterInfo(
                    "テルル",
                    SupporterType.DWARF,
                    listOf("つかまえるたつじん")
                )

                0x23 -> SupporterInfo(
                    "ヨルディ",
                    SupporterType.DWARF,
                    listOf("こんじょうだせ")
                )

                0x24 -> SupporterInfo(
                    "ベルチェ",
                    SupporterType.DWARF,
                    listOf("しょうかんきんし")
                )

                0x25 -> SupporterInfo(
                    "グローコ",
                    SupporterType.DWARF,
                    listOf("ゴーストあたれ")
                )

                // 妖精

                0x26 -> SupporterInfo(
                    "ベル",
                    SupporterType.FAIRY,
                    listOf("ひのかご")
                )

                0x27 -> SupporterInfo(
                    "オリオン",
                    SupporterType.FAIRY,
                    listOf("こおりのかご")
                )

                0x28 -> SupporterInfo(
                    "シュピカ",
                    SupporterType.FAIRY,
                    listOf("かぜのかご")
                )

                0x29 -> SupporterInfo(
                    "レオーネ",
                    SupporterType.FAIRY,
                    listOf("つちのかご")
                )

                0x2a -> SupporterInfo(
                    "スクル",
                    SupporterType.FAIRY,
                    listOf("でんきのかご")
                )

                0x2b -> SupporterInfo(
                    "ウル",
                    SupporterType.FAIRY,
                    listOf("みずのかご")
                )

                0x2c -> SupporterInfo(
                    "シーリョ",
                    SupporterType.FAIRY,
                    listOf("ひかりのかご")
                )

                0x2d -> SupporterInfo(
                    "リゲル",
                    SupporterType.FAIRY,
                    listOf("やみのかご")
                )

                0x2e -> SupporterInfo(
                    "アプス",
                    SupporterType.FAIRY,
                    listOf("ひのたたり")
                )

                0x2f -> SupporterInfo(
                    "ウェルソー",
                    SupporterType.FAIRY,
                    listOf("こおりのたたり")
                )

                0x30 -> SupporterInfo(
                    "アンタレス",
                    SupporterType.FAIRY,
                    listOf("かぜのたたり")
                )

                0x31 -> SupporterInfo(
                    "コメット",
                    SupporterType.FAIRY,
                    listOf("つちのたたり")
                )

                0x32 -> SupporterInfo(
                    "リブラ",
                    SupporterType.FAIRY,
                    listOf("でんきのたたり")
                )

                0x33 -> SupporterInfo(
                    "カリーナ",
                    SupporterType.FAIRY,
                    listOf("みずのたたり")
                )

                0x34 -> SupporterInfo(
                    "コロナ",
                    SupporterType.FAIRY,
                    listOf("ひかりのたたり")
                )

                0x35 -> SupporterInfo(
                    "ベガ",
                    SupporterType.FAIRY,
                    listOf("やみのたたり")
                )

                0x36 -> SupporterInfo(
                    "リラ",
                    SupporterType.FAIRY,
                    listOf("APせつやく")
                )

                // モンスター

                0x37 -> SupporterInfo(
                    "プリンツ",
                    SupporterType.MONSTER,
                    listOf("へっぽこ", "つるぎのまい")
                )

                0x38 -> SupporterInfo(
                    "テラス",
                    SupporterType.MONSTER,
                    listOf("ひのちから", "ファイアボール")
                )

                0x39 -> SupporterInfo(
                    "マーロ",
                    SupporterType.MONSTER,
                    listOf("りゅうのまもり", "ダークボール")
                )

                0x3a -> SupporterInfo(
                    "ブリッツ",
                    SupporterType.MONSTER,
                    listOf("でんきのちから", "エレキボール")
                )

                0x3b -> SupporterInfo(
                    "トポ",
                    SupporterType.MONSTER,
                    listOf("はるのようき", "じしん")
                )

                0x3c -> SupporterInfo(
                    "スース",
                    SupporterType.MONSTER,
                    listOf("どりょくか", "まどろみのけん")
                )

                0x3d -> SupporterInfo(
                    "仲間モンスター07",
                    SupporterType.MONSTER,
                    listOf("まぼろしのすがた", "しのやいば")
                )

                0x3e -> SupporterInfo(
                    "ウモ",
                    SupporterType.MONSTER,
                    listOf("やみのちから", "のろい")
                )

                0x3f -> SupporterInfo(
                    "デンテ",
                    SupporterType.MONSTER,
                    listOf("さきまわり", "ガードシールド")
                )

                0x40 -> SupporterInfo(
                    "ロズ",
                    SupporterType.MONSTER,
                    listOf("ゆうわくされない", "みりょう")
                )

                0x41 -> SupporterInfo(
                    "仲間モンスター11",
                    SupporterType.MONSTER,
                    listOf("おかねガッポリ", "みりょう")
                )

                0x42 -> SupporterInfo(
                    "ミャウ",
                    SupporterType.MONSTER,
                    listOf("くすねる", "しびれぎり")
                )

                0x43 -> SupporterInfo(
                    "ソロ",
                    SupporterType.MONSTER,
                    listOf("けもののまもり", "かまいたち")
                )

                0x44 -> SupporterInfo(
                    "仲間モンスター14",
                    SupporterType.MONSTER,
                    listOf("APかいふく", "HPかいふく")
                )

                0x45 -> SupporterInfo(
                    "フレーズ",
                    SupporterType.MONSTER,
                    listOf("あくまのまもり", "なめる")
                )

                0x46 -> SupporterInfo(
                    "仲間モンスター16",
                    SupporterType.MONSTER,
                    listOf("けもののまもり", "まもる")
                )

                0x47 -> SupporterInfo(
                    "チコ",
                    SupporterType.MONSTER,
                    listOf("まもる", "ガトリングつぶて")
                )

                // 0x48 - 0x4a なし

                0x4b -> SupporterInfo(
                    "勇者(仮)じーさん",
                    SupporterType.MONSTER,
                    listOf("さむいギャグ", "きあいいっぱあつ")
                )

                0x4c -> SupporterInfo(
                    "ダークネスじーさん",
                    SupporterType.MONSTER,
                    listOf("やるきのないおどり", "せなかをかゆくするぞ")
                )

                0x4d -> SupporterInfo(
                    "パスカルせんせい",
                    SupporterType.MONSTER,
                    listOf("じしゅうのじかん", "ウルトラパスカルブレス")
                )

                0x4e -> SupporterInfo(
                    "ぜったいしんじーさん",
                    SupporterType.MONSTER,
                    listOf("こうごうしいひかり", "かみのいかり")
                )

                0x4f -> SupporterInfo(
                    "かれいなるじーさん",
                    SupporterType.MONSTER,
                    listOf("スパイシーなかほり", "かれいしゅう")
                )

                else -> SupporterInfo(
                    "カネコ",
                    SupporterType.DWARF,
                    listOf())
            }
        }
    }

    private val info
        get() = supporterInfoOf(supporterId)

    var supporterId: Short = 1

    override val type: DenpaMemberType
        get() = DenpaMemberType.SUPPORTER

    override var date: LocalDateTime = DATE_TIME_FROM

    override val name: String
        get() = info.name

    override val memo: String
        get() = ""

    override var owner: String = ""
    override var level: Short = 1

    val supporterType: SupporterType
        get() = info.type

    val skills: List<String>
        get() = info.skills

    constructor(buffer: ByteBuffer?, id: Int) : this(id) {
        if (buffer != null) {
            buffer.position(8)
            val seconds = buffer.getInt()
            date = DATE_TIME_FROM
                .plusSeconds(seconds.toLong())

            buffer.getLong()

            // 名前の部分をとばす
            repeat(NAME_LENGTH) {
                buffer.getChar()
            }

            // メモの部分をとばす
            repeat(MEMO_LENGTH) {
                buffer.getChar()
            }

            val ownerChars = mutableListOf<Char>()
            repeat(OWNER_LENGTH) {
                ownerChars += buffer.getChar()
            }

            owner = String(
                ownerChars.takeWhile { it != Char.NULL_CHAR }.toCharArray()
            )

            supporterId = buffer.getShort()

            // レベルの部分まで行く
            repeat(52) {
                buffer.get()
            }

            level = buffer.getShort()
            buffer.position(0)
        }
    }

    override fun updateData(buffer: ByteBuffer) {
        buffer.position(0)
        buffer.putInt(id)
        buffer.putShort(2)
        buffer.put(type.value)

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

        buffer.putShort(supporterId)

        repeat(52) {
            buffer.get()
        }

        buffer.putShort(level)
        buffer.position(0)
    }

    override fun toString(): String {
        return "$name (${supporterType.text})\n" +
                "Lv. ${level}\n" +
                skills.joinToString("\n")
    }
}