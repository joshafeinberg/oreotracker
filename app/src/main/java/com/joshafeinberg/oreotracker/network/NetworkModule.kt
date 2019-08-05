package com.joshafeinberg.oreotracker.network

import com.google.gson.GsonBuilder
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.Stats
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import com.joshafeinberg.oreotracker.sharedmodule.Time
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


object NetworkModule {

    private val retrofit: Retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .build()

        Retrofit.Builder()
                .baseUrl("https://oreo-tracker.appspot.com")
                //.baseUrl("http://10.0.2.2:8080")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(
                        GsonBuilder()
                                .registerTypeAdapter(Time::class.java, Time.TimeAdapter())
                                .registerTypeAdapter(Content::class.java, Content.ContentAdapter())
                                .create()
                ))
                .build()
    }

    val adapter: OreoTrackerNetwork by lazy {
        retrofit.create(OreoTrackerNetwork::class.java)
    }
}

interface OreoTrackerNetwork {
    @GET("throwup")
    suspend fun getThrowUps(): List<ThrowUp>

    @POST("throwup")
    suspend fun postThrowUp(@Body body: ThrowUp)

    @GET("stats")
    suspend fun getStats(): Stats
}