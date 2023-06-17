package com.example.frogmitest.data.network

import com.example.frogmitest.data.entities.FrogmiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FrogmiApiClient {

    @GET("stores?per_page=15")
    suspend fun getAllPosts(@Query("page") page: Int): Response<FrogmiResponse>
}