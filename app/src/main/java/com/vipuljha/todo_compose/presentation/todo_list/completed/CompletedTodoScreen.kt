package com.vipuljha.todo_compose.presentation.todo_list.completed

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vipuljha.todo_compose.presentation.todo_list.components.TodoList
import com.vipuljha.todo_compose.presentation.todo_list.viewmodel.CompletedTodoViewModel

@Composable
fun CompletedTodoScreen(
    modifier: Modifier = Modifier,
    viewModel: CompletedTodoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(CompletedTodoIntent.LoadTodos)

        viewModel.effects.collect { effect ->
            when (effect) {
                is CompletedTodoEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.error != null -> {
                Text(text = state.error!!, modifier = Modifier.align(Alignment.Center))
            }

            else -> {
                if (state.todos.isEmpty()) {
                    Text(
                        text = "Create your first Todo!",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    TodoList(
                        todos = state.todos,
                        onDelete = { viewModel.sendIntent(CompletedTodoIntent.Delete(it)) },
                        onEdit = { },
                        onDone = { todo ->
                            viewModel.sendIntent(CompletedTodoIntent.MarkUncompleted(todo))
                        }
                    )
                }
            }
        }
    }
}