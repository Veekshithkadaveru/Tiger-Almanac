package app.krafted.tigeralmanac.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import app.krafted.tigeralmanac.ui.theme.TigerInk
import app.krafted.tigeralmanac.ui.theme.TigerRed
import app.krafted.tigeralmanac.ui.theme.TigerSurface

@Composable
fun ScreenBackground(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
    dark: Float = 0.62f,
    accent: Color = TigerRed,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            TigerSurface.copy(alpha = dark * 0.5f),
                            TigerInk.copy(alpha = dark),
                            TigerSurface.copy(alpha = (dark + 0.22f).coerceAtMost(0.95f)),
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.55f to Color.Transparent,
                        1f to accent.copy(alpha = 0.28f),
                    )
                )
        )
    }
}
