package com.crm.crmapp.attendence

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Test : AppCompatActivity() {
    private val tex : TextView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tex!!.setText(null)
    }



}