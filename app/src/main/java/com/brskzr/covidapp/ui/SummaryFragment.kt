package com.brskzr.covidapp.ui


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brskzr.covidapp.R
import com.brskzr.covidapp.adapters.SummaryAdapter
import com.brskzr.covidapp.data.Status
import com.brskzr.covidapp.extensions.formatDotted
import com.brskzr.covidapp.extensions.hide
import com.brskzr.covidapp.extensions.show
import com.brskzr.covidapp.extensions.toaster
import com.brskzr.covidapp.model.Country
import com.brskzr.covidapp.viewmodels.SummaryViewModel
import kotlinx.android.synthetic.main.fragment_summary.*
import lecho.lib.hellocharts.model.*


class SummaryFragment : Fragment() {

    private lateinit var viewModel:SummaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SummaryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.brskzr.covidapp.R.layout.fragment_summary, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpWorldSummary()
        setUpSummary()
        chart.hide()
    }

    private fun setUpWorldSummary() {
        viewModel.getToalWorldSummary().observe(this, Observer {
            when(it.status) {
                Status.WAITING -> setLoadingVisibilityOfWorldSummary(true)
                Status.ERROR -> {
                    toaster("Error occured while getting world summary")
                    setLoadingVisibilityOfWorldSummary(false)
                }
                Status.SUCCESS -> {
                    it.data?.let {model ->
                        tv_total_confirmed.text = model.TotalConfirmed.formatDotted()
                        tv_total_deaths.text = model.TotalDeaths.formatDotted()
                        tv_total_recovered.text = model.TotalRecovered.formatDotted()
                    }
                    setLoadingVisibilityOfWorldSummary(false)
                }
            }
        })
    }

    private fun setUpSummary() {
        viewModel.getSummary().observe(this, Observer {
            when(it.status) {
                Status.ERROR -> toaster("Error occured while getting summary of all countries")
                Status.SUCCESS -> {
                    it.data?.let {
                        val adapter = SummaryAdapter(it, ::onSummaryItemClick)
                        rv_summary.layoutManager = LinearLayoutManager(this.activity, RecyclerView.HORIZONTAL, false)
                        rv_summary.adapter = adapter
                        rv_summary.setHasFixedSize(true)

                        adapter.selectedCountry?.Slug?.let {
                            setUpGraph(it)
                        }
                    }
                }
            }
        })
    }

    private fun onSummaryItemClick(country: Country){
        setUpGraph(country.Slug)
    }

    private fun setUpGraph(slug: String) {
        chart.hide()
        viewModel.getSummaryOfCountry(slug).observe(this, Observer {
            when(it.status) {
                Status.ERROR -> toaster("Error occured while drawing graph")
                Status.SUCCESS -> {
                    chart.show()
                    it.data?.let { items ->
                        GraphSummaryOfCountry().draw(chart, items)
                    }
                }
            }
        })
    }

    private fun setLoadingVisibilityOfWorldSummary(isloading: Boolean){

        if(isloading) {
            ll_world_summary.alpha = 0.3f
            progress_world_summary.show()
        }
        else{
            ll_world_summary.alpha = 1f
            progress_world_summary.hide()
        }
    }
}
