package com.example.frogmitest.di

import com.example.frogmitest.data.network.FrogmiApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val authorization = "INSERT HERE AUTHORIZATION TOKEN"
    private const val XCompanyUuid = "INSERT HERE X-COMPANY-UuID"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .connectTimeout(500,TimeUnit.MILLISECONDS)
            .addInterceptor(logging)
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", authorization)
                    .addHeader("X-Company-Uuid", XCompanyUuid)
                    .build()
                chain.proceed(request)
            }).build()


        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.frogmi.com/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideFrogmiApiClient(retrofit: Retrofit): FrogmiApiClient {
        return retrofit.create(FrogmiApiClient::class.java)
    }
}