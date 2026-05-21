package app.krafted.tigeralmanac.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.krafted.tigeralmanac.data.FengShuiRepository
import app.krafted.tigeralmanac.data.db.Bookmark
import app.krafted.tigeralmanac.data.db.BookmarkDao
import app.krafted.tigeralmanac.model.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class FengShuiUiState(
    val rooms: List<Room> = emptyList(),
    val selectedRoom: Room? = null,
    val bookmarkedTipIds: Set<String> = emptySet(),
    val isLoading: Boolean = true
)

class FengShuiViewModel(
    private val fengShuiRepository: FengShuiRepository,
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    private val _state = MutableStateFlow(FengShuiUiState())
    val state: StateFlow<FengShuiUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val loaded = withContext(Dispatchers.IO) {
                fengShuiRepository.loadRooms()
            }
            _state.value = _state.value.copy(rooms = loaded, isLoading = false)
        }
        viewModelScope.launch {
            bookmarkDao.getAllBookmarksFlow().collect { list ->
                _state.value = _state.value.copy(
                    bookmarkedTipIds = list.map { it.tipId }.toSet()
                )
            }
        }
    }

    fun selectRoom(roomId: String) {
        _state.value = _state.value.copy(
            selectedRoom = _state.value.rooms.find { it.id == roomId }
        )
    }

    fun toggleBookmark(roomId: String, tipIndex: Int) {
        viewModelScope.launch {
            val id = tipId(roomId, tipIndex)
            if (_state.value.bookmarkedTipIds.contains(id)) {
                bookmarkDao.deleteBookmarkById(id)
            } else {
                bookmarkDao.insertBookmark(Bookmark(id, roomId, System.currentTimeMillis()))
            }
        }
    }

    companion object {
        fun tipId(roomId: String, index: Int) = "${roomId}_$index"

        fun factory(context: Context, bookmarkDao: BookmarkDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FengShuiViewModel(
                        FengShuiRepository(context.applicationContext),
                        bookmarkDao
                    ) as T
                }
            }
    }
}
