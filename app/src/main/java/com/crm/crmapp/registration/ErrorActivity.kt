package com.crm.crmapp.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.crm.crmapp.R

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        val errorTextView: TextView = findViewById(R.id.errorTextView)
        val errorMessage = intent.getStringExtra("error_message")
        if (errorMessage!=null){
            errorTextView.text = errorMessage
        }


    }
}