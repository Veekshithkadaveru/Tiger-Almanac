package app.krafted.tigeralmanac.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.R
import app.krafted.tigeralmanac.ui.components.GoldParticles
import app.krafted.tigeralmanac.ui.components.RibbonButton
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerRed
import app.krafted.tigeralmanac.viewmodel.UserProfileViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: UserProfileViewModel,
    onEnterAlmanac: () -> Unit,
    onSkipToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profileState by viewModel.profile.collectAsStateWithLifecycle()
    var phase by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(700)
        phase = 1
        delay(900)
        phase = 2
    }

    LaunchedEffect(profileState) {
        val profile = profileState
        if (profile != null && profile.setupComplete) {
            delay(1200)
            onSkipToHome()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF2A1108), Color(0xFF0B0503))
                )
            )
    ) {
        ScreenBackground(
            imageRes = R.drawable.tiger004_back_1,
            dark = 0.72f,
            accent = TigerRed
        )

        GoldParticles(count = 26)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val scale by animateFloatAsState(
                targetValue = if (phase >= 1) 1f else 0.7f,
                animationSpec = tween(1100, easing = EaseOutBack),
                label = "scale"
            )
            val alpha by animateFloatAsState(
                targetValue = if (phase >= 1) 1f else 0f,
                animationSpec = tween(1100),
                label = "alpha"
            )

            Box(
                modifier = Modifier
                    .size(160.dp)
                    .scale(scale)
                    .alpha(alpha)
                    .clip(RoundedCornerShape(36.dp))
                    .border(1.5.dp, TigerGold.copy(alpha = 0.7f), RoundedCornerShape(36.dp))
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            AnimatedVisibility(
                visible = phase >= 1,
                enter = fadeIn(animationSpec = tween(900)) + slideInVertically(
                    initialOffsetY = { 12 },
                    animationSpec = tween(900)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "虎 曆",
                        color = TigerGoldLight,
                        fontFamily = FontFamily.Serif,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 8.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "TIGER ALMANAC",
                        color = TigerCream,
                        fontFamily = FontFamily.Serif,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 4.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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

            Spacer(modifier = Modifier.height(56.dp))

            val showEnterButton =
                phase >= 2 && (profileState == null || !profileState!!.setupComplete)
            AnimatedVisibility(
                visible = showEnterButton,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    initialOffsetY = { 12 },
                    animationSpec = tween(600)
                )
            ) {
                RibbonButton(
                    text = "Enter the Almanac  ⌬",
                    onClick = onEnterAlmanac
                )
            }
        }
    }
}
