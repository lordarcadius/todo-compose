package com.vipuljha.todo_compose.presentation.todo_edit

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vipuljha.todo_compose.domain.model.Todo

@Composable
fun TodoEditScreen(
    todo: Todo?,
    onBack: () -> Unit,
    viewModel: TodoEditViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(TodoEditIntent.Init(todo))

        viewModel.effects.collect { effect ->
            when (effect) {
                TodoEditEffect.NavigateBack -> onBack()
                is TodoEditEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        TextField(
            value = state.title,
            onValueChange = {
                viewModel.sendIntent(
                    TodoEditIntent.TitleChanged(it)
                )
            },
            label = { Text("Title") }
        )

        TextField(
            value = state.description,
            onValueChange = {
                viewModel.sendIntent(
                    TodoEditIntent.DescriptionChanged(it)
                )
            },
            label = { Text("Description") }
        )

        Button(
            onClick = {
                viewModel.sendIntent(TodoEditIntent.Save)
            },
            enabled = !state.isSaving
        ) {
            Text("Save")
        }
    }
}
