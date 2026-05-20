package app.krafted.tigeralmanac.ui.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight

@Composable
fun HexagramSymbol(
    symbol: String,
    modifier: Modifier = Modifier,
    lineColor: Color = TigerGoldLight,
) {
    val lines = symbol.flatMap { trigramLines(it) }
    Column(
        modifier = modifier.width(64.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        lines.forEach { yang ->
            HexagramLine(yang = yang, color = lineColor)
        }
    }
}

@Composable
private fun HexagramLine(yang: Boolean, color: Color) {
    val shape = RoundedCornerShape(2.dp)
    if (yang) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(shape)
                .background(color),
        )
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
