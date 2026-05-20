package app.krafted.tigeralmanac.model

data class Hexagram(
    val id: Int,
    val number: Int,
    val name: String,
    val englishName: String,
    val chineseName: String,
    val symbol: String,
    val element: String,
    val attribute: String,
    val image: String,
    val judgment: String,
    val meaning: String,
    val guidance: String,
    val warning: String,
    val luckyElement: String,
    val luckyColour: String,
    val luckyNumber: Int,
    val tags: List<String>
)
