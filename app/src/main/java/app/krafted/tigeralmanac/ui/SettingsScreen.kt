package app.krafted.tigeralmanac.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.tigeralmanac.R
import app.krafted.tigeralmanac.model.ZodiacAnimal
import app.krafted.tigeralmanac.ui.components.ConfirmDialog
import app.krafted.tigeralmanac.ui.components.GoldFrame
import app.krafted.tigeralmanac.ui.components.GoldParticles
import app.krafted.tigeralmanac.ui.components.RibbonButton
import app.krafted.tigeralmanac.ui.components.ScreenBackground
import app.krafted.tigeralmanac.ui.components.SealHeader
import app.krafted.tigeralmanac.ui.components.YearPicker
import app.krafted.tigeralmanac.ui.components.drawBehindUnderline
import app.krafted.tigeralmanac.ui.components.entrance
import app.krafted.tigeralmanac.ui.theme.CormorantGaramond
import app.krafted.tigeralmanac.ui.theme.InterFont
import app.krafted.tigeralmanac.ui.theme.TigerCream
import app.krafted.tigeralmanac.ui.theme.TigerGold
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import app.krafted.tigeralmanac.ui.theme.TigerRed
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.UserProfileViewModel

@Composable
fun SettingsScreen(
    viewModel: UserProfileViewModel,
    onBack: () -> Unit,
    onResetComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()

    var showYearPicker by remember { mutableStateOf(false) }
    var showNameEditor by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TigerSurface)
    ) {
        ScreenBackground(
            imageRes = R.drawable.tiger004_back_5,
            dark = 0.66f,
            accent = TigerRed
        )

        GoldParticles(count = 12)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onBack() }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "‹ Back",
                        color = TigerCream.copy(alpha = 0.82f),
                        fontFamily = CormorantGaramond,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            item {
                SealHeader(
                    title = "Settings",
                    subtitle = "設定",
                    symbolRes = R.drawable.app_logo,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            when (val current = profile) {
                null -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = TigerGold,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                else -> {
                    val animal = ZodiacAnimal.calculateZodiacAnimal(current.birthYear)
                    item {
                        GoldFrame(
                            modifier = Modifier
                                .fillMaxWidth()
                                .entrance(index = 0),
                            padding = 18.dp
                        ) {
                            Text(
                                text = current.name,
                                color = TigerGoldLight,
                                fontFamily = CormorantGaramond,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 26.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${animal.emoji} ${animal.displayName} (${animal.chineseName})",
                                color = TigerCream,
                                fontFamily = InterFont,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Born ${current.birthYear}",
                                color = TigerCream.copy(alpha = 0.7f),
                                fontFamily = InterFont,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    item {
                        SettingsRow(
                            label = "Change Birth Year",
                            value = current.birthYear.toString(),
                            onClick = { showYearPicker = true },
                            modifier = Modifier.entrance(index = 1)
                        )
                    }

                    item {
                        SettingsRow(
                            label = "Change Name",
                            value = current.name,
                            onClick = { showNameEditor = true },
                            modifier = Modifier.entrance(index = 2)
                        )
                    }

                    item {
                        SettingsRow(
                            label = "Reset Profile",
                            value = "",
                            onClick = { showResetConfirm = true },
                            modifier = Modifier.entrance(index = 3)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tiger Almanac v1.0",
                    color = TigerCream.copy(alpha = 0.5f),
                    fontFamily = InterFont,
                    fontSize = 11.sp,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (showYearPicker && profile != null) {
            BirthYearOverlay(
                initialYear = profile!!.birthYear,
                onSave = {
                    viewModel.updateBirthYear(it)
                    showYearPicker = false
                },
                onDismiss = { showYearPicker = false }
            )
        }

        if (showNameEditor && profile != null) {
            NameEditorOverlay(
                initialName = profile!!.name,
                onSave = {
                    viewModel.updateName(it)
                    showNameEditor = false
                },
                onDismiss = { showNameEditor = false }
            )
        }

        if (showResetConfirm) {
            ConfirmDialog(
                title = "Reset Profile",
                message = "This will clear your saved name, birth year, and onboarding. You will return to setup. This cannot be undone.",
                confirmLabel = "RESET",
                onConfirm = {
                    showResetConfirm = false
                    viewModel.resetProfile(onComplete = onResetComplete)
                },
                onDismiss = { showResetConfirm = false }
            )
        }
    }
}

@Composable
private fun SettingsRow(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GoldFrame(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        padding = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = TigerGoldLight,
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (value.isNotBlank()) {
                    Text(
                        text = value,
                        color = TigerCream.copy(alpha = 0.72f),
                        fontFamily = InterFont,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    text = "›",
                    color = TigerGold.copy(alpha = 0.7f),
                    fontFamily = CormorantGaramond,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
private fun BirthYearOverlay(
    initialYear: Int,
    onSave: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var pickedYear by remember { mutableStateOf(initialYear) }
    val activeAnimal = remember(pickedYear) { ZodiacAnimal.calculateZodiacAnimal(pickedYear) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        GoldFrame(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {},
            padding = 20.dp,
            radius = 18.dp
        ) {
            Text(
                text = "Change Birth Year",
                color = TigerGoldLight,
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            YearPicker(
                selectedYear = pickedYear,
                onYearChange = { pickedYear = it },
                activeAnimal = activeAnimal
            )
            Spacer(modifier = Modifier.height(20.dp))
            RibbonButton(
                text = "SAVE",
                onClick = { onSave(pickedYear) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun NameEditorOverlay(
    initialName: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initialName) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        GoldFrame(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {},
            padding = 20.dp,
            radius = 18.dp
        ) {
            Text(
                text = "Change Name",
                color = TigerGoldLight,
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                textStyle = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp,
                    color = TigerGoldLight,
                    letterSpacing = 1.sp
                ),
                cursorBrush = SolidColor(TigerGoldLight),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "Enter your name…",
                                fontFamily = FontFamily.Serif,
                                fontSize = 24.sp,
                                color = TigerGoldLight.copy(alpha = 0.4f),
                                letterSpacing = 1.sp
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehindUnderline()
            )
            Spacer(modifier = Modifier.height(24.dp))
            RibbonButton(
                text = "SAVE",
                onClick = { onSave(text) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
