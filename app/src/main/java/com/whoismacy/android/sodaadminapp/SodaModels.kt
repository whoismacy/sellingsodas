package com.whoismacy.android.sodaadminapp

import com.google.gson.annotations.SerializedName

data class SodaResponse(
    val brand: String,
    val location: String,
    val metrics: SodaMetrics,
)

data class SodaMetrics(
    val sold: Int,
    @SerializedName("revenueGenerated")
    val revenue: Double,
    @SerializedName("remainingQuantity")
    val remaining: Int,
)

data class SummaryResponse(
    val locations: List<String>,
    val salesData: Map<String, List<SodaSummary>>,
)

data class SodaSummary(
    val brand: String,
    val totalSold: Int,
)

data class RestockResponse(
    val message: String,
    val data: RestockData,
)

data class RestockData(
    val location: String,
    val brand: String,
    val newTotal: Int,
)
