package com.example.imc.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.imc.data.HealthRecord
import com.example.imc.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, viewModel: HealthViewModel) {
    val history by viewModel.history.collectAsState(emptyList());
    var selectedRecord by remember { mutableStateOf<HealthRecord?>(null) }

    if (selectedRecord != null) {
        AlertDialog(
            onDismissRequest = { selectedRecord = null },
            title = { Text("Detalhes da Medição") },
            text = {
                Column {
                    Text("IMC: %.2f (${selectedRecord!!.imcClassification})".format(selectedRecord!!.imc))
                    Text("TMB: %.0f kcal".format(selectedRecord!!.tmb))
                    Text("Gordura: %.1f%%".format(selectedRecord!!.bodyFat))
                    Text("Nec. Calórica: %.0f kcal".format(selectedRecord!!.caloricNeed))
                    Text("Peso: ${selectedRecord!!.weight} kg | Altura: ${selectedRecord!!.height} cm")
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedRecord = null }) { Text("FECHAR") }
            }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Histórico") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(items = history, key = { it.id }) { record ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { selectedRecord = record },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            Text(text = sdf.format(Date(record.date)), style = MaterialTheme.typography.bodySmall)
                            Text(text = "IMC: %.2f".format(record.imc), style = MaterialTheme.typography.titleMedium)
                            Text(text = record.imcClassification, color = if(record.imc > 25) Color.Red else Color.Green)
                        }
                        IconButton(onClick = { viewModel.deleteRecord(record) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Deletar")
                        }
                    }
                }
            }
        }
    }
}