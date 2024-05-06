package com.example.androidta

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.androidta.Retrofit.InstanceRetro
import com.example.androidta.Service.KonsumenApiService
import com.example.androidta.Service.PengajuanApiService
import com.example.androidta.Service.WilayahApiService
import com.example.androidta.model.BrandResponse
import com.example.androidta.model.KonsumenResponse
import com.example.androidta.model.RequestPengajuan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.log

class DataKonsumenActivity : AppCompatActivity() {

    private lateinit var pengajuanApiService: PengajuanApiService
    private lateinit var konsumenApiService: KonsumenApiService
    private lateinit var konsumenResponse: KonsumenResponse
    private lateinit var requestPengajuan: RequestPengajuan
    private lateinit var tempRequestPengajuan: RequestPengajuan
    private var selectedStatusKonsumen: String? = null
    private var selectedStatusPernikahan: String? = null
    private var calendar = Calendar.getInstance()
    private lateinit var txtNoKontrak: TextView
    private lateinit var noKontrak: EditText
    private lateinit var txtNIK: TextView
    private lateinit var nik: EditText
    private lateinit var txtNamaLengkap: TextView
    private lateinit var nama: EditText
    private lateinit var txtNomorHandphone: TextView
    private lateinit var noHandphone: EditText
    private lateinit var txtTanggalLahir: TextView
    private lateinit var btnTanggalLahir: Button
    private lateinit var txtDomisili: TextView
    private lateinit var domisili: EditText
    private lateinit var txtNoRek: TextView
    private lateinit var noRek: EditText
    private lateinit var txtStatusPernikahan: TextView
    private lateinit var statusPernikahan: Spinner
    private lateinit var btnCariNoKontrak: Button
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_konsumen)

        pengajuanApiService = InstanceRetro.pengajuanApiService
        konsumenApiService = InstanceRetro.konsumenApiService

        tempRequestPengajuan = intent.getSerializableExtra("requestPengajuan") as RequestPengajuan

        txtNoKontrak = findViewById(R.id.txtNomorKontrak)
        noKontrak = findViewById(R.id.noKontrak)
        txtNIK = findViewById(R.id.txtNIK)
        nik = findViewById(R.id.nik)
        txtNamaLengkap = findViewById(R.id.txtNamaLengkap)
        nama = findViewById(R.id.nama)
        txtNomorHandphone = findViewById(R.id.txtNomorHandphone)
        noHandphone = findViewById(R.id.noHandphone)
        txtTanggalLahir = findViewById(R.id.txtTanggalLahir)
        btnTanggalLahir = findViewById(R.id.btnTanggalLahir)
        txtDomisili = findViewById(R.id.txtDomisili)
        domisili = findViewById(R.id.domisili)
        txtNoRek = findViewById(R.id.txtNoRek)
        noRek = findViewById(R.id.noRek)
        txtStatusPernikahan = findViewById(R.id.txtStatusPernikahan)
        statusPernikahan = findViewById(R.id.spinnerStatusPernikahan)
        btnCariNoKontrak =  findViewById(R.id.btnCariNoKontrak)
        btnSubmit =  findViewById(R.id.btnSubmit)


        btnTanggalLahir.setOnClickListener {
            showDatePicker()
        }

        btnSubmit.setOnClickListener {
            onSubmit()
        }

        btnCariNoKontrak.setOnClickListener {
           onSearchNoKontrak()
        }

        GlobalScope.launch(Dispatchers.Main) {
            //Pilih Status Konsumen
            val statusKonsumen = arrayOf("- Pilih Status Konsumen -", "Repeat Order", "Reguler")
            val spinnerStatusKonsumen: Spinner = findViewById(R.id.spinnerStatusKonsumen)
            val adapterStatusKonsumen = ArrayAdapter<String>(
                this@DataKonsumenActivity,
                android.R.layout.simple_spinner_item,
                statusKonsumen
            )
            adapterStatusKonsumen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerStatusKonsumen.adapter = adapterStatusKonsumen

            spinnerStatusKonsumen.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) {
                            selectedStatusKonsumen = statusKonsumen[position]
                            Log.d("DataKonsumenActivity", "Status Konsumen dipilih: $selectedStatusKonsumen")

                            if (selectedStatusKonsumen == "Repeat Order") {
                                noKontrak.setText("")

                                noKontrak.visibility = View.VISIBLE
                                txtNoKontrak.visibility = View.VISIBLE
                                btnCariNoKontrak.visibility = View.VISIBLE

                                txtNIK.visibility = View.GONE
                                nik.visibility = View.GONE
                                txtNamaLengkap.visibility = View.GONE
                                nama.visibility = View.GONE
                                txtNomorHandphone.visibility = View.GONE
                                noHandphone.visibility = View.GONE
                                txtTanggalLahir.visibility = View.GONE
                                btnTanggalLahir.visibility = View.GONE
                                txtDomisili.visibility = View.GONE
                                domisili.visibility = View.GONE
                                txtNoRek.visibility = View.GONE
                                noRek.visibility = View.GONE
                                txtStatusPernikahan.visibility = View.GONE
                                statusPernikahan.visibility = View.GONE
                                btnSubmit.visibility = View.GONE

                            } else {

                                nik.setText("")
                                nama.setText("")
                                btnTanggalLahir.text = "- Pilih Tanggal Lahir -"
                                noHandphone.setText("")
                                domisili.setText("")
                                noRek.setText("")
                                val spinnerStatusPernikahan: Spinner = findViewById(R.id.spinnerStatusPernikahan)
                                val statusPernikahanReguler = "- Pilih Status Pernikahan -"
                                val indexStatus = (spinnerStatusPernikahan.adapter as ArrayAdapter<String>).getPosition(statusPernikahanReguler)
                                spinnerStatusPernikahan.setSelection(indexStatus)

                                noKontrak.visibility = View.GONE
                                txtNoKontrak.visibility = View.GONE
                                btnCariNoKontrak.visibility = View.GONE

                                txtNIK.visibility = View.VISIBLE
                                nik.visibility = View.VISIBLE
                                txtNamaLengkap.visibility = View.VISIBLE
                                nama.visibility = View.VISIBLE
                                txtNomorHandphone.visibility = View.VISIBLE
                                noHandphone.visibility = View.VISIBLE
                                txtTanggalLahir.visibility = View.VISIBLE
                                btnTanggalLahir.visibility = View.VISIBLE
                                txtDomisili.visibility = View.VISIBLE
                                domisili.visibility = View.VISIBLE
                                txtNoRek.visibility = View.VISIBLE
                                noRek.visibility = View.VISIBLE
                                txtStatusPernikahan.visibility = View.VISIBLE
                                statusPernikahan.visibility = View.VISIBLE
                                btnSubmit.visibility = View.VISIBLE

                            }

                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }

            //Status Pernikahan
            val statusPernikahan = arrayOf("- Pilih Status Pernikahan -", "Menikah", "Belum Menikah")
            val spinnerStatusPernikahan: Spinner = findViewById(R.id.spinnerStatusPernikahan)
            val adapterStatusPernikahan = ArrayAdapter<String>(
                this@DataKonsumenActivity,
                android.R.layout.simple_spinner_item,
                statusPernikahan
            )
            adapterStatusPernikahan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerStatusPernikahan.adapter = adapterStatusPernikahan

            spinnerStatusPernikahan.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) {
                            selectedStatusPernikahan = statusPernikahan[position]
                            Log.d("DataKonsumenActivity", "Status Pernikahan dipilih: $selectedStatusPernikahan")

                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                btnTanggalLahir.text = "$formattedDate"
                Log.d("DataKonsumenActivity", "Tanggal Lahir: $formattedDate")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun onSearchNoKontrak() {
        val inputNoKontrak = noKontrak.text.toString()
        konsumenApiService.getKonsumen(inputNoKontrak).enqueue(object : Callback<KonsumenResponse> {
            override fun onResponse(call: Call<KonsumenResponse>, response: Response<KonsumenResponse>) {
                if (response.isSuccessful) {
                    val konsumenResponse = response.body()
                    if (konsumenResponse?.success == true) {
                        val konsumenData = konsumenResponse.data
                        if (konsumenData != null) {
                            showToast("Nomor Kontrak ditemukan!")

                            noKontrak.visibility = View.GONE
                            txtNoKontrak.visibility = View.GONE
                            btnCariNoKontrak.visibility = View.GONE

                            txtNIK.visibility = View.VISIBLE
                            nik.visibility = View.VISIBLE
                            txtNamaLengkap.visibility = View.VISIBLE
                            nama.visibility = View.VISIBLE
                            txtNomorHandphone.visibility = View.VISIBLE
                            noHandphone.visibility = View.VISIBLE
                            txtTanggalLahir.visibility = View.VISIBLE
                            btnTanggalLahir.visibility = View.VISIBLE
                            txtDomisili.visibility = View.VISIBLE
                            domisili.visibility = View.VISIBLE
                            txtNoRek.visibility = View.VISIBLE
                            noRek.visibility = View.VISIBLE
                            txtStatusPernikahan.visibility = View.VISIBLE
                            statusPernikahan.visibility = View.VISIBLE
                            btnSubmit.visibility = View.VISIBLE

                            nik.setText(konsumenData.nik)
                            nama.setText(konsumenData.nama)
                            btnTanggalLahir.text = konsumenData.tglLahir
                            noHandphone.setText(konsumenData.noHp)
                            domisili.setText(konsumenData.domisili)
                            noRek.setText(konsumenData.noRek)

                            val spinnerStatusPernikahan: Spinner = findViewById(R.id.spinnerStatusPernikahan)
                            val statusPernikahanDariKonsumen = konsumenData?.status ?: ""
                            val indexStatus = (spinnerStatusPernikahan.adapter as ArrayAdapter<String>).getPosition(statusPernikahanDariKonsumen)
                            spinnerStatusPernikahan.setSelection(indexStatus)

                            Log.d("DataKonsumenActivity", "Data Konsumen setelah klik Cari: $konsumenData")

                        } else {
                            showToast("Data konsumen tidak ditemukan")
                        }
                    } else {
                        showToast("Gagal mengambil data konsumen: ${konsumenResponse?.message}")
                    }
                } else {
                    showToast("Gagal mengambil data konsumen ")
                }
            }

            override fun onFailure(call: Call<KonsumenResponse>, t: Throwable) {
                showToast("Gagal menghubungi server: ${t.message}")
            }
        })
    }

    private fun onSubmit() {
        requestPengajuan = RequestPengajuan(
            wilayah = tempRequestPengajuan.wilayah,
            brandMobil = tempRequestPengajuan.brandMobil,
            tipeMobil = tempRequestPengajuan.tipeMobil,
            hargaMobil = tempRequestPengajuan.hargaMobil,
            asuransi = tempRequestPengajuan.asuransi,
            tahunMobil = tempRequestPengajuan.tahunMobil,
            tenor = tempRequestPengajuan.tenor,
            pencairan = tempRequestPengajuan.pencairan,
            angsuran = tempRequestPengajuan.angsuran,
            nik = nik.text.toString(),
            nama = nama.text.toString(),
            tglLahir = btnTanggalLahir.text.toString(),
            noHp = noHandphone.text.toString(),
            domisili = domisili.text.toString(),
            noRek = noRek.text.toString(),
            status = selectedStatusPernikahan,
            noKontrak = generateRandomRegistrationNumber()

        )
        Log.d("DataKonsumenActivity", "Data Pengajuan setelah diklik submit: $requestPengajuan")
        pengajuanApiService.postPengajuan(requestPengajuan).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("Data berhasil diajukan")
                    navigateToHomePage()
                } else {
                    showToast("Gagal mengajukan data")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Gagal mengajukan data")
            }
        })
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.show()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            toast.cancel() // Tutup toast setelah 5 detik
        }, 3000) // 5000 milliseconds = 5 detik
    }

    private fun navigateToHomePage() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }

    private fun generateRandomRegistrationNumber(): String {
        val randomNumber = (1000..9999).random()
        return "REG$randomNumber"
    }

}