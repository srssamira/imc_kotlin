package com.example.imc.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.imc.domain.ActivityLevel
import com.example.imc.viewmodel.HealthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HealthViewModel) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }
    var selectedActivity by remember { mutableStateOf(ActivityLevel.SEDENTARY) }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Calculadora Saúde") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = height, onValueChange = { height = it },
                label = { Text("Altura (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = weight, onValueChange = { weight = it },
                label = { Text("Peso (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = age, onValueChange = { age = it },
                label = { Text("Idade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Seletor de Sexo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Sexo: ")
                RadioButton(selected = isMale, onClick = { isMale = true })
                Text("Masc")
                Spacer(Modifier.width(8.dp))
                RadioButton(selected = !isMale, onClick = { isMale = false })
                Text("Fem")
            }

            // Dropdown Atividade
            Box {
                Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Atividade: ${selectedActivity.label}")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    ActivityLevel.values().forEach { level ->
                        DropdownMenuItem(
                            text = { Text(level.label) },
                            onClick = {
                                selectedActivity = level
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val result = viewModel.calculateAndSave(weight, height, age, isMale, selectedActivity)
                    if (result.isSuccess) {
                        navController.navigate("history")
                        Toast.makeText(context, "Calculado e salvo!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("CALCULAR E SALVAR")
            }

            OutlinedButton(
                onClick = { navController.navigate("history") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("VER HISTÓRICO")
            }
        }
    }
}