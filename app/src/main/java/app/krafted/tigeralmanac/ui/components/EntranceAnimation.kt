package app.krafted.tigeralmanac.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.entrance(index: Int = 0, delayPerItemMs: Int = 60): Modifier {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, delayMillis = index * delayPerItemMs),
        label = "entranceAlpha",
    )
    val translateY by animateFloatAsState(
        targetValue = if (visible) 0f else 12f,
        animationSpec = tween(durationMillis = 300, delayMillis = index * delayPerItemMs),
        label = "entranceTranslateY",
    )
    return this.graphicsLayer {
        this.alpha = alpha
        translationY = translateY.dp.toPx()
    }
}
