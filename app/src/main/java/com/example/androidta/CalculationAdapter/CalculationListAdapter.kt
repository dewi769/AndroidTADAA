package com.example.androidta.CalculationAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidta.R
import com.example.androidta.model.CalculateResponse
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
class CalculationListAdapter(
    private val context: Context,
    private val dataList: List<CalculateResponse>,
    private val itemClickListener: (CalculateResponse) -> Unit
) : RecyclerView.Adapter<CalculationListAdapter.CalculateResponseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculateResponseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_perhitungan, parent, false)
        return CalculateResponseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalculateResponseViewHolder, position: Int) {
        val calculateResponse = dataList[position]
        holder.bind(calculateResponse)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CalculateResponseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tenorTextView: TextView = itemView.findViewById(R.id.txtTenor)
        private val pencairanTextView: TextView = itemView.findViewById(R.id.txtPencairan)
        private val angsuranTextView: TextView = itemView.findViewById(R.id.txtAngsuran)

        fun bind(calculateResponse: CalculateResponse) {
            tenorTextView.text = "${calculateResponse.tenor} Tahun"

            // Format angka pencairan ke dalam format mata uang
            val formattedPencairan = formatCurrency(calculateResponse.pencairan)
            pencairanTextView.text = formattedPencairan

            // Format angka angsuran ke dalam format mata uang
            val formattedAngsuran = formatCurrency(calculateResponse.angsuran)
            angsuranTextView.text = formattedAngsuran

            // Set OnClickListener untuk menangani klik item
            itemView.setOnClickListener { itemClickListener(calculateResponse) }
        }

        private fun formatCurrency(amount: Long): String {
            val format = DecimalFormat("#,###")
            val symbols = format.decimalFormatSymbols
            symbols.currencySymbol = ""
            format.decimalFormatSymbols = symbols
            return "Rp " + format.format(amount)
        }
    }
}