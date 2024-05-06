package com.example.androidta.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable
import java.util.Date

data class RequestPengajuan (
    val wilayah: Int? = -1,
    val brandMobil: Int? = -1,
    val tipeMobil: Int? = -1,
    val hargaMobil: Int? = -1,
    val asuransi: String = "",
    val tahunMobil: Int = -1,
    val tenor: String = "",
    val pencairan: Int = -1,
    val angsuran: Int = -1,
    val nik: String = "",
    val nama: String = "",
    val tglLahir: String = "",
    val noHp: String = "",
    val domisili: String = "",
    val noRek: String = "",
    val status: String? = "",
    val noKontrak: String = "",

): Serializable

