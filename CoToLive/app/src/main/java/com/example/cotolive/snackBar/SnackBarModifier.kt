package com.example.cotolive.snackBar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cotolive.R

@Composable
fun SnackBarShow (snackbarViewModel: SnackbarViewModel) {
    if (!snackbarViewModel.showSnackbar) return
    Snackbar(
        containerColor = Color.White, // 背景颜色
        shape = RoundedCornerShape(16.dp), // 设置圆角形状
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 使用图标
            Icon(
                modifier = Modifier.size(24.dp),
                painter =  if (snackbarViewModel.isCorrect) painterResource(R.drawable.check_mark) else painterResource(R.drawable.close),
                contentDescription = "snackBarState",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(10.dp))
            // 显示 Snackbar 文本
            Text(text = snackbarViewModel.snackbarMessage, color = Color.Black)
        }
    }
}