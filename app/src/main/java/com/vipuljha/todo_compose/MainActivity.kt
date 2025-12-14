package com.vipuljha.todo_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.rememberNavBackStack
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
        val showFab = backStack.last() is Route.TodoList
        Scaffold(
            floatingActionButton = {
                if (showFab) {
                    FloatingActionButton(
                        modifier = Modifier.padding(end = 6.dp, bottom = 32.dp),
                        onClick = {
                            backStack.open(Route.TodoEdit(null))
                        }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
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