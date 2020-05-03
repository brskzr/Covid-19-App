package com.brskzr.covidapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.view.marginLeft
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brskzr.covidapp.R
import com.brskzr.covidapp.adapters.MenuAdapter
import com.brskzr.covidapp.data.Status
import com.brskzr.covidapp.extensions.hide
import com.brskzr.covidapp.extensions.setTextChangeListener
import com.brskzr.covidapp.extensions.show
import com.brskzr.covidapp.extensions.toaster
import com.brskzr.covidapp.model.CountryModel
import com.brskzr.covidapp.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_menu_left.*


class MainActivity : AppCompatActivity() {
    private lateinit var menuAdapter : MenuAdapter
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        initMenu()
        initContent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setToolbarContent(null,true)
    }

    private fun initContent() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, SummaryFragment())
            .commit()
    }

    private fun setActionBar() {
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.toolbar_main)
            val toolbarView = getCustomView()
            val ivNavMenu = toolbarView.findViewById<ImageView>(R.id.ivNavMenu)
            ivNavMenu.setOnClickListener { toggleLeftDrawer() }

            val ivSettings = toolbarView.findViewById<ImageView>(R.id.ivSettings)
            ivSettings.setOnClickListener { goToSettings() }
        }
    }

    private fun goToSettings() {
        clearBackStackIfNeed(SettingsFragment::class.qualifiedName!!)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SettingsFragment())
            .addToBackStack(SettingsFragment::class.qualifiedName)
            .commit()
        setToolbarContent("Settings",false)
    }

    private fun clearBackStackIfNeed(name:String){
        if(supportFragmentManager.backStackEntryCount > 0){
            for (i in 1..supportFragmentManager.backStackEntryCount)
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun goToCountryTotal(country: CountryModel) {
        clearBackStackIfNeed(CountryTotalFragment::class.qualifiedName!!)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CountryTotalFragment.newInstance(country.Slug))
            .addToBackStack(CountryTotalFragment::class.qualifiedName)
            .commit()

        setToolbarContent(country.Country, false)
    }

    private fun setToolbarContent(header: String?, visible: Boolean){
        supportActionBar?.let {
            val rlToolbar= it.customView.findViewById<RelativeLayout>(R.id.rl_toolbar)

            val headerView = rlToolbar.findViewById<TextView>(R.id.tv_toolbar_header)
            headerView.apply {
                text = header?.let { it } ?: "Covid-19"
            }

            val ivNavMenu = rlToolbar.findViewById<ImageView>(R.id.ivNavMenu)
            val ivSettings = rlToolbar.findViewById<ImageView>(R.id.ivSettings)

            if(visible) {
                ivNavMenu.show()
                ivSettings.show()
            }
            else{
                ivNavMenu.hide()
                ivSettings.hide()
            }

            it.setDisplayHomeAsUpEnabled(!visible)
        }
    }

    private fun initMenu(){
        setActionBar()
        viewModel.getCountries().observe(this, androidx.lifecycle.Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    it.data?.let {countries ->
                        menuAdapter = MenuAdapter(countries, ::onMenuClick)
                        rv_countries.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                        rv_countries.adapter = menuAdapter
                        rv_countries.setHasFixedSize(true)
                        et_filter.setTextChangeListener { menuAdapter.filter(it) }
                        et_filter.isEnabled = true
                    }

                    progress_menu_loading.hide()
                }
                Status.ERROR -> {
                    this.toaster("Error while loading menu!")
                    progress_menu_loading.hide()
                }
                Status.WAITING -> {
                    progress_menu_loading.show()
                }
            }
        })
    }

    private fun onMenuClick(country: CountryModel){
        toggleLeftDrawer()
        goToCountryTotal(country)
    }

    private fun toggleLeftDrawer() {
        if (drawerLayout.isDrawerOpen(leftDrawerMenu)) {
            drawerLayout.closeDrawer(leftDrawerMenu)
        } else {
            drawerLayout.openDrawer(leftDrawerMenu)
        }
    }
}
