package app.krafted.tigeralmanac.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val birthYear: Int,
    val birthMonth: Int,
    val name: String,
    val setupComplete: Boolean = false
)
