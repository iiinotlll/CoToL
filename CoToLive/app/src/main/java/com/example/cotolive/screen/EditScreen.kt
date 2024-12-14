package com.example.cotolive.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.MutableState
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
import com.example.cotolive.R
import com.example.cotolive.ui.theme.CoToLiveTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenLayout(modifier: Modifier = Modifier, article: MutableState<Article>) {
    var isEditMode by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
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
                                painter = painterResource(R.drawable.ic_launcher_foreground),
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
        ) {

            TextField(
                value = article.value.title,
                onValueChange = { if (isEditMode) article.value = article.value.copy(title = it) },
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
                value = article.value.abstrct,
                onValueChange = {
                    if (isEditMode) article.value = article.value.copy(abstrct = it)
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


@Preview
@Composable
fun EditScreenPreview() {
    CoToLiveTheme {
        val article = remember { mutableStateOf(Article("Sample Title", "Sample Abstract", 1)) }
        EditScreenLayout(article = article)
    }
}