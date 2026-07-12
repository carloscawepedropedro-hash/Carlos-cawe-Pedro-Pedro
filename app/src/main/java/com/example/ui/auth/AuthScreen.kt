package com.example.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ZyloViewModel
import com.example.ui.theme.ZyloPrimary
import com.example.ui.theme.ZyloSecondary

@Composable
fun AuthScreen(viewModel: ZyloViewModel, onLoginSuccess: () -> Unit) {
  var isLogin by remember { mutableStateOf(true) }
  var email by remember { mutableStateOf("alex.silva@zylo.com") }
  var password by remember { mutableStateOf("••••••••") }
  var name by remember { mutableStateOf("Alex Silva") }
  var phone by remember { mutableStateOf("+55 (11) 99888-7766") }
  var selectedTab by remember { mutableStateOf(0) } // 0: Email, 1: Phone, 2: Google

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surface
          )
        )
      )
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 440.dp),
      shape = RoundedCornerShape(28.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // App Logo / Title
        Surface(
          modifier = Modifier.size(64.dp),
          shape = RoundedCornerShape(20.dp),
          color = ZyloPrimary
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(
              text = "Z",
              color = androidx.compose.ui.graphics.Color.White,
              fontSize = 32.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Zylo Hub",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Text(
          text = if (isLogin) "Conecte-se com sua rede" else "Crie sua conta agora",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Login Mode Selector Tabs
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(4.dp),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          TabButton(text = "E-mail", selected = selectedTab == 0) { selectedTab = 0 }
          TabButton(text = "Telefone", selected = selectedTab == 1) { selectedTab = 1 }
          TabButton(text = "Google", selected = selectedTab == 2) { selectedTab = 2 }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!isLogin && selectedTab == 0) {
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome Completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
        }

        if (selectedTab == 0) {
          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )
        } else if (selectedTab == 1) {
          OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Número de Telefone") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )
        } else {
          Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Entre rapidamente com sua conta Google",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
              onClick = {
                viewModel.login("Alex Silva", "@alexsilva")
                onLoginSuccess()
              },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
              Text("Continuar com Google", fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedTab != 2) {
          Button(
            onClick = {
              viewModel.login(name.ifEmpty { "Alex Silva" }, "@alexsilva")
              onLoginSuccess()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ZyloPrimary)
          ) {
            Text(
              text = if (isLogin) "Entrar" else "Cadastrar",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isLogin = !isLogin }) {
          Text(
            text = if (isLogin) "Não tem uma conta? Cadastre-se" else "Já tem uma conta? Entre",
            color = ZyloPrimary
          )
        }
      }
    }
  }
}

@Composable
fun RowScope.TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
  val backgroundColor = if (selected) MaterialTheme.colorScheme.surface else androidx.compose.ui.graphics.Color.Transparent
  val contentColor = if (selected) ZyloPrimary else MaterialTheme.colorScheme.onSurfaceVariant

  Surface(
    modifier = Modifier
      .weight(1f)
      .height(36.dp),
    shape = RoundedCornerShape(10.dp),
    color = backgroundColor,
    onClick = onClick
  ) {
    Box(contentAlignment = Alignment.Center) {
      Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        color = contentColor
      )
    }
  }
}
