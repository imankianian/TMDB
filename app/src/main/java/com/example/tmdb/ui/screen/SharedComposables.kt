package com.example.tmdb.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tmdb.R


@Composable
fun LoadingScreen() {
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Gray)
    }
}

@Composable
fun ErrorScreen(message: String) {
    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.ic_offline),
            contentDescription = "You are offline",
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            alpha = 0.3f)
        Divider(color = MaterialTheme.colorScheme.onSurface.copy(0f),
            modifier = Modifier.height(20.dp))
        Text(text = message,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 10.sp,
            modifier = Modifier.padding(10.dp))
    }
}