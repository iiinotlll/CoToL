package com.example.cotolive.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cotolive.navigation.AppNavigation
import com.example.cotolive.ui.theme.CoToLiveTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenLayout(modifier: Modifier = Modifier, navController: NavController) {
    val fetchAbstractsViewModel: FetchAbstractsViewModel = viewModel()
    val fetchAbstractsUiState = fetchAbstractsViewModel.fetchAbstractUiState
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // 发起网络请求
        fetchAbstractsViewModel.getArticleAbstracts()
    }

    LaunchedEffect(fetchAbstractsUiState) {
        when (fetchAbstractsUiState) {
            is FetchAbstractUiState.Success->{
                isLoading = false
            }
            is FetchAbstractUiState.Error -> {

            }
            is FetchAbstractUiState.Loading -> {
                isLoading = true
            }
        }
    }

    val items = listOf(
        "Item 1 - short",
        "Item 2 - longer item with more content",
        "Item 3",
        "Item 4 - this is a longer item to show varying heights",
        "Item 5",
        "Item 6 - this is a longer item to show varying heights"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Good Title",
                            fontSize = 24.sp,
                            fontWeight = FontWeight(800),
                            fontFamily = FontFamily.Monospace
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFEFEBDC), // 设置背景颜色
                        titleContentColor = Color.Black
                    ),
                )

                HorizontalDivider( // 为分界线设置一些间距
                    thickness = 1.5.dp, // 设置分界线的厚度
                    color = Color.Gray // 设置分界线颜色
                )
            }
        },
    ) { innerPadding ->

        if (isLoading) {
            // show loading page
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))

                Text(
                    modifier = Modifier.padding(top=30.dp),
                    text= "加载中"
                )
            }

        } else {
            Column(modifier = modifier.padding(innerPadding)) {
                SingleColumnLayout(items)
            }
        }
    }
}

@Composable
fun SingleColumnLayout(items: List<String>) {
    Column(modifier = Modifier.fillMaxSize()) {
        items.forEach { item ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentHeight() // 高度自适应
                ) {
                    Column {
                        Text(text = item, fontSize = 20.sp)
                        Text(text = item, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    CoToLiveTheme {
        val navController = rememberNavController()
        AppNavigation(navController)
        HomeScreenLayout(navController = navController)
    }
}