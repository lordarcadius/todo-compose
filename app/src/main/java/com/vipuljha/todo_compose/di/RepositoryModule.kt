package com.vipuljha.todo_compose.di

import com.vipuljha.todo_compose.data.repository.TodoRepoImpl
import com.vipuljha.todo_compose.domain.repository.TodoRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTodoRepository(
        todoRepoImpl: TodoRepoImpl
    ): TodoRepo

}