package app.krafted.tigeralmanac.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.R
import app.krafted.tigeralmanac.model.ZodiacAnimal
import app.krafted.tigeralmanac.ui.components.CherryPetals
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.GoldParticles
import app.krafted.tigeralmanac.ui.components.RibbonButton
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.components.Tag
import app.krafted.tigeralmanac.ui.components.TagTone
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerInk
import app.krafted.tigeralmanac.ui.theme.TigerRed
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.UserProfileViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileSetupScreen(
    viewModel: UserProfileViewModel,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableStateOf(0) }
    var selectedYear by remember { mutableStateOf(1986) }
    var selectedMonth by remember { mutableStateOf(7) }
    var nameInput by remember { mutableStateOf("") }

    val zodiacProfiles by viewModel.zodiacProfiles.collectAsState()
    val activeAnimal = remember(selectedYear) {
        ZodiacAnimal.calculateZodiacAnimal(selectedYear)
    }
    val activeProfile = remember(zodiacProfiles, activeAnimal) {
        zodiacProfiles.find { it.id.equals(activeAnimal.name, ignoreCase = true) }
    }

    val totalSteps = 5
    val stepTitle = when (step) {
        0 -> "Welcome, Traveller"
        1 -> "When were you born under the sky?"
        2 -> "And in which moon?"
        3 -> "By what name shall we know you?"
        else -> "Welcome, traveller."
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TigerSurface)
    ) {
        ScreenBackground(
            imageRes = R.drawable.tiger004_back_5,
            dark = 0.66f,
            accent = TigerRed
        )

        GoldParticles(count = 12)
        CherryPetals(count = 10)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 22.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(totalSteps) { i ->
                    val isActive = i <= step
                    val progressColor = if (isActive) TigerGold else TigerCream.copy(alpha = 0.18f)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(progressColor)
                    )
                }
            }

            Text(
                text = "STEP ${step + 1} OF $totalSteps",
                color = TigerCream.copy(alpha = 0.82f),
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stepTitle,
                color = TigerGoldLight,
                fontFamily = FontFamily.Serif,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 36.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                when (step) {
                    0 -> WelcomeStepContent()
                    1 -> YearPickerStep(
                        selectedYear = selectedYear,
                        onYearChange = { selectedYear = it },
                        activeAnimal = activeAnimal
                    )

                    2 -> MonthPickerStep(
                        selectedMonth = selectedMonth,
                        onMonthChange = { selectedMonth = it }
                    )

                    3 -> NameInputStep(
                        name = nameInput,
                        onNameChange = { nameInput = it }
                    )

                    4 -> ConfirmCardStep(
                        animal = activeAnimal,
                        year = selectedYear,
                        profile = activeProfile
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (step > 0) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(999.dp))
                            .background(TigerInk.copy(alpha = 0.5f))
                            .border(1.dp, TigerGold.copy(alpha = 0.4f), RoundedCornerShape(999.dp))
                            .clickable { step-- }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "← BACK",
                            color = TigerCream,
                            fontFamily = FontFamily.Serif,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 1.sp
                        )
                    }
                }

                RibbonButton(
                    text = if (step == 4) "ENTER →" else "CONTINUE →",
                    onClick = {
                        if (step == 4) {
                            viewModel.saveProfile(selectedYear, selectedMonth, nameInput)
                            onComplete()
                        } else {
                            step++
                        }
                    },
                    modifier = Modifier.weight(if (step > 0) 1.4f else 1f)
                )
            }
        }
    }
}

