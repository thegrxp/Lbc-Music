package com.ainrom.lbcmusic.data.remote

import com.ainrom.lbcmusic.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<T, Q> {

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun asFlow(): Flow<Resource<T>> = flow {
        val flow = query()
            .onStart { emit(Resource.loading<T>(null)) }
            .flatMapConcat { data ->
                if (shouldFetch(data)) {
                    emit(Resource.loading(data))
                    try {
                        saveFetchResult(fetch())
                        query().map { Resource.success(it) }
                    } catch (throwable: Throwable) {
                        onFetchFailed(throwable)
                        query().map { Resource.error("Oh no! Something went wrong. ${throwable.message}", it) }
                    }
                } else {
                    query().map { Resource.success(it) }
                }
            }
        emitAll(flow)
    }

    abstract fun query(): Flow<T>
    abstract suspend fun fetch(): Q
    abstract suspend fun saveFetchResult(data: Q)
    open fun onFetchFailed(throwable: Throwable) = Unit
    abstract suspend fun shouldFetch(data: T?): Boolean
}