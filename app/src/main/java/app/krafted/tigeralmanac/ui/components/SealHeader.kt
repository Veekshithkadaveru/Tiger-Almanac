package app.krafted.tigeralmanac.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight

@Composable
fun SealHeader(
    title: String,
    subtitle: String,
    @DrawableRes symbolRes: Int? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (symbolRes != null) {
            Image(
                painter = painterResource(symbolRes),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                color = TigerGoldLight,
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
            )
            Text(
                text = subtitle,
                color = TigerCream.copy(alpha = 0.72f),
                fontFamily = InterFont,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp,
            )
        }
    }
}
