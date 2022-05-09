package com.mihaiv.stockmarketapp.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    companion object {
        const val API_KEY = "Q63Y9NX3TUF587NF"
        const val BASE_URL = "https://alphavantage.co"
    }

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apiKey") apiKey: String = API_KEY,
    ): ResponseBody
}