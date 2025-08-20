package ir.amirroid.simplechat.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ir.amirroid.simplechat.utils.AppPages
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val rooms by viewModel.rooms.collectAsStateWithLifecycle()
    val isCreteRoomDialogVisible = viewModel.isCreteRoomDialogVisible

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                viewModel.isCreteRoomDialogVisible = true
            }) {
                Text("Create room")
            }
        }
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text("Chats")
                }
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(rooms, key = { it.id }) { room ->
                    ListItem(
                        headlineContent = {
                            Text(room.name.orEmpty())
                        },
                        supportingContent = room.lastMessage?.let { lastMessage ->
                            { Text(lastMessage) }
                        },
                        modifier = Modifier.clickable {
                            navController.navigate(
                                AppPages.Messages(
                                    room.id,
                                    room.name.orEmpty()
                                )
                            )
                        }
                    )
                }
            }
        }
    }
    if (isCreteRoomDialogVisible) {
        CreateRoomDialog(
            onCreate = viewModel::createRoom,
            onDismiss = { viewModel.isCreteRoomDialogVisible = false }
        )
    }
}

@Composable
fun CreateRoomDialog(
    onDismiss: () -> Unit,
    onCreate: (userId: String) -> Unit
) {
    var userId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Create Room")
        },
        text = {
            Column {
                Text("Enter User ID:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = userId,
                    onValueChange = { userId = it },
                    singleLine = true,
                    placeholder = { Text("User ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (userId.isNotBlank()) {
                        onCreate(userId)
                        onDismiss()
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
