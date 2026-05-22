package app.krafted.tigeralmanac.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.R
import app.krafted.tigeralmanac.model.ZodiacAnimal
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.GoldParticles
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.components.SealHeader
import app.krafted.tigeralmanac.ui.components.ToolCard
import app.krafted.tigeralmanac.ui.components.WhisperLine
import app.krafted.tigeralmanac.ui.components.entrance
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerInk
import app.krafted.tigeralmanac.ui.theme.TigerRed
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateIching: () -> Unit,
    onNavigateZodiac: () -> Unit,
    onNavigateFengshui: () -> Unit,
    onNavigateProfile: () -> Unit,
    onNavigateSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TigerSurface),
    ) {
        ScreenBackground(
            imageRes = R.drawable.tiger004_back_1,
            dark = 0.62f,
            accent = TigerRed,
        )
        GoldParticles(count = 18)

        if (state.isLoading) {
            CircularProgressIndicator(
                color = TigerGold,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp),
            ) {
                item {
                    Box(modifier = Modifier.entrance(index = 0)) {
                        GreetingHeader(
                            name = state.profile?.name ?: "Traveller",
                            birthYear = state.profile?.birthYear ?: 1990,
                            onNavigateProfile = onNavigateProfile,
                            onNavigateSearch = onNavigateSearch,
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(modifier = Modifier.entrance(index = 1)) {
                        TodayPanel(state)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(modifier = Modifier.entrance(index = 2)) {
                        WhisperStrip(
                            iching = state.todayWhisperIching,
                            zodiac = state.todayWhisperZodiac,
                            fengshui = state.todayWhisperFengshui,
                            onNavigateIching = onNavigateIching,
                            onNavigateZodiac = onNavigateZodiac,
                            onNavigateFengshui = onNavigateFengshui,
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(modifier = Modifier.entrance(index = 3)) {
                        ThreePaths(
                            onNavigateIching = onNavigateIching,
                            onNavigateZodiac = onNavigateZodiac,
                            onNavigateFengshui = onNavigateFengshui,
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun GreetingHeader(
    name: String,
    birthYear: Int,
    onNavigateProfile: () -> Unit,
    onNavigateSearch: () -> Unit,
) {
    val emoji = ZodiacAnimal.calculateZodiacAnimal(birthYear).emoji
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Welcome back,",
                fontFamily = InterFont,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = TigerCream.copy(alpha = 0.72f),
            )
            Text(
                text = name,
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = TigerGoldLight,
            )
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(TigerInk)
                .border(1.dp, TigerGold.copy(alpha = 0.40f), RoundedCornerShape(12.dp))
                .clickable { onNavigateSearch() },
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "🔍", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(TigerInk)
                .border(1.dp, TigerGold.copy(alpha = 0.40f), RoundedCornerShape(12.dp))
                .clickable { onNavigateProfile() },
            contentAlignment = Alignment.Center,
        ) {
            Text(text = emoji, fontSize = 28.sp)
        }
    }
}

@Composable
private fun TodayPanel(
    state: app.krafted.tigeralmanac.viewmodel.HomeState,
) {
    val now = remember { LocalDate.now() }
    val dateText = remember { now.format(DateTimeFormatter.ofPattern("EEE, MMM d")) }
    val luck = state.dailyLuck

    val lunarDate = state.lunarDate.ifEmpty { "4th Moon · Day 4" }
    val dayPillar = state.dayPillar.ifEmpty { "戊寅" }
    val dayAnimal = state.dayAnimal.ifEmpty { "Tiger" }
    val dayElement = state.dayElement.ifEmpty { "Wood" }

    GoldFrame(modifier = Modifier.fillMaxWidth()) {
        SealHeader(
            title = "今日",
            subtitle = "TODAY'S READING",
            symbolRes = R.drawable.app_logo,
        )
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TODAY",
                    fontFamily = InterFont,
                    fontSize = 9.sp,
                    letterSpacing = 2.sp,
                    color = TigerCream.copy(alpha = 0.78f),
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = dateText,
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    color = TigerGoldLight,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = lunarDate,
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = TigerCream,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = dayPillar,
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = TigerGold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${dayAnimal.uppercase()} · ${dayElement.uppercase()}",
                    fontFamily = InterFont,
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp,
                    color = TigerCream.copy(alpha = 0.78f),
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            TigerGold.copy(alpha = 0.25f),
                            Color.Transparent,
                        )
                    )
                ),
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (luck != null) {
            Row(modifier = Modifier.fillMaxWidth()) {
                LuckCell(
                    modifier = Modifier.weight(1f),
                    label = "COLOUR",
                    value = luck.luckyColour,
                    swatch = true
                )
                LuckCell(
                    modifier = Modifier.weight(1f),
                    label = "NUMBER",
                    value = luck.luckyNumber.toString()
                )
                LuckCell(
                    modifier = Modifier.weight(1f),
                    label = "DIRECTION",
                    value = luck.luckyDirection
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                TigerGold.copy(alpha = 0.40f),
                                Color.Transparent,
                            )
                        )
                    ),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "“${luck.affirmation}”",
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = TigerCream.copy(alpha = 0.88f),
            )
        }
    }
}

@Composable
private fun LuckCell(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    swatch: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            fontFamily = InterFont,
            fontSize = 9.sp,
            color = TigerCream.copy(alpha = 0.60f),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (swatch) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(swatchColor(value))
                        .border(1.dp, TigerGold.copy(alpha = 0.40f), RoundedCornerShape(3.dp)),
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = value,
                fontFamily = InterFont,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = TigerGoldLight,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun swatchColor(name: String): Color = when (name.trim().lowercase()) {
    "red", "crimson", "scarlet" -> Color(0xFFC8252B)
    "gold", "golden" -> TigerGold
    "yellow" -> Color(0xFFF4D03F)
    "green", "jade", "emerald" -> Color(0xFF2A8A6E)
    "blue", "azure" -> Color(0xFF1F3A5F)
    "white", "silver" -> Color(0xFFF5E8C8)
    "black", "ink" -> Color(0xFF1A0F08)
    "purple", "violet" -> Color(0xFF7A3F8A)
    "orange" -> Color(0xFFE08A3C)
    "brown" -> Color(0xFF6B4423)
    "pink" -> Color(0xFFE0809B)
    else -> TigerGold
}

@Composable
private fun WhisperStrip(
    iching: String,
    zodiac: String,
    fengshui: String,
    onNavigateIching: () -> Unit,
    onNavigateZodiac: () -> Unit,
    onNavigateFengshui: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SealHeader(title = "今日之語", subtitle = "TODAY'S WHISPER")
        Spacer(modifier = Modifier.height(10.dp))
        GoldFrame(modifier = Modifier.fillMaxWidth()) {
            WhisperLine(sectionLabel = "I CHING", text = iching, onClick = onNavigateIching)
            WhisperLine(sectionLabel = "ZODIAC", text = zodiac, onClick = onNavigateZodiac)
            WhisperLine(sectionLabel = "FENG SHUI", text = fengshui, onClick = onNavigateFengshui)
        }
    }
}

@Composable
private fun ThreePaths(
    onNavigateIching: () -> Unit,
    onNavigateZodiac: () -> Unit,
    onNavigateFengshui: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SealHeader(title = "三途", subtitle = "THREE PATHS")
        Spacer(modifier = Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ToolCard(
                title = "I Ching",
                subtitle = "Daily hexagram reading",
                symbolRes = R.drawable.app_logo,
                onClick = onNavigateIching,
            )
            ToolCard(
                title = "Zodiac",
                subtitle = "Your fortune & destiny",
                symbolRes = R.drawable.app_logo,
                onClick = onNavigateZodiac,
            )
            ToolCard(
                title = "Feng Shui",
                subtitle = "Harmonise your space",
                symbolRes = R.drawable.app_logo,
                onClick = onNavigateFengshui,
            )
        }
    }
}
