package com.example.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.ZyloViewModel
import com.example.ui.theme.ZyloPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
  chatId: Int,
  participantName: String,
  viewModel: ZyloViewModel,
  onBack: () -> Unit
) {
  val messagesFlow = viewModel.getMessagesForChat(chatId)
  val messages by messagesFlow.collectAsState(initial = emptyList())
  var textInput by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(participantName, fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background)
    ) {
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
      ) {
        items(messages) { message ->
          val alignment = if (message.isMe) Alignment.End else Alignment.Start
          val bubbleColor = if (message.isMe) ZyloPrimary else MaterialTheme.colorScheme.surfaceVariant
          val textColor = if (message.isMe) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurfaceVariant

          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = alignment
          ) {
            Surface(
              shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isMe) 16.dp else 4.dp,
                bottomEnd = if (message.isMe) 4.dp else 16.dp
              ),
              color = bubbleColor,
              modifier = Modifier.widthIn(max = 280.dp)
            ) {
              Text(
                text = message.message,
                color = textColor,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
        }
      }

      // Input Bar
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            placeholder = { Text("Digite sua mensagem...") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(
            onClick = {
              if (textInput.isNotBlank()) {
                viewModel.sendMessage(chatId, textInput)
                textInput = ""
              }
            },
            modifier = Modifier
              .size(48.dp)
              .background(ZyloPrimary, CircleShape)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Enviar",
              tint = androidx.compose.ui.graphics.Color.White
            )
          }
        }
      }
    }
  }
}
