package com.vipuljha.todo_compose.domain.usecase

import kotlinx.coroutines.flow.Flow

interface BaseFlowUseCase<in Params, out T> {
    operator fun invoke(params: Params): Flow<T>
}

interface BaseSuspendUseCase<in Params, out T> {
    suspend operator fun invoke(params: Params): T
}

object NoParams