package com.vipuljha.todo_compose.presentation.todo_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipuljha.todo_compose.domain.model.Todo
import com.vipuljha.todo_compose.domain.usecase.AddOrUpdateTodo
import com.vipuljha.todo_compose.domain.usecase.DeleteTodo
import com.vipuljha.todo_compose.domain.usecase.GetCompletedTodos
import com.vipuljha.todo_compose.domain.usecase.NoParams
import com.vipuljha.todo_compose.presentation.todo_list.completed.CompletedTodoEffect
import com.vipuljha.todo_compose.presentation.todo_list.completed.CompletedTodoIntent
import com.vipuljha.todo_compose.presentation.todo_list.completed.CompletedTodoPartialState
import com.vipuljha.todo_compose.presentation.todo_list.completed.CompletedTodoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CompletedTodoViewModel @Inject constructor(
    private val updateTodo: AddOrUpdateTodo,
    private val deleteTodo: DeleteTodo,
    private val getCompletedTodos: GetCompletedTodos
) : ViewModel() {
    private val intents = MutableSharedFlow<CompletedTodoIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(CompletedTodoState())
    val state: StateFlow<CompletedTodoState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<CompletedTodoEffect>()
    val effects = _effects.asSharedFlow()

    init {
        bindIntents()
    }

    fun sendIntent(intent: CompletedTodoIntent) {
        intents.tryEmit(intent)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun bindIntents() {
        intents
            .flatMapMerge { handleIntent(it) }
            .scan(CompletedTodoState(), ::reduce)
            .onEach { newState ->
                _state.update { newState }
            }
            .launchIn(viewModelScope)
    }

    private fun handleIntent(intent: CompletedTodoIntent): Flow<CompletedTodoPartialState> =
        when (intent) {
            CompletedTodoIntent.LoadTodos -> loadCompletedTodos()
            is CompletedTodoIntent.Delete -> delete(intent.todo)
            is CompletedTodoIntent.MarkUncompleted -> markUncompleted(intent.todo)
        }

    private fun loadCompletedTodos(): Flow<CompletedTodoPartialState> = flow {
        emit(CompletedTodoPartialState.Loading)
        emitAll(
            getCompletedTodos(NoParams)
                .map { todos ->
                    CompletedTodoPartialState.TodosLoaded(todos)
                }
        )
    }.catch {
        emit(CompletedTodoPartialState.Error("Failed to load todos"))
    }

    private fun delete(todo: Todo): Flow<CompletedTodoPartialState> = flow {

        if (todo.id == 0) {
            emit(CompletedTodoPartialState.Error("Nothing to delete"))
            return@flow
        }

        emit(CompletedTodoPartialState.TodoDeleting)

        val todo = Todo(
            id = todo.id,
            title = todo.title,
            description = todo.description
        )
        if (deleteTodo(todo)) {
            emit(CompletedTodoPartialState.TodoDeleted)
            _effects.emit(CompletedTodoEffect.ShowToast("Todo deleted!"))
        } else {
            val errorMessage = "Failed to delete todo"
            emit(CompletedTodoPartialState.Error(errorMessage))
            _effects.emit(CompletedTodoEffect.ShowToast(errorMessage))
        }
    }

    private fun markUncompleted(todo: Todo): Flow<CompletedTodoPartialState> = flow {
        if (todo.id == 0) {
            emit(CompletedTodoPartialState.Error("Nothing to mark uncompleted"))
            return@flow
        }

        val todo = Todo(
            id = todo.id,
            title = todo.title,
            description = todo.description,
            isCompleted = false
        )
        if (updateTodo(todo)) {
            emit(CompletedTodoPartialState.TodoDeleted)
            _effects.emit(CompletedTodoEffect.ShowToast("Marked the task uncompleted!"))
        } else {
            val errorMessage = "Failed to mark task uncompleted"
            emit(CompletedTodoPartialState.Error(errorMessage))
            _effects.emit(CompletedTodoEffect.ShowToast(errorMessage))
        }
    }

    private fun reduce(
        previous: CompletedTodoState,
        change: CompletedTodoPartialState
    ): CompletedTodoState =
        when (change) {

            CompletedTodoPartialState.TodoDeleting,
            CompletedTodoPartialState.Loading ->
                previous.copy(isLoading = true, error = null)

            is CompletedTodoPartialState.TodosLoaded ->
                previous.copy(
                    isLoading = false,
                    todos = change.todos,
                    error = null
                )

            is CompletedTodoPartialState.TodoUncompleted,
            CompletedTodoPartialState.TodoDeleted ->
                previous.copy(isLoading = false)

            is CompletedTodoPartialState.Error ->
                previous.copy(
                    isLoading = false,
                    error = change.message
                )
        }
}