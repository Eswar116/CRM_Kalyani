package com.crm.crmapp.marketPlan

import android.content.Intent

interface ViewNavigation {

        fun startActivityForResult(intent: Intent?, requestCode: Int)
}