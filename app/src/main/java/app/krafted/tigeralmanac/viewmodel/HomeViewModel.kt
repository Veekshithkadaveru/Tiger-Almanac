package app.krafted.tigeralmanac.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.krafted.tigeralmanac.data.FengShuiRepository
import app.krafted.tigeralmanac.data.IChingRepository
import app.krafted.tigeralmanac.data.ZodiacRepository
import app.krafted.tigeralmanac.data.db.UserProfile
import app.krafted.tigeralmanac.data.db.UserProfileDao
import app.krafted.tigeralmanac.model.DailyLuck
import app.krafted.tigeralmanac.model.Hexagram
import app.krafted.tigeralmanac.model.ZodiacAnimal
import app.krafted.tigeralmanac.model.ZodiacProfile
import app.krafted.tigeralmanac.model.calculateDailyLuck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

data class HomeState(
    val profile: UserProfile? = null,
    val todayHexagram: Hexagram? = null,
    val dailyLuck: DailyLuck? = null,
    val zodiacProfile: ZodiacProfile? = null,
    val todayWhisperIching: String = "",
    val todayWhisperZodiac: String = "",
    val todayWhisperFengshui: String = "",
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val userProfileDao: UserProfileDao,
    private val iChingRepository: IChingRepository,
    private val zodiacRepository: ZodiacRepository,
    private val fengShuiRepository: FengShuiRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userProfileDao.getUserProfileFlow().collect { profile ->
                if (profile == null) {
                    _state.value = HomeState(isLoading = false)
                } else {
                    loadDailyData(profile)
                }
            }
        }
    }

    private suspend fun loadDailyData(profile: UserProfile) {
        val today = LocalDate.now()
        val dayOfYear = today.dayOfYear
        val month = today.monthValue
        val birthYear = profile.birthYear

        val data = withContext(Dispatchers.IO) {
            val hexagrams = iChingRepository.loadHexagrams()
            val zodiacProfiles = zodiacRepository.loadAnimals()
            val rooms = fengShuiRepository.loadRooms()
            Triple(hexagrams, zodiacProfiles, rooms)
        }

        val hexagrams = data.first
        val zodiacProfiles = data.second
        val rooms = data.third

        val todayHexagram = if (hexagrams.isNotEmpty()) {
            hexagrams[(dayOfYear + birthYear) % hexagrams.size]
        } else null

        val animalName = ZodiacAnimal.calculateZodiacAnimal(birthYear).name
        val zodiacProfile = zodiacProfiles.find { it.id.equals(animalName, ignoreCase = true) }

        val dailyLuck = zodiacProfile?.let {
            calculateDailyLuck(birthYear, dayOfYear, month, it)
        }

        val whisperIching = todayHexagram?.guidance?.let { truncate(it) } ?: ""
        val whisperZodiac =
            zodiacProfile?.monthlyFortune?.get(month.toString())?.let { truncate(it) } ?: ""
        val whisperFengshui = rooms.find { it.id.equals("BEDROOM", ignoreCase = true) }
            ?.tips?.firstOrNull()?.body?.let { truncate(it) } ?: ""

        _state.value = HomeState(
            profile = profile,
            todayHexagram = todayHexagram,
            dailyLuck = dailyLuck,
            zodiacProfile = zodiacProfile,
            todayWhisperIching = whisperIching,
            todayWhisperZodiac = whisperZodiac,
            todayWhisperFengshui = whisperFengshui,
            isLoading = false
        )
    }

    private fun truncate(text: String): String {
        return if (text.length > 80) text.take(80) + "…" else text
    }

    companion object {
        fun factory(context: Context, dao: UserProfileDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val appContext = context.applicationContext
                    return HomeViewModel(
                        dao,
                        IChingRepository(appContext),
                        ZodiacRepository(appContext),
                        FengShuiRepository(appContext)
                    ) as T
                }
            }
    }
}
