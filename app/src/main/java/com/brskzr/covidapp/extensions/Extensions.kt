package com.brskzr.covidapp.extensions

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.util.*


fun EditText.setTextChangeListener(onChange: (test:String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) { }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onChange(s.toString())
        }
    })
}

fun getLanguageCode(): String {
    return Locale.getDefault().language
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun Context.toaster(message:String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.toaster(message: String) {
    Toast.makeText(this.activity, message, Toast.LENGTH_LONG).show()
}

fun Number.formatDotted() : String = NumberFormat.getNumberInstance(Locale.getDefault()).format(this)

fun View.setViewVisibility(isVisible:Boolean) {
    if(isVisible){
        this.show()
    }
    else{
        this.hide()
    }
}


