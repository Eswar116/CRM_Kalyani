package com.crm.crmapp.settings

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingsFragment : Fragment() {

    private lateinit var intent: Intent
    private lateinit var viewOfLayout: View
    private var dialog: Dialog? = null
    private lateinit var tvdays: TextView
    private lateinit var btnSyncCustomers: Button
    private lateinit var progressDialog: ProgressDialog
    private lateinit var rl_showdateindays: RelativeLayout
    private lateinit var rl_syncCustomers: RelativeLayout
    var preferences: SharedPreferences? = null
    var preferencesHelper: PreferencesHelper? = null
    private var pagevalue: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_settings, container, false)!!
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        rl_showdateindays = viewOfLayout.findViewById(R.id.rl_showdateindays)
        rl_syncCustomers = viewOfLayout.findViewById(R.id.rl_syncCustomers)
        tvdays = viewOfLayout.findViewById(R.id.tvdays)
        preferencesHelper = PreferencesHelper(context!!)
        btnSyncCustomers = viewOfLayout.findViewById(R.id.btnSyncCustomers)
        if (preferences!!.getInt(PreferencesHelper.SETTINGS_PAGE_VALUE, 5) != 0) {
            pagevalue = preferences!!.getInt(PreferencesHelper.SETTINGS_PAGE_VALUE, 5)
            tvdays.text = pagevalue.toString()
        }

        rl_showdateindays.setOnClickListener {
            showSettingsDialog()
        }
        btnSyncCustomers.setOnClickListener {
            saveCustomer(it);
        }

        val result = preferencesHelper!!.getObject()
        if (result.getCategory().equals("Admin", true)) {
            rl_syncCustomers.visibility = View.VISIBLE
        } else {
            rl_syncCustomers.visibility = View.GONE
        }

        return viewOfLayout
    }

    override fun onResume() {
        super.onResume()

    }

    fun showSettingsDialog() {
        dialog = Dialog(context!!)
        dialog!!.setCancelable(true)
        val dec_min_value = 5
        val inc_max_value = 25
        if (preferences!!.getInt(PreferencesHelper.SETTINGS_PAGE_VALUE, 5) != 0) {
            pagevalue = preferences!!.getInt(PreferencesHelper.SETTINGS_PAGE_VALUE, 5)
        }
        dialog!!.setContentView(R.layout.layout_dialog_settings)
        val imgbtn_dec = dialog!!.findViewById<ImageButton>(R.id.imgbtn_dec)
        val edt_page = dialog!!.findViewById<EditText>(R.id.edt_page)
        val imgbtn_inc = dialog!!.findViewById<ImageButton>(R.id.imgbtn_inc)
        val btn_ok = dialog!!.findViewById<Button>(R.id.btn_ok)

        edt_page.setText("" + pagevalue)

        imgbtn_dec.setOnClickListener {
            var value = Integer.parseInt(edt_page.getText().toString())
            if (value != dec_min_value && value > dec_min_value) {
                value = value - 5;
                edt_page.setText("" + value)
            }
        }
        imgbtn_inc.setOnClickListener {
            var value = Integer.parseInt(edt_page.getText().toString())
            if (value != inc_max_value && value < inc_max_value) {
                value = value + 5;
                edt_page.setText("" + value)
            }
        }

        btn_ok.setOnClickListener {
            dialog!!.dismiss()
            tvdays.setText(edt_page.getText().toString())
            preferences!!.edit().putInt(
                    PreferencesHelper.SETTINGS_PAGE_VALUE,
                    Integer.parseInt(edt_page.getText().toString())
                ).apply()
        }

        if (dialog != null && !dialog!!.isShowing()) {
            dialog!!.show()
        } else {
            dialog!!.dismiss()
        }
    }

    private fun saveCustomer(view: View) {
        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        val apiInterface = ApiClient.getClientForSyncCustomers().create(ApiInterface::class.java)

        val result1 = SettingsSyncRequestResponseModel()

        result1.cldId = "0000"
        result1.horgId = "01"
        result1.slocId = "1"

        val call: Call<SettingsSyncRequestResponseModel> = apiInterface!!.syncCustomer(result1)

        call.enqueue(object : Callback<SettingsSyncRequestResponseModel> {
            override fun onFailure(call: Call<SettingsSyncRequestResponseModel>, t: Throwable) {
                progressDialog.dismiss()
                ConstantVariable.onSNACK(view, "Something went wrong")
            }

            override fun onResponse(
                call: Call<SettingsSyncRequestResponseModel>,
                response: Response<SettingsSyncRequestResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {

                    try {
                        if (response.body()?.status!!) {
                            /*   preferencesHelper!!.orderId=(response?.body()?.getOrderid())
                           ConstantVariable.onSNACK(tvDate, response.body()!!.getMsg()!!)*/
                            ConstantVariable.onToastSuccess(context!!, response.body()!!.message!!)
                            (context!! as Activity).finish()
                        } else {
                            ConstantVariable.onSNACK(view, response.body()!!.message!!)
                        }
                    } catch (e: NullPointerException) {
                        ConstantVariable.onSNACK(view, "Something went wrong!!")
                    }
                } else {
                    ConstantVariable.onSNACK(view, "Response Body is Empty!!")
                }
            }
        })
    }
}