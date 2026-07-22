package com.chaos.mangaties.navgraph

import androidx.compose.runtime.*
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.chaos.mangaties.presentation.auth.forgetpassword.ForgotPasswordScreen
import com.chaos.mangaties.presentation.auth.login.Login
import com.chaos.mangaties.presentation.auth.signup.SignUp
import com.chaos.mangaties.presentation.dashboard.BottomBarDashboard
import com.chaos.mangaties.presentation.dashboard.bookshelf.screen.chapterscreen.DownloadedChaptersScreen
import com.chaos.mangaties.presentation.dashboard.bookshelf.screen.offlinechapter.OfflineChapterReaderScreen
import com.chaos.mangaties.presentation.dashboard.manga.screen.mangadetailscreen.MangaDetailsScreen
import com.chaos.mangaties.presentation.dashboard.manga.screen.mangadownload.DownloadChaptersScreen
import com.chaos.mangaties.presentation.dashboard.manga.screen.mangareader.ChapterReaderScreen
import com.chaos.mangaties.presentation.onboarding.OnboardingScreen
import com.chaos.mangaties.presentation.splashscreen.SplashScreen
import java.net.URLDecoder

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Onboarding.route,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Onboarding.route) { inclusive = true }
                popUpTo(Screen.Login.route) { inclusive = true }
                popUpTo(Screen.SignUp.route) { inclusive = true }
                popUpTo(Screen.ForgotPassword.route) { inclusive = true }
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToNext = { navController.navigate(Screen.Onboarding.route) }
            )
        }
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(
                navigateToLogin = { navController.navigate(Screen.Login.route){ popUpTo(Screen.Onboarding.route){ inclusive = true } } },
                navigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }
        composable(route = Screen.Login.route) {
            Login(
                navigateToDashboard = { navController.navigate(Screen.Dashboard.route){ popUpTo(Screen.Onboarding.route){ inclusive = true } } },
                navigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                navigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }
        composable(route = Screen.SignUp.route) {
            SignUp(navigateToLogin = { navController.navigate(Screen.Login.route){ popUpTo(Screen.Onboarding.route){ inclusive = true } } })
        }
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.Dashboard.route) {
            BottomBarDashboard(
                onMangaClick = { mangaId ->
                    navController.navigate(Screen.MangaDetailScreen.passMangaId(mangaId))
                },
                onDownloadedMangaClick = { mangaId, mangaTitle ->
                    navController.navigate(Screen.DownloadedChapters.passManga(mangaId, mangaTitle))
                },
                onFavouriteMangaClick = { mangaId ->
                    navController.navigate(Screen.MangaDetailScreen.passMangaId(mangaId))
                }
            )
        }

        composable(
            route = Screen.MangaDetailScreen.route,
            arguments = listOf(navArgument("mangaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getString("mangaId") ?: return@composable
            MangaDetailsScreen(
                mangaId = mangaId,
                onBack = { navController.popBackStack() },
                onNavigateToChapterReader = { chapterId, chapters ->
                    MangaDataHolder.chapters = chapters
                    navController.navigate(Screen.ChapterReaderScreen.passChapterId(chapterId))
                },
                onNavigateToDownload = { mangaId, mangaTitle, mangaCoverUrl, chapters ->
                    MangaDataHolder.chapters = chapters
                    navController.navigate(Screen.DownloadChapters.passManga(mangaId, mangaTitle, mangaCoverUrl))
                }
            )
        }

        composable(
            route = Screen.ChapterReaderScreen.route,
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: return@composable

            ChapterReaderScreen(
                chapterId = chapterId,
                initialChapters = MangaDataHolder.chapters,
                onBack = { navController.popBackStack() },
                onChapterChange = { newChapterId ->
                    navController.navigate(Screen.ChapterReaderScreen.passChapterId(newChapterId)) {
                        popUpTo(Screen.ChapterReaderScreen.passChapterId(chapterId)) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.DownloadChapters.route,
            arguments = listOf(
                navArgument("mangaId") { type = NavType.StringType },
                navArgument("mangaTitle") { type = NavType.StringType },
                navArgument("mangaCoverUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getString("mangaId") ?: return@composable
            val mangaTitle = backStackEntry.arguments?.getString("mangaTitle")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: "Manga"
            val encodedCoverUrl = backStackEntry.arguments?.getString("mangaCoverUrl")
            val mangaCoverUrl = if (encodedCoverUrl == "none") null else encodedCoverUrl?.let {
                URLDecoder.decode(it, "UTF-8")
            }

            DownloadChaptersScreen(
                mangaId = mangaId,
                mangaTitle = mangaTitle,
                chapters = MangaDataHolder.chapters,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DownloadedChapters.route,
            arguments = listOf(
                navArgument("mangaId") { type = NavType.StringType },
                navArgument("mangaTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getString("mangaId") ?: return@composable
            val mangaTitle = backStackEntry.arguments?.getString("mangaTitle")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: "Manga"

            DownloadedChaptersScreen(
                mangaId = mangaId,
                mangaTitle = mangaTitle,
                onBack = { navController.popBackStack() },
                onChapterClick = { chapter, allChapters, index ->
                    MangaDataHolder.currentChapter = chapter
                    MangaDataHolder.downloadedChapters = allChapters
                    MangaDataHolder.currentChapterIndex = index
                    navController.navigate(Screen.OfflineReader.passChapterId(chapter.chapterId))
                }
            )
        }

        composable(
            route = Screen.OfflineReader.route,
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: return@composable

            val chapter = MangaDataHolder.currentChapter
            val allChapters = MangaDataHolder.downloadedChapters

            if (chapter != null) {
                OfflineChapterReaderScreen(
                    chapter = chapter,
                    allChapters = allChapters,
                    onBack = { navController.popBackStack() },
                    onChapterChange = { newChapter ->
                        MangaDataHolder.currentChapter = newChapter
                        // Find index of new chapter
                        val newIndex = allChapters.indexOfFirst { it.chapterId == newChapter.chapterId }
                        if (newIndex != -1) {
                            MangaDataHolder.currentChapterIndex = newIndex
                        }
                        navController.navigate(Screen.OfflineReader.passChapterId(newChapter.chapterId)) {
                            popUpTo(Screen.OfflineReader.passChapterId(chapterId)) { inclusive = true }
                        }
                    }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}
