package app.krafted.tigeralmanac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.krafted.tigeralmanac.ui.theme.TigerAlmanacTheme
import app.krafted.tigeralmanac.ui.theme.TigerSurface

object Routes {
    const val SPLASH = "splash"
    const val PROFILE_SETUP = "profile_setup"
    const val HOME = "home"
    const val ICHING = "iching"
    const val ICHING_ARCHIVE = "iching_archive"
    const val ZODIAC = "zodiac"
    const val ANIMAL_PROFILE = "animal_profile"
    const val COMPATIBILITY = "compatibility"
    const val FENGSHUI_ROOMS = "fengshui_rooms"
    const val FENGSHUI_DETAIL = "fengshui_detail"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TigerAlmanacTheme {
                val navController = rememberNavController()
                TigerAlmanacNavHost(navController)
            }
        }
    }
}

@Composable
fun TigerAlmanacNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) { PlaceholderScreen(Routes.SPLASH) }
        composable(Routes.PROFILE_SETUP) { PlaceholderScreen(Routes.PROFILE_SETUP) }
        composable(Routes.HOME) { PlaceholderScreen(Routes.HOME) }
        composable(Routes.ICHING) { PlaceholderScreen(Routes.ICHING) }
        composable(Routes.ICHING_ARCHIVE) { PlaceholderScreen(Routes.ICHING_ARCHIVE) }
        composable(Routes.ZODIAC) { PlaceholderScreen(Routes.ZODIAC) }
        composable(Routes.ANIMAL_PROFILE) { PlaceholderScreen(Routes.ANIMAL_PROFILE) }
        composable(Routes.COMPATIBILITY) { PlaceholderScreen(Routes.COMPATIBILITY) }
        composable(Routes.FENGSHUI_ROOMS) { PlaceholderScreen(Routes.FENGSHUI_ROOMS) }
        composable(Routes.FENGSHUI_DETAIL) { PlaceholderScreen(Routes.FENGSHUI_DETAIL) }
        composable(Routes.SEARCH) { PlaceholderScreen(Routes.SEARCH) }
        composable(Routes.SETTINGS) { PlaceholderScreen(Routes.SETTINGS) }
    }
}

@Composable
fun PlaceholderScreen(routeName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TigerSurface),
        contentAlignment = Alignment.Center
    ) {
        Text(routeName)
    }
}
