package com.mpe.admin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mpe.admin.data.model.Car
import com.mpe.admin.ui.theme.*

@Composable
fun CarCard(
    car: Car,
    onEdit: (Car) -> Unit,
    onDelete: (Car) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Image
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .background(ObsidianLight)
            ) {
                val imgUrl = car.image.takeIf { it.startsWith("http") }
                    ?: car.images.firstOrNull { it.startsWith("http") }

                if (imgUrl != null) {
                    AsyncImage(
                        model = imgUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = GoldDark,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = car.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = OffWhite,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        color = if (car.category == "luxury") Gold else GoldDark.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = if (car.category == "luxury") "LUXE" else "ÉCON.",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (car.category == "luxury") Obsidian else TextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Specs
                if (car.specs.engine.isNotEmpty() || car.specs.power.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (car.specs.engine.isNotEmpty()) {
                            Text(
                                text = car.specs.engine,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                        if (car.specs.power.isNotEmpty()) {
                            Text(
                                text = "· ${car.specs.power}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                        if (car.specs.seats.isNotEmpty()) {
                            Text(
                                text = "· ${car.specs.seats}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }
                }

                if (car.tagline.fr.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = car.tagline.fr,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onEdit(car) },
                        colors = ButtonDefaults.textButtonColors(contentColor = Gold),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Modifier", style = MaterialTheme.typography.labelSmall)
                    }
                    TextButton(
                        onClick = { onDelete(car) },
                        colors = ButtonDefaults.textButtonColors(contentColor = ErrorRed),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Supprimer", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
