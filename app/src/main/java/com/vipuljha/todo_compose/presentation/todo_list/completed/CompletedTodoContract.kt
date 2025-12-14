package com.vipuljha.todo_compose.presentation.todo_list.completed


import com.vipuljha.todo_compose.domain.model.Todo

sealed interface CompletedTodoIntent {
    data object LoadTodos : CompletedTodoIntent
    data class Delete(val todo: Todo) : CompletedTodoIntent
    data class MarkUncompleted(val todo: Todo) : CompletedTodoIntent
}

sealed interface CompletedTodoPartialState {
    data object Loading : CompletedTodoPartialState
    data class TodosLoaded(val todos: List<Todo>) : CompletedTodoPartialState
    data object TodoDeleting : CompletedTodoPartialState
    data object TodoDeleted : CompletedTodoPartialState
    data object TodoUncompleted : CompletedTodoPartialState
    data class Error(val message: String) : CompletedTodoPartialState
}

data class CompletedTodoState(
    val isLoading: Boolean = false,
    val todos: List<Todo> = emptyList(),
    val error: String? = null
)

sealed interface CompletedTodoEffect {
    data class ShowToast(val message: String) : CompletedTodoEffect
}