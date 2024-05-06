package com.example.androidta.Service

import com.example.androidta.model.AsuransiResponse
import com.example.androidta.model.BrandResponse
import com.example.androidta.model.CalculateResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CalculateApiService {

    @GET("perhitungan")
    suspend fun calculateResult(
        @Query("idVehicle") vehicleId: Int,
        @Query("tahunKendaraan") tahunKendaraan: Int,
        @Query("jenisAsuransi") jenisAsuransi: String
    ): Response<List<CalculateResponse>>

}