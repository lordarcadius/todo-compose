package com.vipuljha.todo_compose.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.vipuljha.todo_compose.domain.model.Todo
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object TodoList: Route

    @Serializable
    data class TodoEdit(val todo: Todo?): Route
}