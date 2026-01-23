package com.whoismacy.android.sodaadminapp

import com.google.gson.annotations.SerializedName

data class SodaResponse(
    val brand: String,
    val location: String,
    val metrics: SodaMetrics
)

data class SodaMetrics(
    val sold: Int,
    @SerializedName("revenueGenerated")
    val revenue: Double
)

data class SummaryResponse(
    val locations: List<String>,
    val salesData: Map<String, List<SodaSummary>>
)

data class SodaSummary(
    val brand: String,
    val totalSold: Int
)
