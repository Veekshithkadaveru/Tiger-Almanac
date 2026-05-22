package app.krafted.tigeralmanac

import app.krafted.tigeralmanac.model.Compatibility
import app.krafted.tigeralmanac.model.ZodiacAnimal
import app.krafted.tigeralmanac.model.ZodiacProfile
import app.krafted.tigeralmanac.model.calculateDailyLuck
import app.krafted.tigeralmanac.viewmodel.HomeViewModel
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AlgorithmUnitTest {

    @Test
    fun testZodiacAnimalCalculation() {
        // Base Rat year 1924
        assertEquals(ZodiacAnimal.RAT, ZodiacAnimal.calculateZodiacAnimal(1924))
        // Ox 1925
        assertEquals(ZodiacAnimal.OX, ZodiacAnimal.calculateZodiacAnimal(1925))
        // Tiger 1926
        assertEquals(ZodiacAnimal.TIGER, ZodiacAnimal.calculateZodiacAnimal(1926))
        // Pig 1935
        assertEquals(ZodiacAnimal.PIG, ZodiacAnimal.calculateZodiacAnimal(1935))
        // Cycle back to Rat 1936
        assertEquals(ZodiacAnimal.RAT, ZodiacAnimal.calculateZodiacAnimal(1936))
        
        // Year before 1924: 1923 should be Pig
        assertEquals(ZodiacAnimal.PIG, ZodiacAnimal.calculateZodiacAnimal(1923))
        // 1922 should be Dog
        assertEquals(ZodiacAnimal.DOG, ZodiacAnimal.calculateZodiacAnimal(1922))
        
        // Future year: 2026 should be Horse
        assertEquals(ZodiacAnimal.HORSE, ZodiacAnimal.calculateZodiacAnimal(2026))
    }

    @Test
    fun testDailyLuckCalculation() {
        val profile = ZodiacProfile(
            id = "RAT",
            name = "Rat",
            chineseName = "鼠",
            years = listOf(1924, 1936, 1948, 1960, 1972, 1984, 1996, 2008, 2020),
            element = "Water",
            polarity = "Yang",
            personality = "Resourceful...",
            strengths = listOf("Intelligent", "Resourceful"),
            weaknesses = listOf("Calculating"),
            luckyNumbers = listOf(2, 3),
            luckyColours = listOf("Blue", "Gold", "Green"),
            luckyDirections = listOf("North", "Northwest", "West"),
            compatibility = Compatibility(
                excellent = listOf("OX", "DRAGON", "MONKEY"),
                good = listOf("RAT"),
                challenging = listOf("HORSE", "ROOSTER")
            ),
            yearFortune = emptyMap(),
            monthlyFortune = emptyMap()
        )

        // Day of Year = 142, Birth Year = 1996, Month = 5 (representing May 22, 2026)
        val dailyLuck = calculateDailyLuck(1996, 142, 5, profile)

        // Verify that values are returned and deterministic
        assertNotNull(dailyLuck)
        
        // Calculations verification:
        // heavenlyStem = 142 % 10 = 2
        // earthlyBranch = 142 % 12 = 10
        // luckyColour: (142 + 1996) % 3 = 2138 % 3 = 2 -> Green
        // luckyNumber: (142 + 1996) % 2 = 2138 % 2 = 0 -> 2
        // luckyDirection: (142 + 5) % 3 = 147 % 3 = 0 -> North
        // luckyElement: elementCycle[2] -> Earth
        // avoidColour: avoidColourCycle[2] -> Red
        // dayEnergy: dayEnergies[10] -> Day of Balance
        // affirmation: affirmations[(142 + 1996) % 24] = 2138 % 24 = 2 -> "A still mind reflects the world as clearly as a calm lake."
        
        assertEquals("Green", dailyLuck.luckyColour)
        assertEquals(2, dailyLuck.luckyNumber)
        assertEquals("North", dailyLuck.luckyDirection)
        assertEquals("Earth", dailyLuck.luckyElement)
        assertEquals("Red", dailyLuck.avoidColour)
        assertEquals("Day of Balance", dailyLuck.dayEnergy)
        assertEquals("A still mind reflects the world as clearly as a calm lake.", dailyLuck.affirmation)
    }

    @Test
    fun testDailyHexagramCalculation() {
        val totalHexagramsCount = 64
        val dayOfYear = 142
        val birthYear = 1996
        
        val selectedIndex = Math.floorMod(dayOfYear + birthYear, totalHexagramsCount)
        assertEquals(26, selectedIndex) // (142 + 1996) % 64 = 2138 % 64 = 26

        // Test with negative birth year (BC birth year or invalid input)
        val negativeBirthYear = -10
        val negativeIndex = Math.floorMod(dayOfYear + negativeBirthYear, totalHexagramsCount)
        // (142 - 10) % 64 = 132 % 64 = 4
        assertEquals(4, negativeIndex)
    }

    @Test
    fun testNegativeBirthYearDailyLuck() {
        val profile = ZodiacProfile(
            id = "RAT",
            name = "Rat",
            chineseName = "鼠",
            years = listOf(1924, 1936, 1948, 1960, 1972, 1984, 1996, 2008, 2020),
            element = "Water",
            polarity = "Yang",
            personality = "Resourceful...",
            strengths = listOf("Intelligent", "Resourceful"),
            weaknesses = listOf("Calculating"),
            luckyNumbers = listOf(2, 3),
            luckyColours = listOf("Blue", "Gold", "Green"),
            luckyDirections = listOf("North", "Northwest", "West"),
            compatibility = Compatibility(
                excellent = listOf("OX", "DRAGON", "MONKEY"),
                good = listOf("RAT"),
                challenging = listOf("HORSE", "ROOSTER")
            ),
            yearFortune = emptyMap(),
            monthlyFortune = emptyMap()
        )

        // Test negative birth year does not crash and yields correct, positive values
        // Day of Year = 142, Birth Year = -10, Month = 5
        val dailyLuck = calculateDailyLuck(-10, 142, 5, profile)
        assertNotNull(dailyLuck)
        
        // (142 - 10) % 3 = 132 % 3 = 0 -> Blue
        assertEquals("Blue", dailyLuck.luckyColour)
        // (142 - 10) % 2 = 132 % 2 = 0 -> 2
        assertEquals(2, dailyLuck.luckyNumber)
    }

    @Test
    fun testLunarAndPillarCalculations() {
        // May 20, 2026 is Wu-Yin (戊寅) day, 4th Moon · Day 4
        val dateMay20 = LocalDate.of(2026, 5, 20)
        
        val lunarMay20 = HomeViewModel.calculateLunarDate(dateMay20)
        val pillarMay20 = HomeViewModel.calculateDayPillar(dateMay20)
        val (animalMay20, elementMay20) = HomeViewModel.calculateDayAnimalAndElement(dateMay20)
        
        assertEquals("4th Moon · Day 4", lunarMay20)
        assertEquals("戊寅", pillarMay20)
        assertEquals("Tiger", animalMay20)
        assertEquals("Wood", elementMay20)
        
        // May 21, 2026 should be Ji-Mao (己卯) day, 4th Moon · Day 5
        val dateMay21 = LocalDate.of(2026, 5, 21)
        
        val lunarMay21 = HomeViewModel.calculateLunarDate(dateMay21)
        val pillarMay21 = HomeViewModel.calculateDayPillar(dateMay21)
        val (animalMay21, elementMay21) = HomeViewModel.calculateDayAnimalAndElement(dateMay21)
        
        assertEquals("4th Moon · Day 5", lunarMay21)
        assertEquals("己卯", pillarMay21)
        assertEquals("Rabbit", animalMay21)
        assertEquals("Wood", elementMay21)
    }
}
