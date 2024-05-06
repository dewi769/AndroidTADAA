package com.example.androidta.Service

import com.example.androidta.model.KonsumenResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface KonsumenApiService {

    @GET("konsumen/{noKontrak}")
    fun getKonsumen(@Path("noKontrak") id: String): Call<KonsumenResponse>

}