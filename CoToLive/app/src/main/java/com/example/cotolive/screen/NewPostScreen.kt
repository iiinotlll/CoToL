package com.example.cotolive.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cotolive.R
import com.example.cotolive.network.ArticleSent
import com.example.cotolive.snackBar.SnackbarViewModel
import com.example.cotolive.ui.theme.CoToLiveTheme
import com.example.cotolive.viewmodels.ArticleManageUiState
import com.example.cotolive.viewmodels.ArticleManageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    snackbarViewModel: SnackbarViewModel
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val articleUploadViewModel: ArticleManageViewModel = viewModel()
    val articlePostUiState = articleUploadViewModel.articleNewPostUiState

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            modifier = modifier.height(30.dp),
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.back),
                                contentDescription = "Back Button"
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "New Article",
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
                            onClick = {
                                articleUploadViewModel.postNewArticle(
                                    ArticleSent(
                                        0,
                                        title,
                                        content
                                    )
                                )
                            },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.upload),
                                contentDescription = "Save Button"
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            TextField(
                value = title,
                onValueChange = {
                    title = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                placeholder = {
                    Text(
                        "请输入标题",
                        fontWeight = FontWeight(500),
                        color = Color.Gray,
                        fontSize = 21.sp
                    )
                },
                textStyle = TextStyle(fontSize = 21.sp, fontWeight = FontWeight(500)),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent, // 聚焦时的指示器颜色
                    unfocusedIndicatorColor = Color.Transparent, // 非聚焦时的指示器颜色
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.Black
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = content,
                onValueChange = {
                    content = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("请输入内容", color = Color.Gray, fontSize = 16.sp) },
                textStyle = TextStyle(fontSize = 16.sp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent, // 聚焦时的指示器颜色
                    unfocusedIndicatorColor = Color.Transparent, // 非聚焦时的指示器颜色
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.Black
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        LaunchedEffect(articlePostUiState) {
            when (articlePostUiState) {
                is ArticleManageUiState.Success-> {
                    snackbarViewModel.showOKSnackbar("上传成功。")
                    navController.popBackStack()
                }
                is ArticleManageUiState.Error -> {
                    snackbarViewModel.showErrSnackbar("上传失败。")
                }
                is ArticleManageUiState.Loading -> {

                }
            }
        }
    }
}


@Preview
@Composable
fun NewPostScreenPreview() {
    CoToLiveTheme {
        val navController = rememberNavController()
        val snackbarViewModel: SnackbarViewModel = viewModel()
        NewPostScreen(navController = navController, snackbarViewModel = snackbarViewModel)
    }
}