package com.crm.crmapp.expense.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.expense.activity.NewExpense
import com.crm.crmapp.expense.adapter.ExpenseAdapter
import com.crm.crmapp.expense.model.SearchExpenseRequestModel
import com.crm.crmapp.expense.model.SearchExpenseResponseModel
import com.crm.crmapp.expense.model.SearchExpenseResult
import com.crm.crmapp.order.activity.FilterActivity
import com.crm.crmapp.order.activity.SortActivity
import com.crm.crmapp.order.util.PaginationScrollListener
import com.crm.crmapp.order.util.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllExpenseFragment : Fragment(), View.OnClickListener, SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var viewOfLayout: View
    private lateinit var rvList: RecyclerView
    private lateinit var tvHeader: TextView
    private lateinit var llSort: LinearLayout
    private lateinit var llFilter: LinearLayout
    private lateinit var llNew: LinearLayout
    private lateinit var svExpense: androidx.appcompat.widget.SearchView
    var searchedValue: String = ""
    var progressDialog: ProgressDialog? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private val PAGE_START = 0
    private var isLoadingScroll = false
    private var isLastPageScroll = false
    private var TOTAL_PAGES = 10
    private var currentPage = PAGE_START
    private lateinit var alResult: ArrayList<SearchExpenseResult>
    private var userId: Int = 0
    private var preferencesHelper: PreferencesHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater?.inflate(com.crm.crmapp.R.layout.fragment_expense_list, container, false)
        alResult = ArrayList()
        findId()
        setListener()
        preferencesHelper = PreferencesHelper(context as Activity)

        if (preferencesHelper!!.getObject() != null) {
            var userData = preferencesHelper!!.getObject()
            userId = userData.getUserId()!!
        }

        if (ConstantVariable.verifyAvailableNetwork(context as Activity)) {
            getSearchExpenseData()
            ScrollListeners()
        } else {
            // getCustomerListFromDB()
        }
        return viewOfLayout
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
                        loadNextPage()
                    },
                    1000
                )
            }
        })
    }

    private fun loadNextPage() {
        progressDialog = ConstantVariable.showProgressDialog(requireActivity(), "Loading loadNextPage")
        var searchExpenseRequestModel = SearchExpenseRequestModel()
        searchExpenseRequestModel.setUserId(userId)
        searchExpenseRequestModel.setOffSet(currentPage)
        searchExpenseRequestModel.setNoRows(TOTAL_PAGES)
        searchExpenseRequestModel.setFilterText(searchedValue)
        val apiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<SearchExpenseResponseModel> = apiInterface.getSearchExpenseList(searchExpenseRequestModel)
        call.enqueue(object : Callback<SearchExpenseResponseModel> {
            override fun onResponse(
                call: Call<SearchExpenseResponseModel>?,
                response: Response<SearchExpenseResponseModel>
            ) {
                progressDialog!!.dismiss()
                isLastPageScroll = false
                isLoadingScroll = false
                if (response?.body() != null) {
                    if (response?.body()?.getStatus().equals("success")) {
                        alResult.addAll(response?.body()?.getResult()!!)!!
                        setRecyclerView()
                    }
                }
                if (currentPage >= TOTAL_PAGES) {
                    isLastPageScroll = false
                    isLoadingScroll = false
                } else {
                    isLastPageScroll = true
                    isLoadingScroll = true
                }
            }

            override fun onFailure(call: Call<SearchExpenseResponseModel>?, t: Throwable?) {
                ConstantVariable.onSNACK(rvList, "Something went wrong")
                progressDialog!!.dismiss()
            }
        })
    }

    private fun getSearchExpenseData() {
        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading..")
        var searchExpenseRequestModel = SearchExpenseRequestModel()
        searchExpenseRequestModel.setUserId(userId)
        searchExpenseRequestModel.setOffSet(currentPage)
        searchExpenseRequestModel.setNoRows(TOTAL_PAGES)
        searchExpenseRequestModel.setFilterText(searchedValue)

        val apiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<SearchExpenseResponseModel> = apiInterface.getSearchExpenseList(searchExpenseRequestModel)
        call.enqueue(object : Callback<SearchExpenseResponseModel> {
            override fun onResponse(
                call: Call<SearchExpenseResponseModel>?,
                response: Response<SearchExpenseResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response?.body() != null) {
                    if (response?.body()?.getStatus().equals("success")
                        && response?.body()?.getResult() != null
                    ) {
                        alResult = (response?.body()?.getResult() as ArrayList<SearchExpenseResult>?)!!

                        if (alResult.size>0) {
                            setRecyclerView()
                        }
                        else{
                            Toast.makeText(activity,"No Record Found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                if (currentPage <= TOTAL_PAGES)
                else isLastPageScroll = true;
            }

            override fun onFailure(call: Call<SearchExpenseResponseModel>?, t: Throwable?) {
               // ConstantVariable.onSNACK(rvList, "Something went wrong")
                Toast.makeText(activity,"Something went wrong!!", Toast.LENGTH_SHORT).show()
                progressDialog!!.dismiss()
            }
        })
    }

    private fun getSearchExpenseData2() {
        var searchExpenseRequestModel = SearchExpenseRequestModel()
        searchExpenseRequestModel.setUserId(userId)
        searchExpenseRequestModel.setOffSet(currentPage)
        searchExpenseRequestModel.setNoRows(TOTAL_PAGES)
        searchExpenseRequestModel.setFilterText(searchedValue)

        val apiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<SearchExpenseResponseModel> = apiInterface.getSearchExpenseList(searchExpenseRequestModel)
        call.enqueue(object : Callback<SearchExpenseResponseModel> {
            override fun onResponse(
                call: Call<SearchExpenseResponseModel>?,
                response: Response<SearchExpenseResponseModel>
            ) {
                if (response?.body() != null) {
                    if (response?.body()?.getStatus().equals("success")) {
                        alResult = (response?.body()?.getResult() as ArrayList<SearchExpenseResult>?)!!
                        setRecyclerView()
                    }
                }
                if (currentPage <= TOTAL_PAGES)
                else isLastPageScroll = true;
            }

            override fun onFailure(call: Call<SearchExpenseResponseModel>?, t: Throwable?) {
                ConstantVariable.onSNACK(rvList, "Something went wrong")
            }
        })
    }

    private fun setRecyclerView() {
        rvList.layoutManager = linearLayoutManager
        rvList.adapter = ExpenseAdapter(context, alResult, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                try {
                    val bundle = Bundle()
                    var expenseInfoFrag = ExpenseInfoFragment()
                    bundle.putString(ConstantVariable.TAG_FROM, "from_all_expense")
                    bundle.putSerializable(ConstantVariable.TAG_OBJECT, alResult.get(position))
                    expenseInfoFrag.setArguments(bundle)
                    svExpense.setQuery("", false)
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(com.crm.crmapp.R.id.fragment_container, expenseInfoFrag)
                        ?.addToBackStack(null)
                        ?.commit()
                } catch (e: NullPointerException) {
                }
            }
        })
    }

    private fun findId() {
        rvList = viewOfLayout.findViewById(com.crm.crmapp.R.id.rvList)
        tvHeader = viewOfLayout.findViewById(com.crm.crmapp.R.id.tvHeader)
        llFilter = viewOfLayout.findViewById(com.crm.crmapp.R.id.llFilter)
        llSort = viewOfLayout.findViewById(com.crm.crmapp.R.id.llSort)
        llNew = viewOfLayout.findViewById(com.crm.crmapp.R.id.llNew)
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        svExpense = viewOfLayout.findViewById(com.crm.crmapp.R.id.svExpense) as androidx.appcompat.widget.SearchView
        svExpense.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        searchedValue = p0!!
        if (!searchedValue.equals("") && searchedValue.length > 2) {
            getSearchExpenseData2()
        }
        return false
    }


    private fun setListener() {
        llFilter.setOnClickListener(this)
        llSort.setOnClickListener(this)
        llNew.setOnClickListener(this)
        svExpense.setOnClickListener(View.OnClickListener {
            svExpense.setIconified(false);
        })
    }

    override fun onClick(p0: View?) {
        when (p0) {
            llFilter -> {
                svExpense.setQuery("", false)
                startActivity(Intent(activity, FilterActivity::class.java))
                activity!!.overridePendingTransition(
                    com.crm.crmapp.R.anim.abc_fade_in,
                    com.crm.crmapp.R.anim.abc_fade_out
                );
                ConstantVariable.animationEffect(llFilter, activity!!)
            }
            llSort -> {
                svExpense.setQuery("", false)
                startActivity(Intent(activity, SortActivity::class.java))
                activity!!.overridePendingTransition(
                    com.crm.crmapp.R.anim.abc_fade_in,
                    com.crm.crmapp.R.anim.abc_fade_out
                );
                ConstantVariable.animationEffect(llSort, activity!!)
            }
            llNew -> {
                svExpense.setQuery("", false)
                ConstantVariable.animationEffect(llNew, activity!!)
                startActivity(Intent(activity, NewExpense::class.java))
                activity!!.overridePendingTransition(
                    com.crm.crmapp.R.anim.abc_fade_in,
                    com.crm.crmapp.R.anim.abc_fade_out
                );
            }

            /*     llSync->{
                     ConstantVariable.animationEffect(this!!.llSync!!, activity!!)
                     val intent = Intent(activity , OfflineOrderActivity::class.java)
                     startActivity(intent)
                     activity!!.overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
                 }*/
        }
    }
}