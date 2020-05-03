package com.brskzr.covidapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brskzr.covidapp.model.CountryModel
import java.lang.Exception

class MenuAdapter(val data:List<CountryModel>, val onMenuClick: (CountryModel) -> Unit) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private var filteredItems : MutableList<CountryModel> = mutableListOf()

    init {
        filteredItems.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.brskzr.covidapp.R.layout.drawer_left_menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filteredItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(filteredItems[position], onMenuClick)

    class ViewHolder(val view:View) : RecyclerView.ViewHolder(view){
        fun bindItem(country: CountryModel, onMenuClick: (CountryModel) -> Unit) {
            try {
                val imgFlag = view.findViewById<ImageView>(com.brskzr.covidapp.R.id.img_flag)
                val context = imgFlag.getContext()
                val id = context.getResources().getIdentifier("${country.ISO2.toLowerCase()}_svg", "drawable", context.getPackageName());
                imgFlag.setImageResource(id)
            }
            catch (ex:Exception) {

            }

            val tvCountry = view.findViewById<TextView>(com.brskzr.covidapp.R.id.tv_country)
            tvCountry.setText(country.Country)
            view.setOnClickListener { onMenuClick(country) }
        }
    }

    fun filter(query: String) {
        filteredItems.clear()

        if(!query.isEmpty()){
            val filtered = data.filter { it.Country.toLowerCase().startsWith(query.toLowerCase()) }
            if(filtered.size > 0) {
                filteredItems.addAll(filtered)
            }
        }
        else {
            filteredItems.addAll(data)
        }
        notifyDataSetChanged()
    }
}