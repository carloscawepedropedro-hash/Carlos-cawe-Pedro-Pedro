package com.example.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.ZyloViewModel
import com.example.ui.theme.ZyloPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
  viewModel: ZyloViewModel,
  onNavigateToSettings: () -> Unit
) {
  val currentUser by viewModel.currentUser.collectAsState()
  val posts by viewModel.allPosts.collectAsState()
  var showEditDialog by remember { mutableStateOf(false) }

  val myPosts = posts.filter { it.authorHandle == (currentUser?.handle ?: "@alexsilva") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Perfil", fontWeight = FontWeight.Bold) },
        actions = {
          IconButton(onClick = onNavigateToSettings) {
            Icon(Icons.Default.Settings, contentDescription = "Configurações")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            AsyncImage(
              model = currentUser?.avatarUrl ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop",
              contentDescription = null,
              modifier = Modifier
                .size(96.dp)
                .clip(CircleShape),
              contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = currentUser?.name ?: "Alex Silva",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = currentUser?.handle ?: "@alexsilva",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = currentUser?.bio ?: "Tech explorer & mobile developer 🚀✨",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly
            ) {
              StatItem(label = "Publicações", count = "${myPosts.size}")
              StatItem(label = "Seguidores", count = "${currentUser?.followersCount ?: 1420}")
              StatItem(label = "Seguindo", count = "${currentUser?.followingCount ?: 380}")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
              onClick = { showEditDialog = true },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ZyloPrimary)
            ) {
              Icon(Icons.Default.Edit, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Editar Perfil")
            }
          }
        }
      }

      item {
        Text(
          text = "Minhas Publicações",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }

      items(myPosts) { post ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.content, style = MaterialTheme.typography.bodyMedium)
            if (!post.imageUrl.isNullOrEmpty()) {
              Spacer(modifier = Modifier.height(8.dp))
              AsyncImage(
                model = post.imageUrl,
                contentDescription = null,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(180.dp)
                  .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
              )
            }
          }
        }
      }
    }
  }

  if (showEditDialog) {
    EditProfileDialog(
      currentUser = currentUser,
      onDismiss = { showEditDialog = false },
      onSave = { name, bio, avatar ->
        viewModel.updateProfile(name, bio, avatar)
        showEditDialog = false
      }
    )
  }
}

@Composable
fun StatItem(label: String, count: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = count, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
    Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
  }
}

@Composable
fun EditProfileDialog(
  currentUser: com.example.data.UserEntity?,
  onDismiss: () -> Unit,
  onSave: (String, String, String) -> Unit
) {
  var name by remember { mutableStateOf(currentUser?.name ?: "") }
  var bio by remember { mutableStateOf(currentUser?.bio ?: "") }
  var avatarUrl by remember { mutableStateOf(currentUser?.avatarUrl ?: "") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Editar Perfil", fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") }, shape = RoundedCornerShape(12.dp))
        OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Biografia") }, shape = RoundedCornerShape(12.dp))
        OutlinedTextField(value = avatarUrl, onValueChange = { avatarUrl = it }, label = { Text("URL da Foto") }, shape = RoundedCornerShape(12.dp))
      }
    },
    confirmButton = {
      Button(
        onClick = { onSave(name, bio, avatarUrl) },
        colors = ButtonDefaults.buttonColors(containerColor = ZyloPrimary),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("Salvar")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancelar") }
    },
    shape = RoundedCornerShape(20.dp)
  )
}
