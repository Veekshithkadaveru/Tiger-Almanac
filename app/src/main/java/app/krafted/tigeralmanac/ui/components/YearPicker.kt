package app.krafted.tigeralmanac.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.model.ZodiacAnimal
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerInk

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun YearPicker(
    selectedYear: Int,
    onYearChange: (Int) -> Unit,
    activeAnimal: ZodiacAnimal,
    modifier: Modifier = Modifier
) {
    val years = remember { (1924..2006).toList() }
    val initialIndex = remember { years.indexOf(selectedYear).coerceAtLeast(0) }
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)

    LaunchedEffect(lazyListState.firstVisibleItemIndex) {
        val centerIndex = lazyListState.firstVisibleItemIndex
        if (centerIndex in years.indices) {
            onYearChange(years[centerIndex])
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoldFrame(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            padding = 0.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = lazyListState,
                    flingBehavior = snapFlingBehavior,
                    contentPadding = PaddingValues(vertical = 66.dp),
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(years) { _, y ->
                        val isSelected = y == selectedYear
                        Text(
                            text = y.toString(),
                            fontFamily = FontFamily.Serif,
                            fontSize = if (isSelected) 32.sp else 20.sp,
                            color = if (isSelected) TigerGoldLight else TigerCream.copy(alpha = 0.82f),
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            letterSpacing = 2.sp,
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .clickable {
                                    val idx = years.indexOf(y)
                                    if (idx >= 0) {
                                        onYearChange(y)
                                    }
                                }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .height(44.dp)
                        .padding(horizontal = 14.dp)
                        .border(1.dp, TigerGold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TigerInk.copy(alpha = 0.55f), RoundedCornerShape(12.dp))
                .border(1.dp, TigerGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activeAnimal.emoji,
                fontSize = 28.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Column {
                Text(
                    text = "Year of the ${activeAnimal.displayName} · ${activeAnimal.chineseName}",
                    color = TigerGoldLight,
                    fontFamily = FontFamily.Serif,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${activeAnimal.element} element",
                    color = TigerCream.copy(alpha = 0.86f),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp
                )
            }
        }
    }
}
