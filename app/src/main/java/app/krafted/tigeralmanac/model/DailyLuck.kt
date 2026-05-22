package app.krafted.tigeralmanac.model

data class DailyLuck(
    val luckyColour: String,
    val luckyNumber: Int,
    val luckyDirection: String,
    val luckyElement: String,
    val avoidColour: String,
    val dayEnergy: String,
    val affirmation: String
)

private val elementCycle = listOf(
    "Wood", "Fire", "Earth", "Metal", "Water",
    "Wood", "Fire", "Earth", "Metal", "Water"
)

private val avoidColourCycle = listOf(
    "Black", "Blue", "Red", "Orange", "Yellow",
    "White", "Silver", "Purple", "Green", "Brown"
)

private val dayEnergies = listOf(
    "Day of Initiation",
    "Day of Abundance",
    "Day of Reflection",
    "Day of Action",
    "Day of Harmony",
    "Day of Transformation",
    "Day of Clarity",
    "Day of Perseverance",
    "Day of Grace",
    "Day of Renewal",
    "Day of Balance",
    "Day of Fulfillment"
)

private val affirmations = listOf(
    "The journey of a thousand miles begins beneath your feet today.",
    "Like water, I yield where needed and persist where it matters.",
    "A still mind reflects the world as clearly as a calm lake.",
    "I plant seeds today whose shade I may never sit beneath.",
    "Bamboo bends in the storm yet rises again unbroken.",
    "When the wind of change blows, I build sails, not walls.",
    "The wise find joy in the small streams as well as the great river.",
    "I move with the season, neither rushing spring nor mourning autumn.",
    "A single spark of patience can outlast the longest night.",
    "The mountain does not boast of its height, yet all look up to it.",
    "I gather strength quietly, as the river gathers the rain.",
    "Every closed door turns my steps toward an open path.",
    "The tea is sweeter when the cup is held with gratitude.",
    "I let go of the heavy stone so my hands are free to climb.",
    "Even the longest day surrenders gently to a peaceful dusk.",
    "The crane stands on one leg yet never loses its balance within.",
    "I trust the unseen roots that hold the tallest tree.",
    "Kindness offered today returns like the tide to a welcoming shore.",
    "The brush that hesitates still leaves a truer mark than the rushed one.",
    "I greet obstacles as teachers wearing the mask of difficulty.",
    "A heart at peace turns an ordinary day into a quiet treasure.",
    "The lantern shines brightest in the room that admits its darkness.",
    "I walk slowly, for the path itself is the destination.",
    "What is rooted in virtue cannot be uprooted by misfortune."
)

fun calculateDailyLuck(
    birthYear: Int,
    dayOfYear: Int,
    month: Int,
    profile: ZodiacProfile
): DailyLuck {
    val heavenlyStem = dayOfYear % 10
    val earthlyBranch = dayOfYear % 12
    return DailyLuck(
        luckyColour = profile.luckyColours[Math.floorMod(
            dayOfYear + birthYear,
            profile.luckyColours.size
        )],
        luckyNumber = profile.luckyNumbers[Math.floorMod(
            dayOfYear + birthYear,
            profile.luckyNumbers.size
        )],
        luckyDirection = profile.luckyDirections[Math.floorMod(
            dayOfYear + month,
            profile.luckyDirections.size
        )],
        luckyElement = elementCycle[heavenlyStem],
        avoidColour = avoidColourCycle[heavenlyStem],
        dayEnergy = dayEnergies[earthlyBranch],
        affirmation = affirmations[Math.floorMod(dayOfYear + birthYear, affirmations.size)]
    )
}
