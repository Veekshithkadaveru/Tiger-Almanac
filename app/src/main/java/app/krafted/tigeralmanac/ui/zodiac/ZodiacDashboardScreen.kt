package app.krafted.tigeralmanac.ui.zodiac

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.R
import app.krafted.tigeralmanac.model.ZodiacAnimal
import app.krafted.tigeralmanac.model.ZodiacProfile
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.components.Tag
import app.krafted.tigeralmanac.ui.components.TagTone
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerCreamSoft
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerInk
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.ZodiacViewModel

@Composable
fun ZodiacDashboardScreen(
    viewModel: ZodiacViewModel,
    onBack: () -> Unit,
    onViewFullProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TigerSurface),
    ) {
        ScreenBackground(
            imageRes = R.drawable.tiger004_back_3,
            accent = TigerGold,
        )

        if (state.isLoading) {
            CircularProgressIndicator(
                color = TigerGold,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            var selectedTab by remember { mutableStateOf(0) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp),
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                TopBar(onBack = onBack)
                Spacer(modifier = Modifier.height(16.dp))
                AnimalHeader(
                    animal = state.animal,
                    zodiacProfile = state.zodiacProfile,
                    birthYear = state.profile?.birthYear,
                )
                Spacer(modifier = Modifier.height(22.dp))
                TabBar(selectedTab = selectedTab, onSelect = { selectedTab = it })
                Spacer(modifier = Modifier.height(20.dp))
                Crossfade(
                    targetState = selectedTab,
                    animationSpec = tween(250),
                    label = "zodiacTab",
                ) { tab ->
                    when (tab) {
                        0 -> YearTab(state.yearFortune)
                        1 -> MonthTab(
                            state.zodiacProfile,
                            state.selectedMonth,
                            viewModel::selectMonth
                        )

                        else -> DayTab(state.dailyLuck)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                ViewFullProfileButton(onClick = onViewFullProfile)
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun TopBar(
    onBack: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "‹ Back",
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TigerGoldLight,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onBack() }
                .padding(vertical = 4.dp, horizontal = 4.dp),
        )
    }
}

@Composable
private fun AnimalHeader(
    animal: ZodiacAnimal?,
    zodiacProfile: ZodiacProfile?,
    birthYear: Int?,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = animal?.emoji ?: "",
            fontSize = 62.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = zodiacProfile?.name ?: animal?.displayName ?: "",
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.SemiBold,
            fontSize = 34.sp,
            color = TigerGoldLight,
            textAlign = TextAlign.Center,
        )
        Text(
            text = zodiacProfile?.chineseName ?: animal?.chineseName ?: "",
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            fontSize = 22.sp,
            color = TigerCream,
            textAlign = TextAlign.Center,
        )
        if (birthYear != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Born $birthYear",
                fontFamily = InterFont,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                color = TigerCreamSoft,
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            val element = zodiacProfile?.element ?: animal?.element
            if (element != null) {
                Tag(text = element, tone = TagTone.GOLD)
                Spacer(modifier = Modifier.width(10.dp))
            }
            zodiacProfile?.polarity?.let {
                Tag(text = it, tone = TagTone.JADE)
            }
        }
    }
}

@Composable
private fun TabBar(
    selectedTab: Int,
    onSelect: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        TabItem(
            label = "Year",
            symbolRes = R.drawable.tiger004_sym_3,
            selected = selectedTab == 0,
            onClick = { onSelect(0) },
        )
        TabItem(
            label = "Month",
            symbolRes = R.drawable.tiger004_sym_4,
            selected = selectedTab == 1,
            onClick = { onSelect(1) },
        )
        TabItem(
            label = "Day",
            symbolRes = R.drawable.tiger004_sym_5,
            selected = selectedTab == 2,
            onClick = { onSelect(2) },
        )
    }
}

@Composable
private fun TabItem(
    label: String,
    symbolRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val underlineWidth by animateDpAsState(
        targetValue = if (selected) 28.dp else 0.dp,
        animationSpec = tween(250),
        label = "tabUnderline",
    )
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(symbolRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontFamily = InterFont,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp,
            color = if (selected) TigerGoldLight else TigerCreamSoft.copy(alpha = 0.7f),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .height(2.dp)
                .width(underlineWidth)
                .clip(RoundedCornerShape(1.dp))
                .background(TigerGold),
        )
    }
}

@Composable
private fun ViewFullProfileButton(
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(TigerInk.copy(alpha = 0.7f))
            .border(1.dp, TigerGold.copy(alpha = 0.55f), shape)
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "View Full Profile",
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = TigerGoldLight,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "›",
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = TigerGoldLight,
        )
    }
}
