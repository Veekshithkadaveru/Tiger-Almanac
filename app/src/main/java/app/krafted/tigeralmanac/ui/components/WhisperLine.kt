package app.krafted.tigeralmanac.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold

@Composable
fun WhisperLine(
    sectionLabel: String,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = sectionLabel,
                color = TigerGold,
                fontFamily = InterFont,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                modifier = Modifier.width(60.dp),
            )
            Text(
                text = text,
                color = TigerCream,
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
            )
            Text(
                text = "›",
                color = TigerGold,
                fontFamily = InterFont,
                fontSize = 16.sp,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(TigerGold.copy(alpha = 0.25f)),
        )
    }
}
