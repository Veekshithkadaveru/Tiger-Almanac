package app.krafted.tigeralmanac.ui.zodiac

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.model.DailyLuck
import app.krafted.tigeralmanac.model.YearFortuneDetail
import app.krafted.tigeralmanac.model.ZodiacProfile
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.Tag
import app.krafted.tigeralmanac.ui.components.TagTone
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerCreamSoft
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerInk
import app.krafted.tigeralmanac.ui.theme.TigerJade
import app.krafted.tigeralmanac.ui.theme.TigerRed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun YearTab(yearFortune: YearFortuneDetail?) {
    if (yearFortune == null) {
        EmptyState("The year's fortune is being read in the stars…")
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GoldFrame(modifier = Modifier.fillMaxWidth()) {
            SectionLabel("Overall Forecast")
            Spacer(Modifier.height(6.dp))
            BodyText(yearFortune.overall)
        }
        ForecastCard("Career", yearFortune.career)
        ForecastCard("Relationships", yearFortune.relationships)
        ForecastCard("Health", yearFortune.health)
        ForecastCard("Finance", yearFortune.finance)
        if (yearFortune.luckyMonths.isNotEmpty()) {
            GoldFrame(modifier = Modifier.fillMaxWidth()) {
                SectionLabel("Lucky Months")
                Spacer(Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    yearFortune.luckyMonths.forEach { month ->
                        Tag(text = monthName(month), tone = TagTone.JADE)
                    }
                }
            }
        }
        if (yearFortune.challengingMonths.isNotEmpty()) {
            GoldFrame(modifier = Modifier.fillMaxWidth()) {
                SectionLabel("Challenging Months")
                Spacer(Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    yearFortune.challengingMonths.forEach { month ->
                        Tag(text = monthName(month), tone = TagTone.RED)
                    }
                }
            }
        }
    }
}

@Composable
fun MonthTab(
    zodiacProfile: ZodiacProfile?,
    selectedMonth: Int,
    onSelectMonth: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ArrowButton("‹", enabled = selectedMonth > 1) {
                onSelectMonth(selectedMonth - 1)
            }
            Text(
                text = monthName(selectedMonth),
                color = TigerCream,
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
            ArrowButton("›", enabled = selectedMonth < 12) {
                onSelectMonth(selectedMonth + 1)
            }
        }
        val fortune = zodiacProfile?.monthlyFortune?.get(selectedMonth.toString())
        GoldFrame(modifier = Modifier.fillMaxWidth()) {
            SectionLabel("Monthly Outlook")
            Spacer(Modifier.height(6.dp))
            BodyText(fortune ?: "No reading is available for this month yet.")
        }
    }
}

@Composable
fun DayTab(dailyLuck: DailyLuck?) {
    if (dailyLuck == null) {
        EmptyState("Today's luck has not yet been cast…")
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GoldFrame(modifier = Modifier.fillMaxWidth()) {
            ColourRow("Lucky Colour", dailyLuck.luckyColour)
            Spacer(Modifier.height(10.dp))
            LuckRow("Lucky Number", dailyLuck.luckyNumber.toString())
            Spacer(Modifier.height(10.dp))
            LuckRow("Lucky Direction", dailyLuck.luckyDirection)
            Spacer(Modifier.height(10.dp))
            LuckRow("Lucky Element", dailyLuck.luckyElement)
            Spacer(Modifier.height(10.dp))
            ColourRow("Avoid Colour", dailyLuck.avoidColour)
            Spacer(Modifier.height(10.dp))
            LuckRow("Day Energy", dailyLuck.dayEnergy)
        }
        GoldFrame(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "“${dailyLuck.affirmation}”",
                color = TigerGoldLight,
                fontFamily = CormorantGaramond,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 28.sp
            )
        }
    }
}

@Composable
private fun ForecastCard(label: String, body: String) {
    GoldFrame(modifier = Modifier.fillMaxWidth()) {
        SectionLabel(label)
        Spacer(Modifier.height(6.dp))
        BodyText(body)
    }
}

@Composable
private fun LuckRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = TigerCreamSoft,
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        )
        Text(
            text = value,
            color = TigerGoldLight,
            fontFamily = InterFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ColourRow(label: String, colourName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = TigerCreamSoft,
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(colourFor(colourName))
                    .border(1.dp, TigerGold.copy(alpha = 0.55f), CircleShape)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = colourName,
                color = TigerGoldLight,
                fontFamily = InterFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ArrowButton(symbol: String, enabled: Boolean, onClick: () -> Unit) {
    val tint = if (enabled) TigerGoldLight else TigerGold.copy(alpha = 0.25f)
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(TigerInk.copy(alpha = 0.65f))
            .border(1.dp, TigerGold.copy(alpha = 0.45f), CircleShape)
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = tint,
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        color = TigerGold,
        fontFamily = InterFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 1.5.sp
    )
}

@Composable
private fun BodyText(text: String) {
    Text(
        text = text,
        color = TigerCream,
        fontFamily = InterFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.25.sp
    )
}

@Composable
private fun EmptyState(text: String) {
    GoldFrame(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            color = TigerCreamSoft,
            fontFamily = CormorantGaramond,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 24.sp
        )
    }
}

private fun monthName(m: Int): String = when (m) {
    1 -> "Jan"
    2 -> "Feb"
    3 -> "Mar"
    4 -> "Apr"
    5 -> "May"
    6 -> "Jun"
    7 -> "Jul"
    8 -> "Aug"
    9 -> "Sep"
    10 -> "Oct"
    11 -> "Nov"
    12 -> "Dec"
    else -> "—"
}

private fun colourFor(name: String): Color = when (name.trim().lowercase()) {
    "red" -> TigerRed
    "blue" -> Color(0xFF2F6FB0)
    "green" -> Color(0xFF2E8B57)
    "gold" -> TigerGold
    "yellow" -> Color(0xFFE6C84F)
    "black" -> Color(0xFF1A1A1A)
    "white" -> Color(0xFFF5F5F5)
    "silver" -> Color(0xFFC0C0C0)
    "purple" -> Color(0xFF7E3FA0)
    "orange" -> Color(0xFFE07B2C)
    "brown" -> Color(0xFF7A4A2B)
    "jade" -> TigerJade
    else -> TigerGold
}
