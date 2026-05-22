package app.krafted.tigeralmanac.ui.iching

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.R
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.GoldParticles
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.components.SealHeader
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
import app.krafted.tigeralmanac.viewmodel.HexagramArchiveEntry
import app.krafted.tigeralmanac.viewmodel.IChingViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HexagramArchiveScreen(
    viewModel: IChingViewModel,
    onBack: () -> Unit,
    onOpenHexagram: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val history = state.archiveHistory
    val today = remember { LocalDate.now() }
    val todayStr = remember(today) { today.format(DateTimeFormatter.ISO_LOCAL_DATE) }

    var visibleCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(history.size) {
        visibleCount = 0
        val animated = minOf(history.size, 10)
        repeat(animated) {
            visibleCount += 1
            delay(60)
        }
        visibleCount = history.size
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TigerSurface),
    ) {
        ScreenBackground(
            imageRes = R.drawable.tiger004_back_2,
            dark = 0.66f,
        )
        GoldParticles(count = 16)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                BackRow(onBack = onBack)
                Spacer(modifier = Modifier.height(8.dp))
                SealHeader(
                    title = "卦曆",
                    subtitle = "HEXAGRAM ARCHIVE",
                    symbolRes = R.drawable.app_logo,
                )
            }

            if (history.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    GoldFrame(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Your readings will gather here. Return each day to build your almanac.",
                            fontFamily = CormorantGaramond,
                            fontStyle = FontStyle.Italic,
                            fontSize = 17.sp,
                            lineHeight = 24.sp,
                            color = TigerCream,
                        )
                    }
                }
            } else {
                itemsIndexed(history) { index, entry ->
                    ArchiveRow(
                        entry = entry,
                        isToday = entry.date == todayStr,
                        today = today,
                        visible = index < visibleCount,
                        onClick = { onOpenHexagram(entry.hexagram.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun BackRow(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onBack() }
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "‹ Back",
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TigerGoldLight,
        )
    }
}

@Composable
private fun ArchiveRow(
    entry: HexagramArchiveEntry,
    isToday: Boolean,
    today: LocalDate,
    visible: Boolean,
    onClick: () -> Unit,
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "rowAlpha",
    )
    val offset by animateFloatAsState(
        targetValue = if (visible) 0f else 14f,
        animationSpec = tween(durationMillis = 300),
        label = "rowOffset",
    )

    val shape = RoundedCornerShape(14.dp)
    val borderColor =
        if (isToday) TigerGoldLight.copy(alpha = 0.95f) else TigerGold.copy(alpha = 0.40f)
    val borderWidth = if (isToday) 2.dp else 1.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .graphicsLayer { translationY = offset.dp.toPx() }
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(TigerInk.copy(alpha = 0.78f), TigerInk.copy(alpha = 0.65f))
                )
            )
            .border(borderWidth, borderColor, shape)
            .clickable { onClick() }
            .padding(14.dp),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(72.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = dateLabel(entry.date, today),
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = TigerCreamSoft,
                )
                if (isToday) {
                    Tag(text = "TODAY", tone = TagTone.GOLD)
                }
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                TigerGold.copy(alpha = 0.40f),
                                Color.Transparent,
                            )
                        )
                    ),
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "#${entry.hexagram.number}",
                        fontFamily = InterFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        color = TigerCreamSoft,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = entry.hexagram.chineseName,
                        fontFamily = CormorantGaramond,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = TigerGoldLight,
                    )
                }
                Text(
                    text = entry.hexagram.englishName,
                    fontFamily = InterFont,
                    fontSize = 12.sp,
                    color = TigerCream,
                )
            }
            Tag(text = entry.hexagram.element, tone = TagTone.GOLD)
        }
    }
}

private fun dateLabel(iso: String, today: LocalDate): String {
    return try {
        val date = LocalDate.parse(iso)
        when {
            date.isEqual(today) -> date.format(DateTimeFormatter.ofPattern("MMM d"))
            date.isEqual(today.minusDays(1)) -> "Yesterday"
            else -> date.format(DateTimeFormatter.ofPattern("MMM d"))
        }
    } catch (e: Exception) {
        iso
    }
}
