package com.crm.crmapp.salesscheme

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.marketPlan.models.SchemeListRequestResponseModel
import com.crm.crmapp.newuser.RecentUserModel
import com.crm.crmapp.order.util.CustomDate
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SalesSchemeFragment : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private lateinit var viewOfLayout: View
    private lateinit var alDialogData: ArrayList<RecentUserModel>
    private lateinit var tvAdd: TextView
    private lateinit var rvSalesScheme: RecyclerView
    private var intent: Intent? = null
    var progressDialog: ProgressDialog? = null
    private lateinit var recentSchemeList: ArrayList<SchemeListRequestResponseModel.Result>
    var preferencesHelper: PreferencesHelper? = null
    private lateinit var mAlertDialogScheme: AlertDialog
    lateinit var tv_scheme_code: TextInputEditText
    lateinit var tv_scheme_name: TextInputEditText
    lateinit var tv_scheme_start_date: TextInputEditText
    lateinit var tv_scheme_end_date: TextInputEditText
    lateinit var yyddmDate_frmDate: CustomDate
    lateinit var yyddmDate_toDate: CustomDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        yyddmDate_frmDate = CustomDate()
        yyddmDate_toDate = CustomDate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_sales_scheme, container, false)
        alDialogData = ArrayList()
        recentSchemeList = ArrayList()
        setListener()
        return viewOfLayout
    }

    override fun onResume() {
        super.onResume()
        try {
            if (ConstantVariable.verifyAvailableNetwork(activity as AppCompatActivity)) {
                getSalesSchemeList()
            } else {
                ConstantVariable.onSNACK(rvSalesScheme, "Check your internet connection !!")
            }
        } catch (e: Exception) {
            ConstantVariable.onSNACK(rvSalesScheme, "Oops something went wrong !!")
        }
        //checkOffLineData()
    }

    private fun getSalesSchemeList() {
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<SchemeListRequestResponseModel>? = apiInterface!!.GetSchemeList()
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<SchemeListRequestResponseModel> {
            override fun onFailure(call: Call<SchemeListRequestResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<SchemeListRequestResponseModel>,
                response: Response<SchemeListRequestResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    recentSchemeList = response.body()
                        ?.getResult() as ArrayList<SchemeListRequestResponseModel.Result>
                    if (recentSchemeList.size > 0) {
                        // todo insert customer in db
                        setRecyclerview(recentSchemeList)
                    } else {
                        Toast.makeText(activity, "No Record Found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun setRecyclerview(recentSchemeList: ArrayList<SchemeListRequestResponseModel.Result>) {
        rvSalesScheme?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvSalesScheme?.adapter =
            SalesSchemeAdapter(context!!, recentSchemeList, object : onItemClick {
                override fun getPositionOfList(position: Int) {
                    setSchemeDialogue(ConstantVariable.FLAG_VIEW, recentSchemeList.get(position))
                }
            }, object : SalesSchemeAdapter.OnActivateClick {
                override fun getActivatePositionOfList(position: Int) {

                }

                override fun getEditPositionOfList(position: Int) {
                    setSchemeDialogue(ConstantVariable.FLAG_EDIT, recentSchemeList.get(position))
                }

            })
    }

    override fun onClick(view: View?) {
        when (view) {
            tvAdd -> {
                ConstantVariable.animationEffect(tvAdd, activity!!)
                setSchemeDialogue(
                    ConstantVariable.FLAG_NEW,
                    SchemeListRequestResponseModel.Result()
                )
            }
        }
    }

    private fun setListener() {
        rvSalesScheme = viewOfLayout.findViewById(R.id.rvSalesScheme) as RecyclerView
        tvAdd = viewOfLayout.findViewById(R.id.tvAdd) as TextView
        preferencesHelper = PreferencesHelper(this!!.activity!!)
        tvAdd.setOnClickListener(this)
    }

    private fun setSchemeDialogue(flag: Int, model: SchemeListRequestResponseModel.Result) {

        try {
            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_sales_scheme, null)
            val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
            //show dialog
            val displayMetric = DisplayMetrics()
            activity!!.windowManager!!.defaultDisplay.getMetrics(displayMetric)
            val layoutParams = WindowManager.LayoutParams()
            mAlertDialogScheme = mBuilder.create()
            mAlertDialogScheme.window
                ?.setBackgroundDrawableResource(android.R.color.transparent);
            mAlertDialogScheme.setCancelable(true)
            layoutParams.copyFrom(mAlertDialogScheme.window!!.attributes);
            tv_scheme_code = mDialogView.findViewById<TextInputEditText>(R.id.tv_scheme_code)!!
            val lblAdddetail = mDialogView.findViewById<AppCompatTextView>(R.id.lblAdddetail)!!
            tv_scheme_name =
                mDialogView.findViewById<TextInputEditText>(R.id.tv_scheme_name)!!
            tv_scheme_start_date =
                mDialogView.findViewById<TextInputEditText>(R.id.tv_scheme_start_date)!!
            tv_scheme_end_date =
                mDialogView.findViewById<TextInputEditText>(R.id.tv_scheme_end_date)!!
            val llsubmit = mDialogView.findViewById<LinearLayout>(R.id.llsubmit)!!
            val btnSubmit = mDialogView.findViewById<AppCompatButton>(R.id.btnSubmit)!!
            val btnCancel = mDialogView.findViewById<AppCompatButton>(R.id.btnCancel)!!

            btnSubmit.setOnClickListener {
                ConstantVariable.animationEffect(tv_scheme_start_date, requireActivity())
                if (isValid(flag)) {
                    if (flag == ConstantVariable.FLAG_NEW) {
                        var model: SchemeListRequestResponseModel.Result =
                            SchemeListRequestResponseModel.Result()
                        model.scheme_code = "0"
                        model.scheme_name = tv_scheme_name.text.toString()
                        model.start_date = yyddmDate_frmDate.date
                        model.end_date = yyddmDate_toDate.date
                        saveSalesSchemeList(model)
                    } else if (flag == ConstantVariable.FLAG_EDIT) {
                        model.scheme_code = tv_scheme_code.text.toString()
                        model.scheme_name = tv_scheme_name.text.toString()
                        model.start_date = yyddmDate_frmDate.date
                        model.end_date = yyddmDate_toDate.date
                        saveSalesSchemeList(model)
                    }
                    mAlertDialogScheme.dismiss()
                }
            }
            btnCancel.setOnClickListener({
                ConstantVariable.animationEffect(tv_scheme_start_date, requireActivity())
                mAlertDialogScheme.dismiss()
            })

            tv_scheme_start_date.setOnClickListener({
                ConstantVariable.animationEffect(tv_scheme_start_date, requireActivity())
                var datePickerDialog = ConstantVariable.datePickerWithYYDDMMRequest(
                    requireContext(),
                    tv_scheme_start_date,
                    yyddmDate_frmDate
                )
                // datePickerDialog.datePicker.maxDate = Date().getTime()
            })

            tv_scheme_end_date.setOnClickListener({
                ConstantVariable.animationEffect(tv_scheme_end_date, requireActivity())
                var datePickerDialog = ConstantVariable.datePickerWithYYDDMMRequest(
                    requireContext(),
                    tv_scheme_end_date,
                    yyddmDate_toDate
                )
                // datePickerDialog.datePicker.minDate = Date().getTime()
            })

            if (flag == ConstantVariable.FLAG_NEW) {
                lblAdddetail.text= "Add Scheme Details"
                tv_scheme_code.visibility = View.GONE
                tv_scheme_name.isEnabled = true
                tv_scheme_start_date.isEnabled = true
                tv_scheme_end_date.isEnabled = true
                llsubmit.visibility = View.VISIBLE
                tv_scheme_start_date.isEnabled = true
                tv_scheme_start_date.setFocusable(false);
                tv_scheme_start_date.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                tv_scheme_end_date.isEnabled = true
                tv_scheme_end_date.setFocusable(false);
                tv_scheme_end_date.setFocusableInTouchMode(false);
            } else if (flag == ConstantVariable.FLAG_EDIT) {
                lblAdddetail.text = "Edit Scheme Details"
                llsubmit.visibility = View.VISIBLE
                tv_scheme_code.isEnabled = false
                tv_scheme_name.isEnabled = true
                tv_scheme_start_date.isEnabled = true
                tv_scheme_start_date.setFocusable(false);
                tv_scheme_start_date.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                tv_scheme_end_date.isEnabled = true
                tv_scheme_end_date.setFocusable(false);
                tv_scheme_end_date.setFocusableInTouchMode(false);
                if (model != null) {
                    tv_scheme_code.setText(model.scheme_code)
                    tv_scheme_name.setText(model.scheme_name)
                    tv_scheme_start_date.setText(ConstantVariable.parseDateToddMMyyyyWithoutTime2(model.start_date.toString()))
                    tv_scheme_end_date.setText(ConstantVariable.parseDateToddMMyyyyWithoutTime2(model.end_date.toString()))
                    yyddmDate_frmDate.date = ConstantVariable.parseDateToyyyyMMdd2(model.start_date.toString())
                    yyddmDate_toDate.date = ConstantVariable.parseDateToyyyyMMdd2(model.end_date.toString())
                }
            } else {
                lblAdddetail.text = "View Scheme Details"
                llsubmit.visibility = View.GONE
                tv_scheme_code.isEnabled = false
                tv_scheme_name.isEnabled = false
                tv_scheme_start_date.isEnabled = false
                tv_scheme_end_date.isEnabled = false
                if (model != null) {
                    tv_scheme_code.setText(model.scheme_code)
                    tv_scheme_name.setText(model.scheme_name)
                    tv_scheme_start_date.setText(ConstantVariable.parseDateToddMMyyyyWithoutTime2(model.start_date.toString()))
                    tv_scheme_end_date.setText(ConstantVariable.parseDateToddMMyyyyWithoutTime2(model.end_date.toString()))
                    yyddmDate_frmDate.date = ConstantVariable.parseDateToyyyyMMdd2(model.start_date.toString())
                    yyddmDate_toDate.date = ConstantVariable.parseDateToyyyyMMdd2(model.end_date.toString())
                }
            }
            mAlertDialogScheme.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun isValid(flag: Int): Boolean {
        var isValid: Boolean = false
        if (flag == ConstantVariable.FLAG_NEW) {
            if (tv_scheme_name.text.toString().length == 0) {
                isValid = false;
                ConstantVariable.onToast(context!!, "Please enter scheme name")
            } else if (tv_scheme_start_date.text.toString().length == 0) {
                isValid = false;
                ConstantVariable.onToast(context!!, "Please enter start date")
            } else if (tv_scheme_end_date.text.toString().length == 0) {
                isValid = false;
                ConstantVariable.onToast(context!!, "Please enter end date")
            } else {
                isValid = true
            }
        } else if (flag == ConstantVariable.FLAG_EDIT) {
            if (tv_scheme_name.text.toString().length == 0) {
                isValid = false;
                ConstantVariable.onToast(context!!, "Please enter scheme name")
            } else if (tv_scheme_start_date.text.toString().length == 0) {
                isValid = false;
                ConstantVariable.onToast(context!!, "Please enter start date")
            } else if (tv_scheme_end_date.text.toString().length == 0) {
                isValid = false;
                ConstantVariable.onToast(context!!, "Please enter end date")
            } else {
                isValid = true
            }
        }
        return isValid;
    }

    private fun saveSalesSchemeList(model: SchemeListRequestResponseModel.Result) {
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<SchemeListRequestResponseModel>? =
            apiInterface!!.AddUpdateMarketSchema(model)
        Log.e("SalesSchemerequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<SchemeListRequestResponseModel> {
            override fun onFailure(call: Call<SchemeListRequestResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<SchemeListRequestResponseModel>,
                response: Response<SchemeListRequestResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null && response.code() == 200) {
                    ConstantVariable.onToast(context!!, response.message())
                    getSalesSchemeList()
                } else {
                    Toast.makeText(activity, "No Record Found", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
