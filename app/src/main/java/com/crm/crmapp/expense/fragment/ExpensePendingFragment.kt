package com.crm.crmapp.expense.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.expense.adapter.ExpensePendingAdapter
import com.crm.crmapp.expense.model.ExpensePendingResponse
import com.crm.crmapp.order.model.RecentOrderRequest
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExpensePendingFragment : Fragment(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private lateinit var designView: View
    private lateinit var svPendingExpense: androidx.appcompat.widget.SearchView
    private lateinit var pendingExpenseList: ArrayList<ExpensePendingResponse.Result>

    //private lateinit var pendingExpenseList_Global: ArrayList<ExpensePendingResponse.Result>
    private lateinit var approvedExpenseList: ArrayList<ExpensePendingResponse.Result>

    //private lateinit var approvedExpenseList_Global: ArrayList<ExpensePendingResponse.Result>
    private lateinit var mAdapter: ExpensePendingAdapter
    private lateinit var mAdapter2: ExpensePendingAdapter
    private lateinit var llsearch: LinearLayout
    private lateinit var btnSearch: Button
    private lateinit var lastSearchdata: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pendingExpenseList = ArrayList();
        approvedExpenseList = ArrayList();
        lastSearchdata = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        designView = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_full, container, false)
        svPendingExpense =
            designView.findViewById(R.id.svCustomer_pending) as androidx.appcompat.widget.SearchView
        llsearch = designView.findViewById(R.id.llsearch) as LinearLayout
        btnSearch = designView.findViewById(R.id.btnSearch) as Button
        svPendingExpense.setOnQueryTextListener(this)
        svPendingExpense.setOnClickListener({
            svPendingExpense.setIconified(false)
        })
        val closeButton =
            svPendingExpense.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
        closeButton.setOnClickListener({
            svPendingExpense.setQuery("", false);
            svPendingExpense.clearFocus()
        })
        setAdapter(pendingExpenseList, approvedExpenseList)
        if (ConstantVariable.verifyAvailableNetwork(context as Activity)) {
            if (lastSearchdata.length > 0) {
                getListPending2(lastSearchdata)
            } else {
                getListPending2("")
            }
        } else {
            Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_SHORT)
        }
        btnSearch.setOnClickListener()
        {
            if (svPendingExpense.query.length != 0 && svPendingExpense.query.length > 2) {
                lastSearchdata = svPendingExpense.query.toString()
                getListPending2(svPendingExpense.query.toString())
            } else {
                ConstantVariable.onToast(context!!, "Please enter text first")
            }
        }
        return designView
    }

    fun setAdapter(
        pendingExpenseList: ArrayList<ExpensePendingResponse.Result>,
        approvedExpenseList: ArrayList<ExpensePendingResponse.Result>
    ) {
        var rvList = designView.findViewById<View>(R.id.rvList) as RecyclerView
        var rvList2 = designView.findViewById<View>(R.id.rvList2) as RecyclerView
        rvList.layoutManager = LinearLayoutManager(activity)
        rvList2.layoutManager = LinearLayoutManager(activity)
        //rvList.setHasFixedSize(true)
        //rvList.setNestedScrollingEnabled(false)
        //rvList2.setHasFixedSize(true)
        ///rvList2.setNestedScrollingEnabled(false)
        mAdapter = ExpensePendingAdapter(activity!!, pendingExpenseList, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                val bundle = Bundle()
                var expenseInfoFrag = ExpenseInfoFragment()
                bundle.putString(ConstantVariable.TAG_FROM, "from_notification_detail")
                bundle.putInt(ConstantVariable.TAG_OBJECT, position)
                expenseInfoFrag.setArguments(bundle)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, expenseInfoFrag)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        })
        mAdapter2 = ExpensePendingAdapter(activity!!, approvedExpenseList, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                val bundle = Bundle()
                var expenseInfoFrag = ExpenseInfoFragment()
                bundle.putString(ConstantVariable.TAG_FROM, "from_notification_detail")
                bundle.putInt(ConstantVariable.TAG_OBJECT, position)
                expenseInfoFrag.setArguments(bundle)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, expenseInfoFrag)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        })

        rvList.adapter = mAdapter
        rvList2.adapter = mAdapter2
    }

    private fun getListPending2(key: String?) {
        val progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var notificationRequest = RecentOrderRequest()
        val preferencesHelper = PreferencesHelper(this!!.activity!!)
        if (preferencesHelper!!.userId != null) {
            notificationRequest?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
            notificationRequest?.setSearch(key)
        }
        var call: Call<ExpensePendingResponse>? =
            apiInterface!!.getListPendingExpense(notificationRequest)
        call?.enqueue(object : Callback<ExpensePendingResponse> {
            override fun onFailure(call: Call<ExpensePendingResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ExpensePendingResponse>,
                response: Response<ExpensePendingResponse>
            ) {
                progressDialog!!.dismiss()
                try {
                    val pendingExpenseList_local =
                        response.body()?.getResult1() as ArrayList<ExpensePendingResponse.Result>
                    val approvedExpenseList_local =
                        response.body()?.getResult2() as ArrayList<ExpensePendingResponse.Result>

                    if (pendingExpenseList_local.size > 0) {
                        pendingExpenseList.clear()
                        pendingExpenseList.addAll(pendingExpenseList_local)
                        mAdapter.notifyDataSetChanged()
                        llsearch.visibility = View.VISIBLE
                    } else {
                        pendingExpenseList.clear()
                        mAdapter.notifyDataSetChanged()
                        llsearch.visibility = View.VISIBLE
                    }

                    if (approvedExpenseList_local.size > 0) {
                        approvedExpenseList.clear()
                        approvedExpenseList.addAll(approvedExpenseList_local)
                        mAdapter2.notifyDataSetChanged()
                        llsearch.visibility = View.VISIBLE
                    } else {
                        approvedExpenseList.clear()
                        mAdapter2.notifyDataSetChanged()
                        llsearch.visibility = View.VISIBLE
                    }

                    if (pendingExpenseList!!.size > 0 || approvedExpenseList!!.size > 0) {
                        val myTextView = designView.findViewById(R.id.txtpendingNoRecordfound) as TextView
                        if (pendingExpenseList!!.size == 0) {
                            myTextView.visibility = View.VISIBLE
                        } else {
                            myTextView.visibility = View.GONE
                        }
                    } else {
                        Toast.makeText(activity, "No Record Found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {

                }
            }
        })
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        if (p0.toString().length != 0 && p0.toString().length > 2) {
            lastSearchdata = p0.toString()
            getListPending2(p0.toString())
        } else {
            ConstantVariable.onToast(context!!, "Please enter text first")
        }
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        filter_by_api(p0)
        return false
    }


    fun filter(text: String?) {
        if (!text!!.isEmpty()) {
            if (pendingExpenseList.size > 0) {
                val temp = ArrayList<ExpensePendingResponse.Result>()
                for (d in pendingExpenseList) {
                    //or use .equal(text) with you want equal match
                    //use .toLowerCase() for better matches
                    if (d.getFullName()!!.toLowerCase().contains(text!!.toLowerCase())) {
                        temp.add(d)
                    } else if (d.getExpName()!!.toLowerCase().contains(text!!.toLowerCase())) {
                        temp.add(d)
                    }
                }
                if (temp.size > 0) {
                    mAdapter.setItemList(temp)
                }
            }
        }
    }

    fun filter2(text: String?) {
        if (!text!!.isEmpty()) {
            if (approvedExpenseList.size > 0) {
                val temp = ArrayList<ExpensePendingResponse.Result>()
                for (d in approvedExpenseList) {
                    //or use .equal(text) with you want equal match
                    //use .toLowerCase() for better matches
                    if (d.getFullName()!!.toLowerCase().contains(text!!.toLowerCase())) {
                        temp.add(d)
                    } else if (d.getExpName()!!.toLowerCase().contains(text!!.toLowerCase())) {
                        temp.add(d)
                    }
                }
                if (temp.size > 0) {
                    mAdapter2.setItemList(temp)
                }
            }
        }
    }

    fun filter_by_api(text: String?) {
        if (!text!!.isEmpty() && text!!.length > 2) {
            lastSearchdata = text
            filter(text)
            filter2(text)
        } else if (text!!.isEmpty()) {
            getListPending2("")
        }
    }

}