package com.crm.crmapp.order.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.DataBase.CRMdatabase
import com.crm.crmapp.order.activity.*
import com.crm.crmapp.order.adapter.OrderAdapter
import com.crm.crmapp.order.model.OrderAllModel
import com.crm.crmapp.order.model.OrderListRequest
import com.crm.crmapp.order.model.Result
import com.crm.crmapp.order.util.PaginationScrollListener
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllOrderListFragment : Fragment(), View.OnClickListener {
    private lateinit var viewOfLayout: View
    private lateinit var rvList: RecyclerView
    private lateinit var tvHeader: TextView
    private lateinit var tvNoData: TextView
    private lateinit var llFilter: LinearLayout
    private lateinit var llSort: LinearLayout
    private lateinit var llNew: LinearLayout
    private lateinit var llSync: LinearLayout
    private lateinit var orderAllModelList: ArrayList<Result>
    private var linearLayoutManager: LinearLayoutManager? = null
    private val PAGE_START = 0
    private var isLoadingScroll = false
    private var isLastPageScroll = false
    private var TOTAL_PAGES = 10
    private var currentPage = PAGE_START

    var progressDialog: ProgressDialog? = null
    var preferencesHelper: PreferencesHelper? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater?.inflate(R.layout.fragment_allorder_list, container, false)
        findId()
        setListener()
        return viewOfLayout
    }

    private fun checkOffLineData() {
        var mDb = CRMdatabase.getInstance(this!!.activity!!)
        if (mDb != null) {
            AsyncTask.execute {
                try {
                    if (mDb!!.crmDoa().allOrder().size > 0) {
                        llSync.visibility = View.VISIBLE
                    } else {
                        llSync.visibility = View.GONE
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun setListener() {
        llFilter.setOnClickListener(this)
        llSort.setOnClickListener(this)
        llNew.setOnClickListener(this)
        llSync.setOnClickListener(this)
    }

    private fun findId() {
        orderAllModelList = ArrayList<Result>()
        rvList = viewOfLayout.findViewById(R.id.rvList)
        llSync = viewOfLayout.findViewById<LinearLayout>(R.id.llSync)
        llFilter = viewOfLayout.findViewById(R.id.llFilter)
        llSort = viewOfLayout.findViewById(R.id.llSort)
        llNew = viewOfLayout.findViewById(R.id.llNew)
        tvHeader = viewOfLayout.findViewById(R.id.tvHeader)
        tvNoData = viewOfLayout.findViewById(R.id.tvNoData)
        preferencesHelper = PreferencesHelper(this!!.activity!!)
        linearLayoutManager = LinearLayoutManager(activity)
    }

    private fun setAdapter() {
        rvList.layoutManager = linearLayoutManager
        rvList.adapter = OrderAdapter(context, orderAllModelList, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                val intent = Intent(activity, OrderDetailActivity::class.java)
                preferencesHelper!!.orderId = (position.toString())
                intent.putExtra(ConstantVariable.online_order_id, position)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
        })
    }

    private fun ScrollListeners() {

        rvList.addOnScrollListener(object : PaginationScrollListener(this!!.linearLayoutManager!!) {
            override val totalPageCount: Int
                get() = TOTAL_PAGES
            override val isLastPage: Boolean
                get() = isLastPageScroll
            override val isLoading: Boolean
                get() = isLoadingScroll

            override fun loadMoreItems() {
                isLoadingScroll = true
                currentPage = currentPage + TOTAL_PAGES
                Handler().postDelayed(
                    {

                        try {
                            if (ConstantVariable.verifyAvailableNetwork(activity as AppCompatActivity)) {
                                loadNextPage()
                            } else {
                                ConstantVariable.onSNACK(tvNoData!!, "No Internet Connection")
                            }

                        } catch (e: Exception) {
                            ConstantVariable.onSNACK(tvNoData, "Oops something went wrong !!")
                        }

                    },
                    1000
                )
            }
        })
    }

    private fun loadNextPage() {
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var orderLisrRequest = OrderListRequest()
        orderLisrRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull())

        if (preferencesHelper!!.bySort.equals("", ignoreCase = true)) {
            orderLisrRequest.setColumnName("order_date")
            orderLisrRequest.setOrderByType("Asc")
        } else {
            orderLisrRequest.setColumnName(preferencesHelper!!.bySort!!)
            orderLisrRequest.setOrderByType(preferencesHelper!!.typeOfSort!!)
        }

        if (preferencesHelper!!.filterValue.equals("", ignoreCase = true)) {
            orderLisrRequest.setFilterText("1")
        } else {
            orderLisrRequest.setFilterText(preferencesHelper!!.filterValue!!)
        }
        orderLisrRequest.setOffSet(currentPage)
        orderLisrRequest.setNoRows(TOTAL_PAGES)
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<OrderAllModel>? = apiInterface!!.getOrderList(orderLisrRequest)

        call!!.enqueue(object : Callback<OrderAllModel> {
            override fun onFailure(call: Call<OrderAllModel>, t: Throwable) {
                progressDialog!!.dismiss()
                // ConstantVariable.onSNACK(tvNoData, "Something went wrong")
                tvNoData.visibility = View.GONE
                rvList.visibility = View.VISIBLE
            }

            override fun onResponse(call: Call<OrderAllModel>, response: Response<OrderAllModel>) {
                progressDialog!!.dismiss()
                tvNoData.visibility = View.GONE
                rvList.visibility = View.VISIBLE
                isLastPageScroll = false
                isLoadingScroll = false

                if (response.body() != null) {
                    orderAllModelList.addAll(response.body()?.getResult() as ArrayList<Result>)

                    setAdapter()

                    if (currentPage >= TOTAL_PAGES) {
                        isLastPageScroll = false
                        isLoadingScroll = false
                    } else {
                        isLastPageScroll = true
                        isLoadingScroll = true
                    }
                }


            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0) {
            llFilter -> {
                startActivity(Intent(activity, FilterActivity::class.java))
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                ConstantVariable.animationEffect(llFilter, activity!!)
            }
            llSort -> {
                startActivity(Intent(activity, SortActivity::class.java))
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                ConstantVariable.animationEffect(llSort, activity!!)

            }
            llNew -> {

                ConstantVariable.animationEffect(llNew, activity!!)
                startActivity(Intent(activity, NewOrderActivity::class.java))
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

            llSync -> {
                ConstantVariable.animationEffect(this!!.llSync!!, activity!!)
                val intent = Intent(activity, OfflineOrderActivity::class.java)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        }
    }

    private fun getOrderList() {
        orderAllModelList.clear()
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var orderLisrRequest = OrderListRequest()
        orderLisrRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull())

        if (preferencesHelper!!.bySort.equals("", ignoreCase = true)) {
            orderLisrRequest.setColumnName("order_date")
            orderLisrRequest.setOrderByType("Desc")
        } else {
            orderLisrRequest.setColumnName(preferencesHelper!!.bySort!!)
            orderLisrRequest.setOrderByType(preferencesHelper!!.typeOfSort!!)
        }
        if (preferencesHelper!!.filterValue.equals("", ignoreCase = true)) {
            orderLisrRequest.setFilterText("1")
        } else {
            orderLisrRequest.setFilterText(preferencesHelper!!.filterValue!!)
        }
        orderLisrRequest.setOffSet(currentPage)
        orderLisrRequest.setNoRows(TOTAL_PAGES)
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<OrderAllModel>? = apiInterface!!.getOrderList(orderLisrRequest)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<OrderAllModel> {
            override fun onFailure(call: Call<OrderAllModel>, t: Throwable) {
                progressDialog!!.dismiss()

                tvNoData.visibility = View.VISIBLE
                rvList.visibility = View.GONE
            }

            override fun onResponse(call: Call<OrderAllModel>, response: Response<OrderAllModel>) {
                progressDialog!!.dismiss()
                tvNoData.visibility = View.GONE
                rvList.visibility = View.VISIBLE

                if (response.body() != null) {
                    orderAllModelList = response.body()?.getResult() as ArrayList<Result>
                    if (orderAllModelList.size > 0) {

                        setAdapter()
                        if (currentPage <= TOTAL_PAGES) {
                            isLastPageScroll = false
                        } else {
                            isLastPageScroll = true
                        }
                    } else {

                        tvNoData.visibility = View.VISIBLE
                        rvList.visibility = View.GONE

                        /*ConstantVariable.onSNACK(tvNoData, response.body()!!.getMessage()!!)*/

                        Toast.makeText(activity, response.body()!!.getMessage()!!, Toast.LENGTH_SHORT)
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.e("onResume", "onResume")
        findId()
        checkOffLineData();
        ScrollListeners()
        try {
            if (ConstantVariable.verifyAvailableNetwork(activity as AppCompatActivity)) {
                getOrderList()
            } else {
                ConstantVariable.onSNACK(tvNoData, "Check your internet connection !!")
            }
        } catch (e: Exception) {
            ConstantVariable.onSNACK(tvNoData, "Oops something went wrong !!")
        }
    }
}
