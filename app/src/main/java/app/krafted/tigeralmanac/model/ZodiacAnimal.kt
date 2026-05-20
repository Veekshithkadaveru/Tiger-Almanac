package app.krafted.tigeralmanac.model

enum class ZodiacAnimal {
    RAT, OX, TIGER, RABBIT, DRAGON, SNAKE,
    HORSE, GOAT, MONKEY, ROOSTER, DOG, PIG;

    companion object {
        fun calculateZodiacAnimal(birthYear: Int): ZodiacAnimal {
            val cycle = (birthYear - 1924) % 12
            val index = if (cycle >= 0) cycle else cycle + 12
            return values()[index]
        }
    }
}
