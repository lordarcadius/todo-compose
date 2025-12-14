package com.vipuljha.todo_compose.domain.usecase

import com.vipuljha.todo_compose.domain.model.Todo
import com.vipuljha.todo_compose.domain.repository.TodoRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUncompletedTodos @Inject constructor(
    private val todoRepo: TodoRepo
) : BaseFlowUseCase<NoParams, List<Todo>> {
    override fun invoke(params: NoParams): Flow<List<Todo>> {
        return todoRepo.getUncompletedTodos()
    }
}