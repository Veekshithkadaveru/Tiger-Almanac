package app.krafted.tigeralmanac.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark ORDER BY savedAt DESC")
    fun getAllBookmarksFlow(): Flow<List<Bookmark>>

    @Query("SELECT * FROM bookmark")
    suspend fun getAllBookmarks(): List<Bookmark>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmark WHERE tipId = :tipId LIMIT 1)")
    fun isBookmarkedFlow(tipId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)

    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmark WHERE tipId = :tipId")
    suspend fun deleteBookmarkById(tipId: String)
}
