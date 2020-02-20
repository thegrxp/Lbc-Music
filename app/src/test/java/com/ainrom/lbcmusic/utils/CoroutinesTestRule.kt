package com.ainrom.lbcmusic.utils

import kotlinx.coroutines.CoroutineDispatcher
import org.junit.rules.TestWatcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutinesTestRule(private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()) : TestWatcher() {

    override fun starting(description: Description?) {
        Dispatchers.setMain(dispatcher)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}