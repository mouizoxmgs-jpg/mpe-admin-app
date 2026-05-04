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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mpe.admin.data.model.Property
import com.mpe.admin.ui.theme.*

@Composable
fun PropertyCard(
    property: Property,
    onEdit: (Property) -> Unit,
    onDelete: (Property) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Image header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(ObsidianLight)
            ) {
                val imgUrl = property.image.takeIf { it.startsWith("http") }
                    ?: property.images.firstOrNull { it.startsWith("http") }

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
                            imageVector = Icons.Default.House,
                            contentDescription = null,
                            tint = GoldDark,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Badge overlay
                if (property.badge.fr.isNotEmpty()) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        color = Gold,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = property.badge.fr,
                            style = MaterialTheme.typography.labelSmall,
                            color = Obsidian,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Card content
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = property.name.fr.ifEmpty { property.name.en },
                    style = MaterialTheme.typography.titleMedium,
                    color = OffWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (property.name.en.isNotEmpty() && property.name.en != property.name.fr) {
                    Text(
                        text = property.name.en,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Specs
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SpecItem(Icons.Default.Bed, property.bedrooms.toString(), "ch")
                    SpecItem(Icons.Default.Bathtub, property.bathrooms.toString(), "sdb")
                    if (property.kitchen > 0)
                        SpecItem(Icons.Default.Kitchen, property.kitchen.toString(), "cui")
                    if (property.pool > 0)
                        SpecItem(Icons.Default.Pool, property.pool.toString(), "pisc")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onEdit(property) },
                        colors = ButtonDefaults.textButtonColors(contentColor = Gold)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Modifier")
                    }
                    TextButton(
                        onClick = { onDelete(property) },
                        colors = ButtonDefaults.textButtonColors(contentColor = ErrorRed)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Supprimer")
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecItem(icon: ImageVector, value: String, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Gold,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "$value $label",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}
