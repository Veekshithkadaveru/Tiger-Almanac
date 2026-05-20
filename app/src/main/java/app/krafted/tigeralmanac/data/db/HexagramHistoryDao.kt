package app.krafted.tigeralmanac.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HexagramHistoryDao {
    @Query("SELECT * FROM hexagram_history ORDER BY date DESC LIMIT 30")
    fun getRecentHistoryFlow(): Flow<List<HexagramHistory>>

    @Query("SELECT * FROM hexagram_history ORDER BY date DESC LIMIT 30")
    suspend fun getRecentHistory(): List<HexagramHistory>

    @Query("SELECT * FROM hexagram_history WHERE date = :date LIMIT 1")
    suspend fun getHistoryForDate(date: String): HexagramHistory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HexagramHistory)

    @Query("DELETE FROM hexagram_history")
    suspend fun clearHistory()
}
