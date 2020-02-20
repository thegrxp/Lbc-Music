package com.ainrom.lbcmusic.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.ainrom.lbcmusic.data.remote.NetworkBoundResource
import com.ainrom.lbcmusic.utils.CoroutinesTestRule
import com.ainrom.lbcmusic.utils.Resource
import com.ainrom.lbcmusic.utils.Status
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@FlowPreview
class NetworkBoundResourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var networkBoundResource: Flow<Resource<Foo>>
    private var dbData = MutableLiveData<Foo>()
    private lateinit var handleFetchResult: (Foo) -> Unit
    private lateinit var handleShouldFetch: (Foo?) -> Boolean
    private lateinit var handleFetch: () -> Any
    private val fetchedOnce = AtomicBoolean(false)

    @ExperimentalCoroutinesApi
    @Before
    fun init() {
        networkBoundResource = object : NetworkBoundResource<Foo, Foo>() {
            override fun query(): Flow<Foo> = dbData.asFlow()

            override suspend fun fetch(): Foo = handleFetch() as Foo

            override suspend fun saveFetchResult(data: Foo) = handleFetchResult(data)

            override suspend fun shouldFetch(data: Foo?): Boolean {
                return handleShouldFetch(data) && fetchedOnce.compareAndSet(false, true)
            }
        }.asFlow()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun basicFromNetwork() = runBlocking {
        val saved = AtomicReference<Foo>()
        dbData.postValue(null)
        handleShouldFetch = { it == null }
        handleFetchResult = { foo ->
            saved.set(foo)
            dbData.setValue(foo)
        }
        val networkResult = Foo(2)
        handleFetch = { networkResult }

        var emitCount = 1
        networkBoundResource.take(3).collect {
            when (emitCount) {
                1 -> {
                    assertThat(it.status, `is`(Status.LOADING))
                    assertNull(it.data)
                }
                2 -> {
                    assertThat(it.status, `is`(Status.LOADING))
                    assertNull(it.data)
                }
                3 -> {
                    assertThat(it.status, `is`(Status.SUCCESS))
                    assertThat(saved.get(), `is`(it.data))
                }
            }
            emitCount++
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun failureFromNetwork() = runBlocking {
        val saved = AtomicBoolean(false)
        dbData.postValue(null)
        handleShouldFetch = { it == null }
        handleFetchResult = {
            saved.set(true)
        }
        handleFetch = { false }

        var emitCount = 1
        networkBoundResource.take(3).collect {
            when (emitCount) {
                1 -> {
                    assertThat(it.status, `is`(Status.LOADING))
                    assertNull(it.data)
                }
                2 -> {
                    assertThat(it.status, `is`(Status.LOADING))
                    assertNull(it.data)
                }
                3 -> {
                    assertThat(it.status, `is`(Status.ERROR))
                    assertThat(saved.get(), `is`(false))
                }
            }
            emitCount++
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun dbSuccessWithoutNetwork() = runBlocking {
        val saved = AtomicBoolean(false)
        dbData.postValue(Foo(3))
        handleShouldFetch = { it == null }
        handleFetchResult = {
            saved.set(true)
        }
        handleFetch = { false }

        var emitCount = 1
        networkBoundResource.take(2).collect {
            when (emitCount) {
                1 -> {
                    assertThat(it.status, `is`(Status.LOADING))
                    assertNull(it.data)
                }
                2 -> {
                    assertThat(it.status, `is`(Status.SUCCESS))
                    assertThat(dbData.value, `is`(it.data))
                }
            }
            emitCount++
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun dbSuccessWithFetchSuccess() = runBlocking {
        val saved = AtomicReference<Foo>()
        dbData.postValue(Foo(3))
        handleShouldFetch = { foo -> foo === dbData.value }
        handleFetchResult = { foo ->
            saved.set(foo)
            dbData.setValue(foo)
        }
        val networkResult = Foo(2)
        handleFetch = { networkResult }

        var emitCount = 1
        networkBoundResource.take(3).collect {
            when (emitCount) {
                1 -> {
                    assertThat(it.status, `is`(Status.LOADING))
                    assertNull(it.data)
                }
                2 -> {
                    assertThat(it.status, `is`(Status.LOADING))
                    assertThat(dbData.value, `is`(it.data))
                }
                3 -> {
                    assertThat(it.status, `is`(Status.SUCCESS))
                    assertThat(saved.get(), `is`(it.data))
                }
            }
            emitCount++
        }
    }

    data class Foo(var value: Int)
}