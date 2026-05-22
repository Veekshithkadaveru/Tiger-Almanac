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
import java.time.temporal.ChronoUnit

data class HomeState(
    val profile: UserProfile? = null,
    val todayHexagram: Hexagram? = null,
    val dailyLuck: DailyLuck? = null,
    val zodiacProfile: ZodiacProfile? = null,
    val todayWhisperIching: String = "",
    val todayWhisperZodiac: String = "",
    val todayWhisperFengshui: String = "",
    val lunarDate: String = "",
    val dayPillar: String = "",
    val dayAnimal: String = "",
    val dayElement: String = "",
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
            hexagrams[Math.floorMod(dayOfYear + birthYear, hexagrams.size)]
        } else null

        val animalName = ZodiacAnimal.calculateZodiacAnimal(birthYear).name
        val zodiacProfile = zodiacProfiles.find { it.id.equals(animalName, ignoreCase = true) }

        val dailyLuck = zodiacProfile?.let {
            calculateDailyLuck(birthYear, dayOfYear, month, it)
        }

        val calculatedLunarDate = calculateLunarDate(today)

        val calculatedDayPillar = calculateDayPillar(today)

        val (calculatedDayAnimal, calculatedDayElement) = calculateDayAnimalAndElement(today)

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
            lunarDate = calculatedLunarDate,
            dayPillar = calculatedDayPillar,
            dayAnimal = calculatedDayAnimal,
            dayElement = calculatedDayElement,
            isLoading = false
        )
    }

    private fun truncate(text: String): String {
        return if (text.length > 80) text.take(80) + "…" else text
    }

    companion object {
        private val cnyDates = mapOf(
            2023 to LocalDate.of(2023, 1, 22),
            2024 to LocalDate.of(2024, 2, 10),
            2025 to LocalDate.of(2025, 1, 29),
            2026 to LocalDate.of(2026, 2, 17),
            2027 to LocalDate.of(2027, 2, 6),
            2028 to LocalDate.of(2028, 1, 26),
            2029 to LocalDate.of(2029, 2, 13),
            2030 to LocalDate.of(2030, 2, 3)
        )

        fun calculateLunarDate(today: LocalDate): String {
            val currentYearCny = cnyDates[today.year]
            val refCNY = if (currentYearCny != null) {
                if (!today.isBefore(currentYearCny)) {
                    currentYearCny
                } else {
                    cnyDates[today.year - 1]
                }
            } else {
                null
            }

            val days = if (refCNY != null) {
                ChronoUnit.DAYS.between(refCNY, today)
            } else {
                (today.dayOfYear + 10L)
            }

            val lunarMonthLength = 29.53059
            val totalLunarDays = if (days >= 0) days else days + 354
            val cycleDay = (totalLunarDays % lunarMonthLength)
            val lunarMonthVal = ((totalLunarDays / lunarMonthLength).toInt() % 12) + 1
            val lunarDayVal = (cycleDay.toInt() % 30) + 1

            val monthSuffix = when (lunarMonthVal) {
                1 -> "1st"
                2 -> "2nd"
                3 -> "3rd"
                else -> "${lunarMonthVal}th"
            }
            return "$monthSuffix Moon · Day $lunarDayVal"
        }

        fun calculateDayPillar(today: LocalDate): String {

            val refDate = LocalDate.of(2026, 5, 20)
            val daysBetween = ChronoUnit.DAYS.between(refDate, today)

            val stemIdx = (((4 + daysBetween) % 10) + 10) % 10
            val branchIdx = (((2 + daysBetween) % 12) + 12) % 12

            val stems = listOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
            val branches =
                listOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")

            return stems[stemIdx.toInt()] + branches[branchIdx.toInt()]
        }

        fun calculateDayAnimalAndElement(today: LocalDate): Pair<String, String> {
            val refDate = LocalDate.of(2026, 5, 20)
            val daysBetween = ChronoUnit.DAYS.between(refDate, today)
            val branchIdx = (((2 + daysBetween) % 12) + 12) % 12
            val animal = ZodiacAnimal.values()[branchIdx.toInt()]
            return Pair(animal.displayName, animal.element)
        }

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
