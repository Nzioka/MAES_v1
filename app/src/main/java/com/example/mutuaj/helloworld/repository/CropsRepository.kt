package com.example.mutuaj.helloworld.repository

import com.example.mutuaj.helloworld.api.CropResponse
import com.example.mutuaj.helloworld.api.MaesService
import io.reactivex.Single

class CropsRepository(private val service: MaesService) {


    fun postToServer(
        x: String,
        y: String,
        maturity: String,
        droughtTolerance: String
    ): Single<List<CropResponse>> {
        return service.getCropData(x, y, maturity, droughtTolerance)
    }
}