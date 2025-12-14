package com.vipuljha.todo_compose.presentation.todo

import com.vipuljha.todo_compose.domain.model.Todo

sealed interface TodoIntent {
    data object LoadTodos : TodoIntent
    data class AddOrUpdate(val todo: Todo) : TodoIntent
    data class Delete(val todo: Todo) : TodoIntent
}

sealed interface TodoPartialState {
    data object Loading : TodoPartialState
    data class TodosLoaded(val todos: List<Todo>) : TodoPartialState
    data object TodoSaved : TodoPartialState
    data object TodoDeleted : TodoPartialState
    data class Error(val message: String) : TodoPartialState
}

data class TodoState(
    val isLoading: Boolean = false,
    val todos: List<Todo> = emptyList(),
    val error: String? = null
)