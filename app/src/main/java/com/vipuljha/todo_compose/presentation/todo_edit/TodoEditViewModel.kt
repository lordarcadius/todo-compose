package com.vipuljha.todo_compose.presentation.todo_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipuljha.todo_compose.domain.model.Todo
import com.vipuljha.todo_compose.domain.usecase.AddOrUpdateTodo
import com.vipuljha.todo_compose.domain.usecase.DeleteTodo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TodoEditViewModel @Inject constructor(
    private val addOrUpdateTodo: AddOrUpdateTodo,
    private val deleteTodo: DeleteTodo
) : ViewModel() {

    private val intents = MutableSharedFlow<TodoEditIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(TodoEditState())
    val state: StateFlow<TodoEditState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<TodoEditEffect>()
    val effects = _effects.asSharedFlow()

    fun sendIntent(intent: TodoEditIntent) {
        intents.tryEmit(intent)
    }

    init {
        bindIntents()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun bindIntents() {
        intents
            .flatMapLatest { handleIntent(it) }
            .scan(TodoEditState(), ::reduce)
            .onEach { newState ->
                _state.update { newState }
            }
            .launchIn(viewModelScope)
    }

    fun handleIntent(intent: TodoEditIntent): Flow<TodoEditPartialState> =
        when (intent) {
            is TodoEditIntent.Init -> flowOf(TodoEditPartialState.DataLoaded(intent.todo))
            is TodoEditIntent.TitleChanged -> flowOf(TodoEditPartialState.TitleUpdated(intent.title))
            is TodoEditIntent.DescriptionChanged -> flowOf(
                TodoEditPartialState.DescriptionUpdated(
                    intent.description
                )
            )

            is TodoEditIntent.Save -> saveTodo()
            is TodoEditIntent.Delete -> deleteTodo()
        }

    private fun saveTodo(): Flow<TodoEditPartialState> = flow {
        val current = state.value
        if (current.title.isBlank()) {
            val errorMessage = "Title cannot be empty"
            emit(TodoEditPartialState.Error(errorMessage))
            _effects.emit(TodoEditEffect.ShowToast(errorMessage))
            return@flow
        }

        emit(TodoEditPartialState.Saving)

        val todo = Todo(
            id = current.id,
            title = current.title,
            description = current.description
        )
        if (addOrUpdateTodo(todo)) {
            emit(TodoEditPartialState.Saved)
            _effects.emit(TodoEditEffect.NavigateBack)
            _effects.emit(TodoEditEffect.ShowToast("Todo saved!"))
        } else {
            val errorMessage = "Failed to save todo"
            emit(TodoEditPartialState.Error(errorMessage))
            _effects.emit(TodoEditEffect.ShowToast(errorMessage))
        }
    }

    private fun deleteTodo(): Flow<TodoEditPartialState> = flow {
        val current = state.value

        if (current.id == 0) {
            emit(TodoEditPartialState.Error("Nothing to delete"))
            return@flow
        }

        emit(TodoEditPartialState.Deleting)

        val todo = Todo(
            id = current.id,
            title = current.title,
            description = current.description
        )
        if (deleteTodo(todo)) {
            emit(TodoEditPartialState.Deleted)
            _effects.emit(TodoEditEffect.NavigateBack)
            _effects.emit(TodoEditEffect.ShowToast("Todo deleted!"))
        } else {
            val errorMessage = "Failed to delete todo"
            emit(TodoEditPartialState.Error(errorMessage))
            _effects.emit(TodoEditEffect.ShowToast(errorMessage))
        }
    }

    private fun reduce(
        prev: TodoEditState,
        change: TodoEditPartialState
    ): TodoEditState =
        when (change) {

            is TodoEditPartialState.DataLoaded ->
                change.todo?.let {
                    prev.copy(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        error = null
                    )
                } ?: prev

            is TodoEditPartialState.TitleUpdated ->
                prev.copy(title = change.value, error = null)

            is TodoEditPartialState.DescriptionUpdated ->
                prev.copy(description = change.value)

            TodoEditPartialState.Saving ->
                prev.copy(isSaving = true, error = null)

            TodoEditPartialState.Saved ->
                prev.copy(isSaving = false)

            TodoEditPartialState.Deleting ->
                prev.copy(isDeleting = true)

            TodoEditPartialState.Deleted ->
                prev.copy(isDeleting = false)

            is TodoEditPartialState.Error ->
                prev.copy(
                    isSaving = false,
                    isDeleting = false,
                    error = change.message
                )
        }
}