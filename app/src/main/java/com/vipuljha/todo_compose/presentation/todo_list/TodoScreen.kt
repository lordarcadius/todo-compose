package com.vipuljha.todo_compose.presentation.todo_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        state.error != null -> {
            Box(modifier = modifier.fillMaxSize()) {
                Text(text = state.error!!, modifier = Modifier.align(Alignment.Center))
            }
        }

        else -> {
            Column(modifier = modifier.fillMaxSize()) {
                if (state.todos.isEmpty()) {
                    Box(modifier = modifier.fillMaxSize()) {
                        Text(
                            text = "Create your first Todo!",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    state.todos.forEach {
                        Text(text = it.title)
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Todo Screen"
)
@Composable
fun TodoScreenPreview() {
    TodoScreen()
}