package com.chaos.mangaties.navgraph

import java.net.URLEncoder

sealed class Screen(val route: String) {
    object Splash : Screen("splashscreen")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgotPassword : Screen("forgetPassword")
    object Dashboard : Screen("dashboard")
    object MangaDetailScreen : Screen("manga_details_screen/{mangaId}") {
        fun passMangaId(mangaId: String): String = "manga_details_screen/$mangaId"
    }
    object ChapterReaderScreen : Screen("chapter_reader/{chapterId}") {
        fun passChapterId(chapterId: String): String = "chapter_reader/$chapterId"
    }
    object DownloadChapters : Screen("download_chapters/{mangaId}/{mangaTitle}/{mangaCoverUrl}") {
        fun passManga(mangaId: String, mangaTitle: String, mangaCoverUrl: String?): String {
            val encodedTitle = URLEncoder.encode(mangaTitle, "UTF-8")
            val encodedCover = mangaCoverUrl?.let { URLEncoder.encode(it, "UTF-8") } ?: "none"
            return "download_chapters/$mangaId/$encodedTitle/$encodedCover"
        }
    }
    object DownloadedChapters : Screen("downloaded_chapters/{mangaId}/{mangaTitle}") {
        fun passManga(mangaId: String, mangaTitle: String): String {
            val encodedTitle = URLEncoder.encode(mangaTitle, "UTF-8")
            return "downloaded_chapters/$mangaId/$encodedTitle"
        }
    }
    object OfflineReader : Screen("offline_reader/{chapterId}") {
        fun passChapterId(chapterId: String): String = "offline_reader/$chapterId"
    }
}