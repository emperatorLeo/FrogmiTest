package com.example.frogmitest.data

import com.example.frogmitest.data.network.StoreService
import javax.inject.Inject

class FrogmiRepository @Inject constructor(private val service: StoreService) {

    suspend fun getStores(page: Int) = service.getStore(page)
}