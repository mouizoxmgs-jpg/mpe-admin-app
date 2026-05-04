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
import com.mpe.admin.data.model.LocalizedText
import com.mpe.admin.data.model.Property
import com.mpe.admin.ui.theme.*

@Composable
fun PropertyDialog(
    property: Property? = null,
    onDismiss: () -> Unit,
    onSave: (Property) -> Unit
) {
    var nameFr by remember { mutableStateOf(property?.name?.fr ?: "") }
    var nameEn by remember { mutableStateOf(property?.name?.en ?: "") }
    var bedrooms by remember { mutableStateOf(property?.bedrooms?.toString() ?: "0") }
    var bathrooms by remember { mutableStateOf(property?.bathrooms?.toString() ?: "0") }
    var kitchen by remember { mutableStateOf(property?.kitchen?.toString() ?: "0") }
    var parking by remember { mutableStateOf(property?.parking?.toString() ?: "0") }
    var pool by remember { mutableStateOf(property?.pool?.toString() ?: "0") }
    var imageUrl by remember { mutableStateOf(property?.image ?: "") }
    var badgeFr by remember { mutableStateOf(property?.badge?.fr ?: "") }

    val isEditing = property != null
    val isValid = nameFr.isNotBlank()

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
                        text = if (isEditing) "MODIFIER" else "AJOUTER",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Gold,
                            letterSpacing = 3.sp
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
                    // Names
                    FormSection("NOM") {
                        FormField(
                            value = nameFr,
                            onValueChange = { nameFr = it },
                            label = "Nom (Français) *",
                            imeAction = ImeAction.Next
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        FormField(
                            value = nameEn,
                            onValueChange = { nameEn = it },
                            label = "Name (English)",
                            imeAction = ImeAction.Next
                        )
                    }

                    // Specs
                    FormSection("CARACTÉRISTIQUES") {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            NumberField(
                                value = bedrooms,
                                onValueChange = { bedrooms = it },
                                label = "Chambres",
                                modifier = Modifier.weight(1f)
                            )
                            NumberField(
                                value = bathrooms,
                                onValueChange = { bathrooms = it },
                                label = "Sdb",
                                modifier = Modifier.weight(1f)
                            )
                            NumberField(
                                value = kitchen,
                                onValueChange = { kitchen = it },
                                label = "Cuisine",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            NumberField(
                                value = parking,
                                onValueChange = { parking = it },
                                label = "Parking",
                                modifier = Modifier.weight(1f)
                            )
                            NumberField(
                                value = pool,
                                onValueChange = { pool = it },
                                label = "Piscine",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    // Image
                    FormSection("IMAGE") {
                        FormField(
                            value = imageUrl,
                            onValueChange = { imageUrl = it },
                            label = "URL de l'image principale",
                            keyboardType = KeyboardType.Uri,
                            imeAction = ImeAction.Next
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Collez l'URL d'une image (https://...)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    // Badge
                    FormSection("BADGE (optionnel)") {
                        FormField(
                            value = badgeFr,
                            onValueChange = { badgeFr = it },
                            label = "Badge (ex: NOUVEAU, SOLD OUT)",
                            imeAction = ImeAction.Done
                        )
                    }
                }

                HorizontalDivider(color = GoldDark.copy(alpha = 0.3f))

                // Save button
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
                            onSave(
                                Property(
                                    id = property?.id ?: "",
                                    name = LocalizedText(fr = nameFr, en = nameEn),
                                    bedrooms = bedrooms.toIntOrNull() ?: 0,
                                    bathrooms = bathrooms.toIntOrNull() ?: 0,
                                    kitchen = kitchen.toIntOrNull() ?: 0,
                                    parking = parking.toIntOrNull() ?: 0,
                                    pool = pool.toIntOrNull() ?: 0,
                                    image = imageUrl,
                                    images = if (imageUrl.isNotEmpty()) listOf(imageUrl) else emptyList(),
                                    badge = LocalizedText(fr = badgeFr, en = badgeFr)
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
private fun FormSection(title: String, content: @Composable ColumnScope.() -> Unit) {
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
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        colors = dialogFieldColors(),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    )
}

@Composable
private fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) onValueChange(it) },
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        colors = dialogFieldColors(),
        modifier = modifier,
        shape = RoundedCornerShape(4.dp)
    )
}

@Composable
private fun dialogFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Gold,
    unfocusedBorderColor = GoldDark.copy(alpha = 0.5f),
    focusedLabelColor = Gold,
    unfocusedLabelColor = TextSecondary,
    cursorColor = Gold,
    focusedTextColor = OffWhite,
    unfocusedTextColor = OffWhite,
    focusedContainerColor = ObsidianLight,
    unfocusedContainerColor = ObsidianLight
)
