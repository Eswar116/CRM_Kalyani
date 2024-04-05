package com.crm.crmapp.competition.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.competition.adapter.AllCompetitorListAdapter
import com.crm.crmapp.competition.model.CompetitionListResponseModel
import com.crm.crmapp.competition.model.CompetitionListResult
import com.crm.crmapp.complaint.model.ComplaintReqest
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.order.util.PaginationScrollListener
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AllCompetitionFragment : Fragment(), View.OnClickListener,
    SearchView.OnQueryTextListener {
    private lateinit var intent: Intent
    private lateinit var viewOfLayout: View
    private lateinit var rvList: RecyclerView
    private var tvNew: TextView? = null
    private var svCustomer: SearchView? = null
    private lateinit var progressDialog: ProgressDialog
    private lateinit var competitorResultList: ArrayList<CompetitionListResult>
    var linearLayoutManager: LinearLayoutManager? = null
    private val PAGE_START = 0
    private var isLoadingScroll = false
    private var isLastPageScroll = false
    private var TOTAL_PAGES = 10
    private var currentPage = PAGE_START
    var searchedValue: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater?.inflate(R.layout.fragment_all_competition_list, container, false)
        return viewOfLayout
    }

    private fun findId() {
        rvList = viewOfLayout.findViewById(R.id.rvList)
        tvNew = viewOfLayout.findViewById(R.id.tvNew)
        svCustomer =
            viewOfLayout.findViewById(R.id.svCustomer) as SearchView

    }

    private fun setListener() {
        tvNew?.setOnClickListener(this)
        svCustomer?.setOnQueryTextListener(this)
        svCustomer?.setOnClickListener(View.OnClickListener {
            svCustomer?.setIconified(false);
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
                        loadNextPage()

                    },
                    1000
                )
            }
        })
    }

    private fun setRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvList.layoutManager = linearLayoutManager
        rvList.adapter =
            AllCompetitorListAdapter(context!!, competitorResultList, object : onItemClick {
                override fun getPositionOfList(position: Int) {
                    val intent = Intent(activity, CompetitionDetailActivity::class.java)
                    intent.putExtra(ConstantVariable.TAG_FROM, "edit")
                    intent.putExtra(ConstantVariable.TAG_OBJECT, competitorResultList.get(position))
                    startActivity(intent)
                    activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }

            })
    }

    override fun onClick(data: View?) {
        when (data) {
            tvNew -> {
                ConstantVariable.animationEffect(this!!.tvNew!!, activity!!)
                intent = Intent(activity, CompetitionDetailActivity::class.java);
                intent.putExtra(ConstantVariable.TAG_FROM, "new")
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        }
    }

    private fun getCompetitorListApi() {
        competitorResultList.clear()
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var complaintRequest = ComplaintReqest()
        val preferencesHelper = PreferencesHelper(this!!.activity!!)
        complaintRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        complaintRequest.setFilterText(searchedValue)
        complaintRequest.setNoRows(TOTAL_PAGES)
        complaintRequest.setOffSet(currentPage)

        var call: Call<CompetitionListResponseModel>? =
            apiInterface!!.getCompetitionList(complaintRequest)
        call?.enqueue(object : Callback<CompetitionListResponseModel> {
            override fun onFailure(call: Call<CompetitionListResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                // tvNew?.let { ConstantVariable.onSNACK(it, "NO Record Found") }

                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CompetitionListResponseModel>,
                response: Response<CompetitionListResponseModel>
            ) {
                progressDialog!!.dismiss()

                isLastPageScroll = false
                isLoadingScroll = false
                try {

                    competitorResultList =
                        response.body()?.getResult() as ArrayList<CompetitionListResult>
                    if (competitorResultList.size > 0) {
                        setRecyclerView()
                    } else {
                        Toast.makeText(activity, "No Record Found", Toast.LENGTH_SHORT).show()

                    }
                } catch (e: Exception) {
                    // tvNew?.let { ConstantVariable.onSNACK(it, "NO Record Found") }
                    progressDialog!!.dismiss()
                }
                if (currentPage >= TOTAL_PAGES) {
                    isLastPageScroll = false
                    isLoadingScroll = false
                } else {
                    isLastPageScroll = true
                    isLoadingScroll = true
                }
            }
        })
    }

    private fun getCompetitorListApi2() {
        competitorResultList.clear()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var complaintRequest = ComplaintReqest()
        val preferencesHelper = PreferencesHelper(this!!.activity!!)
        complaintRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        complaintRequest.setFilterText(searchedValue)
        complaintRequest.setNoRows(TOTAL_PAGES)
        complaintRequest.setOffSet(currentPage)

        var call: Call<CompetitionListResponseModel>? =
            apiInterface!!.getCompetitionList(complaintRequest)
        call?.enqueue(object : Callback<CompetitionListResponseModel> {
            override fun onFailure(call: Call<CompetitionListResponseModel>, t: Throwable) {
                tvNew?.let { ConstantVariable.onSNACK(it, "NO Record Found") }
            }

            override fun onResponse(
                call: Call<CompetitionListResponseModel>,
                response: Response<CompetitionListResponseModel>
            ) {
                try {
                    competitorResultList =
                        response.body()?.getResult() as ArrayList<CompetitionListResult>
                    setRecyclerView()
                } catch (e: Exception) {
                    tvNew?.let { ConstantVariable.onSNACK(it, "NO Record Found") }
                }
                if (currentPage <= TOTAL_PAGES)
                else isLastPageScroll = true;
            }
        })
    }


    private fun loadNextPage() {
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var complaintRequest = ComplaintReqest()

        val preferencesHelper = PreferencesHelper(this!!.activity!!)
        complaintRequest?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        complaintRequest.setFilterText(searchedValue)
        complaintRequest.setNoRows(TOTAL_PAGES)
        complaintRequest.setOffSet(currentPage)
        var call: Call<CompetitionListResponseModel>? =
            apiInterface!!.getCompetitionList(complaintRequest)
        call?.enqueue(object : Callback<CompetitionListResponseModel> {
            override fun onFailure(call: Call<CompetitionListResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                // tvNew?.let { ConstantVariable.onSNACK(it, "NO Record Found") }
            }

            override fun onResponse(
                call: Call<CompetitionListResponseModel>,
                response: Response<CompetitionListResponseModel>
            ) {
                progressDialog!!.dismiss()
                isLastPageScroll = false;
                try {
                    competitorResultList.addAll(
                        response.body()?.getResult() as ArrayList<CompetitionListResult>
                    )
                    setRecyclerView()
                } catch (e: Exception) {
                    tvNew?.let { ConstantVariable.onSNACK(it, "NO Record Found") }
                    progressDialog!!.dismiss()
                }

                if (currentPage <= TOTAL_PAGES)
                else isLastPageScroll = true;
            }
        })
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        searchedValue = p0!!
        if (!searchedValue.equals("") && searchedValue.length > 2) {
            getCompetitorListApi2()
        }
        return false
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        competitorResultList = ArrayList<CompetitionListResult>()

        findId()
        if (ConstantVariable.verifyAvailableNetwork(this!!.activity!!)) {
            getCompetitorListApi()
        } else {
            tvNew?.let { ConstantVariable.onSNACK(it, "No Internet Connection") }
        }
        setListener()
        setRecyclerView()
        ScrollListeners()
    }
}
