package com.smartstoragebin.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Files : Screen("files")
    object FilesWithCategory : Screen("files/{category}") {
        fun createRoute(category: String) = "files/$category"
    }
    object FileDetail : Screen("file_detail/{fileId}") {
        fun createRoute(fileId: Long) = "file_detail/$fileId"
    }
    object Temporary : Screen("temporary")
    object Storage : Screen("storage")
    object Categories : Screen("categories")
    object Bin : Screen("bin")
    object Search : Screen("search")
    object Settings : Screen("settings")
}
