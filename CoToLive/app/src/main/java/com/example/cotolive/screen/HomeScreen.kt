package com.example.cotolive.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenLayout(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "CoToLive",
                        fontSize = 24.sp,
                        fontWeight = FontWeight(800),
                        fontFamily = FontFamily.Monospace
                    )
                }
            )
        },
    ) { innerPadding ->
        Text(
            text = "Hello, Scaffold!",
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenLayout()
}