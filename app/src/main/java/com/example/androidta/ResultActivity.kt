package com.example.androidta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidta.CalculationAdapter.CalculationListAdapter
import com.example.androidta.model.CalculateResponse
import com.example.androidta.model.RequestPengajuan
import kotlin.math.log

class ResultActivity : AppCompatActivity() {

    private lateinit var CalculateResponse: List<CalculateResponse>
    private lateinit var requestPengajuan: RequestPengajuan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Menerima data dari intent
        CalculateResponse = intent.getSerializableExtra("calculateList") as? List<CalculateResponse> ?: emptyList()
        val tempRequestPengajuan = intent.getSerializableExtra("dataKendaraan") as RequestPengajuan

        // Inisialisasi RecyclerView dan adapter menggunakan dataList
        val recyclerView: RecyclerView = findViewById(R.id.recycleViewItem)
        val adapter = CalculationListAdapter(this, CalculateResponse) { calculateResponse ->

            requestPengajuan = RequestPengajuan(
                wilayah = tempRequestPengajuan.wilayah,
                brandMobil = tempRequestPengajuan.brandMobil,
                tipeMobil = tempRequestPengajuan.tipeMobil,
                hargaMobil = tempRequestPengajuan.hargaMobil,
                asuransi = tempRequestPengajuan.asuransi,
                tahunMobil = tempRequestPengajuan.tahunMobil,
                tenor = calculateResponse.tenor,
                pencairan = calculateResponse.pencairan.toInt(),
                angsuran = calculateResponse.angsuran.toInt()

            )
            Log.d("ResultActivity", "Data Kendaraan setelah pilih recycle: $requestPengajuan")

            val intent = Intent(this, DataKonsumenActivity::class.java)
            intent.putExtra("requestPengajuan", requestPengajuan)
            startActivity(intent)

//            val toastMessage = "Tenor: ${calculateResponse.tenor} Tahun\n" +
//                    "Pencairan: ${calculateResponse.pencairan}\n" +
//                    "Angsuran: ${calculateResponse.angsuran}"
//            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}