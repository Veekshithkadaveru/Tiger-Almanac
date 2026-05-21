package app.krafted.tigeralmanac.ui.zodiac

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import app.krafted.tigeralmanac.model.CompatibilityLevel
import app.krafted.tigeralmanac.model.ZodiacAnimal
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.components.SealHeader
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerCreamSoft
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerInk
import app.krafted.tigeralmanac.ui.theme.TigerJade
import app.krafted.tigeralmanac.ui.theme.TigerRed
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.ZodiacViewModel

@Composable
fun CompatibilityScreen(
    viewModel: ZodiacViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp),
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

                SealHeader(
                    title = "Compatibility",
                    subtitle = "TAP A SIGN TO REVEAL THE BOND",
                    symbolRes = R.drawable.tiger004_sym_3,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(20.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(ZodiacAnimal.values().toList()) { animal ->
                        AnimalCard(
                            animal = animal,
                            isOwn = animal == state.animal,
                            onClick = { viewModel.selectCompatibilityAnimal(animal) },
                        )
                    }
                }
            }

            val selected = state.selectedCompatibility
            AnimatedVisibility(
                visible = selected != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                if (selected != null) {
                    CompatibilityPanel(
                        animal = selected.animal,
                        level = selected.level,
                        description = selected.description,
                        onDismiss = { viewModel.clearCompatibility() },
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimalCard(
    animal: ZodiacAnimal,
    isOwn: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(shape)
            .background(TigerInk.copy(alpha = 0.55f))
            .border(
                width = if (isOwn) 2.dp else 1.dp,
                color = if (isOwn) TigerGold else TigerGold.copy(alpha = 0.25f),
                shape = shape,
            )
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = animal.emoji,
            fontSize = 34.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = animal.displayName,
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = TigerGoldLight,
            textAlign = TextAlign.Center,
        )
        Text(
            text = animal.chineseName,
            fontFamily = CormorantGaramond,
            fontStyle = FontStyle.Italic,
            fontSize = 14.sp,
            color = TigerCream,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CompatibilityPanel(
    animal: ZodiacAnimal,
    level: CompatibilityLevel,
    description: String,
    onDismiss: () -> Unit,
) {
    val accent = levelColour(level)
    GoldFrame(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = animal.emoji,
                fontSize = 30.sp,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = animal.displayName,
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = TigerGoldLight,
                )
                Text(
                    text = levelLabel(level),
                    fontFamily = InterFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    letterSpacing = 1.5.sp,
                    color = accent,
                )
            }
            Text(
                text = "✕",
                fontSize = 18.sp,
                color = TigerCreamSoft,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onDismiss() }
                    .padding(8.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(accent.copy(alpha = 0.6f)),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = description,
            fontFamily = InterFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.25.sp,
            color = TigerCream,
        )
    }
}

private fun levelColour(level: CompatibilityLevel): Color = when (level) {
    CompatibilityLevel.EXCELLENT -> TigerJade
    CompatibilityLevel.GOOD -> TigerGold
    CompatibilityLevel.NEUTRAL -> TigerCream
    CompatibilityLevel.CHALLENGING -> TigerRed
}

private fun levelLabel(level: CompatibilityLevel): String = when (level) {
    CompatibilityLevel.EXCELLENT -> "Excellent"
    CompatibilityLevel.GOOD -> "Good"
    CompatibilityLevel.NEUTRAL -> "Neutral"
    CompatibilityLevel.CHALLENGING -> "Challenging"
}
