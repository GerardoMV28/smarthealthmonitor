package com.example.smarthealthmonitor

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smarthealthmonitor.SmartHealthMonitorTheme
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {}
) {

    // Estados
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var showPassword by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var emailError by remember {
        mutableStateOf("")
    }

    // Validación
    fun validar(): Boolean {

        emailError = when {

            email.isBlank() ->
                "El email no puede estar vacío"

            !email.contains("@") ->
                "Email inválido"

            password.length < 6 ->
                "Mínimo 6 caracteres"

            else -> ""
        }

        return emailError.isEmpty()
    }

    SmartHealthMonitorTheme {

        Scaffold { paddingValues ->

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),

                horizontalAlignment = Alignment.CenterHorizontally,

                verticalArrangement = Arrangement.Center
            ) {

                // Logo
                Icon(
                    imageVector = Icons.Default.Favorite,

                    contentDescription = "SmartHealth Logo",

                    tint = MaterialTheme.colorScheme.primary,

                    modifier = Modifier.size(80.dp)
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "SmartHealth Monitor",

                    style = MaterialTheme.typography.headlineMedium,

                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                // Campo email
                OutlinedTextField(

                    value = email,

                    onValueChange = {
                        email = it
                        emailError = ""
                    },

                    label = {
                        Text("Correo electrónico")
                    },

                    isError = emailError.isNotEmpty(),

                    supportingText = {

                        if (emailError.isNotEmpty()) {

                            Text(
                                text = emailError,

                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),

                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                // Campo contraseña
                OutlinedTextField(

                    value = password,

                    onValueChange = {
                        password = it
                    },

                    label = {
                        Text("Contraseña")
                    },

                    visualTransformation =

                        if (showPassword)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),

                    trailingIcon = {

                        IconButton(

                            onClick = {
                                showPassword = !showPassword
                            }

                        ) {

                            Icon(

                                imageVector =

                                    if (showPassword)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,

                                contentDescription = "Mostrar contraseña"
                            )
                        }
                    },

                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                // Botón login
                Button(

                    onClick = {

                        if (validar()) {

                            isLoading = true

                            onLoginSuccess()
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    enabled = !isLoading
                ) {

                    if (isLoading) {

                        CircularProgressIndicator(

                            modifier = Modifier.size(24.dp),

                            color = MaterialTheme.colorScheme.onPrimary
                        )

                    } else {

                        Text(
                            text = "ENTRAR",

                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                TextButton(
                    onClick = {}
                ) {

                    Text(
                        text = "¿Olvidaste tu contraseña?"
                    )
                }

            } // Column

        } // Scaffold

    } // Theme
}

@Preview(
    name = "Login - Light",
    showBackground = true,
    showSystemUi = true,
    device = "id:pixel_6"
)

@Preview(
    name = "Login - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)

@Preview(
    name = "Login - Big Font",
    showBackground = true,
    fontScale = 1.5f
)

@Composable
private fun LoginScreenPreview() {

    SmartHealthMonitorTheme {

        LoginScreen()
    }
}