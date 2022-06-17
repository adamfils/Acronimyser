package com.adamfils.acronimyser.viewmodel

import android.os.Looper
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WordViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        mockkConstructor(Log::class)
        mockkConstructor(Looper::class)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test query and result values`() = runBlocking {
        val viewModel = WordViewModel(mockk(relaxed = true))
        viewModel.searchWord(mockk(relaxed = true), "NASA")
        viewModel.getQuery().observeForever {
            assertEquals("NASA", it)
            assert(viewModel.getWordList().value != null)
        }
    }

}