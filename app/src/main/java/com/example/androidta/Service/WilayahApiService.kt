package com.example.androidta.Service

import com.example.androidta.model.WilayahResponse
import retrofit2.http.GET

interface WilayahApiService {

    @GET("wilayah")
    suspend fun getAllWilayah(): List<WilayahResponse>

}
