package ir.amirroid.simplechat.features.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.amirroid.simplechat.models.message.Message
import ir.amirroid.simplechat.models.message.MessageDeliveryStatus
import ir.amirroid.simplechat.utils.Check
import ir.amirroid.simplechat.utils.DoneAll
import ir.amirroid.simplechat.utils.Send
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = koinViewModel()
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isUpdatingMessages by viewModel.isUpdatingMessages.collectAsStateWithLifecycle()
    val newMessage = viewModel.newMessage
    val lazyState = rememberLazyListState()
    val myUser by viewModel.myUser.collectAsStateWithLifecycle(null)

    LaunchedEffect(messages) {
        lazyState.scrollToItem(0)
    }

    LaunchedEffect(myUser, messages) {
        snapshotFlow { lazyState.layoutInfo.visibleItemsInfo }.collect { visibleItemsInfo ->
            val messages = visibleItemsInfo.map { messages[it.index] }
            messages.filter {
                it.sender.isMe.not() &&
                        it.statuses.firstOrNull { status -> status.user.userId == myUser?.userId }?.status != MessageDeliveryStatus.SEEN
            }.forEach {
                viewModel.seenMessage(it)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(title = {
            Text(viewModel.name)
        })
        Box(Modifier.weight(1f)) {
            LazyColumn(modifier = Modifier.fillMaxSize(), reverseLayout = true, state = lazyState) {
                items(messages, key = { it.id }) { message ->
                    MessageItem(message)
                }
            }
            UpdatingMessage(isUpdatingMessages)
        }
        OutlinedTextField(
            newMessage,
            onValueChange = { viewModel.newMessage = it },
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp),
            placeholder = { Text("New Message...") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { viewModel.sendMessage() }),
            trailingIcon = {
                IconButton(onClick = viewModel::sendMessage) {
                    Icon(Send, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun MessageItem(message: Message) {
    val isMe = message.sender.isMe
    val formattedTime = remember(message) {
        val time = message.updatedAt ?: message.createdAt
        time.format(LocalDateTime.Format { byUnicodePattern("yyyy/MM/dd - HH:mm") })
    }
    val icon = remember(message.statuses) {
        val statuses = message.statuses
        when {
            statuses.any { it.status == MessageDeliveryStatus.SEEN } -> DoneAll
            statuses.isNotEmpty() -> Check
            else -> null
        }.takeIf { message.sender.isMe }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isMe) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(
                topStart = if (isMe) 16.dp else 0.dp,
                topEnd = if (isMe) 0.dp else 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            modifier = Modifier
                .widthIn(max = maxWidth * 0.7f)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isMe) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    icon?.let {
                        Icon(it, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun UpdatingMessage(visible: Boolean) {
    AnimatedVisibility(visible, enter = expandVertically(), exit = shrinkVertically()) {
        Surface(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 1.dp,
                    strokeCap = StrokeCap.Round
                )
                Spacer(Modifier.width(8.dp))
                Text("Updating messages...")
            }
        }
    }
}