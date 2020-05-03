package com.brskzr.covidapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brskzr.covidapp.R
import com.brskzr.covidapp.extensions.formatDotted
import com.brskzr.covidapp.model.Country
import com.brskzr.covidapp.model.SummaryModel

class SummaryAdapter(val summaryModel: SummaryModel, val onItemClick: (Country)->Unit) : RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {

    private var countries : List<Country> = summaryModel.Countries.sortedByDescending { it.TotalConfirmed }
    private var country:Country? = countries.firstOrNull()
    var index = if(countries.size > 0) 0 else -1

    val selectedCountry:Country?
        get() = country

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.summary_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = countries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(countries[position], onItemClick)
        holder.view.setOnClickListener {
            onItemClick(countries[position])
            index = position
            notifyDataSetChanged()
        }

        if(index == position){
            holder.view.setBackgroundColor(Color.parseColor("#99FF0000"));
        }else{
            holder.view.setBackgroundColor(Color.parseColor("#99000000"));
        }
    }

    class ViewHolder(val view:View) : RecyclerView.ViewHolder(view){

        fun bindItems(country: Country, onItemClick: (Country)->Unit) {
            val tvCountryName = view.findViewById<TextView>(R.id.tv_country_name)
            val tvNewConfirmed = view.findViewById<TextView>(R.id.tv_new_confirmed)
            val tvTotalConfirmed= view.findViewById<TextView>(R.id.tv_total_confirmed)
            val tvNewDeaths = view.findViewById<TextView>(R.id.tv_new_deaths)
            val tvTotalDeaths = view.findViewById<TextView>(R.id.tv_total_deaths)
            val tvNewRecovered = view.findViewById<TextView>(R.id.tv_new_recovered)
            val tvTotalRecovered = view.findViewById<TextView>(R.id.tv_total_recovered)

            tvCountryName.text = country.Country
            tvNewConfirmed.text = country.NewConfirmed.formatDotted()
            tvTotalConfirmed.text = country.TotalConfirmed.formatDotted()
            tvNewDeaths.text = country.NewDeaths.formatDotted()
            tvTotalDeaths.text = country.TotalDeaths.formatDotted()
            tvNewRecovered.text = country.NewRecovered.formatDotted()
            tvTotalRecovered.text = country.TotalRecovered.formatDotted()
        }


    }
}