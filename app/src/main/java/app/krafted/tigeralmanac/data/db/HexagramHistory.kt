package app.krafted.tigeralmanac.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hexagram_history")
data class HexagramHistory(
    @PrimaryKey val date: String,
    val hexagramId: Int,
    val drawnAt: Long
)
