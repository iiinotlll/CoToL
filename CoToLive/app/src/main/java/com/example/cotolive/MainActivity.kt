package com.example.cotolive

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.cotolive.navigation.AppNavigation
import com.example.cotolive.snackBar.SnackBarShow
import com.example.cotolive.snackBar.SnackbarViewModel
import com.example.cotolive.ui.theme.CoToLiveTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoToLiveTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val snackbarViewModel: SnackbarViewModel = viewModel()
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(
                            snackbarHostState,
                            modifier = Modifier.padding(bottom = 20.dp),
                            snackbar = { SnackBarShow(snackbarViewModel) }
                        )
                    },
                    content = {
                        // 这里放置导航相关的内容
                        AppNavigation(
                            navController = navController,
                            snackbarHostState = snackbarHostState,
                            snackbarViewModel = snackbarViewModel
                        )
                    }
                )
            }
        }
    }
}

