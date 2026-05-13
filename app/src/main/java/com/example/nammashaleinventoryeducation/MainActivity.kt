package com.example.nammashaleinventoryeducation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.nammashaleinventoryeducation.ui.navigation.NavGraph
import com.example.nammashaleinventoryeducation.ui.theme.NammaShaleInventoryEducationTheme
import com.example.nammashaleinventoryeducation.utils.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display to support modern Android navigation bars and status bars
        enableEdgeToEdge()

        val sessionManager = SessionManager(this)

        setContent {
            // State for dark mode, initialized from preferences
            var isDarkMode by remember { mutableStateOf(sessionManager.isDarkMode()) }

            NammaShaleInventoryEducationTheme(darkTheme = isDarkMode) {
                // Surface ensures the background color is applied correctly across the entire screen
                // which is important when enableEdgeToEdge is used.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        sessionManager = sessionManager,
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { enabled ->
                            isDarkMode = enabled
                            sessionManager.setDarkMode(enabled)
                        }
                    )
                }
            }
        }
    }
}
