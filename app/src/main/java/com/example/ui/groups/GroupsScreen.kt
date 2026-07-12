package com.example.ui.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun GroupsScreen(viewModel: ZyloViewModel) {
  val communities by viewModel.allCommunities.collectAsState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Comunidades & Grupos", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      items(communities) { community ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
              model = community.imageUrl,
              contentDescription = null,
              modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
              contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = community.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = community.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(12.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "${community.memberCount} membros",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.primary,
                  fontWeight = FontWeight.Bold
                )

                Button(
                  onClick = { viewModel.toggleCommunityJoin(community) },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (community.isJoined) MaterialTheme.colorScheme.surfaceVariant else ZyloPrimary
                  ),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text(
                    text = if (community.isJoined) "Participando ✓" else "Participar",
                    color = if (community.isJoined) MaterialTheme.colorScheme.onSurfaceVariant else androidx.compose.ui.graphics.Color.White
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
