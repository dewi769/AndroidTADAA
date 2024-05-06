package com.example.androidta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.example.androidta.Retrofit.InstanceRetro
import com.example.androidta.Service.BrandApiService
import com.example.androidta.Service.CalculateApiService
import com.example.androidta.Service.MobilApiService
import com.example.androidta.Service.WilayahApiService
import com.example.androidta.model.BrandResponse
import com.example.androidta.model.CalculateResponse
import com.example.androidta.model.MobilResponse
import com.example.androidta.model.RequestPengajuan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.androidta.model.WilayahResponse
import java.text.NumberFormat
import java.util.Calendar
import java.util.Currency


class MainActivity : AppCompatActivity() {

    private lateinit var wilayahApiService: WilayahApiService
    private lateinit var wilayahList: List<WilayahResponse>
    private lateinit var brandMobilApiService: BrandApiService
    private lateinit var brandMobilList: List<BrandResponse>
    private lateinit var mobilApiService: MobilApiService
    private lateinit var mobilList: List<MobilResponse>
    private lateinit var calculateApiService: CalculateApiService
    private lateinit var calculateList: List<CalculateResponse>
    private lateinit var requestPengajuan: RequestPengajuan

    private var selectedWilayah: WilayahResponse? = null
    private var selectedBrand: BrandResponse? = null
    private var selectedMobil: MobilResponse? = null
    private var selectedAsuransi: String? = null
    private var selectedYear: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wilayahApiService = InstanceRetro.wilayahApiService
        brandMobilApiService = InstanceRetro.brandMobilApiService
        mobilApiService = InstanceRetro.mobilApiService
        calculateApiService = InstanceRetro.calculateApiService


        GlobalScope.launch(Dispatchers.Main) {
            try {
                //Pilih Data Wilayah
                val wilayahResponse = wilayahApiService.getAllWilayah()
                wilayahList = wilayahResponse
                val spinnerWilayah: Spinner = findViewById(R.id.spinnerWilayah)
                val provinsiList = mutableListOf("- Pilih Wilayah -")
                provinsiList.addAll(wilayahList.map { it.provinsi })
                val adapterWilayah = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, provinsiList)
                adapterWilayah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerWilayah.adapter = adapterWilayah
                spinnerWilayah.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (position != 0) { // Pastikan bahwa "Pilih dahulu" tidak diproses
                            selectedWilayah = wilayahList[position - 1] // Perhatikan bahwa Anda perlu mengurangi satu dari posisi untuk menyesuaikan dengan indeks di dalam wilayahList

                            selectedWilayah?.let {
                                getBrandsByWilayah(it.idWilayah)
                                Log.d("MainActivity", "Wilayah ID dipilih: ${it.idWilayah}")
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.e("MainActivity", "Error: Belum Memilih Wilayah" )
                    }
                }

                //Pilih Jenis Asuransi
                val jenisAsuransi = arrayOf("- Pilih Jenis Asuransi", "Kombinasi", "All Risk")
                val spinnerAsuransi: Spinner = findViewById(R.id.spinnerJenisAsuransi)
                val adapterAsuransi = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, jenisAsuransi)
                adapterAsuransi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerAsuransi.adapter = adapterAsuransi

                spinnerAsuransi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (position != 0) { // Memastikan bahwa "Pilih terlebih dahulu" tidak diproses
                            selectedAsuransi = jenisAsuransi[position]
                            Log.d("MainActivity", "Asuransi dipilih: $selectedAsuransi")
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }

                //Pilih Tahun Kendaraan
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val years = mutableListOf<String>("- Pilih Tahun Kendaraan -")
                for (year in currentYear downTo currentYear - 10) {
                    years.add(year.toString())
                }

