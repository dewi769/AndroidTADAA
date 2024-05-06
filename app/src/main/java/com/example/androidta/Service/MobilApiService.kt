package com.example.androidta.Service

import com.example.androidta.model.MobilResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MobilApiService {

    @GET("vehicle/{idVehicle}")
    suspend fun getVehicleByidBrand(@Path("idVehicle") idVehicle: Int): List<MobilResponse>

}