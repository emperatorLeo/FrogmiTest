package com.example.frogmitest.ui

import com.example.frogmitest.core.Store
import kotlin.Boolean

sealed class UiState {

    data class Loading(val isVisible: Boolean) : UiState()

    data class Success(val data: List<Store>) : UiState()

    object ReachLimit: UiState()

    sealed class Error : UiState() {
        object ServerError : Error()
        object BadRequestError : Error()
        object ConnectionError : Error()
    }
}