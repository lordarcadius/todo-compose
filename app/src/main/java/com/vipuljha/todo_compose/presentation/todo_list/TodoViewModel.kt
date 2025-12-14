package com.vipuljha.todo_compose.presentation.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipuljha.todo_compose.domain.model.Todo
import com.vipuljha.todo_compose.domain.usecase.DeleteTodo
import com.vipuljha.todo_compose.domain.usecase.GetAllTodos
import com.vipuljha.todo_compose.domain.usecase.NoParams
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val deleteTodo: DeleteTodo,
    private val getAllTodos: GetAllTodos
) : ViewModel() {

    private val intents = MutableSharedFlow<TodoIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(TodoState())
    val state: StateFlow<TodoState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<TodoEffect>()
    val effects = _effects.asSharedFlow()

    init {
        bindIntents()
    }

    fun sendIntent(intent: TodoIntent) {
        intents.tryEmit(intent)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun bindIntents() {
        intents
            .flatMapLatest { handleIntent(it) }
            .scan(TodoState(), ::reduce)
            .onEach { newState ->
                _state.update { newState }
            }
            .launchIn(viewModelScope)
    }

    private fun handleIntent(intent: TodoIntent): Flow<TodoPartialState> =
        when (intent) {
            TodoIntent.LoadTodos -> loadTodos()
            is TodoIntent.Delete -> delete(intent.todo)
        }

    private fun loadTodos(): Flow<TodoPartialState> = flow {
        emit(TodoPartialState.Loading)
        emitAll(
            getAllTodos(NoParams)
                .map { todos ->
                    TodoPartialState.TodosLoaded(todos)
                }
        )
    }.catch {
        emit(TodoPartialState.Error("Failed to load todos"))
    }

    private fun delete(todo: Todo): Flow<TodoPartialState> = flow {

        if (todo.id == 0) {
            emit(TodoPartialState.Error("Nothing to delete"))
            return@flow
        }

        emit(TodoPartialState.TodoDeleting)

        val todo = Todo(
            id = todo.id,
            title = todo.title,
            description = todo.description
        )
        if (deleteTodo(todo)) {
            emit(TodoPartialState.TodoDeleted)
            _effects.emit(TodoEffect.ShowToast("Todo deleted!"))
        } else {
            val errorMessage = "Failed to delete todo"
            emit(TodoPartialState.Error(errorMessage))
            _effects.emit(TodoEffect.ShowToast(errorMessage))
        }
    }

    private fun reduce(
        previous: TodoState,
        change: TodoPartialState
    ): TodoState =
        when (change) {

            TodoPartialState.TodoDeleting,
            TodoPartialState.Loading ->
                previous.copy(isLoading = true, error = null)

            is TodoPartialState.TodosLoaded ->
                previous.copy(
                    isLoading = false,
                    todos = change.todos,
                    error = null
                )

            TodoPartialState.TodoDeleted ->
                previous.copy(isLoading = false)

            is TodoPartialState.Error ->
                previous.copy(
                    isLoading = false,
                    error = change.message
                )
        }
}