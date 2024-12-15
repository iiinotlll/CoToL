package com.example.cotolive.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cotolive.ui.theme.CoToLiveTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenLayout(modifier: Modifier = Modifier) {
    var userMail by remember { mutableStateOf("") }
    var userPwd by remember { mutableStateOf("") }
    var PwdReCheck by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxSize()
            .padding(top = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Good Title",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 45.sp,
            fontFamily = FontFamily.Monospace
        )


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .height(60.dp),
            value = userMail,
            onValueChange = { userMail = it },
            singleLine = true,
            label = {
                Text(
                    text = "账号",
                    fontSize = 23.sp,
                )
            },
            placeholder = {
                Text(
                    text = "邮箱",
                )
            },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray, // 非聚焦时的指示器颜色
                unfocusedContainerColor = Color.White,
            )
        )


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .height(60.dp)
                .background(color = Color.Transparent),
            value = userPwd,
            singleLine = true,
            onValueChange = { userPwd = it },
            label = {
                Text(
                    text = "密码",
                    fontSize = 23.sp,
                )
            },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray, // 非聚焦时的指示器颜色
                unfocusedContainerColor = Color.White,
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .height(60.dp)
                .background(color = Color.Transparent),
            value = PwdReCheck,
            singleLine = true,
            onValueChange = { PwdReCheck = it },
            label = {
                Text(
                    text = "确认密码",
                    fontSize = 23.sp,
                )
            },
            placeholder = {
                Text(
                    text = "请再次输入",
                )
            },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray, // 非聚焦时的指示器颜色
                unfocusedContainerColor = Color.White,
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 5.dp, end = 5.dp),
            onClick = {  singUpClick () },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text(
                "注 册",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Cursive,
                fontSize = 20.sp
            )
        }
    }
}

private fun singUpClick() {
//    TODO: send sign up msg.
}


@Preview
@Composable
fun SignUpScreenPreview() {
    CoToLiveTheme {
        SignUpScreenLayout()
    }
}