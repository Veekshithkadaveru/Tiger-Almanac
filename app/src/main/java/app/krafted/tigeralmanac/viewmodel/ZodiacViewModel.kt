package app.krafted.tigeralmanac.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.krafted.tigeralmanac.data.ZodiacRepository
import app.krafted.tigeralmanac.data.db.UserProfile
import app.krafted.tigeralmanac.data.db.UserProfileDao
import app.krafted.tigeralmanac.model.DailyLuck
import app.krafted.tigeralmanac.model.YearFortuneDetail
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

data class ZodiacUiState(
    val profile: UserProfile? = null,
    val animal: ZodiacAnimal? = null,
    val zodiacProfile: ZodiacProfile? = null,
    val yearFortune: YearFortuneDetail? = null,
    val dailyLuck: DailyLuck? = null,
    val selectedMonth: Int = LocalDate.now().monthValue,
    val isLoading: Boolean = true
)

class ZodiacViewModel(
    private val userProfileDao: UserProfileDao,
    private val zodiacRepository: ZodiacRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ZodiacUiState())
    val state: StateFlow<ZodiacUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userProfileDao.getUserProfileFlow().collect { profile ->
                if (profile == null) {
                    _state.value = ZodiacUiState(isLoading = false)
                } else {
                    loadZodiacData(profile)
                }
            }
        }
    }

    private suspend fun loadZodiacData(profile: UserProfile) {
        val today = LocalDate.now()

        val animals = withContext(Dispatchers.IO) {
            zodiacRepository.loadAnimals()
        }

        val animal = ZodiacAnimal.calculateZodiacAnimal(profile.birthYear)
        val zodiacProfile = animals.find { it.id.equals(animal.name, ignoreCase = true) }
        val yearFortune = zodiacProfile?.yearFortune?.get(today.year.toString())
        val dailyLuck = zodiacProfile?.let {
            calculateDailyLuck(profile.birthYear, today.dayOfYear, today.monthValue, it)
        }

        _state.value = ZodiacUiState(
            profile = profile,
            animal = animal,
            zodiacProfile = zodiacProfile,
            yearFortune = yearFortune,
            dailyLuck = dailyLuck,
            selectedMonth = today.monthValue,
            isLoading = false
        )
    }

    fun selectMonth(month: Int) {
        val wrapped = ((month - 1).mod(12)) + 1
        _state.value = _state.value.copy(selectedMonth = wrapped)
    }

    companion object {
        fun factory(context: Context, dao: UserProfileDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val appContext = context.applicationContext
                    return ZodiacViewModel(
                        dao,
                        ZodiacRepository(appContext)
                    ) as T
                }
            }
    }
}
