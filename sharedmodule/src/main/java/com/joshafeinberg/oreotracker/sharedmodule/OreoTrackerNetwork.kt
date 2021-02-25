package com.joshafeinberg.oreotracker.sharedmodule

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OreoTrackerNetwork : SicknessService, WeightService, StatsService

interface SicknessService {
    @GET("throwup")
    suspend fun getThrowUps(): List<ThrowUp>

    @POST("throwup")
    suspend fun postThrowUp(@Body body: ThrowUp)
}

interface WeightService {
    @GET("weight")
    suspend fun getWeights(): List<Weight>

    @POST("weight")
    suspend fun postWeight(@Body body: Weight)
}

interface StatsService {
    @GET("stats")
    suspend fun getStats(): Stats
}
