package com.crm.crmapp.order.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {



    fun startProgress(context: Context, title: String, msg: String, isCancelable: Boolean): ProgressDialog {
        val dialog = ProgressDialog(context)
        dialog.setTitle(title)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(isCancelable)
        dialog.setMessage(msg)
        dialog.show()
        return dialog
    }

    fun stopProgress(dialog: ProgressDialog?) {
        try {
            dialog?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
   }
