package com.whoismacy.android.sodaadminapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface SodaApiService {
    @GET("soda-info")
    fun getSodaDetails(
        @Query("location") location: String,
        @Query("brand") brand: String,
    ): Call<SodaResponse>

    @GET("sales-summary")
    fun getSalesSummary(): Call<SummaryResponse>

    @GET("restock")
    fun restock(
        @Query("location") location: String,
        @Query("brand") brand: String,
        @Query("quantity") quantity: Int,
    ): Call<RestockResponse>

    companion object {
        private const val BASE_URL = "https://kasie-nongranular-darwin.ngrok-free.dev/"

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
