package com.example.cotolive.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cotolive.ui.theme.CoToLiveTheme

// Article 类实现 MutableState<Article>
data class Article(var title: String, var abstrct: String, val id: Int)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenLayout(modifier: Modifier = Modifier, ) {
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
            HeaderTopBar("CoToLive")
        },
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            SingleColumnLayout(items)
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
        HomeScreenLayout()
    }
}