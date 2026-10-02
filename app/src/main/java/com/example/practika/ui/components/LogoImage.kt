package com.example.practika.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.practika.R

@Composable
fun LogoImage() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo de PRACTIKA",
        modifier = Modifier.size(300.dp)
    )
}