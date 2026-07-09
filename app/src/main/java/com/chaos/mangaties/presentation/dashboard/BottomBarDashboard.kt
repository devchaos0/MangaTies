package com.chaos.mangaties.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chaos.mangaties.core.component.text.AppText
import com.chaos.mangaties.navgraph.AuthViewModel
import com.chaos.mangaties.presentation.dashboard.bookshelf.BookShelfScreen
import com.chaos.mangaties.presentation.dashboard.discover.DiscoverScreen
import com.chaos.mangaties.presentation.dashboard.home.HomeScreen
import com.chaos.mangaties.presentation.dashboard.settings.SettingsScreen

enum class BottomNavItem(
    val title: String,
    val icon: ImageVector
){
    Home("Home", Icons.Default.Home),
    Bookshelf("Bookshelf", Icons.Default.Bookmarks),
    Discover("Discover", Icons.Default.Search),
    Settings("Settings", Icons.Default.Settings)
}

@Composable
fun BottomBarDashboard(
    onMangaClick: (String) -> Unit,
    onDownloadedMangaClick: (String, String) -> Unit,
    onFavouriteMangaClick: (String) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    // Save the selected tab state across configuration changes and navigation back
    var selectedTab by rememberSaveable { mutableStateOf(BottomNavItem.Home) }

    Scaffold(
        bottomBar = {
            BottomBar(selectedTab = selectedTab) { tab ->
                selectedTab = tab
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            when(selectedTab){
                BottomNavItem.Home -> HomeScreen(onMangaClick = onMangaClick)
                BottomNavItem.Bookshelf -> BookShelfScreen(
                    onDownloadedMangaClick = onDownloadedMangaClick,
                    onFavouriteMangaClick = onFavouriteMangaClick

                )
                BottomNavItem.Discover -> DiscoverScreen(
                    onMangaClick = onMangaClick
                )
                BottomNavItem.Settings -> SettingsScreen(
                    onMessageClick = {},
                    onHelpClick = {},
                    onAboutClick = {},
                    onPrivacyPolicyClick = {},
                    onLogoutClick = {
                        authViewModel.logout()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    selectedTab: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 0.dp
    ) {
        BottomNavItem.values().forEach { item ->
            NavigationBarItem(
                selected = selectedTab == item,
                onClick = {onTabSelected(item)},
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    AppText(
                        text = item.title,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            )
        }

    }
}
