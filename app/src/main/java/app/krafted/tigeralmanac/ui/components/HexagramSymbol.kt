package app.krafted.tigeralmanac.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight

@Composable
fun HexagramSymbol(
    symbol: String,
    modifier: Modifier = Modifier,
    lineColor: Color = TigerGoldLight,
    animated: Boolean = false,
) {
    val lines = symbol.flatMap { trigramLines(it) }
    var visible by remember { mutableStateOf(!animated) }
    LaunchedEffect(animated) { if (animated) visible = true }
    Column(
        modifier = modifier.width(64.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        lines.forEachIndexed { index, yang ->
            val progress by animateFloatAsState(
                targetValue = if (visible) 1f else 0f,
                animationSpec = tween(durationMillis = 300, delayMillis = index * 120),
                label = "hexagramLine",
            )
            HexagramLine(yang = yang, color = lineColor, progress = progress)
        }
    }
}

@Composable
private fun HexagramLine(yang: Boolean, color: Color, progress: Float = 1f) {
    val shape = RoundedCornerShape(2.dp)
    if (yang) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .alpha(progress)
                .height(6.dp)
                .clip(shape)
                .background(color),
        )
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth(progress)
                .alpha(progress),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(shape)
                    .background(color),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(shape)
                    .background(color),
            )
        }
    }
}

private fun trigramLines(c: Char): List<Boolean> = when (c) {
    '☰' -> listOf(true, true, true)
    '☱' -> listOf(false, true, true)
    '☲' -> listOf(true, false, true)
    '☳' -> listOf(false, false, true)
    '☴' -> listOf(true, true, false)
    '☵' -> listOf(false, true, false)
    '☶' -> listOf(true, false, false)
    '☷' -> listOf(false, false, false)
    else -> listOf(true, true, true)
}
