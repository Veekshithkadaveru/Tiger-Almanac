package app.krafted.tigeralmanac.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.krafted.tigeralmanac.data.FengShuiRepository
import app.krafted.tigeralmanac.data.IChingRepository
import app.krafted.tigeralmanac.data.ZodiacRepository
import app.krafted.tigeralmanac.model.Hexagram
import app.krafted.tigeralmanac.model.Room
import app.krafted.tigeralmanac.model.ZodiacAnimal
import app.krafted.tigeralmanac.model.ZodiacProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface SearchResult {
    data class IChing(
        val id: Int,
        val number: Int,
        val name: String,
        val englishName: String,
        val element: String
    ) : SearchResult

    data class Zodiac(val id: String, val name: String, val emoji: String) : SearchResult

    data class FengShui(
        val roomId: String,
        val roomName: String,
        val tipTitle: String,
        val category: String
    ) : SearchResult
}

data class SearchUiState(
    val query: String = "",
    val ichingResults: List<SearchResult.IChing> = emptyList(),
    val zodiacResults: List<SearchResult.Zodiac> = emptyList(),
    val fengShuiResults: List<SearchResult.FengShui> = emptyList(),
    val isLoading: Boolean = true,
) {
    val hasQuery get() = query.isNotBlank()
    val hasResults get() = ichingResults.isNotEmpty() || zodiacResults.isNotEmpty() || fengShuiResults.isNotEmpty()
}

class SearchViewModel(
    private val iChingRepository: IChingRepository,
    private val zodiacRepository: ZodiacRepository,
    private val fengShuiRepository: FengShuiRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    private var hexagrams: List<Hexagram> = emptyList()
    private var animals: List<ZodiacProfile> = emptyList()
    private var rooms: List<Room> = emptyList()

    init {
        viewModelScope.launch {
            val loadedHexagrams = withContext(Dispatchers.IO) { iChingRepository.loadHexagrams() }
            val loadedAnimals = withContext(Dispatchers.IO) { zodiacRepository.loadAnimals() }
            val loadedRooms = withContext(Dispatchers.IO) { fengShuiRepository.loadRooms() }
            hexagrams = loadedHexagrams
            animals = loadedAnimals
            rooms = loadedRooms
            _state.value = _state.value.copy(isLoading = false)
            recompute(queryFlow.value)
        }

        viewModelScope.launch {
            observeQuery()
        }
    }

    @OptIn(FlowPreview::class)
    private suspend fun observeQuery() {
        queryFlow.debounce(300).collect { query ->
            recompute(query)
        }
    }

    fun onQueryChange(query: String) {
        _state.value = _state.value.copy(query = query)
        queryFlow.value = query
    }

    private fun recompute(query: String) {
        val normalized = query.trim().lowercase()
        if (normalized.isBlank()) {
            _state.value = _state.value.copy(
                ichingResults = emptyList(),
                zodiacResults = emptyList(),
                fengShuiResults = emptyList()
            )
            return
        }

        val ichingResults = hexagrams.filter { hexagram ->
            hexagram.name.lowercase().contains(normalized) ||
                    hexagram.englishName.lowercase().contains(normalized) ||
                    hexagram.tags.any { it.lowercase().contains(normalized) }
        }.map { SearchResult.IChing(it.id, it.number, it.name, it.englishName, it.element) }

        val zodiacResults = animals.filter { profile ->
            profile.name.lowercase().contains(normalized) ||
                    profile.personality.lowercase().contains(normalized)
        }.map { profile ->
            val emoji =
                runCatching { ZodiacAnimal.valueOf(profile.id) }.getOrNull()?.emoji.orEmpty()
            SearchResult.Zodiac(profile.id, profile.name, emoji)
        }

        val fengShuiResults = rooms.flatMap { room ->
            room.tips.filter { tip ->
                tip.title.lowercase().contains(normalized) ||
                        tip.body.lowercase().contains(normalized)
            }.map { tip ->
                SearchResult.FengShui(room.id, room.name, tip.title, tip.category)
            }
        }

        _state.value = _state.value.copy(
            ichingResults = ichingResults,
            zodiacResults = zodiacResults,
            fengShuiResults = fengShuiResults
        )
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val appContext = context.applicationContext
                    return SearchViewModel(
                        IChingRepository(appContext),
                        ZodiacRepository(appContext),
                        FengShuiRepository(appContext)
                    ) as T
                }
            }
    }
}
