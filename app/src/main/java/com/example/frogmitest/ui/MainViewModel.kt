package com.example.frogmitest.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frogmitest.core.Store
import com.example.frogmitest.core.util.getNextPage
import com.example.frogmitest.core.util.mapToStore
import com.example.frogmitest.core.util.retry
import com.example.frogmitest.data.entities.FrogmiResponse
import com.example.frogmitest.usecase.GetStoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import retrofit2.Response

@HiltViewModel
class MainViewModel @Inject constructor(private val stores: GetStoresUseCase) : ViewModel() {
    var composeUiState by mutableStateOf<UiState>(UiState.Loading(true))
        private set
    private var page: Int? = 1

    private val _storeList = MutableLiveData<ArrayList<Store>>(arrayListOf())
    val storeList: LiveData<ArrayList<Store>> = _storeList

    fun getStores() {
        if (page != null) {
            composeUiState = UiState.Loading(true)
            viewModelScope.launch {
                val response = stores(page!!)
                if (response.isSuccessful) {
                    successResponse(response)
                } else {
                    errorResponse(response.code())
                }
            }
        } else {
            composeUiState = UiState.ReachLimit
        }
    }

    private fun successResponse(response: Response<FrogmiResponse>) {
        val data = response.body()?.data!!
        val next = response.body()?.links?.next
        if (next != null) {
            page = next.getNextPage()
            _storeList.value!!.addAll(mapToStore(data))
        } else page = null

        composeUiState = UiState.Loading(false)
    }

    private fun errorResponse(code: Int) {
        when (code) {
            in 400..499 -> {
                composeUiState = UiState.Error.BadRequestError
            }

            in 500..Int.MAX_VALUE -> {
                composeUiState = UiState.Loading(true)
                viewModelScope.launch {
                    try {
                        retry(1) {
                            val response2 = stores(page!!)
                            successResponse(response2)
                        }
                    } catch (e: Exception) {
                        composeUiState = UiState.Loading(false)
                        composeUiState = UiState.Error.ServerError
                    }
                }
            }
            else -> {
                composeUiState = UiState.Error.ConnectionError
            }
        }
    }

}