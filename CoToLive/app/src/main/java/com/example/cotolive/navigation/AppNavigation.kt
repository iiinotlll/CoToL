package com.example.cotolive.navigation

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cotolive.R
import com.example.cotolive.screen.EditScreenLayout
import com.example.cotolive.screen.HomeScreenLayout
import com.example.cotolive.screen.LogInScreenLayout
import com.example.cotolive.screen.NewPostScreen
import com.example.cotolive.screen.SignUpScreenLayout
import com.example.cotolive.snackBar.SnackbarViewModel

enum class CoToLScreen(@StringRes val title: Int) {
    Home(title = R.string.homeScreen),
    LogIn(title = R.string.loginScreen),
    SignUp(title = R.string.signupScreen),
    Edit(title = R.string.editScreen),
    NewPost(title = R.string.newpostScreen)
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    snackbarViewModel: SnackbarViewModel
) {
    LaunchedEffect(snackbarViewModel.showSnackbar) {
        if (snackbarViewModel.showSnackbar) {
            // 显示 Snackbar，并设置自动消失的持续时间
            snackbarHostState.showSnackbar("修改后的提示信息", duration = SnackbarDuration.Short)
            // 重置状态为 false，避免重复弹出
            snackbarViewModel.hideSnackbar()
        }
    }

    NavHost(
        navController = navController,
        startDestination = CoToLScreen.LogIn.name,
    ) {
        composable(CoToLScreen.LogIn.name) {
            LogInScreenLayout(navController = navController, snackbarViewModel = snackbarViewModel)
        }

        composable(CoToLScreen.SignUp.name) {
            SignUpScreenLayout(navController = navController, snackbarViewModel = snackbarViewModel)
        }

        composable(CoToLScreen.Home.name) {
            HomeScreenLayout(navController = navController, snackbarViewModel = snackbarViewModel)
        }

        composable(CoToLScreen.Edit.name + "/{articleID}") { backStackEntry ->
            // 从路由中获取 itemId 参数
            val articleID = backStackEntry.arguments?.getString("articleID")
            EditScreenLayout(navController = navController, articleID = articleID, snackbarViewModel = snackbarViewModel)
        }

        composable(CoToLScreen.NewPost.name) {
            NewPostScreen(navController = navController, snackbarViewModel = snackbarViewModel)
        }
    }
}