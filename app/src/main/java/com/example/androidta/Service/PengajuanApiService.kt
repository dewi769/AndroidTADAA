package com.example.androidta.Service

import com.example.androidta.model.RequestPengajuan
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PengajuanApiService {

    @POST("pengajuan/save")
    fun postPengajuan(@Body requestPengajuan: RequestPengajuan): Call<Void>

}