package com.crm.crmapp.order.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.util.ConstantVariable.Companion.TAG_FROM
import com.crm.crmapp.order.util.ConstantVariable.Companion.TAG_OBJECT
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.DataBase.CRMdatabase
import com.crm.crmapp.DataBase.NewCustomerDB
import com.crm.crmapp.DataBase.RecentCustomerTable
import com.crm.crmapp.order.adapter.SearchCustomerAdapter
import com.crm.crmapp.order.model.SearchCustomerModel
import com.crm.crmapp.order.model.SearchCustomerRequestModel
import com.crm.crmapp.order.model.SearchCustomerResultModel
import com.crm.crmapp.order.util.PaginationScrollListener
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchCustomer : AppCompatActivity(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener, View.OnClickListener {
    private lateinit var context: Context
    private lateinit var activity: Activity;
    private lateinit var rvSearchCustomer: RecyclerView
    private lateinit var tvApply: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvTitle: TextView
    private lateinit var alResult: ArrayList<SearchCustomerResultModel>
    private lateinit var searchCustomerRequestModel: SearchCustomerRequestModel
    private lateinit var svCustomer: androidx.appcompat.widget.SearchView
    var searchedValue: String = ""
    var from: String = ""
    var progressDialog: ProgressDialog? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var PAGE_START = 0
    private var isLoadingScroll = false
    private var isLastPageScroll = false
    private var TOTAL_PAGES = 10
    private var currentPage = PAGE_START
    private lateinit var databaseCustomerList: List<RecentCustomerTable>
    private lateinit var newCustomerList: List<NewCustomerDB>
    private var preferencesHelper: PreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_customer)
        alResult = ArrayList<SearchCustomerResultModel>()
        databaseCustomerList = ArrayList<RecentCustomerTable>()
        newCustomerList = ArrayList<NewCustomerDB>()
        context = this@SearchCustomer
        activity = this@SearchCustomer
        preferencesHelper = PreferencesHelper(context)
        findId()
        setToolBar()
        setListener()
        setRecyclerView()

        if (ConstantVariable.verifyAvailableNetwork(this@SearchCustomer)) {
            ScrollListeners()
            getSearchCustomerData()
        } else {
            getCustomerListFromDB()
        }
        if (intent.getStringExtra(ConstantVariable.TAG_FROM) != null) {
            from = intent.getStringExtra(ConstantVariable.TAG_FROM)
        }
    }

    private fun setToolBar() {
        tvApply.visibility = View.INVISIBLE
        tvTitle.setText("Search Customers")
    }

    private fun ScrollListeners() {
        rvSearchCustomer.addOnScrollListener(object :
            PaginationScrollListener(this!!.linearLayoutManager!!) {
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
                        loadNextPage()
                    },
                    1000
                )
            }
        })
    }

    //todo pagination of api

    private fun loadNextPage() {
        progressDialog = ConstantVariable.showProgressDialog(this@SearchCustomer, "Loading")
        searchCustomerRequestModel = SearchCustomerRequestModel()
        searchCustomerRequestModel.setNoRows(TOTAL_PAGES)
        searchCustomerRequestModel.setOffSet(currentPage)
        searchCustomerRequestModel.setSearchKey(searchedValue)
        searchCustomerRequestModel.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        val apiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<SearchCustomerModel> =
            apiInterface.getSearchCustomerList(searchCustomerRequestModel)
        call.enqueue(object : Callback<SearchCustomerModel> {
            override fun onResponse(
                call: Call<SearchCustomerModel>?,
                response: Response<SearchCustomerModel>
            ) {
                progressDialog!!.dismiss()
                isLastPageScroll = false
                isLoadingScroll = false
                if (response?.body() != null) {
                    if (response?.body()?.getStatus().equals("success")) {

                        if (from.equals("new_market_plan")) {
                            var list =
                                response?.body()?.getResult()!! as List<SearchCustomerResultModel>?
                            if (list!!.size > 0) {
                                for (searchobj: SearchCustomerResultModel in list) {
                                    if (searchobj.custType.equals("distributer", true)) {
                                        alResult.add(searchobj)!!
                                    }
                                }
                            }
                        } else {
                            alResult.addAll(response?.body()?.getResult()!!)!!
                        }

                        //  setRecyclerView()
                        rvSearchCustomer.adapter!!.notifyItemRangeInserted(
                            rvSearchCustomer.adapter!!.getItemCount(),
                            alResult.size
                        );

                        if (currentPage >= TOTAL_PAGES) {
                            isLastPageScroll = false
                            isLoadingScroll = false
                        } else {
                            isLastPageScroll = true
                            isLoadingScroll = true
                        }
                    }
                }

            }

            override fun onFailure(call: Call<SearchCustomerModel>?, t: Throwable?) {
                // ConstantVariable.onSNACK(tvApply, t.toString())
                progressDialog!!.dismiss()
            }
        })
    }

    private fun findId() {
        rvSearchCustomer = findViewById<RecyclerView>(R.id.rvSearchCustomer)
        tvApply = findViewById(R.id.tvApply) as TextView
        tvTitle = findViewById(R.id.tvTitle) as TextView
        tvCancel = findViewById(R.id.tvCancel) as TextView
        svCustomer = findViewById(R.id.svCustomer) as androidx.appcompat.widget.SearchView
        svCustomer.setOnQueryTextListener(this)

        linearLayoutManager = LinearLayoutManager(activity)
    }

    private fun setListener() {
        tvCancel.setOnClickListener(this)
        svCustomer.setOnClickListener({
            svCustomer.setIconified(false)
        })

        val closeButton =
            svCustomer.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView

        closeButton.setOnClickListener({
            PAGE_START = 0
            isLoadingScroll = false
            isLastPageScroll = false
            TOTAL_PAGES = 10
            currentPage = PAGE_START
            searchedValue = ""
            getSearchCustomerData()
            svCustomer.setQuery("", false);
            svCustomer.clearFocus()
        })
    }

    private fun setRecyclerView() {
        rvSearchCustomer.layoutManager = linearLayoutManager
        rvSearchCustomer.adapter = SearchCustomerAdapter(this, alResult, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                try {
                    if (intent != null && (intent.getStringExtra("NewOrderKey").equals(
                            "NewOrderKey",
                            ignoreCase = true
                        ) || intent.getStringExtra("NewOrderKey").equals(
                            "new_market_plan",
                            ignoreCase = true
                        )
                                || intent.getStringExtra("NewOrderKey").equals(
                            "NewOrderWithMarketPlanId",
                            ignoreCase = true
                        )
                                || intent.getStringExtra("NewOrderKey").equals(
                            "NewDistributorKey",
                            ignoreCase = true
                        ))
                    ) {
                        val intent = Intent(context, Main2Activity::class.java)
                        intent.putExtra("CustomerPosition", alResult.get(position).getCustName())
                        intent.putExtra("CustomerId", alResult.get(position).getCustId())
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        val intent = Intent(context, Main2Activity::class.java)
                        intent.putExtra(TAG_OBJECT, alResult.get(position))
                        intent.putExtra(TAG_FROM, "from_search_customer")
                        startActivity(intent)
                    }
                } catch (e: NullPointerException) {
                }
            }
        })


    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        searchedValue = p0!!

        PAGE_START = 0
        isLoadingScroll = false
        isLastPageScroll = false
        TOTAL_PAGES = 10
        currentPage = PAGE_START

        getSearchCustomerData()

        return false
    }

    // todo api to get customer list and search api
    private fun getSearchCustomerData() {
        //alResult.clear()
        val apiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        searchCustomerRequestModel = SearchCustomerRequestModel()
        searchCustomerRequestModel.setNoRows(TOTAL_PAGES)
        searchCustomerRequestModel.setOffSet(currentPage)
        searchCustomerRequestModel.setSearchKey(searchedValue)
        searchCustomerRequestModel.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        if (intent != null && intent.getStringExtra("NewOrderKey") != null && (intent.getStringExtra(
                "NewOrderKey"
            ).equals(
                "new_market_plan"
            ))
        ) {
            var state_code = intent.getStringExtra(ConstantVariable.TAG_STATE_CODE);
            searchCustomerRequestModel.setStateCode(state_code)
        }

        val call: Call<SearchCustomerModel>

        if (intent != null && intent.getStringExtra("NewOrderKey") != null && intent.getStringExtra(
                "NewOrderKey"
            ).equals(
                "NewOrderWithMarketPlanId",
                ignoreCase = true
            )
        ) {
            var marketPlanId = Integer.parseInt(intent.getStringExtra("marketPlanId"))
            searchCustomerRequestModel.setMPId(marketPlanId)
            call = apiInterface.getCustomerSearchMP(searchCustomerRequestModel)
        } else if (intent != null && intent.getStringExtra("NewOrderKey") != null && (intent.getStringExtra(
                "NewOrderKey"
            ).equals(
                "new_market_plan"
            ))
        ) {
            call =
                apiInterface.getCustomerSearchMPbyState(searchCustomerRequestModel) // commented because there is not Api for this on 16-06-2020
            //searchCustomerRequestModel.setMPId(0)
            //call = apiInterface.getCustomerSearchMP(searchCustomerRequestModel)
        } else {
            call = apiInterface.getSearchCustomerList(searchCustomerRequestModel)
        }

        call.enqueue(object : Callback<SearchCustomerModel> {
            override fun onResponse(
                call: Call<SearchCustomerModel>?,
                response: Response<SearchCustomerModel>
            ) {
                if (response?.body() != null) {
                    if (response?.body()?.getStatus().equals("success")) {
                        alResult.clear()
                        if (from.equals("new_market_plan")) {
                            var list =
                                response?.body()?.getResult()!! as List<SearchCustomerResultModel>?
                            if (list!!.size > 0) {
                                for (searchobj: SearchCustomerResultModel in list) {
                                    if (searchobj.custType.equals("distributer", true)) {
                                        alResult.add(searchobj)!!
                                    }
                                }
                            }
                        } else {
                            alResult.addAll(response?.body()?.getResult()!!)!!
                        }

                        if (alResult.size > 0) {

                            setRecyclerView()
                            if (currentPage <= TOTAL_PAGES) {
                                isLastPageScroll = false
                            } else {
                                isLastPageScroll = true
                            }
                        } else {
                            Toast.makeText(activity, "No Record Found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

            override fun onFailure(call: Call<SearchCustomerModel>?, t: Throwable?) {
                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                // progressDialog!!.dismiss()
            }
        })
    }


    private fun getSearchCustomerData2() {
        searchCustomerRequestModel = SearchCustomerRequestModel()
        searchCustomerRequestModel.setNoRows(TOTAL_PAGES)
        searchCustomerRequestModel.setOffSet(currentPage)
        searchCustomerRequestModel.setSearchKey(searchedValue)
        val apiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<SearchCustomerModel> =
            apiInterface.getSearchCustomerList(searchCustomerRequestModel)
        call.enqueue(object : Callback<SearchCustomerModel> {
            override fun onResponse(
                call: Call<SearchCustomerModel>?,
                response: Response<SearchCustomerModel>
            ) {
                if (response?.body() != null) {
                    if (response?.body()?.getStatus().equals("success")) {
                        alResult = (response?.body()
                            ?.getResult() as ArrayList<SearchCustomerResultModel>?)!!
                        setRecyclerView()
                        if (currentPage <= TOTAL_PAGES) {
                            isLastPageScroll = false
                        } else {
                            isLastPageScroll = true
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SearchCustomerModel>?, t: Throwable?) {
                ConstantVariable.onSNACK(tvCancel, "Something went wrong")
            }
        })
    }


    override fun onClick(v: View?) {
        when (v) {
            tvCancel -> {
                ConstantVariable.animationEffect(tvCancel, this!!)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish()
            }
        }
    }

    //TODO CUSTOMER LIST FROM DB WHEN NO INTERNET CONNECTION
    private fun getCustomerListFromDB() {
        alResult.clear()
        progressDialog = ConstantVariable.showProgressDialog(this@SearchCustomer, "Loading")
        var mDb = CRMdatabase.getInstance(this)
        val t = Thread {
            var list = mDb!!.crmDoa().allCustomer() as List<NewCustomerDB>
            if (list.size > 0) {

                if (from.equals("new_market_plan")) {

                    for (item: Int in list.indices) {
                        if (list.get(item).cust_category.equals("Distributer", true)) {
                            val result1 = SearchCustomerResultModel()
                            result1.setCustName(list.get(item).cust_name)
                            result1.setCustId(0)
                            result1.setGrpCustName(list.get(item).cust_category)
                            alResult.add(result1)
                        }
                    }

                } else {
                    for (item: Int in list.indices) {
                        val result1 = SearchCustomerResultModel()
                        result1.setCustName(list.get(item).cust_name)
                        result1.setCustId(0)
                        result1.setGrpCustName(list.get(item).cust_category)
                        alResult.add(result1)
                        //newCustomerList.(list[item])
                    }
                }
            }
        }
        t.start()
        t.join()

        val t2 = Thread {
            var list = mDb!!.crmDoa().allRecentCustomer() as List<NewCustomerDB>
            if (list.size > 0) {
                if (from.equals("new_market_plan")) {

                    for (item: Int in list.indices) {
                        if (list.get(item).cust_category.equals("Distributer", true)) {
                            val result1 = SearchCustomerResultModel()
                            result1.setCustName(list.get(item).cust_name)
                            result1.setCustId(0)
                            result1.setGrpCustName(list.get(item).cust_category)
                            alResult.add(result1)
                        }
                    }
                } else {
                    for (item: Int in list.indices) {
                        val result1 = SearchCustomerResultModel()
                        result1.setCustName(list.get(item).cust_name)
                        result1.setCustId(0)
                        result1.setGrpCustName(list.get(item).cust_category)
                        alResult.add(result1)
                        //newCustomerList.(list[item])
                    }
                }
            } else {
                progressDialog!!.dismiss()
            }
        }
        t2.start()
        t2.join()

        setRecyclerView()
        progressDialog!!.dismiss()
    }
}
