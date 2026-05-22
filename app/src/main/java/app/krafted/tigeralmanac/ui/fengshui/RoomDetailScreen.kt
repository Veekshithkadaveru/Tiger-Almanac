package app.krafted.tigeralmanac.ui.fengshui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.model.FengShuiTip
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.components.Tag
import app.krafted.tigeralmanac.ui.components.TagTone
import app.krafted.tigeralmanac.ui.components.rememberDrawableId
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.FengShuiViewModel

@Composable
fun RoomDetailScreen(
    viewModel: FengShuiViewModel,
    roomId: String?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(roomId) {
        roomId?.let { viewModel.selectRoom(it) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TigerSurface),
    ) {
        val room = state.selectedRoom
        if (room == null) {
            CircularProgressIndicator(
                color = TigerGold,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            ScreenBackground(
                imageRes = rememberDrawableId(room.background),
                accent = TigerGold,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(12.dp))
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

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(rememberDrawableId(room.symbol)),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = room.name,
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 34.sp,
                    color = TigerGoldLight,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = room.chineseName,
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    fontSize = 22.sp,
                    color = TigerCream,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = room.energyRole,
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    letterSpacing = 0.25.sp,
                    color = TigerCream,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(24.dp))

                room.tips.forEachIndexed { index, tip ->
                    val bookmarked = state.bookmarkedTipIds.contains(
                        FengShuiViewModel.tipId(room.id, index)
                    )
                    TipCard(
                        tip = tip,
                        bookmarked = bookmarked,
                        onToggle = { viewModel.toggleBookmark(room.id, index) },
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TipCard(
    tip: FengShuiTip,
    bookmarked: Boolean,
    onToggle: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable { expanded = !expanded }
            .then(
                if (bookmarked) {
                    Modifier.border(1.5.dp, TigerGold, shape)
                } else {
                    Modifier
                }
            )
            .animateContentSize(),
    ) {
        GoldFrame(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = tip.title,
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = TigerGoldLight,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (bookmarked) "★" else "☆",
                    fontSize = 22.sp,
                    color = if (bookmarked) TigerGoldLight else TigerCream.copy(alpha = 0.6f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onToggle() }
                        .padding(4.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Tag(text = tip.category, tone = TagTone.INK)
                Tag(text = tip.priority, tone = priorityTone(tip.priority))
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = tip.body,
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    letterSpacing = 0.25.sp,
                    color = TigerCream,
                )
            }
        }
    }
}

private fun priorityTone(priority: String): TagTone = when (priority.trim().lowercase()) {
    "essential" -> TagTone.RED
    "important" -> TagTone.GOLD
    "recommended" -> TagTone.JADE
    "optional" -> TagTone.INK
    else -> TagTone.JADE
}
