package app.krafted.tigeralmanac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
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
import app.krafted.tigeralmanac.ui.SettingsScreen
import app.krafted.tigeralmanac.ui.SplashScreen
import app.krafted.tigeralmanac.ui.fengshui.RoomDetailScreen
import app.krafted.tigeralmanac.ui.fengshui.RoomSelectScreen
import app.krafted.tigeralmanac.ui.iching.HexagramArchiveScreen
import app.krafted.tigeralmanac.ui.iching.TodaysHexagramScreen
import app.krafted.tigeralmanac.ui.search.SearchScreen
import app.krafted.tigeralmanac.ui.theme.TigerAlmanacTheme
import app.krafted.tigeralmanac.ui.zodiac.AnimalProfileScreen
import app.krafted.tigeralmanac.ui.zodiac.CompatibilityScreen
import app.krafted.tigeralmanac.ui.zodiac.ZodiacDashboardScreen
import app.krafted.tigeralmanac.viewmodel.FengShuiViewModel
import app.krafted.tigeralmanac.viewmodel.HomeViewModel
import app.krafted.tigeralmanac.viewmodel.IChingViewModel
import app.krafted.tigeralmanac.viewmodel.SearchViewModel
import app.krafted.tigeralmanac.viewmodel.UserProfileViewModel
import app.krafted.tigeralmanac.viewmodel.ZodiacViewModel

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
    const val ANIMAL_PROFILE_ROUTE = "animal_profile?animalId={animalId}"
    fun animalProfileWithId(id: String) = "animal_profile?animalId=$id"
    const val COMPATIBILITY = "compatibility"
    const val FENGSHUI_ROOMS = "fengshui_rooms"
    const val FENGSHUI_DETAIL_ROUTE = "fengshui_detail/{roomId}"
    fun fengShuiDetailWithRoom(roomId: String) = "fengshui_detail/$roomId"
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
            UserProfileViewModel.factory(database, zodiacRepository)
        val homeViewModelFactory = HomeViewModel.factory(this, userProfileDao)
        val iChingViewModelFactory =
            IChingViewModel.factory(this, userProfileDao, hexagramHistoryDao)
        val zodiacViewModelFactory = ZodiacViewModel.factory(this, userProfileDao)
        val fengShuiViewModelFactory = FengShuiViewModel.factory(this, database.bookmarkDao())
        val searchViewModelFactory = SearchViewModel.factory(this)

        setContent {
            TigerAlmanacTheme {
                val navController = rememberNavController()
                val userProfileViewModel: UserProfileViewModel =
                    viewModel(factory = userProfileViewModelFactory)
                val iChingViewModel: IChingViewModel =
                    viewModel(factory = iChingViewModelFactory)
                val fengShuiViewModel: FengShuiViewModel =
                    viewModel(factory = fengShuiViewModelFactory)

                TigerAlmanacNavHost(
                    navController = navController,
                    userProfileViewModel = userProfileViewModel,
                    homeViewModelFactory = homeViewModelFactory,
                    iChingViewModel = iChingViewModel,
                    zodiacViewModelFactory = zodiacViewModelFactory,
                    fengShuiViewModel = fengShuiViewModel,
                    searchViewModelFactory = searchViewModelFactory
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
    iChingViewModel: IChingViewModel,
    zodiacViewModelFactory: ViewModelProvider.Factory,
    fengShuiViewModel: FengShuiViewModel,
    searchViewModelFactory: ViewModelProvider.Factory
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(350)
            ) + fadeIn(tween(350))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(350)
            ) + fadeOut(tween(350))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(350)
            ) + fadeIn(tween(350))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(350)
            ) + fadeOut(tween(350))
        },
    ) {
        composable(
            Routes.SPLASH,
            enterTransition = { fadeIn(tween(350)) },
            exitTransition = { fadeOut(tween(350)) },
            popEnterTransition = { fadeIn(tween(350)) },
            popExitTransition = { fadeOut(tween(350)) },
        ) {
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
        composable(
            Routes.PROFILE_SETUP,
            enterTransition = { fadeIn(tween(350)) },
            exitTransition = { fadeOut(tween(350)) },
            popEnterTransition = { fadeIn(tween(350)) },
            popExitTransition = { fadeOut(tween(350)) },
        ) {
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
                onNavigateSearch = { navController.navigate(Routes.SEARCH) },
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
        composable(Routes.ZODIAC) {
            val zodiacViewModel: ZodiacViewModel = viewModel(factory = zodiacViewModelFactory)
            ZodiacDashboardScreen(
                viewModel = zodiacViewModel,
                onBack = { navController.popBackStack() },
                onViewFullProfile = { navController.navigate(Routes.ANIMAL_PROFILE) },
            )
        }
        composable(
            route = Routes.ANIMAL_PROFILE_ROUTE,
            arguments = listOf(
                navArgument("animalId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val zodiacViewModel: ZodiacViewModel = viewModel(factory = zodiacViewModelFactory)
            AnimalProfileScreen(
                viewModel = zodiacViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToCompatibility = { navController.navigate(Routes.COMPATIBILITY) },
                animalId = backStackEntry.arguments?.getString("animalId"),
            )
        }
        composable(Routes.COMPATIBILITY) {
            val zodiacViewModel: ZodiacViewModel = viewModel(factory = zodiacViewModelFactory)
            CompatibilityScreen(
                viewModel = zodiacViewModel,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.FENGSHUI_ROOMS) {
            RoomSelectScreen(
                viewModel = fengShuiViewModel,
                onBack = { navController.popBackStack() },
                onSelectRoom = { id ->
                    navController.navigate(Routes.fengShuiDetailWithRoom(id))
                }
            )
        }
        composable(
            route = Routes.FENGSHUI_DETAIL_ROUTE,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            RoomDetailScreen(
                viewModel = fengShuiViewModel,
                roomId = backStackEntry.arguments?.getString("roomId"),
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.SEARCH) {
            val searchViewModel: SearchViewModel = viewModel(factory = searchViewModelFactory)
            SearchScreen(
                viewModel = searchViewModel,
                onBack = { navController.popBackStack() },
                onOpenHexagram = { id -> navController.navigate(Routes.ichingWithHexagram(id)) },
                onOpenAnimal = { id -> navController.navigate(Routes.animalProfileWithId(id)) },
                onOpenRoom = { id -> navController.navigate(Routes.fengShuiDetailWithRoom(id)) },
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                viewModel = userProfileViewModel,
                onBack = { navController.popBackStack() },
                onResetComplete = {
                    navController.navigate(Routes.PROFILE_SETUP) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}

