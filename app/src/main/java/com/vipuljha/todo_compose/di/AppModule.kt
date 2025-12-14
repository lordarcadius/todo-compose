package com.vipuljha.todo_compose.di

import android.content.Context
import androidx.room.Room
import com.vipuljha.todo_compose.data.source.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context, TodoDatabase::class.java,
            "todo_db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoDao(todoDatabase: TodoDatabase) = todoDatabase.todoDao()
}