package com.brskzr.covidapp.ui


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
import com.brskzr.covidapp.adapters.CountryTotalAdapter
import com.brskzr.covidapp.adapters.SummaryAdapter
import com.brskzr.covidapp.data.Status
import com.brskzr.covidapp.extensions.hide
import com.brskzr.covidapp.extensions.show
import com.brskzr.covidapp.extensions.toaster
import com.brskzr.covidapp.viewmodels.SummaryViewModel
import kotlinx.android.synthetic.main.fragment_country_total.*
import kotlinx.android.synthetic.main.fragment_summary.*


private const val ARG_SLUG = "slug"

class CountryTotalFragment : Fragment() {
    private lateinit var viewModel: SummaryViewModel

    private var slug: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            slug = it.getString(ARG_SLUG)
        }

        viewModel = ViewModelProvider(this).get(SummaryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_country_total, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slug?.let {
            getSummaryOfCountry(it)
        }
    }

    private fun getSummaryOfCountry(slug: String) {
        viewModel.getSummaryOfCountry(slug).observe(this, Observer {
            when(it.status) {
                Status.ERROR -> toaster("Error occured while getting country detail")
                Status.SUCCESS -> {
                    it.data?.let { items ->
                        rvCountryTotal.adapter = CountryTotalAdapter(items.sortedByDescending { it.Date })
                        rvCountryTotal.layoutManager = LinearLayoutManager(this.activity, RecyclerView.VERTICAL, false)
                        rvCountryTotal.setHasFixedSize(true)
                    }
                }
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(slug: String) =
            CountryTotalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SLUG, slug)
                }
            }
    }
}
