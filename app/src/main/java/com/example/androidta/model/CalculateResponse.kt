package com.example.androidta.model

import java.io.Serializable

data class CalculateResponse(

    val tenor: String,
    val pencairan: Long,
    val angsuran: Long
): Serializable