@Composable
fun WelcomeStepContent() {
    val infiniteTransition = rememberInfiniteTransition(label = "welcome_animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .scale(scale)
                .clip(RoundedCornerShape(36.dp))
                .border(1.5.dp, TigerGold.copy(alpha = 0.7f), RoundedCornerShape(36.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(TigerGold.copy(alpha = 0.45f), Color.Transparent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.tiger004_sym_1),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.9f),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "虎 曆",
            color = TigerGoldLight,
            fontFamily = FontFamily.Serif,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 8.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TIGER ALMANAC",
            color = TigerCream,
            fontFamily = FontFamily.Serif,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 4.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "AN ATLAS OF EASTERN WISDOM",
            color = TigerCream.copy(alpha = 0.72f),
            fontFamily = FontFamily.SansSerif,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 3.sp,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun YearPickerStep(
    selectedYear: Int,
    onYearChange: (Int) -> Unit,
    activeAnimal: ZodiacAnimal
) {
    val years = remember { (1924..2006).toList() }
    val initialIndex = remember { years.indexOf(selectedYear).coerceAtLeast(0) }
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)

    LaunchedEffect(lazyListState.firstVisibleItemIndex) {
        val centerIndex = lazyListState.firstVisibleItemIndex
        if (centerIndex in years.indices) {
            onYearChange(years[centerIndex])
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoldFrame(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            padding = 0.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = lazyListState,
                    flingBehavior = snapFlingBehavior,
                    contentPadding = PaddingValues(vertical = 66.dp),
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(years) { _, y ->
                        val isSelected = y == selectedYear
                        Text(
                            text = y.toString(),
                            fontFamily = FontFamily.Serif,
                            fontSize = if (isSelected) 32.sp else 20.sp,
                            color = if (isSelected) TigerGoldLight else TigerCream.copy(alpha = 0.82f),
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            letterSpacing = 2.sp,
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .clickable {
                                    val idx = years.indexOf(y)
                                    if (idx >= 0) {
                                        onYearChange(y)
                                    }
                                }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .height(44.dp)
                        .padding(horizontal = 14.dp)
                        .border(1.dp, TigerGold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TigerInk.copy(alpha = 0.55f), RoundedCornerShape(12.dp))
                .border(1.dp, TigerGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activeAnimal.emoji,
                fontSize = 28.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Column {
                Text(
                    text = "Year of the ${activeAnimal.displayName} · ${activeAnimal.chineseName}",
                    color = TigerGoldLight,
                    fontFamily = FontFamily.Serif,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${activeAnimal.element} element",
                    color = TigerCream.copy(alpha = 0.86f),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun MonthPickerStep(
    selectedMonth: Int,
    onMonthChange: (Int) -> Unit
) {
    val months = remember {
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    }
    val elementByMonth = remember {
        listOf(
            "Water",
            "Water",
            "Wood",
            "Wood",
            "Earth",
            "Fire",
            "Fire",
            "Earth",
            "Metal",
            "Metal",
            "Earth",
            "Water"
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (row in 0 until 4) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (col in 0 until 3) {
                            val monthIndex = row * 3 + col
                            val m = months[monthIndex]
                            val isSelected = selectedMonth == monthIndex + 1

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) {
                                            Brush.verticalGradient(
                                                listOf(
                                                    TigerGold.copy(alpha = 0.22f),
                                                    TigerGold.copy(alpha = 0.05f)
                                                )
                                            )
                                        } else {
                                            SolidColor(TigerInk.copy(alpha = 0.45f))
                                        }
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) TigerGold.copy(alpha = 0.7f) else TigerGold.copy(
                                            alpha = 0.2f
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onMonthChange(monthIndex + 1) }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = m,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 19.sp,
                                        color = if (isSelected) TigerGoldLight else TigerCream,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = elementByMonth[monthIndex].uppercase(),
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 9.sp,
                                        color = TigerCream.copy(alpha = 0.72f),
                                        letterSpacing = 1.5.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NameInputStep(
    name: String,
    onNameChange: (String) -> Unit
) {
    GoldFrame(
        modifier = Modifier.fillMaxWidth(),
        padding = 20.dp
    ) {
        Text(
            text = "OPTIONAL",
            color = TigerCream.copy(alpha = 0.72f),
            fontFamily = FontFamily.SansSerif,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        BasicTextField(
            value = name,
            onValueChange = onNameChange,
            textStyle = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                color = TigerGoldLight,
                letterSpacing = 1.sp
            ),
            cursorBrush = SolidColor(TigerGoldLight),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 0.dp,
                            color = Color.Transparent
                        )
                        .padding(bottom = 8.dp)
                ) {
                    if (name.isEmpty()) {
                        Text(
                            text = "Enter your name…",
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,
                            color = TigerGoldLight.copy(alpha = 0.4f),
                            letterSpacing = 1.sp
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .drawBehindUnderline()
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Your name is kept only on this device. It appears in your daily reading and never leaves.",
            color = TigerCream.copy(alpha = 0.78f),
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun ConfirmCardStep(
    animal: ZodiacAnimal,
    year: Int,
    profile: app.krafted.tigeralmanac.model.ZodiacProfile?
) {
    GoldFrame(
        modifier = Modifier.fillMaxWidth(),
        padding = 20.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFFFD86B), TigerRed)
                        )
                    )
                    .border(1.5.dp, TigerGold.copy(alpha = 0.8f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = animal.emoji,
                    fontSize = 36.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "${animal.chineseName} ${animal.displayName}",
                    color = TigerGoldLight,
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )

                val formattedYears = remember(profile, year) {
                    profile?.years?.map { it.toString().takeLast(2) }?.joinToString(" · ")
                        ?: year.toString()
                }

                Text(
                    text = "${animal.element} element · $formattedYears",
                    color = TigerCream.copy(alpha = 0.82f),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            TigerGold.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (profile != null) {
            Text(
                text = "\"${profile.personality}\"",
                color = TigerCream,
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                profile.strengths.take(4).forEach { strength ->
                    Tag(text = strength, tone = TagTone.GOLD)
                }
            }
        } else {
            Text(
                text = "Zodiac profiles loading...",
                color = TigerCream.copy(alpha = 0.5f),
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
fun Modifier.drawBehindUnderline(): Modifier {
    return this.background(
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.95f to Color.Transparent,
            1f to TigerGold.copy(alpha = 0.5f)
        )
    )
}
