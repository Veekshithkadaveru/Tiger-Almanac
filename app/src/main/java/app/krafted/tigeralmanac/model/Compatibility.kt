package app.krafted.tigeralmanac.model

enum class CompatibilityLevel { EXCELLENT, GOOD, NEUTRAL, CHALLENGING }

data class CompatibilityResult(
    val animal: ZodiacAnimal,
    val level: CompatibilityLevel,
    val description: String,
)

fun resolveCompatibility(userProfile: ZodiacProfile, other: ZodiacAnimal): CompatibilityResult {
    val id = other.name
    val compatibility = userProfile.compatibility
    val level = when {
        compatibility.excellent.any {
            it.equals(
                id,
                ignoreCase = true
            )
        } -> CompatibilityLevel.EXCELLENT

        compatibility.good.any { it.equals(id, ignoreCase = true) } -> CompatibilityLevel.GOOD
        compatibility.challenging.any {
            it.equals(
                id,
                ignoreCase = true
            )
        } -> CompatibilityLevel.CHALLENGING

        else -> CompatibilityLevel.NEUTRAL
    }
    val description = compatibility.notes[id] ?: ""
    return CompatibilityResult(other, level, description)
}
