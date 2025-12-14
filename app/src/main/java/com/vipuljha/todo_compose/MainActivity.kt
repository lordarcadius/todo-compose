package com.vipuljha.todo_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberNavBackStack
import com.vipuljha.todo_compose.core.util.back
import com.vipuljha.todo_compose.core.util.open
import com.vipuljha.todo_compose.presentation.navigation.AppNavigation
import com.vipuljha.todo_compose.presentation.navigation.Route
import com.vipuljha.todo_compose.presentation.theme.TodoComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoComposeTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        val backStack = rememberNavBackStack(Route.TodoList)
        val isMainScreen = backStack.last() is Route.TodoList
        Scaffold(
            topBar = {
                TopBar(
                    onEditClick = { backStack.open(Route.TodoEdit(null)) },
                    onCompletedClick = { backStack.open(Route.CompletedTodoList) },
                    isMainScreen = isMainScreen,
                    onBack = { backStack.back() }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AppNavigation(
                modifier = Modifier.padding(innerPadding),
                backStack = backStack
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onEditClick: () -> Unit,
    onCompletedClick: () -> Unit,
    isMainScreen: Boolean,
    onBack: () -> Unit
) {
    TopAppBar(
        title = { Text(if (isMainScreen) "Todo Compose" else "Add/Edit Todo") },
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        navigationIcon = {
            if (!isMainScreen) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (isMainScreen) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
                IconButton(onClick = onCompletedClick) {
                    Icon(Icons.Default.Done, contentDescription = "Completed")
                }
            }
        }
    )
}