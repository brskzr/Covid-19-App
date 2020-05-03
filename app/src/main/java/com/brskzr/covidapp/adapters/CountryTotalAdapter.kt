package com.brskzr.covidapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brskzr.covidapp.R
import com.brskzr.covidapp.extensions.formatDotted
import com.brskzr.covidapp.model.SummaryOfCountryModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class CountryTotalAdapter(val items: List<SummaryOfCountryModel>) : RecyclerView.Adapter<CountryTotalAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_totay_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        fun bindItems(model: SummaryOfCountryModel) {
            val tvDate = view.findViewById<TextView>(R.id.tv_date)
            val tvConfirmed = view.findViewById<TextView>(R.id.tv_confirmed)
            val tvDeaths = view.findViewById<TextView>(R.id.tv_deaths)
            val tvRecovered= view.findViewById<TextView>(R.id.tv_recovered)
            val tvActive = view.findViewById<TextView>(R.id.tv_active)


            tvDate.text = model.Date.toFormattedDate()
            tvConfirmed.text = model.Confirmed.formatDotted()
            tvDeaths.text = model.Deaths.formatDotted()
            tvRecovered.text = model.Recovered.formatDotted()
            tvActive.text = model.Active.formatDotted()
        }

        companion object {
            private val formatter = SimpleDateFormat("dd-MMMM-yyyy")
        }

        fun Date.toFormattedDate() : String {
            return formatter.format(this)
        }
    }
}