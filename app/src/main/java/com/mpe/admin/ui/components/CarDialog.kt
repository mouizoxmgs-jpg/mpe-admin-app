package com.mpe.admin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mpe.admin.data.model.Car
import com.mpe.admin.data.model.CarSpecs
import com.mpe.admin.data.model.LocalizedText
import com.mpe.admin.ui.theme.*

@Composable
fun CarDialog(
    car: Car? = null,
    onDismiss: () -> Unit,
    onSave: (Car) -> Unit
) {
    var name by remember { mutableStateOf(car?.name ?: "") }
    var category by remember { mutableStateOf(car?.category ?: "luxury") }
    var imageUrl by remember { mutableStateOf(car?.image ?: "") }
    var engine by remember { mutableStateOf(car?.specs?.engine ?: "") }
    var power by remember { mutableStateOf(car?.specs?.power ?: "") }
    var seats by remember { mutableStateOf(car?.specs?.seats ?: "") }
    var taglineFr by remember { mutableStateOf(car?.tagline?.fr ?: "") }
    var taglineEn by remember { mutableStateOf(car?.tagline?.en ?: "") }

    val isEditing = car != null
    val isValid = name.isNotBlank()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isEditing) "MODIFIER VÉHICULE" else "AJOUTER VÉHICULE",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Gold,
                            letterSpacing = 2.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextSecondary)
                    }
                }

                HorizontalDivider(color = GoldDark.copy(alpha = 0.3f))

                // Form
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Name
                    CarFormSection("NOM") {
                        CarFormField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Nom du véhicule *"
                        )
                    }

                    // Category
                    CarFormSection("CATÉGORIE") {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilterChip(
                                selected = category == "luxury",
                                onClick = { category = "luxury" },
                                label = { Text("Luxe") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Gold,
                                    selectedLabelColor = Obsidian,
                                    containerColor = ObsidianLight,
                                    labelColor = TextSecondary
                                )
                            )
                            FilterChip(
                                selected = category == "economy",
                                onClick = { category = "economy" },
                                label = { Text("Économie") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Gold,
                                    selectedLabelColor = Obsidian,
                                    containerColor = ObsidianLight,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    // Image
                    CarFormSection("IMAGE") {
                        CarFormField(
                            value = imageUrl,
                            onValueChange = { imageUrl = it },
                            label = "URL de l'image principale",
                            keyboardType = KeyboardType.Uri
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Collez l'URL d'une image (https://...)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    // Specs
                    CarFormSection("CARACTÉRISTIQUES TECHNIQUES") {
                        CarFormField(
                            value = engine,
                            onValueChange = { engine = it },
                            label = "Moteur (ex: V8 4.0L)"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            CarFormField(
                                value = power,
                                onValueChange = { power = it },
                                label = "Puissance (ex: 450 ch)",
                                modifier = Modifier.weight(1f)
                            )
                            CarFormField(
                                value = seats,
                                onValueChange = { seats = it },
                                label = "Places (ex: 5)",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Tagline
                    CarFormSection("SLOGAN (optionnel)") {
                        CarFormField(
                            value = taglineFr,
                            onValueChange = { taglineFr = it },
                            label = "Slogan (Français)"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        CarFormField(
                            value = taglineEn,
                            onValueChange = { taglineEn = it },
                            label = "Tagline (English)",
                            imeAction = ImeAction.Done
                        )
                    }
                }

                HorizontalDivider(color = GoldDark.copy(alpha = 0.3f))

                // Actions
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(TextSecondary)
                        )
                    ) { Text("Annuler") }

                    Button(
                        onClick = {
                            val badge = if (category == "luxury")
                                LocalizedText(fr = "LUXE", en = "LUXURY")
                            else
                                LocalizedText(fr = "ÉCONOMIE", en = "ECONOMY")
                            onSave(
                                Car(
                                    id = car?.id ?: "",
                                    name = name,
                                    category = category,
                                    image = imageUrl,
                                    images = if (imageUrl.isNotEmpty()) listOf(imageUrl) else emptyList(),
                                    specs = CarSpecs(engine = engine, power = power, seats = seats),
                                    tagline = LocalizedText(fr = taglineFr, en = taglineEn),
                                    badge = badge
                                )
                            )
                        },
                        enabled = isValid,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gold,
                            contentColor = Obsidian
                        )
                    ) {
                        Text(if (isEditing) "MODIFIER" else "AJOUTER")
                    }
                }
            }
        }
    }
}

@Composable
private fun CarFormSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp),
            color = Gold,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        content()
    }
}

@Composable
private fun CarFormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Gold,
            unfocusedBorderColor = GoldDark.copy(alpha = 0.5f),
            focusedLabelColor = Gold,
            unfocusedLabelColor = TextSecondary,
            cursorColor = Gold,
            focusedTextColor = OffWhite,
            unfocusedTextColor = OffWhite,
            focusedContainerColor = ObsidianLight,
            unfocusedContainerColor = ObsidianLight
        ),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    )
}
