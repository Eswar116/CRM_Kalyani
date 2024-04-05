package com.crm.crmapp.order.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.customer.model.Orders
import com.crm.crmapp.DataBase.CRMdatabase
import com.crm.crmapp.expense.model.SearchExpenseRequestModel
import com.crm.crmapp.order.activity.NewOrderActivity
import com.crm.crmapp.order.activity.OfflineOrderActivity
import com.crm.crmapp.order.activity.OrderDetailActivity
import com.crm.crmapp.order.adapter.RecentOrderAdapter
import com.crm.crmapp.order.adapter.RecentOrderGridAdapter
import com.crm.crmapp.order.model.*
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.order.util.doAsync
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecentOrderFragment : Fragment(), View.OnClickListener, onItemClick {
    override fun getPositionOfList(position: Int) {
    }

    private lateinit var tvAllOrder: TextView
    private lateinit var tvlastdays: TextView
    private lateinit var llSync: LinearLayout
    private lateinit var tvNew: TextView
    private lateinit var txt_syncBtn: TextView
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var txt_sync: TextView
    private lateinit var txt_close: TextView
    private lateinit var btn_sync: Button
    private lateinit var txt_lastsync: TextView
    private lateinit var designView: View
    private lateinit var rvRecentOrder: RecyclerView
    private lateinit var rvRecentOrderGrid: RecyclerView
    private lateinit var recentOrderGridAdapter: RecentOrderGridAdapter
    private lateinit var recentOrderAdapter: RecentOrderAdapter
    private lateinit var progressDialog: ProgressDialog
    private lateinit var recentOrderList: ArrayList<RecentOrderResult>
    private lateinit var orderDashboardList: ArrayList<OrderDashboardResult>
    private var preferencesHelper: PreferencesHelper? = null
    var containerValue: ViewGroup? = null
    var preferences: SharedPreferences? = null
    private var pagevalue: Int = 0
    private var isShown: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        designView = inflater.inflate(R.layout.fragment_recent_order, container, false)
        containerValue = container
        findId()
        initVariable()
        setListener()
        slideDown(bottomNavView)
        // setRecyclerView()
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences!!.getInt(PreferencesHelper.SETTINGS_PAGE_VALUE, 5) != 0) {
            pagevalue = preferences!!.getInt(PreferencesHelper.SETTINGS_PAGE_VALUE, 5)
            tvlastdays.setText("Last $pagevalue days")
            var date = preferences!!.getString(ConstantVariable.TAG_LAST_SYNC, "");
            txt_lastsync.setText(
                "Last Sync :" + date
            )
        }
        return designView
    }

    private fun findId() {
//        preferencesHelper = PreferencesHelper(this.activity!!)
        preferencesHelper = PreferencesHelper(this.requireActivity())
        recentOrderList = ArrayList<RecentOrderResult>()
        orderDashboardList = ArrayList<OrderDashboardResult>()
        tvAllOrder = designView.findViewById<TextView>(R.id.tvAllOrder)
        tvNew = designView.findViewById<TextView>(R.id.tvNew)
        llSync = designView.findViewById<LinearLayout>(R.id.llSync)
        rvRecentOrder = designView.findViewById<RecyclerView>(R.id.rvRecentOrder)
        rvRecentOrderGrid = designView.findViewById<RecyclerView>(R.id.rvGrid)
        tvlastdays = designView.findViewById<TextView>(R.id.tvlastdays)
        txt_lastsync = designView.findViewById<TextView>(R.id.txt_lastsync)

        txt_syncBtn = designView.findViewById<TextView>(R.id.txt_syncBtn)
        bottomNavView = designView.findViewById<BottomNavigationView>(R.id.bottomNavView)
        txt_sync = designView.findViewById<TextView>(R.id.txt_sync)
        txt_close = designView.findViewById<TextView>(R.id.txt_close)
        btn_sync = designView.findViewById<Button>(R.id.btn_sync)
    }

    private fun initVariable() {
    }

    //   TODO CHECK OFFLINE DATA
    private fun checkOffLineData() {
        doAsync {
            try {
                var mDb = CRMdatabase.getInstance(this!!.activity!!)
                // do work here ...
                if (mDb != null) {

                    if (mDb!!.crmDoa().allOrder().size > 0 || mDb!!.crmDoa().allCustomer().size > 0) {
                        activity!!.runOnUiThread {
                            llSync.visibility = View.VISIBLE
                        }
                    } else {
                        activity!!.runOnUiThread {
                            llSync.visibility = View.GONE
                        }
                    }
                }
            } catch (e: Exception) {
                // checkOffLineData()
                Log.e("exception", "e" + e.message)
            }
        }
    }

    private fun setListener() {
        tvAllOrder.setOnClickListener(this)
        tvNew.setOnClickListener(this)
        llSync.setOnClickListener(this)
        txt_syncBtn.setOnClickListener(this)
        txt_close.setOnClickListener(this)
        btn_sync.setOnClickListener(this)
    }

    private fun setRecyclerView() {
        rvRecentOrder.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recentOrderAdapter = RecentOrderAdapter(recentOrderList, context, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                val intent = Intent(activity, OrderDetailActivity::class.java)
                preferencesHelper!!.orderId = (position.toString())
                intent.putExtra(ConstantVariable.online_order_id, position)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        })
        rvRecentOrder.adapter = recentOrderAdapter
    }

    private fun setGridRecyclerView() {
        rvRecentOrderGrid.layoutManager = GridLayoutManager(context, 2)
        recentOrderGridAdapter = RecentOrderGridAdapter(context, orderDashboardList)
        rvRecentOrderGrid.adapter = recentOrderGridAdapter
    }

    override fun onClick(data: View?) {
        when (data) {
            tvAllOrder -> {
                ConstantVariable.animationEffect(this!!.tvAllOrder!!, activity!!)
                var pickpowerfrag = AllOrderListFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, pickpowerfrag)
                    ?.addToBackStack(null)
                    ?.commit()
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
            tvNew -> {
                ConstantVariable.animationEffect(this!!.tvNew!!, activity!!)
                val intent = Intent(activity, NewOrderActivity::class.java)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
            llSync -> {
                ConstantVariable.animationEffect(this!!.llSync!!, activity!!)
                val intent = Intent(activity, OfflineOrderActivity::class.java)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
            txt_syncBtn -> {
                ConstantVariable.animationEffect(this!!.llSync!!, activity!!)
                if (!isShown) {
                    isShown = true
                    slideUp(bottomNavView);
                }
            }
            txt_close -> {
                ConstantVariable.animationEffect(this!!.llSync!!, activity!!)
                if (isShown) {
                    isShown = false
                    slideDown(bottomNavView);
                }
            }
            btn_sync -> {
                ConstantVariable.animationEffect(this!!.llSync!!, activity!!)
                syncDataWithERP()
                slideDown(bottomNavView);
            }

        }
    }

    private fun slideUp(child: BottomNavigationView) {
        child.clearAnimation()
        child.visibility = View.VISIBLE
        child.animate().translationY(0f).duration = 200
    }

    private fun slideDown(child: BottomNavigationView) {
        child.clearAnimation()
        child.visibility = View.GONE
        child.animate().translationY(child.height.toFloat()).duration = 200
    }


    override fun onResume() {
        Log.e("onResume", "onResume")
        super.onResume()
        findId()
        checkOffLineData()
        if (ConstantVariable.verifyAvailableNetwork(activity as AppCompatActivity)) {
            getApiRecentOrder()
            getSalesOrderDashboardData()
        } else {
            ConstantVariable.onSNACK(tvAllOrder!!, "No Internet Connection")
        }
    }

//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//    }


    private fun getApiRecentOrder() {
        recentOrderList.clear()
        progressDialog = ConstantVariable.showProgressDialog(activity!!, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var recentOrderRequest = RecentOrderRequest()
        recentOrderRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull())

        var call: Call<RecentOrderResponse>? = apiInterface!!.recentOrderApi(recentOrderRequest)
        call?.enqueue(object : Callback<RecentOrderResponse> {
            override fun onFailure(call: Call<RecentOrderResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                //   ConstantVariable.onSNACK(tvNew,"Something went wrong")
                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<RecentOrderResponse>, response: Response<RecentOrderResponse>) {
                progressDialog!!.dismiss()
                try {
                    if (response.body() != null)
                        if (response.body()?.getStatus().equals("success", ignoreCase = true)) {
                            recentOrderList = (response.body()?.getResult() as ArrayList<RecentOrderResult>)

                            if (recentOrderList.size > 0) {
                                setRecyclerView()
                            } else {
                                Toast.makeText(activity, response.body()!!.getMessage()!!, Toast.LENGTH_SHORT)
                            }
                        } else {
                            Toast.makeText(activity, response.body()!!.getMessage()!!, Toast.LENGTH_SHORT)
                        }
                } catch (e: Exception) {

                }
            }
        })
    }

    private fun getSalesOrderDashboardData() {
        orderDashboardList.clear()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var searchExpenseRequestModel = SearchExpenseRequestModel()
        searchExpenseRequestModel.setUser_Id(preferencesHelper!!.userId!!.toIntOrNull())

        var call: Call<OrderDashboardResponseModel>? = apiInterface!!.getOrderDashboardData(searchExpenseRequestModel)
        call?.enqueue(object : Callback<OrderDashboardResponseModel> {
            override fun onFailure(call: Call<OrderDashboardResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                //   ConstantVariable.onSNACK(tvNew,"Something went wrong")
                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OrderDashboardResponseModel>,
                response: Response<OrderDashboardResponseModel>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response.body() != null)
                        if (response.body()?.getStatus().equals("success", ignoreCase = true)) {
                            orderDashboardList = (response.body()?.getResult() as ArrayList<OrderDashboardResult>)
                            if (orderDashboardList.size > 0) {
                                setGridRecyclerView()
                            } else {
                                Toast.makeText(activity, response.body()!!.getMessage()!!, Toast.LENGTH_SHORT)
                            }
                        } else {
                            Toast.makeText(activity, response.body()!!.getMessage()!!, Toast.LENGTH_SHORT)
                        }
                } catch (e: Exception) {

                }
            }
        })
    }

    private fun syncDataWithERP() {
        progressDialog.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var recentOrderRequest = RecentOrderRequest()
        recentOrderRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull())
        var call: Call<Orders>? = apiInterface!!.getSyncDataWithERP(recentOrderRequest)
        call?.enqueue(object : Callback<Orders> {
            override fun onFailure(call: Call<Orders>, t: Throwable) {
                progressDialog!!.dismiss()
                //   ConstantVariable.onSNACK(tvNew,"Something went wrong")
                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Orders>,
                response: Response<Orders>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response.body() != null)
                        if (response.body()?.getStatus().equals("success", ignoreCase = true)) {
                            var editor = preferences!!.edit()
                            var date = ConstantVariable.defaultTimeWithDate(txt_lastsync, context!!)
                            editor.putString(ConstantVariable.TAG_LAST_SYNC, date);
                            txt_lastsync.setText(
                                "Last Sync :" + date
                            )
                            editor.commit();
                            getApiRecentOrder()
                            getSalesOrderDashboardData()
                        } else {
                            var date = preferences!!.getString(ConstantVariable.TAG_LAST_SYNC, "");
                            txt_lastsync.setText(
                                "Last Sync :" + date
                            )
                            Toast.makeText(activity, response.body()!!.getMessage()!!, Toast.LENGTH_SHORT)
                        }
                } catch (e: Exception) {

                }
            }
        })
    }


}
