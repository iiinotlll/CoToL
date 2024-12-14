package com.example.cotolive.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderTopBar(headerName: String) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = headerName,
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
}