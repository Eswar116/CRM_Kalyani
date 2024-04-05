package com.crm.crmapp.aboutUS

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.crm.crmapp.R

class AboutUsFragment : Fragment() {
    private var viewOfLayout: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater?.inflate(R.layout.activity_about_app_simple_blue, container, false)
        return viewOfLayout
    }
}