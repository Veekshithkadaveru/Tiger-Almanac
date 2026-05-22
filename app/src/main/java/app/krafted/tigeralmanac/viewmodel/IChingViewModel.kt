package app.krafted.tigeralmanac.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.krafted.tigeralmanac.data.IChingRepository
import app.krafted.tigeralmanac.data.db.HexagramHistory
import app.krafted.tigeralmanac.data.db.HexagramHistoryDao
import app.krafted.tigeralmanac.data.db.UserProfileDao
import app.krafted.tigeralmanac.model.Hexagram
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HexagramArchiveEntry(
    val date: String,
    val hexagram: Hexagram
)

data class IChingState(
    val todayHexagram: Hexagram? = null,
    val archiveHistory: List<HexagramArchiveEntry> = emptyList(),
    val isLoading: Boolean = true
)

class IChingViewModel(
    private val userProfileDao: UserProfileDao,
    private val hexagramHistoryDao: HexagramHistoryDao,
    private val iChingRepository: IChingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(IChingState())
    val state: StateFlow<IChingState> = _state.asStateFlow()

    private var hexagrams: List<Hexagram> = emptyList()
    private val hexagramsById = mutableMapOf<Int, Hexagram>()

    init {
        viewModelScope.launch {
            ensureHexagramsLoaded()

            launch {
                userProfileDao.getUserProfileFlow().collect { profile ->
                    if (profile == null) {
                        _state.value = _state.value.copy(
                            todayHexagram = null,
                            isLoading = false
                        )
                    } else {
                        val today = LocalDate.now()
                        val dayOfYear = today.dayOfYear
                        val birthYear = profile.birthYear

                        val todayHexagram = if (hexagrams.isNotEmpty()) {
                            hexagrams[Math.floorMod(dayOfYear + birthYear, hexagrams.size)]
                        } else null

                        if (todayHexagram != null) {
                            val dateKey = today.format(DateTimeFormatter.ISO_LOCAL_DATE)
                            val existing = hexagramHistoryDao.getHistoryForDate(dateKey)
                            if (existing == null || existing.hexagramId != todayHexagram.id) {
                                hexagramHistoryDao.insertHistory(
                                    HexagramHistory(
                                        date = dateKey,
                                        hexagramId = todayHexagram.id,
                                        drawnAt = System.currentTimeMillis()
                                    )
                                )
                            }
                        }

                        _state.value = _state.value.copy(
                            todayHexagram = todayHexagram,
                            isLoading = false
                        )
                    }
                }
            }

            launch {
                hexagramHistoryDao.getRecentHistoryFlow().collect { history ->
                    val entries = history.mapNotNull { row ->
                        hexagramsById[row.hexagramId]?.let { HexagramArchiveEntry(row.date, it) }
                    }
                    _state.value = _state.value.copy(archiveHistory = entries)
                }
            }
        }
    }

    fun hexagramForId(id: Int): Hexagram? = hexagramsById[id]

    private suspend fun ensureHexagramsLoaded() {
        if (hexagrams.isNotEmpty()) return
        val loaded = withContext(Dispatchers.IO) { iChingRepository.loadHexagrams() }
        hexagrams = loaded
        loaded.forEach { hexagramsById[it.id] = it }
    }

    companion object {
        fun factory(
            context: Context,
            userProfileDao: UserProfileDao,
            hexagramHistoryDao: HexagramHistoryDao
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val appContext = context.applicationContext
                return IChingViewModel(
                    userProfileDao,
                    hexagramHistoryDao,
                    IChingRepository(appContext)
                ) as T
            }
        }
    }
}
