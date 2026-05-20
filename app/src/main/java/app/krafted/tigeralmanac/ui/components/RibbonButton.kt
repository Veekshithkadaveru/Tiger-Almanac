package app.krafted.tigeralmanac.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerRedDark

@Composable
fun RibbonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
) {
    val shape = RoundedCornerShape(999.dp)
    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.45f)
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFD63839), Color(0xFFA01A1D), TigerRedDark)
                )
            )
            .border(1.dp, TigerGold.copy(alpha = 0.85f), shape)
            .clickable(enabled = enabled) { onClick() }
            .padding(
                if (small) PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                else PaddingValues(horizontal = 22.dp, vertical = 14.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = TigerGoldLight,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = if (small) 14.sp else 16.sp,
            letterSpacing = 1.2.sp,
            textAlign = TextAlign.Center,
        )
    }
}
