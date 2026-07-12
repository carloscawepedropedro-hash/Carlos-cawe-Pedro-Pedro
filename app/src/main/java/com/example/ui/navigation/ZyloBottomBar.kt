package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
  object Feed : Screen("feed", "Início", Icons.Default.Home)
  object ChatList : Screen("chat_list", "Chat", Icons.Default.Chat)
  object Groups : Screen("groups", "Grupos", Icons.Default.Group)
  object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}

@Composable
fun ZyloBottomBar(currentRoute: String, onNavigate: (String) -> Unit) {
  val items = listOf(Screen.Feed, Screen.ChatList, Screen.Groups, Screen.Profile)

  if (items.any { it.route == currentRoute }) {
    NavigationBar(
      containerColor = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp
    ) {
      items.forEach { screen ->
        NavigationBarItem(
          icon = { Icon(screen.icon, contentDescription = screen.title) },
          label = { Text(screen.title) },
          selected = currentRoute == screen.route,
          onClick = { onNavigate(screen.route) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
          )
        )
      }
    }
  }
}
