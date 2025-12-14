package com.vipuljha.todo_compose.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.vipuljha.todo_compose.core.util.back
import com.vipuljha.todo_compose.core.util.open
import com.vipuljha.todo_compose.presentation.todo_edit.TodoEditScreen
import com.vipuljha.todo_compose.presentation.todo_list.completed.CompletedTodoScreen
import com.vipuljha.todo_compose.presentation.todo_list.uncompleted.TodoScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey>
) {
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.back() },
        entryProvider = entryProvider {
            entry<Route.TodoList> {
                TodoScreen(onEditClick = { todo ->
                    backStack.open(Route.TodoEdit(todo))
                })
            }
            entry<Route.CompletedTodoList> {
                CompletedTodoScreen()
            }
            entry<Route.TodoEdit> {
                TodoEditScreen(
                    todo = it.todo,
                    onBack = { backStack.back() }
                )
            }
        },
        predictivePopTransitionSpec = {
            (slideInHorizontally { it } + fadeIn()).togetherWith(
                slideOutHorizontally { -it } + fadeOut()
            )
        },
    )
}