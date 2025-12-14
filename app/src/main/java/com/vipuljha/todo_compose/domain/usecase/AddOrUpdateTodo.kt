package com.vipuljha.todo_compose.domain.usecase

import com.vipuljha.todo_compose.domain.model.Todo
import com.vipuljha.todo_compose.domain.repository.TodoRepo
import javax.inject.Inject

class AddOrUpdateTodo @Inject constructor(
    private val todoRepo: TodoRepo
) : BaseSuspendUseCase<Todo, Boolean> {
    override suspend fun invoke(params: Todo): Boolean {
        return todoRepo.upsertTodo(params)
    }
}