                val spinnerTahunKendaraan: Spinner = findViewById(R.id.tahunKendaraan)
                val adapterTahunKendaraan = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, years)
                adapterTahunKendaraan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTahunKendaraan.adapter = adapterTahunKendaraan

                spinnerTahunKendaraan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedYear = years[position]
                        Log.d("MainActivity", "Tahun dipilih: $selectedYear")
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.e("MainActivity", "Error: Belum Memilih Tahun Kendaraan" )
                    }
                }

                //Calculate Data
                val calculateButton: Button = findViewById(R.id.hitung)

                calculateButton.setOnClickListener {
                    calculateResult()
                }

            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.message}", e)
            }
        }
    }

    private fun getBrandsByWilayah(wilayahId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                // Ambil data merek mobil berdasarkan wilayah
                val brandsResponse = brandMobilApiService.getBrandsByWilayah(wilayahId)
                brandMobilList = brandsResponse
                val spinnerBrandMobil: Spinner = findViewById(R.id.spinnerBrand)

                val brandList = mutableListOf("- Pilih Brand Mobil -")
                brandList.addAll(brandMobilList.map { it.brandMobil })

//                val brandList = brandMobilList.map { it.brandMobil }
                val adapterBrandMobil = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, brandList)
                adapterBrandMobil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerBrandMobil.adapter = adapterBrandMobil

                spinnerBrandMobil.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (position != 0) {
//                            selectedBrand = brandMobilList[position]
                            selectedBrand = brandMobilList[position-1]
                            selectedBrand?.let {
                                Log.d("MainActivity", "Brand ID dipilih: ${it.idBrand}")
                                getMobilByBrand(it.idBrand)
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.e("MainActivity", "Error: Belum Memilih Brand Mobil" )
                    }
                }

            } catch (e: Exception) {
                // Tangani kesalahan
                Log.e("MainActivity", "Error: ${e.message}", e)
            }
        }
    }

    private fun getMobilByBrand(brandId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                // Ambil data merek mobil berdasarkan wilayah
                val mobilResponse = mobilApiService.getVehicleByidBrand(brandId)
                mobilList = mobilResponse

                val spinnerMobil: Spinner = findViewById(R.id.spinnerMobil)

                val listMobil = mutableListOf("- Pilih Tipe Mobil -")
                listMobil.addAll(mobilList.map { it.tipeMobil })

//                val listMobil = mobilList.map { it.tipeMobil }
                val adapterMobil = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, listMobil)
                adapterMobil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerMobil.adapter = adapterMobil

                spinnerMobil.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (position != 0) {
//                            selectedMobil = mobilList[position]
                            selectedMobil = mobilList[position-1]
                            selectedMobil?.let {
                                Log.d("MainActivity", "Mobil id dipilih: ${it.idVehicle}")
                                Log.d("MainActivity", "Harga Mobil dipilih: ${it.harga}")

                                val formattedPrice = NumberFormat.getCurrencyInstance().apply {
                                    currency = Currency.getInstance("IDR")
                                }.format(it.harga.toDouble()) // Assuming harga is a String

                                val textView = findViewById<TextView>(R.id.harga)
                                textView.text = "  Rp " + formattedPrice.substring(3)
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.e("MainActivity", "Error: Belum Memilih Brand Mobil" )
                    }
                }

            } catch (e: Exception) {
                // Tangani kesalahan
                Log.e("MainActivity", "Error: ${e.message}", e)
            }
        }
    }

    private fun calculateResult() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val vehicleId = selectedMobil?.idVehicle ?: -1
                val tahunKendaraan = selectedYear?.toInt() ?: -1
                val jenisAsuransi = selectedAsuransi ?: ""

                requestPengajuan = RequestPengajuan(
                    wilayah = selectedWilayah?.idWilayah,
                    brandMobil = selectedBrand?.idBrand,
                    tipeMobil = selectedMobil?.idVehicle,
                    hargaMobil = selectedMobil?.harga,
                    asuransi = selectedAsuransi?:"",
                    tahunMobil = selectedYear?.toInt() ?:-1,
                )
                Log.d("MainActivity", "Data RequestPengajuan: $requestPengajuan")

                val response = calculateApiService.calculateResult(vehicleId, tahunKendaraan, jenisAsuransi)
                if (response.isSuccessful) {
                    val calculationResults = response.body()
                    calculationResults?.let {
                        Log.d("MainActivity", "Response body: $it")
                        calculateList = it
                        navigateToResultPage()
                    } ?: run {
                        Log.e("MainActivity", "Response body is empty")
                    }
                } else {
                    Log.e("MainActivity", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.message}", e)
            }
        }
    }

    private fun navigateToResultPage() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("dataKendaraan", requestPengajuan)
        intent.putExtra("calculateList", ArrayList(calculateList))
        startActivity(intent)
    }



}