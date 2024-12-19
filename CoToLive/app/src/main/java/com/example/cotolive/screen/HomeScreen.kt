package com.example.cotolive.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cotolive.R
import com.example.cotolive.navigation.CoToLScreen
import com.example.cotolive.network.ArticleAbstract
import com.example.cotolive.ui.theme.CoToLiveTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenLayout(modifier: Modifier = Modifier, navController: NavController) {
    val fetchAbstractsViewModel: FetchAbstractsViewModel = viewModel()
    val fetchAbstractsUiState = fetchAbstractsViewModel.fetchAbstractUiState
    var isLoading by remember { mutableStateOf(true) }
    var articleItems = fetchAbstractsViewModel.fetchAbstractsResults

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
                    actions = {
                        IconButton(
                            onClick = { navController.navigate(CoToLScreen.NewPost.name) },
                            modifier = Modifier.height(30.dp)
                            ) {
                            Icon(
                                painter = painterResource(R.drawable.add),
                                contentDescription = "Add Button"
                            )
                        }
                    }
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
                SingleColumnLayout(articleItems, navController)
            }
        }
    }
}

@Composable
fun SingleColumnLayout(items: List<ArticleAbstract>, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        items.forEach { item ->
            Card(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .clickable {
                        // 当点击卡片时执行跳转
                        navController.navigate(CoToLScreen.Edit.name + "/${item.aid}")
                    },
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentHeight() // 高度自适应
                ) {
                    Column {
                        Text(text = item.title+"...", fontSize = 20.sp)
                        Text(text = item.abstract+"...", fontSize = 15.sp)
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
        HomeScreenLayout(navController = navController)
    }
}