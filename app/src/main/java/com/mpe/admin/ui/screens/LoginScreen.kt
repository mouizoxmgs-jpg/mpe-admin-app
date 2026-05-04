package com.mpe.admin.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mpe.admin.ui.theme.*
import com.mpe.admin.viewmodel.AdminViewModel

@Composable
fun LoginScreen(viewModel: AdminViewModel) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val doLogin = {
        focusManager.clearFocus()
        isLoading = true
        val success = viewModel.login(password)
        if (!success) {
            showError = true
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Obsidian, ObsidianMid, ObsidianLight)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            // Logo
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "MPE",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Gold,
                    fontSize = 42.sp,
                    letterSpacing = 6.sp
                )
            )

            Text(
                text = "MARRAKECH PREMIUM EXPERIENCE",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    letterSpacing = 3.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ADMIN PANEL",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = GoldDark,
                    letterSpacing = 4.sp
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Divider
            HorizontalDivider(
                modifier = Modifier.width(60.dp),
                color = GoldDark,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    showError = false
                },
                label = { Text("Mot de passe") },
                singleLine = true,
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { doLogin() }),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Gold,
                    unfocusedBorderColor = GoldDark,
                    focusedLabelColor = Gold,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = Gold,
                    focusedTextColor = OffWhite,
                    unfocusedTextColor = OffWhite
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp)
            )

            // Error message
            AnimatedVisibility(visible = showError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Mot de passe incorrect",
                    color = ErrorRed,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = { doLogin() },
                enabled = password.isNotEmpty() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor = Obsidian,
                    disabledContainerColor = GoldDark.copy(alpha = 0.4f),
                    disabledContentColor = Obsidian.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Obsidian,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "SE CONNECTER",
                        style = MaterialTheme.typography.titleMedium.copy(
                            letterSpacing = 3.sp
                        )
                    )
                }
            }
        }
    }
}
