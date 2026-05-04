package com.mpe.admin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mpe.admin.data.model.Car
import com.mpe.admin.ui.components.CarCard
import com.mpe.admin.ui.components.CarDialog
import com.mpe.admin.ui.theme.*
import com.mpe.admin.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarsScreen(viewModel: AdminViewModel) {
    val cars by viewModel.cars.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var selectedFilter by remember { mutableStateOf("tous") }
    var showAddDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Car?>(null) }
    var deleteTarget by remember { mutableStateOf<Car?>(null) }

    val filters = listOf("tous" to "Tous", "luxury" to "Luxe", "economy" to "Économie")
    val filteredCars = if (selectedFilter == "tous") cars
    else cars.filter { it.category == selectedFilter }

    if (showAddDialog || editTarget != null) {
        CarDialog(
            car = editTarget,
            onDismiss = {
                showAddDialog = false
                editTarget = null
            },
            onSave = { car ->
                if (editTarget != null) viewModel.updateCar(car)
                else viewModel.addCar(car)
                showAddDialog = false
                editTarget = null
            }
        )
    }

    deleteTarget?.let { car ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("Supprimer") },
            text = { Text("Voulez-vous supprimer \"${car.name}\" ?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteCar(car.id)
                    deleteTarget = null
                }) { Text("Supprimer", color = ErrorRed) }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) { Text("Annuler") }
            },
            containerColor = ObsidianSurface
        )
    }

    Scaffold(
        containerColor = Obsidian,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "VÉHICULES",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Gold,
                                letterSpacing = 3.sp
                            )
                        )
                        Text(
                            text = "${filteredCars.size} véhicule${if (filteredCars.size > 1) "s" else ""}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ObsidianMid)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Gold,
                contentColor = Obsidian
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Obsidian)
                .padding(padding)
        ) {
            // Filter chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { (key, label) ->
                    FilterChip(
                        selected = selectedFilter == key,
                        onClick = { selectedFilter = key },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Gold,
                            selectedLabelColor = Obsidian,
                            containerColor = ObsidianCard,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Gold)
                }
            } else if (filteredCars.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Aucun véhicule",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Appuyez sur + pour ajouter",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredCars, key = { it.id }) { car ->
                        CarCard(
                            car = car,
                            onEdit = { editTarget = it },
                            onDelete = { deleteTarget = it }
                        )
                    }
                }
            }
        }
    }
}
