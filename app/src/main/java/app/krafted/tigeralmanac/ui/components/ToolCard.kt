package app.krafted.tigeralmanac.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerInk

@Composable
fun ToolCard(
    title: String,
    subtitle: String,
    @DrawableRes symbolRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GoldFrame(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            val boxShape = RoundedCornerShape(12.dp)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(boxShape)
                    .background(TigerInk)
                    .border(1.dp, TigerGold.copy(alpha = 0.4f), boxShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(symbolRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = title,
                    color = TigerGoldLight,
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
                Text(
                    text = subtitle,
                    color = TigerCream.copy(alpha = 0.78f),
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                )
            }
            Text(
                text = "›",
                color = TigerGold,
                fontFamily = InterFont,
                fontSize = 20.sp,
            )
        }
    }
}
