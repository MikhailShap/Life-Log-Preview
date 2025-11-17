package com.lifelog.feature.today

import com.lifelog.core.domain.model.Entry
import com.lifelog.core.domain.repository.EntryRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TodayViewModelTest {

    private lateinit var viewModel: TodayViewModel
    private lateinit var entryRepository: EntryRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        entryRepository = mockk(relaxed = true)
        viewModel = TodayViewModel(entryRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveEntry calls repository with correct data`() = runTest {
        // Given
        val mood = 4
        val energy = 0.8f
        val anxiety = 0.3f
        val notes = "A good day"

        viewModel.onMoodChange(mood)
        viewModel.onEnergyChange(energy)
        viewModel.onAnxietyChange(anxiety)
        viewModel.onNotesChange(notes)

        // When
        viewModel.saveEntry()
        testDispatcher.scheduler.advanceUntilIdle() // Execute the coroutine

        // Then
        coVerify {
            entryRepository.saveEntry(
                match {
                    it.mood == mood &&
                    it.energy == (energy * 5).toInt() &&
                    it.anxiety == (anxiety * 5).toInt() &&
                    it.notes == notes
                }
            )
        }
    }
}
