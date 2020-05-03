package com.brskzr.covidapp.ui


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.brskzr.covidapp.R
import com.brskzr.covidapp.extensions.hide
import com.brskzr.covidapp.extensions.show
import com.brskzr.covidapp.extensions.setViewVisibility
import com.brskzr.covidapp.extensions.toaster
import com.brskzr.covidapp.notification.NotificationPeriod
import com.brskzr.covidapp.notification.NotificationWorkFactory
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {


    companion object{
        const val PREF_SETTINGS = "PREF_SETTINGS"
        const val HAS_NOTIFICATION = "hasNotification"
        const val NOTIFICATION_PERIOD = "notificationPeriod"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSettings()
        sw_notification.setOnCheckedChangeListener { buttonView, isChecked -> rgPeriods.setViewVisibility(isChecked) }
        btnSave.setOnClickListener { saveSettings() }
    }

    private fun saveSettings() {
        val period = getPeriodValue()
        val hasNotification = sw_notification.isChecked

        if(period == NotificationPeriod.None){
            toaster("Please choose period")
            return
        }

        val sharedPreference =  context!!.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putBoolean(HAS_NOTIFICATION, hasNotification)
        editor.putInt(NOTIFICATION_PERIOD, period.ordinal)
        editor.commit()


        activity?.let {
            if(sw_notification.isChecked) {
                NotificationWorkFactory.create(it.applicationContext, period)
            }
            else{
                NotificationWorkFactory.cancel(it.applicationContext)
            }
        }

        toaster("Settings saved")
    }

    inline private fun getPeriodValue(): NotificationPeriod  = when(rgPeriods.checkedRadioButtonId) {
        R.id.rbHourly -> NotificationPeriod.Hourly
        R.id.rbFourHour -> NotificationPeriod.PerFourHour
        R.id.rbEndOfDay -> NotificationPeriod.EndOfDay
        else -> NotificationPeriod.None
    }

    private fun loadSettings () {
        val sharedPreference =  context!!.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE)
        val hasNotification = sharedPreference.getBoolean(HAS_NOTIFICATION, false)
        val period = sharedPreference.getInt(NOTIFICATION_PERIOD, 0)

        if(hasNotification) {
            sw_notification.isChecked = true
            rgPeriods.setViewVisibility(hasNotification)

            when(period) {
                NotificationPeriod.Hourly.ordinal -> rbHourly.isChecked = true
                NotificationPeriod.PerFourHour.ordinal -> rbFourHour.isChecked = true
                NotificationPeriod.EndOfDay.ordinal -> rbEndOfDay.isChecked = true
            }
        }
    }
}
