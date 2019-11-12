package com.godinho.desafioimgur

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.godinho.desafioimgur.feature.home.presentation.HomeViewModel
import com.godinho.desafioimgur.feature.home.repository.HomeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var homeRepository: HomeRepository

    private val dispatcher = Dispatchers.Unconfined

    private val job = SupervisorJob()

    private val homeViewModel by lazy {
        spyk(
            HomeViewModel(homeRepository, dispatcher, job)
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `when request content list return null`() {
        coEvery {
            homeRepository.getContentList()
        } returns null

        homeViewModel.onRequestContentList()
        Assert.assertEquals(Unit, homeViewModel.showErrorDialog().value)
    }

    @Test
    fun `when request content list return empty list`() {
        coEvery {
            homeRepository.getContentList()
        } returns emptyList()

        homeViewModel.onRequestContentList()
        Assert.assertEquals(Unit, homeViewModel.showNoContentDialog().value)
    }

    @Test
    fun `when request content list return list of content`() {
        val list = listOf(URL)
        coEvery {
            homeRepository.getContentList()
        } returns listOf(URL)

        homeViewModel.onRequestContentList()
        Assert.assertEquals(list, homeViewModel.fetchListWithData().value)
    }

    companion object {
        private const val URL = "https://www.google.com"
    }
}