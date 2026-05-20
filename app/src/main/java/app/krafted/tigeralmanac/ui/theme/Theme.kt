package app.krafted.tigeralmanac.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ImperialColorScheme = darkColorScheme(
    primary = TigerRed,
    onPrimary = TigerGoldLight,
    secondary = TigerGold,
    onSecondary = TigerInk,
    tertiary = TigerJade,
    onTertiary = TigerCream,
    background = TigerSurface,
    onBackground = TigerCream,
    surface = TigerSurface,
    onSurface = TigerCream,
    surfaceVariant = TigerInk,
    onSurfaceVariant = TigerCreamSoft,
    error = TigerRedDark,
    onError = TigerGoldLight,
)

@Composable
fun TigerAlmanacTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ImperialColorScheme,
        typography = Typography,
        content = content
    )
}
