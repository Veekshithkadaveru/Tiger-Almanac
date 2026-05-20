package app.krafted.tigeralmanac.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class Bookmark(
    @PrimaryKey val tipId: String,
    val roomId: String,
    val savedAt: Long
)
