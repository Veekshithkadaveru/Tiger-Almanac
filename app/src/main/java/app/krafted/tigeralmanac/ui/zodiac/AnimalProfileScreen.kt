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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.R
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.RibbonButton
import app.krafted.tigeralmanac.ui.components.entrance
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.components.Tag
import app.krafted.tigeralmanac.ui.components.TagTone
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerInk
import app.krafted.tigeralmanac.ui.theme.TigerJade
import app.krafted.tigeralmanac.ui.theme.TigerRed
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.ZodiacViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimalProfileScreen(
    viewModel: ZodiacViewModel,
    onBack: () -> Unit,
    onNavigateToCompatibility: () -> Unit,
    animalId: String? = null,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(animalId) { animalId?.let(viewModel::showAnimal) }

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
            val animal = state.animal
            val zodiacProfile = state.zodiacProfile

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(12.dp))
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

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = animal?.emoji ?: "",
                    fontSize = 80.sp,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = zodiacProfile?.name ?: animal?.displayName ?: "",
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 36.sp,
                    color = TigerGoldLight,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = zodiacProfile?.chineseName ?: animal?.chineseName ?: "",
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    fontSize = 24.sp,
                    color = TigerCream,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(12.dp))

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

                Spacer(modifier = Modifier.height(24.dp))

                GoldFrame(modifier = Modifier.fillMaxWidth().entrance(index = 0)) {
                    Text(
                        text = "PERSONALITY",
                        color = TigerGold,
                        fontFamily = InterFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = zodiacProfile?.personality ?: "",
                        color = TigerCream,
                        fontFamily = InterFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        letterSpacing = 0.25.sp,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                GoldFrame(modifier = Modifier.fillMaxWidth().entrance(index = 1)) {
                    Text(
                        text = "STRENGTHS",
                        color = TigerGold,
                        fontFamily = InterFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        zodiacProfile?.strengths?.forEach { strength ->
                            Tag(text = strength, tone = TagTone.JADE)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "WEAKNESSES",
                        color = TigerGold,
                        fontFamily = InterFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        zodiacProfile?.weaknesses?.forEach { weakness ->
                            Tag(text = weakness, tone = TagTone.RED)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GoldFrame(modifier = Modifier.fillMaxWidth().entrance(index = 2)) {
                    Text(
                        text = "LUCKY INFLUENCES",
                        color = TigerGold,
                        fontFamily = InterFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(TigerInk.copy(alpha = 0.4f))
                                .border(
                                    1.dp,
                                    TigerGold.copy(alpha = 0.25f),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(12.dp),
                        ) {
                            Text(
                                text = "NUMBERS",
                                color = TigerGold,
                                fontFamily = InterFont,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                zodiacProfile?.luckyNumbers?.forEach { num ->
                                    Text(
                                        text = num.toString(),
                                        color = TigerCream,
                                        fontFamily = CormorantGaramond,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp,
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(TigerInk.copy(alpha = 0.4f))
                                .border(
                                    1.dp,
                                    TigerGold.copy(alpha = 0.25f),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(12.dp),
                        ) {
                            Text(
                                text = "DIRECTIONS",
                                color = TigerGold,
                                fontFamily = InterFont,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Column {
                                zodiacProfile?.luckyDirections?.forEach { dir ->
                                    Text(
                                        text = dir,
                                        color = TigerCream,
                                        fontFamily = InterFont,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(TigerInk.copy(alpha = 0.4f))
                            .border(1.dp, TigerGold.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                    ) {
                        Text(
                            text = "COLOURS",
                            color = TigerGold,
                            fontFamily = InterFont,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            zodiacProfile?.luckyColours?.forEach { col ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(colourFor(col))
                                            .border(
                                                1.dp,
                                                TigerGold.copy(alpha = 0.45f),
                                                CircleShape
                                            ),
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = col,
                                        color = TigerCream,
                                        fontFamily = InterFont,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GoldFrame(modifier = Modifier.fillMaxWidth().entrance(index = 3)) {
                    Text(
                        text = "BIRTH YEARS",
                        color = TigerGold,
                        fontFamily = InterFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        zodiacProfile?.years?.forEach { yr ->
                            Tag(text = yr.toString(), tone = TagTone.GOLD)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                RibbonButton(
                    text = "Check Compatibility",
                    onClick = onNavigateToCompatibility,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
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
    "grey" -> Color(0xFF808080)
    "pink" -> Color(0xFFE0809B)
    "jade" -> TigerJade
    else -> TigerGold
}
