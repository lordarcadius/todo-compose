package com.vipuljha.todo_compose.data.repository

import com.vipuljha.todo_compose.data.source.TodoDao
import com.vipuljha.todo_compose.data.source.toDomain
import com.vipuljha.todo_compose.data.source.toEntity
import com.vipuljha.todo_compose.domain.model.Todo
import com.vipuljha.todo_compose.domain.repository.TodoRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepoImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoRepo {

    override suspend fun upsertTodo(todo: Todo): Boolean {
        try {
            todoDao.upsertTodo(todo.toEntity())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override suspend fun deleteTodo(todo: Todo): Boolean {
        try {
            todoDao.deleteTodo(todo.toEntity())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override fun getTodos(): Flow<List<Todo>> {
        try {
            return todoDao
                .getTodos()
                .map { entities ->
                    entities.map { it.toDomain() }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyFlow()
        }
    }
}