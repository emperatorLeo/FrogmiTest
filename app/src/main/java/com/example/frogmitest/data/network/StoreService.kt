package com.example.frogmitest.data.network

import com.example.frogmitest.data.entities.FrogmiResponse
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class StoreService @Inject constructor(private val api: FrogmiApiClient) {

    suspend fun getStore(page: Int): Response<FrogmiResponse> {
        return withContext(Dispatchers.IO) {
            api.getAllPosts(page)
        }
    }
}