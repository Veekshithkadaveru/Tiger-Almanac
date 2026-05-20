package app.krafted.tigeralmanac.model

data class Room(
    val id: String,
    val name: String,
    val chineseName: String,
    val symbol: String,
    val background: String,
    val principle: String,
    val energyRole: String,
    val tips: List<FengShuiTip>
)
