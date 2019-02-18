package com.example.mutuaj.helloworld.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MaesService {

    @GET("/list")
    fun getCropData(
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("maturity") maturity: String,
        @Query("droughtTolerance") droughtTolerance: String
    ): Single<List<CropResponse>>

    companion object {
        const val BASE_URL = "http://3.95.145.154:80"
    }
}