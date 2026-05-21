package app.krafted.tigeralmanac.model

data class Compatibility(
    val excellent: List<String>,
    val good: List<String>,
    val challenging: List<String>,
    val notes: Map<String, String> = emptyMap()
)

data class YearFortuneDetail(
    val overall: String,
    val career: String,
    val relationships: String,
    val health: String,
    val finance: String,
    val luckyMonths: List<Int>,
    val challengingMonths: List<Int>
)

data class ZodiacProfile(
    val id: String,
    val name: String,
    val chineseName: String,
    val years: List<Int>,
    val element: String,
    val polarity: String,
    val personality: String,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val luckyNumbers: List<Int>,
    val luckyColours: List<String>,
    val luckyDirections: List<String>,
    val compatibility: Compatibility,
    val yearFortune: Map<String, YearFortuneDetail>,
    val monthlyFortune: Map<String, String>
)
