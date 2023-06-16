package com.example.frogmitest.core.util

import android.util.Log
import com.example.frogmitest.core.Store
import com.example.frogmitest.data.entities.Data
import com.example.frogmitest.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

fun mapToStore(data: List<Data>): List<Store> {
    return data.map {
            dataItem -> Store(name = dataItem.attributes.name, code = dataItem.attributes.code, dataItem.attributes.fullAddress)
    }
}

fun String.getNextPage(): Int {
    return this.substringAfter("&page=").toInt()
}


 suspend fun <T> retry(
    numberOfRetries: Int,
    initDelayMillis: Long = 100,
    maxDelayMillis: Long = 1000,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initDelayMillis
    repeat(numberOfRetries) {
        try {
            return block()
        } catch (exception: Exception) {
            Log.e("Error retrying",exception.message!!)
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
    }
    return block()
}