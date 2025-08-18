package ir.amirroid.simplechat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.utils.ErrorI

@Composable
fun ErrorBox(errorI: ErrorI, onRetry: () -> Unit) {
    val errorMessage = remember(errorI) {
        when (errorI) {
            is NetworkErrors -> {
                errorI.response?.message ?: errorI::class.simpleName.orEmpty()
            }

            else -> "An error occurred!"
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(errorMessage, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}