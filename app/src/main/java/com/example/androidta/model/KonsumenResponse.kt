package com.example.androidta.model

data class KonsumenResponse(
    val data: KonsumenData?,
    val success: Boolean,
    val errorCode: String?,
    val message: String?,
    val status: Int?,
    val timestamp: String?
)

data class KonsumenData(
    val nik: String?,
    val nama: String?,
    val noHp: String?,
    val tglLahir: String?,
    val noRek: String?,
    val status: String?,
    val domisili: String?
)
