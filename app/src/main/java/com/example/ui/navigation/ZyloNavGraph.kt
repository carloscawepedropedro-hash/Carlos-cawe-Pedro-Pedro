package com.example.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.ZyloViewModel
import com.example.ui.auth.AuthScreen
import com.example.ui.chat.ChatListScreen
import com.example.ui.chat.ChatRoomScreen
import com.example.ui.feed.FeedScreen
import com.example.ui.groups.GroupsScreen
import com.example.ui.notifications.NotificationsScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.search.SearchScreen
import com.example.ui.settings.SettingsScreen

@Composable
fun ZyloNavGraph(
  viewModel: ZyloViewModel,
  isDarkTheme: Boolean,
  onThemeToggle: (Boolean) -> Unit
) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route ?: "auth"

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      ZyloBottomBar(
        currentRoute = currentRoute,
        onNavigate = { route ->
          if (route != currentRoute) {
            navController.navigate(route) {
              popUpTo(navController.graph.startDestinationId) { saveState = true }
              launchSingleTop = true
              restoreState = true
            }
          }
        }
      )
    }
  ) { innerPadding ->
    NavHost(
      navController = navController,
      startDestination = "auth",
      modifier = Modifier.padding(innerPadding)
    ) {
      composable("auth") {
        AuthScreen(
          viewModel = viewModel,
          onLoginSuccess = {
            navController.navigate(Screen.Feed.route) {
              popUpTo("auth") { inclusive = true }
            }
          }
        )
      }

      composable(Screen.Feed.route) {
        FeedScreen(
          viewModel = viewModel,
          onNavigateToSearch = { navController.navigate("search") },
          onNavigateToNotifications = { navController.navigate("notifications") }
        )
      }

      composable(Screen.ChatList.route) {
        ChatListScreen(
          viewModel = viewModel,
          onNavigateToChatRoom = { chatId, participantName ->
            navController.navigate("chat_room/$chatId/$participantName")
          }
        )
      }

      composable(
        route = "chat_room/{chatId}/{participantName}",
        arguments = listOf(
          navArgument("chatId") { type = NavType.IntType },
          navArgument("participantName") { type = NavType.StringType }
        )
      ) { backStackEntry ->
        val chatId = backStackEntry.arguments?.getInt("chatId") ?: 1
        val participantName = backStackEntry.arguments?.getString("participantName") ?: "Chat"
        ChatRoomScreen(
          chatId = chatId,
          participantName = participantName,
          viewModel = viewModel,
          onBack = { navController.popBackStack() }
        )
      }

      composable(Screen.Groups.route) {
        GroupsScreen(viewModel = viewModel)
      }

      composable(Screen.Profile.route) {
        ProfileScreen(
          viewModel = viewModel,
          onNavigateToSettings = { navController.navigate("settings") }
        )
      }

      composable("search") {
        SearchScreen(
          viewModel = viewModel,
          onBack = { navController.popBackStack() }
        )
      }

      composable("notifications") {
        NotificationsScreen(
          viewModel = viewModel,
          onBack = { navController.popBackStack() }
        )
      }

      composable("settings") {
        SettingsScreen(
          viewModel = viewModel,
          isDarkTheme = isDarkTheme,
          onThemeToggle = onThemeToggle,
          onBack = { navController.popBackStack() },
          onLogout = {
            navController.navigate("auth") {
              popUpTo(0) { inclusive = true }
            }
          }
        )
      }
    }
  }
}
