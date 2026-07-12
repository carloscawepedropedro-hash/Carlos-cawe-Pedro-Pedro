package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import com.example.ui.ZyloViewModel
import com.example.ui.navigation.ZyloNavGraph
import com.example.ui.theme.ZyloAppTheme

class MainActivity : ComponentActivity() {
  private val viewModel: ZyloViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      var darkThemePreference by remember { mutableStateOf<Boolean?>(null) }
      val systemDark = isSystemInDarkTheme()
      val isDark = darkThemePreference ?: systemDark

      ZyloAppTheme(darkTheme = isDark) {
        ZyloNavGraph(
          viewModel = viewModel,
          isDarkTheme = isDark,
          onThemeToggle = { darkThemePreference = it }
        )
      }
    }
  }
}
