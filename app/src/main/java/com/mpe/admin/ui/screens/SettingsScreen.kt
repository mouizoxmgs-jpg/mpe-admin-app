package com.mpe.admin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mpe.admin.data.model.AppSettings
import com.mpe.admin.ui.theme.*
import com.mpe.admin.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: AdminViewModel) {
    val currentSettings by viewModel.settings.collectAsStateWithLifecycle()

    var phone by remember(currentSettings) { mutableStateOf(currentSettings.phone) }
    var instagram by remember(currentSettings) { mutableStateOf(currentSettings.instagram) }
    var snapchat by remember(currentSettings) { mutableStateOf(currentSettings.snapchat) }
    var tiktok by remember(currentSettings) { mutableStateOf(currentSettings.tiktok) }
    var imgbbKey by remember(currentSettings) { mutableStateOf(currentSettings.imgbbKey) }
    var keyVisible by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Obsidian,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PARAMÈTRES",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Gold,
                            letterSpacing = 3.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ObsidianMid)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Obsidian)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingsSection(title = "CONTACT") {
                    SettingsField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "WhatsApp (ex: 212600000000)",
                        icon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone
                    )
                }
            }

            item {
                SettingsSection(title = "RÉSEAUX SOCIAUX") {
                    SettingsField(
                        value = instagram,
                        onValueChange = { instagram = it },
                        label = "Instagram (URL)",
                        icon = Icons.Default.Link
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SettingsField(
                        value = snapchat,
                        onValueChange = { snapchat = it },
                        label = "Snapchat (URL)",
                        icon = Icons.Default.Link
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SettingsField(
                        value = tiktok,
                        onValueChange = { tiktok = it },
                        label = "TikTok (URL)",
                        icon = Icons.Default.Link
                    )
                }
            }

            item {
                SettingsSection(title = "UPLOAD IMAGES") {
                    OutlinedTextField(
                        value = imgbbKey,
                        onValueChange = { imgbbKey = it },
                        label = { Text("Clé API ImgBB") },
                        leadingIcon = {
                            Icon(Icons.Default.Key, contentDescription = null, tint = TextSecondary)
                        },
                        trailingIcon = {
                            IconButton(onClick = { keyVisible = !keyVisible }) {
                                Icon(
                                    imageVector = if (keyVisible) Icons.Default.VisibilityOff
                                    else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (keyVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        colors = settingsFieldColors(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Obtenez votre clé sur api.imgbb.com",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        viewModel.saveSettings(
                            AppSettings(
                                phone = phone,
                                instagram = instagram,
                                snapchat = snapchat,
                                tiktok = tiktok,
                                imgbbKey = imgbbKey
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold,
                        contentColor = Obsidian
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SAUVEGARDER",
                        style = MaterialTheme.typography.titleMedium.copy(letterSpacing = 2.sp)
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 3.sp),
                color = Gold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

@Composable
private fun SettingsField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Uri
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(icon, contentDescription = null, tint = TextSecondary)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        colors = settingsFieldColors(),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    )
}

@Composable
private fun settingsFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Gold,
    unfocusedBorderColor = GoldDark,
    focusedLabelColor = Gold,
    unfocusedLabelColor = TextSecondary,
    cursorColor = Gold,
    focusedTextColor = OffWhite,
    unfocusedTextColor = OffWhite
)
