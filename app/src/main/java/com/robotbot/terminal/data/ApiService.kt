package com.robotbot.terminal.data

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("aggs/ticker/AAPL/range/{timeframe}/2024-01-04/2025-01-04?adjusted=true&sort=desc&limit=50000&apiKey=6UpSfjXVxIVpmbnwqtjNumCfv51QPHTK")
    suspend fun loadBars(
        @Path("timeframe") timeFrame: String
    ): Result

}