package com.vipuljha.todo_compose.presentation.todo_edit

import com.vipuljha.todo_compose.domain.model.Todo

sealed interface TodoEditIntent {
    data class Init(val todo: Todo?) : TodoEditIntent
    data class TitleChanged(val title: String) : TodoEditIntent
    data class DescriptionChanged(val description: String) : TodoEditIntent

    data object Save : TodoEditIntent
    data object Delete : TodoEditIntent
}

sealed interface TodoEditPartialState {
    data class DataLoaded(val todo: Todo?) : TodoEditPartialState
    data class TitleUpdated(val value: String) : TodoEditPartialState
    data class DescriptionUpdated(val value: String) : TodoEditPartialState
    data object Saving : TodoEditPartialState
    data object Saved : TodoEditPartialState
    data object Deleting : TodoEditPartialState
    data object Deleted : TodoEditPartialState
    data class Error(val message: String) : TodoEditPartialState
}

data class TodoEditState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val error: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
)

sealed interface TodoEditEffect {
    data class ShowToast(val message: String) : TodoEditEffect
    data object NavigateBack : TodoEditEffect
}