package com.example.minn.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * ViewModels launch into `viewModelScope`, which is hard-wired to
 * `Dispatchers.Main.immediate`. Unit tests have no main looper, so the
 * dispatcher has to be swapped before the ViewModel is constructed and
 * restored afterwards.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}