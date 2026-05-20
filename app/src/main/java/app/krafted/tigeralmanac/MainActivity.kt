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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.krafted.tigeralmanac.data.ZodiacRepository
import app.krafted.tigeralmanac.data.db.AppDatabase
import app.krafted.tigeralmanac.ui.HomeScreen
import app.krafted.tigeralmanac.ui.ProfileSetupScreen
import app.krafted.tigeralmanac.ui.SplashScreen
import app.krafted.tigeralmanac.ui.iching.HexagramArchiveScreen
import app.krafted.tigeralmanac.ui.iching.TodaysHexagramScreen
import app.krafted.tigeralmanac.ui.theme.TigerAlmanacTheme
import app.krafted.tigeralmanac.ui.theme.TigerSurface
import app.krafted.tigeralmanac.viewmodel.HomeViewModel
import app.krafted.tigeralmanac.viewmodel.IChingViewModel
import app.krafted.tigeralmanac.viewmodel.UserProfileViewModel

object Routes {
    const val SPLASH = "splash"
    const val PROFILE_SETUP = "profile_setup"
    const val HOME = "home"
    const val ICHING = "iching"
    const val ICHING_ROUTE = "iching?hexagramId={hexagramId}"
    const val ICHING_ARCHIVE = "iching_archive"

    fun ichingWithHexagram(hexagramId: Int) = "iching?hexagramId=$hexagramId"
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
        val hexagramHistoryDao = database.hexagramHistoryDao()
        val zodiacRepository = ZodiacRepository(this)
        val userProfileViewModelFactory =
            UserProfileViewModel.factory(userProfileDao, zodiacRepository)
        val homeViewModelFactory = HomeViewModel.factory(this, userProfileDao)
        val iChingViewModelFactory =
            IChingViewModel.factory(this, userProfileDao, hexagramHistoryDao)

        setContent {
            TigerAlmanacTheme {
                val navController = rememberNavController()
                val userProfileViewModel: UserProfileViewModel =
                    viewModel(factory = userProfileViewModelFactory)
                val iChingViewModel: IChingViewModel =
                    viewModel(factory = iChingViewModelFactory)

                TigerAlmanacNavHost(
                    navController = navController,
                    userProfileViewModel = userProfileViewModel,
                    homeViewModelFactory = homeViewModelFactory,
                    iChingViewModel = iChingViewModel
                )
            }
        }
    }
}

@Composable
fun TigerAlmanacNavHost(
    navController: NavHostController,
    userProfileViewModel: UserProfileViewModel,
    homeViewModelFactory: ViewModelProvider.Factory,
    iChingViewModel: IChingViewModel
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
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateIching = { navController.navigate(Routes.ICHING) },
                onNavigateZodiac = { navController.navigate(Routes.ZODIAC) },
                onNavigateFengshui = { navController.navigate(Routes.FENGSHUI_ROOMS) },
                onNavigateProfile = { navController.navigate(Routes.SETTINGS) },
            )
        }
        composable(
            route = Routes.ICHING_ROUTE,
            arguments = listOf(
                navArgument("hexagramId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val hexagramId = backStackEntry.arguments?.getString("hexagramId")?.toIntOrNull()
            TodaysHexagramScreen(
                viewModel = iChingViewModel,
                hexagramId = hexagramId,
                onBack = { navController.popBackStack() },
                onViewArchive = { navController.navigate(Routes.ICHING_ARCHIVE) },
            )
        }
        composable(Routes.ICHING_ARCHIVE) {
            HexagramArchiveScreen(
                viewModel = iChingViewModel,
                onBack = { navController.popBackStack() },
                onOpenHexagram = { id ->
                    navController.navigate(Routes.ichingWithHexagram(id))
                },
            )
        }
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
