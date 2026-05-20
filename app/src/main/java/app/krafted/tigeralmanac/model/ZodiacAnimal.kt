package app.krafted.tigeralmanac.model

enum class ZodiacAnimal(
    val displayName: String,
    val chineseName: String,
    val emoji: String,
    val element: String
) {
    RAT("Rat", "鼠", "🐀", "Water"),
    OX("Ox", "牛", "🐂", "Earth"),
    TIGER("Tiger", "虎", "🐅", "Wood"),
    RABBIT("Rabbit", "兔", "🐇", "Wood"),
    DRAGON("Dragon", "龍", "🐉", "Earth"),
    SNAKE("Snake", "蛇", "🐍", "Fire"),
    HORSE("Horse", "馬", "🐎", "Fire"),
    GOAT("Goat", "羊", "🐐", "Earth"),
    MONKEY("Monkey", "猴", "🐒", "Metal"),
    ROOSTER("Rooster", "雞", "🐓", "Metal"),
    DOG("Dog", "狗", "🐕", "Earth"),
    PIG("Pig", "豬", "🐖", "Water");

    companion object {
        fun calculateZodiacAnimal(birthYear: Int): ZodiacAnimal {
            val cycle = (birthYear - 1924) % 12
            val index = if (cycle >= 0) cycle else cycle + 12
            return values()[index]
        }
    }
}
