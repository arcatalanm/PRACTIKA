package com.example.practika.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practika.ui.navigation.HomeSection
import com.example.practika.viewmodel.AuthViewModel
import com.example.practika.viewmodel.PerfilViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Vista incial post Login/Register (MAIN CONTAINER)
fun HomeScreen(
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier,
    // Inyeccion ViewModels
    viewModel: AuthViewModel = viewModel(),
    perfilViewModel: PerfilViewModel = viewModel()
) {
    // Obtener estados (Sesion, Usuario, URI)
    var seccionActual by remember { mutableStateOf(HomeSection.INICIO) }
    val usuario by viewModel.usuarioActivo.collectAsState()
    val imagenUri by perfilViewModel.imagenUri.collectAsState()

    // Carga de imagen asincrona
    LaunchedEffect(usuario?.rut) {
        perfilViewModel.cargarImagen(usuario?.rut)
    }

    // Animacion Confeti
    var mostrarConfeti by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(2600)
        mostrarConfeti = false
    }

    // Titulos Topbar
    val tituloBarra = when (seccionActual) {
        HomeSection.ASIGNATURAS -> "Asignaturas"
        HomeSection.PERFIL -> "Mi Perfil"
        HomeSection.INICIO -> "PRACTIKA"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = tituloBarra, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                HomeSection.entries.forEach { seccion ->
                    NavigationBarItem(
                        selected = seccionActual == seccion,
                        onClick = { seccionActual = seccion },
                        icon = { Icon(seccion.icono, contentDescription = seccion.titulo) },
                        label = { Text(seccion.titulo) }
                    )
                }
            }
        },
        modifier = modifier
    ) { padding ->
        // Componente transicion suave
        Crossfade(
            targetState = seccionActual,
            modifier = Modifier.padding(padding)
        ) { seccion ->
            when (seccion) {
                HomeSection.ASIGNATURAS -> AsignaturasScreen()

                HomeSection.INICIO -> InicioScreen(
                    usuario = usuario,
                    imagenUri = imagenUri,
                    dispararConfeti = mostrarConfeti
                )

                HomeSection.PERFIL -> PerfilScreen(
                    usuario = usuario,
                    onLogout = onLogout,
                    perfilViewModel = perfilViewModel
                )
            }
        }
    }
}
