package com.chaos.mangaties

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.chaos.mangaties.core.component.toast.AppToastHost
import com.chaos.mangaties.core.component.toast.LocalToastController
import com.chaos.mangaties.core.component.toast.rememberToastController
import com.chaos.mangaties.core.theme.MangaTiesTheme
import com.chaos.mangaties.navgraph.AuthViewModel
import com.chaos.mangaties.navgraph.NavGraph
import com.chaos.mangaties.navgraph.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MangaTiesTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val toastController = rememberToastController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val authState by authViewModel.authState.collectAsState()

                    val startDestination = if (authState.isAuthenticated) {
                        Screen.Dashboard.route
                    } else {
                        Screen.Splash.route
                    }

                    Column() {
                        CompositionLocalProvider(LocalToastController provides toastController) {
                            Box(Modifier.fillMaxSize()) {
                                NavGraph(
                                    navController = navController,
                                    startDestination = startDestination
                                )
                                AppToastHost(controller = toastController)
                            }
                        }
                    }
                }
            }
        }
    }
}