package com.example.androidta.Retrofit

import com.example.androidta.Service.BrandApiService
import com.example.androidta.Service.CalculateApiService
import com.example.androidta.Service.KonsumenApiService
import com.example.androidta.Service.MobilApiService
import com.example.androidta.Service.PengajuanApiService
import com.example.androidta.Service.WilayahApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InstanceRetro {
    private const val BASE_URL = "http://10.0.2.2:8082/api/v1/" // Ganti dengan URL API Anda

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val wilayahApiService: WilayahApiService by lazy {
        retrofit.create(WilayahApiService::class.java)
    }

    val brandMobilApiService: BrandApiService by lazy {
        retrofit.create(BrandApiService::class.java)
    }

    val mobilApiService: MobilApiService by lazy {
        retrofit.create(MobilApiService::class.java)
    }

    val calculateApiService: CalculateApiService by lazy {
        retrofit.create(CalculateApiService::class.java)
    }

    val pengajuanApiService: PengajuanApiService by lazy {
        retrofit.create(PengajuanApiService::class.java)
    }

    val konsumenApiService: KonsumenApiService by lazy {
        retrofit.create(KonsumenApiService::class.java)
    }


}
