package com.example.cotolive.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cotolive.R
import com.example.cotolive.screen.EditScreenLayout
import com.example.cotolive.screen.HomeScreenLayout
import com.example.cotolive.screen.LogInScreenLayout
import com.example.cotolive.screen.NewPostScreen
import com.example.cotolive.screen.SignUpScreenLayout

enum class CoToLScreen(@StringRes val title: Int) {
    Home(title = R.string.homeScreen),
    LogIn(title = R.string.loginScreen),
    SignUp(title = R.string.signupScreen),
    Edit(title = R.string.editScreen),
    NewPost(title = R.string.newpostScreen)
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = CoToLScreen.LogIn.name,
    ) {
        composable(CoToLScreen.LogIn.name) {
            LogInScreenLayout(navController = navController)
        }

        composable(CoToLScreen.SignUp.name) {
            SignUpScreenLayout(navController = navController)
        }

        composable(CoToLScreen.Home.name) {
            HomeScreenLayout(navController = navController)
        }

        composable(CoToLScreen.Edit.name + "/{articleID}") { backStackEntry ->
            // 从路由中获取 itemId 参数
            val articleID = backStackEntry.arguments?.getString("articleID")
            EditScreenLayout(navController = navController, articleID = articleID)
        }

        composable(CoToLScreen.NewPost.name) {
            NewPostScreen(navController = navController)
        }
    }
}