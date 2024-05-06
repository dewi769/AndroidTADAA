package com.example.androidta.Service

import com.example.androidta.model.BrandResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BrandApiService {

    @GET("brand/{idWilayah}")
    suspend fun getBrandsByWilayah(@Path("idWilayah") idWilayah: Int): List<BrandResponse>

}