package ir.amirroid.simplechat.features.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.amirroid.simplechat.components.ErrorBox
import ir.amirroid.simplechat.components.LoadingBox
import ir.amirroid.simplechat.data.response.onError
import ir.amirroid.simplechat.data.response.onIdle
import ir.amirroid.simplechat.data.response.onLoading
import ir.amirroid.simplechat.data.response.onSuccess
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val userId = viewModel.userId
    val username = viewModel.username
    val password = viewModel.password
    val isSignInMethod = viewModel.isSignInMethod
    val response by viewModel.registerResponse.collectAsStateWithLifecycle()

    response.onIdle {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CenterAlignedTopAppBar(
                title = { Text(if (isSignInMethod) "Login" else "Sign up") }
            )

            OutlinedTextField(
                value = userId,
                onValueChange = { viewModel.userId = it },
                label = { Text("User ID") },
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(!isSignInMethod) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { viewModel.username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSignInMethod,
                    onCheckedChange = { viewModel.isSignInMethod = it }
                )
                Text(if (isSignInMethod) "Login" else "Sign up")
            }

            Button(
                onClick = {
                    if (isSignInMethod) {
                        viewModel.login()
                    } else {
                        viewModel.signup()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isSignInMethod) "Login" else "Sign up")
            }
        }
    }.onSuccess { response ->
        LaunchedEffect(Unit) {
            viewModel.saveToken(response.data)
        }
    }.onLoading {
        LoadingBox()
    }.onError {
        ErrorBox(it, onRetry = viewModel::clearResponse)
    }
}