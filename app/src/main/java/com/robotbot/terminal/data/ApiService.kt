package com.robotbot.terminal.data

import retrofit2.http.GET

interface ApiService {

    @GET("aggs/ticker/AAPL/range/1/hour/2023-01-09/2024-01-09?adjusted=true&sort=asc&limit=50000&apiKey=6UpSfjXVxIVpmbnwqtjNumCfv51QPHTK")
    suspend fun loadBars(): Result

}