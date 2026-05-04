package com.mpe.admin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mpe.admin.data.model.Car
import com.mpe.admin.data.model.Property
import com.mpe.admin.ui.theme.*
import com.mpe.admin.viewmodel.AdminViewModel

@Composable
fun DashboardScreen(viewModel: AdminViewModel) {
    val villas by viewModel.villas.collectAsStateWithLifecycle()
    val apartments by viewModel.apartments.collectAsStateWithLifecycle()
    val cars by viewModel.cars.collectAsStateWithLifecycle()
    val fbConnected by viewModel.firebaseConnected.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Déconnexion") },
            text = { Text("Voulez-vous vous déconnecter ?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout()
                }) { Text("Déconnecter", color = ErrorRed) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Annuler") }
            },
            containerColor = ObsidianSurface
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Obsidian),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MPE ADMIN",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Gold,
                            letterSpacing = 3.sp
                        )
                    )
                    Text(
                        text = "Tableau de bord",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
                IconButton(
                    onClick = { showLogoutDialog = true },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = TextSecondary
                    )
                ) {
                    Icon(Icons.Default.Logout, contentDescription = "Déconnexion")
                }
            }
        }

        // Firebase status
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            if (fbConnected) SuccessGreen else ErrorRed,
                            shape = RoundedCornerShape(50)
                        )
                )
                Text(
                    text = if (fbConnected) "Firebase connecté" else "Mode hors ligne",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (fbConnected) SuccessGreen else ErrorRed
                )
            }
        }

        // Stats cards
        item {
            Text(
                text = "STATISTIQUES",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 3.sp),
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.House,
                    count = villas.size,
                    label = "Villas"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Apartment,
                    count = apartments.size,
                    label = "Appartements"
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.DirectionsCar,
                    count = cars.size,
                    label = "Véhicules"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Dashboard,
                    count = villas.size + apartments.size + cars.size,
                    label = "Total"
                )
            }
        }

        // Recent villas
        if (villas.isNotEmpty()) {
            item {
                SectionHeader("VILLAS RÉCENTES")
            }
            items(villas.takeLast(2)) { villa ->
                RecentPropertyItem(property = villa)
            }
        }

        // Recent apartments
        if (apartments.isNotEmpty()) {
            item { SectionHeader("APPARTEMENTS RÉCENTS") }
            items(apartments.takeLast(2)) { apt ->
                RecentPropertyItem(property = apt)
            }
        }

        // Recent cars
        if (cars.isNotEmpty()) {
            item { SectionHeader("VÉHICULES RÉCENTS") }
            items(cars.takeLast(2)) { car ->
                RecentCarItem(car = car)
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    count: Int,
    label: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = Gold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 3.sp),
        color = TextSecondary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun RecentPropertyItem(property: Property) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (property.image.startsWith("http")) {
                AsyncImage(
                    model = property.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .background(ObsidianLight, RoundedCornerShape(6.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(ObsidianLight, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.House, contentDescription = null, tint = GoldDark)
                }
            }
            Column {
                Text(
                    text = property.name.fr.ifEmpty { property.name.en },
                    style = MaterialTheme.typography.titleMedium,
                    color = OffWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${property.bedrooms} ch. · ${property.bathrooms} sdb · ${if (property.pool > 0) "Piscine" else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun RecentCarItem(car: Car) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (car.image.startsWith("http")) {
                AsyncImage(
                    model = car.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .background(ObsidianLight, RoundedCornerShape(6.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(ObsidianLight, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = GoldDark)
                }
            }
            Column {
                Text(
                    text = car.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = OffWhite,
                    maxLines = 1
                )
                Text(
                    text = car.category.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gold
                )
            }
        }
    }
}
