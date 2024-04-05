package com.crm.crmapp.order.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ProgressBar
import com.crm.crmapp.R

class CustomProgressDialog(context: Context) : AlertDialog(context) {

    private val progressBar: ProgressBar

    init {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_progress_dialog, null)
        setView(view)
        setCancelable(false) // Optional: Prevent dismissing on outside touch
        progressBar = view.findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setMessage(message: String) {
        // Optionally set a message or update any other views in the layout
    }
}
