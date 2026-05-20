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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.krafted.tigeralmanac.data.ZodiacRepository
import app.krafted.tigeralmanac.data.db.AppDatabase
import app.krafted.tigeralmanac.ui.ProfileSetupScreen
import app.krafted.tigeralmanac.ui.SplashScreen
import app.krafted.tigeralmanac.ui.theme.TigerAlmanacTheme
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.UserProfileViewModel

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

        val database = AppDatabase.getDatabase(this)
        val userProfileDao = database.userProfileDao()
        val zodiacRepository = ZodiacRepository(this)
        val userProfileViewModelFactory =
            UserProfileViewModel.factory(userProfileDao, zodiacRepository)

        setContent {
            TigerAlmanacTheme {
                val navController = rememberNavController()
                val userProfileViewModel: UserProfileViewModel =
                    viewModel(factory = userProfileViewModelFactory)

                TigerAlmanacNavHost(
                    navController = navController,
                    userProfileViewModel = userProfileViewModel
                )
            }
        }
    }
}

@Composable
fun TigerAlmanacNavHost(
    navController: NavHostController,
    userProfileViewModel: UserProfileViewModel
) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                viewModel = userProfileViewModel,
                onEnterAlmanac = {
                    navController.navigate(Routes.PROFILE_SETUP) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onSkipToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.PROFILE_SETUP) {
            ProfileSetupScreen(
                viewModel = userProfileViewModel,
                onComplete = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.PROFILE_SETUP) { inclusive = true }
                    }
                }
            )
        }
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
        Text(
            text = routeName,
            color = app.krafted.tigeralmanac.ui.theme.TigerGold,
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp
        )
    }
}
