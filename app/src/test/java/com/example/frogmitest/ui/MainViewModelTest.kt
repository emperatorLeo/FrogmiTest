package com.example.frogmitest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.frogmitest.MockFrogmiResponse
import com.example.frogmitest.core.Store
import com.example.frogmitest.usecase.GetStoresUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response


@OptIn(ExperimentalCoroutinesApi::class)
internal class MainViewModelTest {

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var getStoresUseCase: GetStoresUseCase

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        mainViewModel = MainViewModel(getStoresUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewModel calls getStores return successful, UiState should return success`() =
        runTest {
            // GIVEN
            val response = MockFrogmiResponse.provideFrogmiResponse()

            coEvery { getStoresUseCase.invoke(1) } returns Response.success(response)

            // WHEN
            mainViewModel.getStores()

            // THEN
            val mutableStoreList = mutableListOf(
                Store("name1", "code1", "fullAddress1"),
                Store("name2", "code2", "fullAddress2"),
                Store("name3", "code3", "fullAddress3")
            )

            assert(mainViewModel.composeUiState == UiState.Success(mutableStoreList))
        }

    @Test
    fun `when viewModel calls getStores return Error 400 family, UiState should return BadRequestError`() =
        runTest {
            coEvery { getStoresUseCase.invoke(1) } returns Response.error(400, EMPTY_RESPONSE)

            // WHEN
            mainViewModel.getStores()

            assert(mainViewModel.composeUiState == UiState.Error.BadRequestError)
        }

    @Test
    fun `when viewModel calls getStores return Error 500 family, UiState should return ServerError`() =
        runTest {
            coEvery { getStoresUseCase.invoke(1) } returns Response.error(500, EMPTY_RESPONSE)

            // WHEN
            mainViewModel.getStores()

            assert(mainViewModel.composeUiState == UiState.Error.ServerError)
        }
}