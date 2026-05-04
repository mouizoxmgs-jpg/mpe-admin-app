package com.mpe.admin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.mpe.admin.data.model.Property
import com.mpe.admin.ui.components.PropertyCard
import com.mpe.admin.ui.components.PropertyDialog
import com.mpe.admin.ui.theme.*
import com.mpe.admin.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesScreen(type: String, viewModel: AdminViewModel) {
    val villas by viewModel.villas.collectAsStateWithLifecycle()
    val apartments by viewModel.apartments.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val properties = if (type == "villas") villas else apartments
    val title = if (type == "villas") "VILLAS" else "APPARTEMENTS"

    var showAddDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Property?>(null) }
    var deleteTarget by remember { mutableStateOf<Property?>(null) }

    // Add/Edit dialog
    if (showAddDialog || editTarget != null) {
        PropertyDialog(
            property = editTarget,
            onDismiss = {
                showAddDialog = false
                editTarget = null
            },
            onSave = { prop ->
                if (editTarget != null) {
                    if (type == "villas") viewModel.updateVilla(prop)
                    else viewModel.updateApartment(prop)
                } else {
                    if (type == "villas") viewModel.addVilla(prop)
                    else viewModel.addApartment(prop)
                }
                showAddDialog = false
                editTarget = null
            }
        )
    }

    // Delete confirmation
    deleteTarget?.let { prop ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("Supprimer") },
            text = {
                Text("Voulez-vous supprimer \"${prop.name.fr.ifEmpty { prop.name.en }}\" ?")
            },
            confirmButton = {
                TextButton(onClick = {
                    if (type == "villas") viewModel.deleteVilla(prop.id)
                    else viewModel.deleteApartment(prop.id)
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
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Gold,
                                letterSpacing = 3.sp
                            )
                        )
                        Text(
                            text = "${properties.size} élément${if (properties.size > 1) "s" else ""}",
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
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Gold)
            }
        } else if (properties.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Aucun élément",
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
                modifier = Modifier
                    .fillMaxSize()
                    .background(Obsidian)
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(properties, key = { it.id }) { property ->
                    PropertyCard(
                        property = property,
                        onEdit = { editTarget = it },
                        onDelete = { deleteTarget = it }
                    )
                }
                item { Spacer(modifier = Modifier.height(72.dp)) }
            }
        }
    }
}
