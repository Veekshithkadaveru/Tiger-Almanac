package app.krafted.tigeralmanac.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.R
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.GoldParticles
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.components.SealHeader
import app.krafted.tigeralmanac.ui.components.Tag
import app.krafted.tigeralmanac.ui.components.TagTone
import app.krafted.tigeralmanac.ui.components.drawBehindUnderline
import app.krafted.tigeralmanac.ui.components.entrance
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerCreamSoft
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.SearchResult
import app.krafted.tigeralmanac.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBack: () -> Unit,
    onOpenHexagram: (Int) -> Unit,
    onOpenAnimal: (String) -> Unit,
    onOpenRoom: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TigerSurface),
    ) {
        ScreenBackground(
            imageRes = R.drawable.tiger004_back_3,
            accent = TigerGold,
        )
        GoldParticles(count = 16)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                BackRow(onBack = onBack)
                Spacer(modifier = Modifier.height(8.dp))
                SealHeader(
                    title = "搜尋",
                    subtitle = "SEARCH THE ALMANAC",
                    symbolRes = R.drawable.app_logo,
                )
                Spacer(modifier = Modifier.height(8.dp))
                SearchBar(
                    query = state.query,
                    onQueryChange = viewModel::onQueryChange,
                    focusRequester = focusRequester,
                )
            }

            if (!state.hasQuery) {
                item {
                    EmptyState(onSuggestion = viewModel::onQueryChange)
                }
            } else if (!state.hasResults && !state.isLoading) {
                item {
                    NoResultsState(query = state.query)
                }
            } else {
                if (state.ichingResults.isNotEmpty()) {
                    item { SectionLabel("I CHING") }
                    itemsIndexed(state.ichingResults, key = { _, it -> "iching-${it.id}" }) { index, result ->
                        IChingRow(
                            result = result,
                            onClick = { onOpenHexagram(result.id) },
                            modifier = Modifier.entrance(index = index),
                        )
                    }
                }
                if (state.zodiacResults.isNotEmpty()) {
                    item { SectionLabel("ZODIAC") }
                    itemsIndexed(state.zodiacResults, key = { _, it -> "zodiac-${it.id}" }) { index, result ->
                        ZodiacRow(
                            result = result,
                            onClick = { onOpenAnimal(result.id) },
                            modifier = Modifier.entrance(index = index),
                        )
                    }
                }
                if (state.fengShuiResults.isNotEmpty()) {
                    item { SectionLabel("FENG SHUI") }
                    itemsIndexed(
                        state.fengShuiResults,
                        key = { _, it -> "fengshui-${it.roomId}-${it.tipTitle}" },
                    ) { index, result ->
                        FengShuiRow(
                            result = result,
                            onClick = { onOpenRoom(result.roomId) },
                            modifier = Modifier.entrance(index = index),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BackRow(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onBack() }
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "‹ Back",
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TigerGoldLight,
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        textStyle = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 22.sp,
            color = TigerGoldLight,
            letterSpacing = 0.5.sp,
        ),
        cursorBrush = SolidColor(TigerGoldLight),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = "Search the almanac…",
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        color = TigerGoldLight.copy(alpha = 0.4f),
                        letterSpacing = 0.5.sp,
                    )
                }
                innerTextField()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .drawBehindUnderline(),
    )
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = InterFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 2.sp,
        color = TigerCream.copy(alpha = 0.72f),
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
    )
}

@Composable
private fun ResultRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    GoldFrame(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
    ) {
        content()
    }
}

@Composable
private fun IChingRow(
    result: SearchResult.IChing,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResultRow(onClick = onClick, modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "#${result.number}  ${result.name}",
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = TigerGoldLight,
                )
                Text(
                    text = result.englishName,
                    fontFamily = InterFont,
                    fontSize = 12.sp,
                    color = TigerCream,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Tag(text = result.element, tone = TagTone.GOLD)
        }
    }
}

@Composable
private fun ZodiacRow(
    result: SearchResult.Zodiac,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResultRow(onClick = onClick, modifier = modifier) {
        Text(
            text = "${result.emoji}  ${result.name}",
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = TigerGoldLight,
        )
    }
}

@Composable
private fun FengShuiRow(
    result: SearchResult.FengShui,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResultRow(onClick = onClick, modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.roomName,
                    fontFamily = CormorantGaramond,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = TigerGoldLight,
                )
                Text(
                    text = result.tipTitle,
                    fontFamily = InterFont,
                    fontSize = 12.sp,
                    color = TigerCream,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Tag(text = result.category, tone = TagTone.JADE)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmptyState(onSuggestion: (String) -> Unit) {
    val suggestions = listOf("strength", "patience", "bedroom")
    GoldFrame(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Search hexagrams, zodiac animals, and feng shui wisdom across the almanac.",
            fontFamily = CormorantGaramond,
            fontStyle = FontStyle.Italic,
            fontSize = 17.sp,
            lineHeight = 24.sp,
            color = TigerCream,
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "TRY",
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            letterSpacing = 2.sp,
            color = TigerCreamSoft,
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { term ->
                Tag(
                    text = term,
                    tone = TagTone.INK,
                    modifier = Modifier.clickable { onSuggestion(term) },
                )
            }
        }
    }
}

@Composable
private fun NoResultsState(query: String) {
    GoldFrame(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Nothing found for “$query”. Try a different word — the almanac is vast.",
            fontFamily = CormorantGaramond,
            fontStyle = FontStyle.Italic,
            fontSize = 17.sp,
            lineHeight = 24.sp,
            color = TigerCream,
        )
    }
}
