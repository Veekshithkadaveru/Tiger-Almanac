package app.krafted.tigeralmanac.ui.components

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import app.krafted.tigeralmanac.ui.theme.TigerGold

@Composable
fun rememberDrawableId(name: String): Int {
    val context = LocalContext.current
    return remember(name) {
        context.resources.getIdentifier(name, "drawable", context.packageName)
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
