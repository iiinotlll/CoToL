package com.example.cotolive.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cotolive.R
import com.example.cotolive.network.ArticleGet
import com.example.cotolive.network.ArticleSent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenLayout(
    modifier: Modifier = Modifier,
    navController: NavController,
    articleID: String?
) {
    val aid = articleID?.toIntOrNull() ?: -1 // 如果转换失败，默认值为 -1
    if (aid == -1) {
        navController.popBackStack()
    }

    var isEditMode by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    var articleReadViewModel: ArticleManageViewModel = viewModel()
    var articleReadUiState = articleReadViewModel.articleReadUiState
    var articleResult = articleReadViewModel.articleReadResult
    var editingTitle by remember { mutableStateOf("") }
    var editingContent by remember { mutableStateOf("") }

    fun onBackPressed () {
        if (articleResult.title != editingTitle || articleResult.content != editingContent) {
            articleReadViewModel.modifyArticle(ArticleSent(aid = aid, title = editingTitle, content = editingContent))
            isLoading = true
        }
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        // 发起网络请求
        articleReadViewModel.readArticle(aid)
    }

    LaunchedEffect(articleReadUiState) {
        when (articleReadUiState) {
            is ArticleManageUiState.Success -> {
                editingTitle = articleResult.title
                editingContent = articleResult.content
                isLoading = false
            }

            is ArticleManageUiState.Error -> {
                navController.popBackStack()
            }

            is ArticleManageUiState.Loading -> {
                isLoading = true
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            modifier = modifier.height(30.dp),
                            onClick = { onBackPressed() }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.back),
                                contentDescription = "Back Button"
                            )
                        }
                    },
                    title = {
                        Text(
                            text = if (isEditMode) "Edit" else "View",
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
                        IconButton(onClick = { isEditMode = !isEditMode }) {
                            Icon(
                                painter = painterResource(if (!isEditMode) R.drawable.eye else  R.drawable.edit_text),
                                contentDescription = "Edit Button"
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
                    modifier = Modifier.padding(top = 30.dp),
                    text = "加载中"
                )
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(10.dp)
            ) {
                TextField(
                    value = editingTitle,
                    onValueChange = {
                        if (isEditMode) editingTitle = it
                    },
                    enabled = isEditMode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
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
                    value = editingContent,
                    onValueChange = {
                        if (isEditMode) editingContent = it
                    },
                    enabled = isEditMode,
                    modifier = Modifier.fillMaxWidth(),
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
        }
    }
}

