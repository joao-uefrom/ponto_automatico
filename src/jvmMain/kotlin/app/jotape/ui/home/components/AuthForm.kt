package app.jotape.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import app.jotape.ui.home.HomeUIState
import app.jotape.ui.home.HomeViewModel

@Composable
fun AuthForm(uiState: HomeUIState, modifier: Modifier) {
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

            Row {
                IconButton(
                    enabled = uiState.isLoading.not() && HomeViewModel.canGoBackAuthFields(),
                    onClick = HomeViewModel::getUserStored,
                    modifier = Modifier.weight(.3f).padding(end = 8.dp)
                ) {
                    Icon(
                        Icons.Filled.Undo,
                        null
                    )
                }
                Button(
                    enabled = uiState.isLoading.not() && uiState.isValid.not(),
                    modifier = Modifier.weight(.7f).fillMaxWidth(),
                    onClick = HomeViewModel::validateAuth
                ) {
                    Text(
                        if (uiState.isLoading) "Validando"
                        else if (uiState.isValid.not()) "Validar"
                        else "Validado"
                    )
                }
            }
        }
    }
}