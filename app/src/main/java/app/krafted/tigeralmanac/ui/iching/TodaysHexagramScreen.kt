package app.krafted.tigeralmanac.ui.iching

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.R
import app.krafted.tigeralmanac.model.Hexagram
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.GoldParticles
import app.krafted.tigeralmanac.ui.components.HexagramSymbol
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
import app.krafted.tigeralmanac.ui.theme.TigerRed
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.IChingViewModel
import kotlinx.coroutines.delay

@Composable
fun TodaysHexagramScreen(
    viewModel: IChingViewModel,
    hexagramId: Int? = null,
    onBack: () -> Unit,
    onViewArchive: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val hexagram =
        if (hexagramId != null) viewModel.hexagramForId(hexagramId) else state.todayHexagram

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TigerSurface),
    ) {
        ScreenBackground(
            imageRes = R.drawable.tiger004_back_2,
            dark = 0.66f,
            accent = TigerGoldDeepAccent,
        )
        GoldParticles(count = 16)

        when {
            state.isLoading && hexagram == null -> {
                CircularProgressIndicator(
                    color = TigerGold,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            hexagram == null -> {
                Text(
                    text = "No reading available",
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = TigerCream,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            else -> {
                HexagramContent(
                    hexagram = hexagram,
                    onBack = onBack,
                    onViewArchive = onViewArchive,
                )
            }
        }
    }
}

private val TigerGoldDeepAccent = Color(0xFF5C3F12)

@Composable
private fun HexagramContent(
    hexagram: Hexagram,
    onBack: () -> Unit,
    onViewArchive: () -> Unit,
) {
    var showBack by remember { mutableStateOf(false) }
    var showHeader by remember { mutableStateOf(false) }
    var showBadges by remember { mutableStateOf(false) }
    var showJudgment by remember { mutableStateOf(false) }
    var showMeaning by remember { mutableStateOf(false) }
    var showGuidance by remember { mutableStateOf(false) }
    var showWarning by remember { mutableStateOf(false) }
    var showLucky by remember { mutableStateOf(false) }
    var showTags by remember { mutableStateOf(false) }

    LaunchedEffect(hexagram.id) {
        showBack = true
        delay(80)
        showHeader = true
        delay(80)
        showBadges = true
        delay(80)
        showJudgment = true
        delay(80)
        showMeaning = true
        delay(80)
        showGuidance = true
        delay(80)
        showWarning = true
        delay(80)
        showLucky = true
        delay(80)
        showTags = true
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
    ) {
        item {
            StaggerSection(visible = showBack) {
                TopBar(onBack = onBack, onViewArchive = onViewArchive)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            StaggerSection(visible = showHeader) {
                HexagramHeader(hexagram = hexagram)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            StaggerSection(visible = showBadges) {
                BadgeRow(element = hexagram.element, attribute = hexagram.attribute)
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
            StaggerSection(visible = showJudgment) {
                JudgmentCard(judgment = hexagram.judgment)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            StaggerSection(visible = showMeaning) {
                MeaningCard(meaning = hexagram.meaning)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            StaggerSection(visible = showGuidance) {
                GuidanceCard(guidance = hexagram.guidance)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            StaggerSection(visible = showWarning) {
                WarningCard(warning = hexagram.warning)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            StaggerSection(visible = showLucky) {
                LuckyStrip(
                    luckyElement = hexagram.luckyElement,
                    luckyColour = hexagram.luckyColour,
                    luckyNumber = hexagram.luckyNumber,
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            StaggerSection(visible = showTags) {
                TagsRow(tags = hexagram.tags)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StaggerSection(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "sectionAlpha",
    )
    val offset by animateFloatAsState(
        targetValue = if (visible) 0f else 18f,
        animationSpec = tween(durationMillis = 350),
        label = "sectionOffset",
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .padding(top = offset.dp),
    ) {
        content()
    }
}

@Composable
private fun TopBar(
    onBack: () -> Unit,
    onViewArchive: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
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
        Text(
            text = "Archive ›",
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TigerGoldLight,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onViewArchive() }
                .padding(vertical = 4.dp, horizontal = 4.dp),
        )
    }
}

@Composable
private fun HexagramHeader(
    hexagram: Hexagram,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "#${hexagram.number}",
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            letterSpacing = 2.sp,
            color = TigerCreamSoft,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.tiger004_sym_1),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = hexagram.chineseName,
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.SemiBold,
                fontSize = 46.sp,
                color = TigerGoldLight,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.width(14.dp))
            Image(
                painter = painterResource(R.drawable.tiger004_sym_2),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
            )
        }
        Text(
            text = hexagram.englishName,
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            fontSize = 20.sp,
            color = TigerCream,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(18.dp))
        HexagramSymbol(
            symbol = hexagram.symbol,
            lineColor = TigerGoldLight,
        )
    }
}

@Composable
private fun BadgeRow(
    element: String,
    attribute: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Tag(text = element, tone = TagTone.GOLD)
        Spacer(modifier = Modifier.width(10.dp))
        Tag(text = attribute, tone = TagTone.INK)
    }
}

@Composable
private fun JudgmentCard(
    judgment: String,
) {
    GoldFrame(modifier = Modifier.fillMaxWidth()) {
        SealHeader(
            title = "卦辭",
            subtitle = "THE JUDGMENT",
            symbolRes = R.drawable.tiger004_sym_1,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = judgment,
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = TigerCream,
        )
    }
}

@Composable
private fun MeaningCard(
    meaning: String,
) {
    GoldFrame(modifier = Modifier.fillMaxWidth()) {
        SealHeader(
            title = "釋義",
            subtitle = "MEANING",
            symbolRes = R.drawable.tiger004_sym_2,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = meaning,
            fontFamily = InterFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = TigerCream,
        )
    }
}

@Composable
private fun GuidanceCard(
    guidance: String,
) {
    val shape = RoundedCornerShape(14.dp)
    Column(modifier = Modifier.fillMaxWidth()) {
        SealHeader(title = "指引", subtitle = "GUIDANCE", symbolRes = R.drawable.tiger004_sym_1)
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            TigerGold.copy(alpha = 0.16f),
                            TigerGold.copy(alpha = 0.08f),
                        )
                    )
                )
                .border(1.dp, TigerGold.copy(alpha = 0.45f), shape)
                .padding(16.dp),
        ) {
            Text(
                text = guidance,
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                fontSize = 17.sp,
                lineHeight = 25.sp,
                color = TigerGoldLight,
            )
        }
    }
}

@Composable
private fun WarningCard(
    warning: String,
) {
    GoldFrame(modifier = Modifier.fillMaxWidth()) {
        SealHeader(title = "戒", subtitle = "CAUTION", symbolRes = R.drawable.tiger004_sym_2)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(TigerRed.copy(alpha = 0.85f)),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "CAUTION",
                    fontFamily = InterFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFFFFB0A8),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = warning,
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = TigerCream,
                )
            }
        }
    }
}

@Composable
private fun LuckyStrip(
    luckyElement: String,
    luckyColour: String,
    luckyNumber: Int,
) {
    GoldFrame(modifier = Modifier.fillMaxWidth()) {
        SealHeader(title = "吉兆", subtitle = "FORTUNE", symbolRes = R.drawable.tiger004_sym_1)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            LuckCell(
                modifier = Modifier.weight(1f),
                label = "ELEMENT",
                value = luckyElement,
            )
            LuckCell(
                modifier = Modifier.weight(1f),
                label = "COLOUR",
                value = luckyColour,
                swatch = true,
            )
            LuckCell(
                modifier = Modifier.weight(1f),
                label = "NUMBER",
                value = luckyNumber.toString(),
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
            letterSpacing = 0.5.sp,
            color = TigerCream.copy(alpha = 0.60f),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
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
    "gold", "golden" -> Color(0xFFF4B834)
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
private fun TagsRow(
    tags: List<String>,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SealHeader(title = "標記", subtitle = "TAGS")
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tags.forEach { tag ->
                Tag(text = tag, tone = TagTone.GOLD)
            }
        }
    }
}
