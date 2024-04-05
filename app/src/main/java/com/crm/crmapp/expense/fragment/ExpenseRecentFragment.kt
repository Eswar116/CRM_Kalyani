package com.crm.crmapp.expense.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast

import com.crm.crmapp.R
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.util.ConstantVariable.Companion.TAG_FROM
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.customer.model.CustomerResult
import com.crm.crmapp.expense.activity.NewExpense
import com.crm.crmapp.expense.adapter.ExpensePendingAdapter
import com.crm.crmapp.expense.model.*
import com.crm.crmapp.order.model.*
import com.crm.crmapp.order.util.PreferencesHelper
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ExpenseRecentFragment : Fragment(), View.OnClickListener {


    private lateinit var tvAllExpense: TextView
    private lateinit var tvPendingCount: TextView
    private lateinit var tvApprovedCount: TextView
    private lateinit var tvRejectedCount: TextView
    private lateinit var tvPendingSum: TextView
    private lateinit var tvApprovedSum: TextView
    private lateinit var tvRejectedSum: TextView
    private lateinit var tvtotalcount: TextView
    private lateinit var tvtotalamount: TextView
    private lateinit var tvdaysvalue: TextView
    private lateinit var tvNew: TextView
    private lateinit var designView: View
    private var chart: BarChart? = null
    private lateinit var progressDialog: ProgressDialog
    var containerValue: ViewGroup? = null
    private lateinit var recentOrderList: ArrayList<RecentOrderResult>
    private var userId: Int = 0
    private var pagevalue: Int = 0
    private var preferencesHelper: PreferencesHelper? = null
    var preferences: SharedPreferences? = null
    private var bottom_sheet: View? = null
    private var mBehavior: BottomSheetBehavior<*>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        designView = inflater.inflate(R.layout.fragment_expense_recent, container, false)
        containerValue = container
        findId()
        setListener()
        //setBarChart()
        preferencesHelper = PreferencesHelper(context as Activity)
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferencesHelper!!.getObject() != null) {
            var userData = preferencesHelper!!.getObject()
            userId = userData.getUserId()!!
        }
        if (preferences!!.getInt(PreferencesHelper.SETTINGS_PAGE_VALUE, 5) != 0) {
            pagevalue = preferences!!.getInt(PreferencesHelper.SETTINGS_PAGE_VALUE, 5)
            tvdaysvalue.setText("Last " + pagevalue.toString() + " days")
        }
        return designView
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause","onPause")
    }

    override fun onResume() {
        super.onResume()
        val bundle = this.arguments
        if (ConstantVariable.verifyAvailableNetwork(context as Activity)) {
            getExpenseDashboardData(bundle)
        } else {
            Toast.makeText(activity,"No Internet Connection", Toast.LENGTH_SHORT)
        }
    }

    private fun getExpenseDashboardData(bundle: Bundle?) {
        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var searchExpenseRequestModel = SearchExpenseRequestModel()
        searchExpenseRequestModel.setUser_Id(userId)
        searchExpenseRequestModel.setDays(pagevalue)

        val apiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<ExpenseDashBoardResponseModel> = apiInterface.getExpenseDashboardList(searchExpenseRequestModel)
        call.enqueue(object : Callback<ExpenseDashBoardResponseModel> {
            override fun onResponse(
                call: Call<ExpenseDashBoardResponseModel>?, response: Response<ExpenseDashBoardResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response?.body() != null) {
                    if (response?.body()?.getStatus().equals("success")) {
                        var alResult = (response?.body()?.getResult() as ArrayList<ExpenseDashBoardResult>?)!!
                        var alResult2 = (response?.body()?.getResult2() as ArrayList<Result2>?)!!

                        if (alResult.size > 0) {


                            tvPendingCount.setText(alResult.get(0).getCntPending().toString())
                            tvApprovedCount.setText(alResult.get(0).getCntApproved().toString())
                            tvRejectedCount.setText(alResult.get(0).getCntRejected().toString())
                            tvtotalcount.setText(alResult.get(0).getCntTot().toString())
                            tvtotalamount.setText(alResult.get(0).getAmtTot().toString())

                            tvPendingSum.setText(alResult.get(0).getSumPending().toString())
                            tvApprovedSum.setText(alResult.get(0).getSumApproved().toString())
                            tvRejectedSum.setText(alResult.get(0).getSumRejected().toString())
                        }
                        if (alResult2.size > 0) {
                            setBarChart(alResult2)
                        }

                        if (bundle != null && bundle.getString(ConstantVariable.homeMenu) != null) {
                            showBottomSheetDialog()
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ExpenseDashBoardResponseModel>?, t: Throwable?) {
              //  ConstantVariable.onSNACK(tvAllExpense, "Something went wrong")
                Toast.makeText(activity,"Something went wrong!!", Toast.LENGTH_SHORT).show()

                progressDialog!!.dismiss()
            }
        })
    }


    private fun findId() {
        recentOrderList = ArrayList<RecentOrderResult>()
        tvAllExpense = designView.findViewById<TextView>(R.id.tvAllExpense)
        tvNew = designView.findViewById<TextView>(R.id.tvNew)
//        chart = designView.findViewById<BarChart>(R.id.chart)
        tvdaysvalue = designView.findViewById<TextView>(R.id.tvdaysvalue)
        tvPendingCount = designView.findViewById<TextView>(R.id.tvPendingCount)
        tvApprovedCount = designView.findViewById<TextView>(R.id.tvApprovedCount)
        tvRejectedCount = designView.findViewById<TextView>(R.id.tvRejectedCount)
        tvtotalcount = designView.findViewById<TextView>(R.id.tvtotalcount)
        tvtotalamount = designView.findViewById<TextView>(R.id.tvtotalamount)
        tvPendingSum = designView.findViewById<TextView>(R.id.tvPendingSum)
        tvApprovedSum = designView.findViewById<TextView>(R.id.tvApprovedSum)
        tvRejectedSum = designView.findViewById<TextView>(R.id.tvRejectedSum)

        bottom_sheet = designView.findViewById(R.id.bottom_sheet)
        mBehavior = BottomSheetBehavior.from<View>(bottom_sheet)
    }


    private fun showBottomSheetDialog() {
        if (mBehavior!!.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }

        val view = layoutInflater.inflate(R.layout.fragment_bottom_sheet_dialog_full, null)
        var mBottomSheetDialog = BottomSheetDialog(activity!!)

        getListPending(view,mBottomSheetDialog)
        mBottomSheetDialog.setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow()!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        mBottomSheetDialog.show()
       /* mBottomSheetDialog.setOnDismissListener { mBottomSheetDialog = null }*/
    }

 fun   setAdapter(
     view: View,
     pendingExpenseList: ArrayList<ExpensePendingResponse.Result>,
     mBottomSheetDialog: BottomSheetDialog
 ) {
        var   rvList = view.findViewById<View>(R.id.rvList) as RecyclerView

        val recentCustomerList = ArrayList<CustomerResult>()
        rvList.layoutManager = LinearLayoutManager(activity)
        rvList.setHasFixedSize(true)
        var mAdapter = ExpensePendingAdapter(activity!!, pendingExpenseList, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                mBottomSheetDialog.dismiss()
                val bundle = Bundle()
                var expenseInfoFrag = ExpenseInfoFragment()
                bundle.putString(ConstantVariable.TAG_FROM, "from_notification_detail")
                bundle.putInt(ConstantVariable.TAG_OBJECT,position)
                expenseInfoFrag.setArguments(bundle)

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(com.crm.crmapp.R.id.fragment_container, expenseInfoFrag)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        })

        rvList.adapter = mAdapter

    }



    private fun getListPending(
        view: View,
        mBottomSheetDialog: BottomSheetDialog
    ) {

        var  pendingExpenseList: ArrayList<ExpensePendingResponse.Result>
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!,"Loading...")
        progressDialog!!.show()
        var apiInterface  = ApiClient.getClient().create(ApiInterface::class.java)
        var notificationRequest= RecentOrderRequest()

        val preferencesHelper = PreferencesHelper(this!!.activity!!)
        if(preferencesHelper!!.userId!=null) {
            notificationRequest?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        }
        var call : Call<ExpensePendingResponse>? = apiInterface!!.getListPendingExpense(notificationRequest)
        call?.enqueue(object : Callback<ExpensePendingResponse> {
            override fun onFailure(call: Call<ExpensePendingResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(activity,"Something went wrong!!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<ExpensePendingResponse>, response: Response<ExpensePendingResponse>) {
                progressDialog!!.dismiss()
                try {
                    pendingExpenseList = response.body()?.getResult() as java.util.ArrayList<ExpensePendingResponse.Result>

                    if (pendingExpenseList!!.size>0) {

                        setAdapter(view ,pendingExpenseList,mBottomSheetDialog)
                    }
                    else{

                        Toast.makeText(activity,"No Record Found", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {

                }
            }
        })
    }


    private fun setListener() {
        tvAllExpense.setOnClickListener(this)
        tvNew.setOnClickListener(this)
    }


    private fun randomColor(): Int {
        val rand = Random()
        val r = rand.nextInt(255)
        val g = rand.nextInt(255)
        val b = rand.nextInt(255)
        val randomColor = Color.rgb(r, g, b)
        return randomColor
    }


    private fun setBarChart(alResult2: ArrayList<Result2>) {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        val color = ArrayList<Int>()

        for (i: Int in alResult2.indices) {
            entries.add(BarEntry(alResult2.get(i).getAmt()!!.toFloat(), i))
            labels.add(alResult2.get(i).getExpDate()!!)

        }

        val barDataSet = BarDataSet(entries, "Amount")
        for (i: Int in entries.indices) {
            Log.e("color>>>>>", "" + randomColor())
            //  Color.rgb(color);
            color.add(randomColor())

        }


        var xAxis = chart?.xAxis
        //    xAxis?.mLabelWidth=5
        xAxis?.position = XAxis.XAxisPosition.BOTTOM

        chart?.axisRight?.isEnabled = false
        xAxis?.setDrawGridLines(false)
        barDataSet.setColors(color)

        val data = BarData(labels, barDataSet)
        chart?.data = data // set the data and list of lables into chart

        //    chart.setDescription("Set Bar Chart Description")  // set the description

        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)

        chart?.animateY(2000)
    }


    override fun onClick(p0: View?) {
        when (p0) {

            tvAllExpense -> {

                ConstantVariable.animationEffect(this!!.tvAllExpense!!, activity!!)
                //containerValue?.removeAllViews()
                var pickpowerfrag = AllExpenseFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, pickpowerfrag)
                    ?.addToBackStack(null)
                    ?.commit()



            }

            tvNew -> {
                ConstantVariable.animationEffect(this!!.tvNew!!, activity!!)
                val intent = Intent(activity, NewExpense::class.java)
                intent.putExtra(TAG_FROM, "new")
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        }
    }

}





