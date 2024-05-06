package com.example.androidta

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val buttonHitung = findViewById<Button>(R.id.btnHitung)

        buttonHitung.setOnClickListener {

            navigateToDisclaimerActivity()
        }
    }

    fun navigateToDisclaimerActivity() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.term_and_condition, null)

        val checkbox = view.findViewById<CheckBox>(R.id.checkbox_terms_conditions)

        builder
            .setTitle("Disclaimer")
            .setView(view)
            .setPositiveButton("Setuju") { dialog, which ->
                if (checkbox.isChecked) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Jika checkbox tidak dicentang, tampilkan pesan kesalahan atau lakukan tindakan lain.
                    Toast.makeText(this, "Please accept the terms & conditions.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal") { dialog, which ->
                // Lakukan sesuatu ketika tombol Decline ditekan.
                // Contoh: Tutup dialog atau batalkan tindakan.
                dialog.dismiss()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()

//
    }
}