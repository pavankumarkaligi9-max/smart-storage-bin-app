package com.smartstoragebin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smartstoragebin.ui.navigation.BottomNavBar
import com.smartstoragebin.ui.navigation.NavGraph
import com.smartstoragebin.ui.navigation.Screen
import com.smartstoragebin.ui.theme.SmartStorageBinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartStorageBinTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val mainRoutes = listOf(
                    Screen.Home.route,
                    Screen.Files.route,
                    Screen.Temporary.route,
                    Screen.Storage.route,
                    Screen.Bin.route
                )
                val showBottomBar = mainRoutes.any { currentRoute?.startsWith(it) == true } && currentRoute?.startsWith(Screen.FileDetail.route.substringBefore("/")) == false

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }
}
