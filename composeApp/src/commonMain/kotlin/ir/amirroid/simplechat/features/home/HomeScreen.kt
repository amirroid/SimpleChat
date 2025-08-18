package ir.amirroid.simplechat.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val rooms by viewModel.rooms.collectAsStateWithLifecycle()

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
                    }
                )
            }
        }
    }
}