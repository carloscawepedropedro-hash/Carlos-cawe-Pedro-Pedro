package com.example.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.CommentEntity
import com.example.data.PostEntity
import com.example.ui.ZyloViewModel
import com.example.ui.theme.ZyloPrimary
import com.example.ui.theme.ZyloSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
  viewModel: ZyloViewModel,
  onNavigateToSearch: () -> Unit,
  onNavigateToNotifications: () -> Unit
) {
  val posts by viewModel.allPosts.collectAsState()
  val currentUser by viewModel.currentUser.collectAsState()
  var showCreatePostDialog by remember { mutableStateOf(false) }
  var selectedPostForComments by remember { mutableStateOf<PostEntity?>(null) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Zylo Hub",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = ZyloPrimary
          )
        },
        actions = {
          IconButton(onClick = onNavigateToSearch) {
            Icon(Icons.Default.Search, contentDescription = "Pesquisar")
          }
          IconButton(onClick = onNavigateToNotifications) {
            Icon(Icons.Default.Notifications, contentDescription = "Notificações")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showCreatePostDialog = true },
        containerColor = ZyloPrimary,
        contentColor = androidx.compose.ui.graphics.Color.White,
        shape = RoundedCornerShape(16.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = "Criar Publicação")
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background),
      contentPadding = PaddingValues(vertical = 8.dp)
    ) {
      // Stories Row
      item {
        StoriesSection(currentUser?.avatarUrl)
      }

      // Create Post Quick Card
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showCreatePostDialog = true }
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            AsyncImage(
              model = currentUser?.avatarUrl ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop",
              contentDescription = "Avatar",
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
              contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "No que você está pensando, ${currentUser?.name?.substringBefore(" ") ?: "Alex"}?",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }
      }

      // Posts Feed
      items(posts) { post ->
        PostCard(
          post = post,
          onLike = { viewModel.toggleLike(post) },
          onCommentClick = { selectedPostForComments = post }
        )
      }
    }
  }

  if (showCreatePostDialog) {
    CreatePostDialog(
      onDismiss = { showCreatePostDialog = false },
      onSubmit = { content, imageUrl ->
        viewModel.createPost(content, imageUrl)
        showCreatePostDialog = false
      }
    )
  }

  if (selectedPostForComments != null) {
    CommentsBottomSheet(
      post = selectedPostForComments!!,
      viewModel = viewModel,
      onDismiss = { selectedPostForComments = null }
    )
  }
}

@Composable
fun StoriesSection(myAvatar: String?) {
  val stories = listOf(
    Pair("Seu Story", myAvatar ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop"),
    Pair("Beatriz", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=500&auto=format&fit=crop"),
    Pair("Carlos", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&auto=format&fit=crop"),
    Pair("Juliana", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=500&auto=format&fit=crop"),
    Pair("Marcos", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=500&auto=format&fit=crop")
  )

  LazyRow(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    items(stories) { story ->
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
      ) {
        Box(
          modifier = Modifier
            .size(68.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(
                colors = listOf(ZyloPrimary, ZyloSecondary)
              )
            )
            .padding(3.dp)
        ) {
          AsyncImage(
            model = story.second,
            contentDescription = story.first,
            modifier = Modifier
              .fillMaxSize()
              .clip(CircleShape),
            contentScale = ContentScale.Crop
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = story.first,
          style = MaterialTheme.typography.labelSmall,
          maxLines = 1,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }
  }
}

@Composable
fun PostCard(
  post: PostEntity,
  onLike: () -> Unit,
  onCommentClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Author header
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        AsyncImage(
          model = post.authorAvatar,
          contentDescription = null,
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape),
          contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = post.authorName,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = post.authorHandle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Content text
      Text(
        text = post.content,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
      )

      // Optional Image
      if (!post.imageUrl.isNullOrEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        AsyncImage(
          model = post.imageUrl,
          contentDescription = null,
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(14.dp)),
          contentScale = ContentScale.Crop
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Actions row (Like, Comment, Share)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onLike) {
            Icon(
              imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Curtir",
              tint = if (post.isLiked) androidx.compose.ui.graphics.Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Text(
            text = "${post.likesCount}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onCommentClick) {
            Icon(
              imageVector = Icons.Default.ChatBubbleOutline,
              contentDescription = "Comentar",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Text(
            text = "${post.commentsCount}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        IconButton(onClick = {}) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Compartilhar",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Composable
fun CreatePostDialog(onDismiss: () -> Unit, onSubmit: (String, String?) -> Unit) {
  var content by remember { mutableStateOf("") }
  var hasImage by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Criar Publicação", fontWeight = FontWeight.Bold) },
    text = {
      Column {
        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          placeholder = { Text("O que você quer compartilhar no Zylo Hub?") },
          modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
          shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(onClick = { hasImage = !hasImage }) {
            Icon(Icons.Default.Image, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (hasImage) "Imagem Adicionada ✓" else "Adicionar Imagem")
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (content.isNotBlank()) {
            val img = if (hasImage) "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&auto=format&fit=crop" else null
            onSubmit(content, img)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = ZyloPrimary),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("Publicar")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancelar")
      }
    },
    shape = RoundedCornerShape(20.dp)
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
  post: PostEntity,
  viewModel: ZyloViewModel,
  onDismiss: () -> Unit
) {
  val commentsFlow = viewModel.getCommentsForPost(post.id)
  val comments by commentsFlow.collectAsState(initial = emptyList())
  var newCommentText by remember { mutableStateOf("") }

  ModalBottomSheet(onDismissRequest = onDismiss) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.75f)
        .padding(16.dp)
    ) {
      Text(
        text = "Comentários",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(12.dp))

      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(comments) { comment ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
              .padding(12.dp),
            verticalAlignment = Alignment.Top
          ) {
            AsyncImage(
              model = comment.authorAvatar,
              contentDescription = null,
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
              contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = comment.authorName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = newCommentText,
          onValueChange = { newCommentText = it },
          placeholder = { Text("Escreva um comentário...") },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
          onClick = {
            if (newCommentText.isNotBlank()) {
              viewModel.addComment(post.id, newCommentText)
              newCommentText = ""
            }
          },
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ZyloPrimary)
        ) {
          Text("Enviar")
        }
      }
    }
  }
}
