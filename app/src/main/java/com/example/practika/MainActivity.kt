package com.example.practika

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.practika.ui.navigation.AppNavigation
import com.example.practika.ui.theme.PRACTIKATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PRACTIKATheme {
                AppNavigation()
            }
        }
    }
}