package app.jotape.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import app.jotape.services.GlobalService
import app.jotape.ui.home.HomeViewModel
import app.jotape.ui.home.HomeUIState

@Composable
fun AuthForm(uiState: HomeUIState, modifier: Modifier) {
    val globalState by GlobalService.state.collectAsState()

    Column(modifier) {
        SectionTitle("Autenticação")

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            TextField(
                enabled = uiState.isLoading.not(),
                isError = uiState.emailHasError,
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Filled.Email, "") },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = HomeViewModel::setEmail,
                singleLine = true,
                value = uiState.email
            )

            TextField(
                enabled = uiState.isLoading.not(),
                isError = uiState.passwordHasError,
                label = { Text("Senha") },
                leadingIcon = { Icon(Icons.Filled.Lock, "") },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = HomeViewModel::setPassword,
                singleLine = true,
                value = uiState.password,
                visualTransformation = PasswordVisualTransformation()
            )

            TextField(
                enabled = uiState.isLoading.not(),
                isError = uiState.twoFaHasError,
                label = { Text("Chave 2FA") },
                leadingIcon = { Icon(Icons.Filled.Key, "") },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = HomeViewModel::set2fa,
                singleLine = true,
                value = uiState.twoFa,
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                enabled = uiState.isLoading.not() && globalState.isValid.not(),
                modifier = Modifier.fillMaxWidth(),
                onClick = HomeViewModel::validateAuth
            ) {
                Text(if (uiState.isLoading) "Validando" else if (globalState.isValid.not()) "Validar" else "Validado")
            }
        }
    }
}