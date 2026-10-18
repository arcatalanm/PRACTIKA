package com.example.practika.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ImagenInteligente(
    imagenUri: Uri?,
    modifier: Modifier = Modifier,
    size: Dp = 180.dp,
    descripcion: String = "Imagen de perfil"
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(3.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.65f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (imagenUri != null) {
            AsyncImage(
                model = imagenUri,
                contentDescription = descripcion,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Sin imagen de perfil",
                modifier = Modifier.size(size / 2),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
