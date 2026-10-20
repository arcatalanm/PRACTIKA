package com.example.practika.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practika.ui.screens.HomeScreen
import com.example.practika.ui.screens.LoginScreen
import com.example.practika.ui.screens.RegisterScreen
import com.example.practika.viewmodel.AuthViewModel
// Orquestador de vistas (estados)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {
    // Validacion state (boolean)
    val destinoInicial = if (authViewModel.estaUsuarioLogueado()) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = destinoInicial,
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToHome = {
                    navController.navigate("home") {
                        // Destruye el Back Stack del login
                        popUpTo("login") { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                // Vuelve atras al login
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }

        composable("home") {
            HomeScreen(
                onLogout = {
                    // Cierre sesion y navega al login
                    authViewModel.cerrarSesion()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }
    }
}
