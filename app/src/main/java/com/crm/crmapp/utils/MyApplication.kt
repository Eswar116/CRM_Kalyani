package com.crm.crmapp.utils

import android.app.Application
import android.content.Intent
import android.util.Log
import com.crm.crmapp.registration.ErrorActivity

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            // Handle the uncaught exception here
            Log.e("UncaughtException ERROR", throwable.message, throwable)

            val intent = Intent(applicationContext, ErrorActivity::class.java)
            intent.putExtra("error_message", throwable.message)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
          android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(2)
        }
    }
}
