package com.vipuljha.todo_compose.domain.repository

import com.vipuljha.todo_compose.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepo {
    suspend fun upsertTodo(todo: Todo): Boolean
    suspend fun deleteTodo(todo: Todo): Boolean
    fun getAllTodos(): Flow<List<Todo>>
    fun getCompletedTodos(): Flow<List<Todo>>
    fun getUncompletedTodos(): Flow<List<Todo>>
}