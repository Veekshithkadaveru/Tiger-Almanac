package app.krafted.tigeralmanac.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerInk
import app.krafted.tigeralmanac.ui.theme.TigerJade
import app.krafted.tigeralmanac.ui.theme.TigerRed

enum class TagTone {
    GOLD, RED, JADE, INK
}

@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    tone: TagTone = TagTone.GOLD
) {
    val (bgColor, borderColor, textColor) = when (tone) {
        TagTone.GOLD -> Triple(
            TigerGold.copy(alpha = 0.14f),
            TigerGold.copy(alpha = 0.5f),
            TigerGoldLight
        )

        TagTone.RED -> Triple(
            TigerRed.copy(alpha = 0.18f),
            TigerRed.copy(alpha = 0.55f),
            Color(0xFFFFB0A8)
        )

        TagTone.JADE -> Triple(
            TigerJade.copy(alpha = 0.18f),
            TigerJade.copy(alpha = 0.55f),
            Color(0xFF9BE0C2)
        )

        TagTone.INK -> Triple(
            TigerInk.copy(alpha = 0.5f),
            TigerCream.copy(alpha = 0.3f),
            TigerCream
        )
    }

    val shape = RoundedCornerShape(999.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(bgColor)
            .border(1.dp, borderColor, shape)
            .padding(horizontal = 9.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontFamily = FontFamily.SansSerif,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp,
            maxLines = 1,
            softWrap = false
        )
    }
}
