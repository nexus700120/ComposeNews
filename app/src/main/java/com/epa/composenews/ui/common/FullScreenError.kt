package com.epa.composenews.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epa.composenews.R
import com.epa.composenews.ui.theme.Neutral600
import com.epa.composenews.ui.theme.Neutral900

@Composable
fun FullScreenError(
    text: String,
    retryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .align(BiasAlignment(0F, -.2F)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_cone),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                textAlign = TextAlign.Center,
                text = "Ошибка",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral900
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center,
                text = text,
                fontSize = 16.sp,
                color = Neutral600
            )
        }
        SecondaryButton(
            text = "Повторить",
            modifier = Modifier.padding(20.dp).align(Alignment.BottomCenter),
            onClick = retryClick
        )
    }
}