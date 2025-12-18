package com.example.imc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.imc.ui.HistoryScreen
import com.example.imc.ui.HomeScreen
import com.example.imc.viewmodel.HealthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Instancia o ViewModel
            val viewModel: HealthViewModel = viewModel()
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(navController, viewModel) }
                composable("history") { HistoryScreen(navController, viewModel) }
            }
        }
    }
}