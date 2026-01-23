package com.whoismacy.android.sodaadminapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface SodaApiService {
    @GET("api/soda-info")
    fun getSodaDetails(
        @Query("location") location: String,
        @Query("brand") brand: String,
    ): Call<SodaResponse>

    @GET("api/sales-summary")
    fun getSalesSummary(): Call<SummaryResponse>

    companion object {
        private const val BASE_URL = "http://localhost.com/" // Replace with your actual base URL

        fun create(): SodaApiService {
            val retrofit =
                Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(SodaApiService::class.java)
        }
    }
}
