package app.krafted.tigeralmanac.ui.fengshui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.R
import app.krafted.tigeralmanac.model.Room
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.components.SealHeader
import app.krafted.tigeralmanac.ui.components.Tag
import app.krafted.tigeralmanac.ui.components.TagTone
import app.krafted.tigeralmanac.ui.components.rememberDrawableId
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerInk
import app.krafted.tigeralmanac.ui.theme.TigerJade
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.FengShuiViewModel
import kotlinx.coroutines.delay

@Composable
fun RoomSelectScreen(
    viewModel: FengShuiViewModel,
    onBack: () -> Unit,
    onSelectRoom: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TigerSurface),
    ) {
        ScreenBackground(
            imageRes = R.drawable.tiger004_back_4,
            accent = TigerJade,
        )

        if (state.isLoading) {
            CircularProgressIndicator(
                color = TigerGold,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    TopBar(onBack = onBack)
                }
                item {
                    SealHeader(
                        title = "風水",
                        subtitle = "FENG SHUI",
                        symbolRes = R.drawable.tiger004_sym_6,
                    )
                }
                itemsIndexed(state.rooms) { index, room ->
                    StaggerItem(index = index) {
                        RoomCard(room = room, onClick = { onSelectRoom(room.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    onBack: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "‹ Back",
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TigerGoldLight,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onBack() }
                .padding(vertical = 4.dp, horizontal = 4.dp),
        )
    }
}

@Composable
private fun StaggerItem(
    index: Int,
    content: @Composable () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 60L)
        visible = true
    }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "cardAlpha",
    )
    val offset by animateFloatAsState(
        targetValue = if (visible) 0f else 18f,
        animationSpec = tween(durationMillis = 350),
        label = "cardOffset",
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .padding(top = offset.dp),
    ) {
        content()
    }
}

@Composable
private fun RoomCard(
    room: Room,
    onClick: () -> Unit,
) {
    GoldFrame(
        modifier = Modifier
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
                    painter = painterResource(rememberDrawableId(room.symbol)),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = room.name,
                    color = TigerGoldLight,
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
                Text(
                    text = room.chineseName,
                    color = TigerCream.copy(alpha = 0.72f),
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    fontSize = 13.sp,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = room.principle,
                    color = TigerCream.copy(alpha = 0.78f),
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Tag(text = "${room.tips.size} TIPS", tone = TagTone.JADE)
            }
        }
    }
}
