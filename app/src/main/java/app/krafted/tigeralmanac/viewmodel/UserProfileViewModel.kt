package app.krafted.tigeralmanac.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.krafted.tigeralmanac.data.ZodiacRepository
import app.krafted.tigeralmanac.data.db.UserProfile
import app.krafted.tigeralmanac.data.db.UserProfileDao
import app.krafted.tigeralmanac.model.ZodiacAnimal
import app.krafted.tigeralmanac.model.ZodiacProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val dao: UserProfileDao,
    private val zodiacRepository: ZodiacRepository
) : ViewModel() {

    val profile: StateFlow<UserProfile?> = dao.getUserProfileFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _zodiacProfiles = MutableStateFlow<List<ZodiacProfile>>(emptyList())
    val zodiacProfiles = _zodiacProfiles.asStateFlow()

    init {
        loadZodiacProfiles()
    }

    private fun loadZodiacProfiles() {
        viewModelScope.launch {
            _zodiacProfiles.value = zodiacRepository.loadAnimals()
        }
    }

    fun getProfileForAnimal(animal: ZodiacAnimal): ZodiacProfile? {
        return zodiacProfiles.value.find { it.id.equals(animal.name, ignoreCase = true) }
    }

    fun saveProfile(birthYear: Int, birthMonth: Int, name: String) {
        viewModelScope.launch {
            dao.insertProfile(
                UserProfile(
                    id = 1,
                    birthYear = birthYear,
                    birthMonth = birthMonth,
                    name = name.ifBlank { "Traveller" },
                    setupComplete = true
                )
            )
        }
    }

    companion object {
        fun factory(
            dao: UserProfileDao,
            zodiacRepository: ZodiacRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return UserProfileViewModel(dao, zodiacRepository) as T
                }
            }
    }
}
