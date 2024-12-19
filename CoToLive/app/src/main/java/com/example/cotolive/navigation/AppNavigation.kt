package com.example.cotolive.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cotolive.screen.EditScreenLayout
import com.example.cotolive.screen.HomeScreenLayout
import com.example.cotolive.screen.LogInScreenLayout
import com.example.cotolive.screen.SignUpScreenLayout

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController,
        startDestination = "LogInScreen",
    ) {
        composable("LogInScreen") {
            LogInScreenLayout(navController = navController)
        }

        composable("SignUpScreen") {
            SignUpScreenLayout(navController = navController)
        }

        composable("HomeScreen") {
            HomeScreenLayout(navController = navController)
        }

        composable("EditScreen") {
//            EditScreenLayout(navController = navController)
        }
    }
}