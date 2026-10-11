package com.smartstoragebin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smartstoragebin.ui.screens.bin.BinScreen
import com.smartstoragebin.ui.screens.bin.BinViewModel
import com.smartstoragebin.ui.screens.categories.CategoriesScreen
import com.smartstoragebin.ui.screens.categories.CategoriesViewModel
import com.smartstoragebin.ui.screens.files.FileDetailScreen
import com.smartstoragebin.ui.screens.files.FileDetailViewModel
import com.smartstoragebin.ui.screens.files.FilesScreen
import com.smartstoragebin.ui.screens.files.FilesViewModel
import com.smartstoragebin.ui.screens.home.HomeScreen
import com.smartstoragebin.ui.screens.home.HomeViewModel
import com.smartstoragebin.ui.screens.search.SearchScreen
import com.smartstoragebin.ui.screens.search.SearchViewModel
import com.smartstoragebin.ui.screens.settings.SettingsScreen
import com.smartstoragebin.ui.screens.settings.SettingsViewModel
import com.smartstoragebin.ui.screens.storage.StorageScreen
import com.smartstoragebin.ui.screens.storage.StorageViewModel
import com.smartstoragebin.ui.screens.temporary.TemporaryScreen
import com.smartstoragebin.ui.screens.temporary.TemporaryViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel()
            HomeScreen(
                viewModel = viewModel,
                onNavigateToFiles = { navController.navigate(Screen.Files.route) },
                onNavigateToTemporary = { navController.navigate(Screen.Temporary.route) },
                onNavigateToCategories = { navController.navigate(Screen.Categories.route) },
                onNavigateToStorage = { navController.navigate(Screen.Storage.route) },
                onNavigateToBin = { navController.navigate(Screen.Bin.route) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onFileClick = { fileId -> navController.navigate(Screen.FileDetail.createRoute(fileId)) }
            )
        }
        
        composable(Screen.Files.route) {
            val viewModel: FilesViewModel = viewModel()
            FilesScreen(
                viewModel = viewModel,
                onFileClick = { fileId -> navController.navigate(Screen.FileDetail.createRoute(fileId)) },
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.FilesWithCategory.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val viewModel: FilesViewModel = viewModel()
            FilesScreen(
                viewModel = viewModel,
                onFileClick = { fileId -> navController.navigate(Screen.FileDetail.createRoute(fileId)) },
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.FileDetail.route,
            arguments = listOf(navArgument("fileId") { type = NavType.LongType })
        ) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getLong("fileId") ?: -1L
            val viewModel: FileDetailViewModel = viewModel()
            FileDetailScreen(
                viewModel = viewModel,
                fileId = fileId,
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable(Screen.Temporary.route) {
            val viewModel: TemporaryViewModel = viewModel()
            TemporaryScreen(
                viewModel = viewModel,
                onFileClick = { fileId -> navController.navigate(Screen.FileDetail.createRoute(fileId)) },
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable(Screen.Storage.route) {
            val viewModel: StorageViewModel = viewModel()
            StorageScreen(
                viewModel = viewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable(Screen.Categories.route) {
            val viewModel: CategoriesViewModel = viewModel()
            CategoriesScreen(
                viewModel = viewModel,
                onCategoryClick = { category -> navController.navigate(Screen.FilesWithCategory.createRoute(category)) },
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable(Screen.Bin.route) {
            val viewModel: BinViewModel = viewModel()
            BinScreen(
                viewModel = viewModel,
                onFileClick = { fileId -> navController.navigate(Screen.FileDetail.createRoute(fileId)) },
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable(Screen.Search.route) {
            val viewModel: SearchViewModel = viewModel()
            SearchScreen(
                viewModel = viewModel,
                onFileClick = { fileId -> navController.navigate(Screen.FileDetail.createRoute(fileId)) },
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel()
            SettingsScreen(
                viewModel = viewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
