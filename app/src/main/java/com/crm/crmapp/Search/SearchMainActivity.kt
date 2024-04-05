package com.crm.crmapp.search

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.complaint.model.ComplaintReqest
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.order.activity.Main2Activity
import com.crm.crmapp.order.activity.OrderDetailActivity
import com.crm.crmapp.order.util.PaginationScrollListener
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class SearchMainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, View.OnClickListener {
    private var svCustomer: androidx.appcompat.widget.SearchView? = null
    private var rvList: RecyclerView? = null
    private var bt_mic: ImageButton? = null
    var searchedValue: String = ""
    private val REQ_CODE_SPEECH_INPUT = 100
    var linearLayoutManager: LinearLayoutManager? = null
    private lateinit var progressDialog: ProgressDialog
    private var PAGE_START = 0
    private var isLoadingScroll = false
    private var isLastPageScroll = false
    private var TOTAL_PAGES = 10
    private var currentPage = PAGE_START
    private lateinit var resultList: ArrayList<SearchMainResponse.Result>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main_search)
        findId()

        /*  if (ConstantVariable.verifyAvailableNetwork(this)){
              getSearchListApi()
          }
          else{
              bt_mic?.let { ConstantVariable.onSNACK(it,"No Internet Connection") }
          }*/

        setListener()
        setAdapter()
        ScrollListeners()

    }
    private fun ScrollListeners() {
        rvList!!.addOnScrollListener(object : PaginationScrollListener(this!!.linearLayoutManager!!) {
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

                        if (ConstantVariable.verifyAvailableNetwork(this@SearchMainActivity)) {
                            loadNextPage()
                        } else {
                            ConstantVariable.onSNACK(svCustomer!!, "No Internet Connection")
                        }
                    },
                    1000
                )
            }
        })
    }

    private fun loadNextPage() {
        progressDialog = ConstantVariable.showProgressDialog(this, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var complaintRequest = ComplaintReqest()
        val preferencesHelper = PreferencesHelper(this)
        complaintRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        complaintRequest.setSearchKey(searchedValue)
        complaintRequest.setNoRows(TOTAL_PAGES)
        complaintRequest.setOffSet(currentPage)

        var call: Call<SearchMainResponse>? = apiInterface!!.mainSearchApi(complaintRequest)
        call?.enqueue(object : Callback<SearchMainResponse> {
            override fun onFailure(call: Call<SearchMainResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                bt_mic?.let { ConstantVariable.onSNACK(it, "NO Record Found") }
            }

            override fun onResponse(call: Call<SearchMainResponse>, response: Response<SearchMainResponse>) {
                progressDialog!!.dismiss()
                isLastPageScroll = false
                isLoadingScroll = false
                try {
                    resultList.addAll(response.body()?.getResult() as ArrayList<SearchMainResponse.Result>)
                    setAdapter()

                    if (currentPage >= TOTAL_PAGES) {
                        isLastPageScroll = false
                        isLoadingScroll = false
                    } else {
                        isLastPageScroll = true
                        isLoadingScroll = true
                    }
                } catch (e: Exception) {
                    bt_mic?.let { ConstantVariable.onSNACK(it, "NO Record Found") }
                    progressDialog!!.dismiss()
                }

            }
        })
    }

    private fun setAdapter() {

        rvList?.layoutManager = linearLayoutManager
        rvList?.adapter = SearchMainAdapter(this!!, resultList, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                try {
                    if (resultList.get(position).getType().equals("SalesOrder", ignoreCase = true)) {
                        var intent = Intent(this@SearchMainActivity, OrderDetailActivity::class.java)
                        intent.putExtra(ConstantVariable.online_order_id, resultList.get(position).getId())
                        startActivity(intent)
                    } else {
                        var intent = Intent(this@SearchMainActivity, Main2Activity::class.java)
                        intent.putExtra(ConstantVariable.TAG_OBJECT, resultList.get(position))
                        intent.putExtra(ConstantVariable.TAG_FROM, "from_search_main_customer")
                        startActivity(intent)
                    }
                } catch (e: NullPointerException) {
                }
            }
        })
    }

    private fun setListener() {
        svCustomer?.setOnQueryTextListener(this)
        bt_mic?.setOnClickListener(this)
        svCustomer?.setOnClickListener(View.OnClickListener {
            svCustomer?.setIconified(false);
        })

        val closeButton = svCustomer!!.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView

// Set on click listener
        closeButton.setOnClickListener({

            PAGE_START = 0
            isLoadingScroll = false
            isLastPageScroll = false
            TOTAL_PAGES = 10
            currentPage = PAGE_START

            searchedValue = ""
            getSearchListApi()
            svCustomer!!.setQuery("", false);
            svCustomer!!.clearFocus()
        })

    }

    private fun findId() {
        resultList = ArrayList()
        svCustomer = findViewById(R.id.svCustomer) as androidx.appcompat.widget.SearchView
        bt_mic = findViewById(R.id.bt_mic) as ImageButton
        rvList = findViewById(R.id.rvList) as RecyclerView
        linearLayoutManager = LinearLayoutManager(this)

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
        getSearchListApi()
        return false
    }

    private fun getSearchListApi() {

        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var complaintRequest = ComplaintReqest()
        val preferencesHelper = PreferencesHelper(this)
        complaintRequest.setUser_Id(preferencesHelper!!.userId!!.toIntOrNull()!!)
        complaintRequest.setSearchKey(searchedValue)
        complaintRequest.setNoRows(TOTAL_PAGES)
        complaintRequest.setOffSet(currentPage)

        var call: Call<SearchMainResponse>? = apiInterface!!.mainSearchApi(complaintRequest)
        call?.enqueue(object : Callback<SearchMainResponse> {
            override fun onFailure(call: Call<SearchMainResponse>, t: Throwable) {
                // bt_mic?.let { ConstantVariable.onSNACK(it,"NO Record Found") }
                Toast.makeText(this@SearchMainActivity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SearchMainResponse>, response: Response<SearchMainResponse>) {
                try {
                    resultList = response.body()?.getResult() as ArrayList<SearchMainResponse.Result>
                    if (resultList.size > 0) {
                        setAdapter()
                        if (currentPage <= TOTAL_PAGES) {
                            isLastPageScroll = false
                        } else {
                            isLastPageScroll = true
                        }
                    } else {
                        Toast.makeText(this@SearchMainActivity, "NO Record Found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // bt_mic?.let { ConstantVariable.onSNACK(it,"NO Record Found") }

                }

            }
        })
    }

    override fun onClick(v: View?) {
        promptSpeechInput()
    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            getString(R.string.speech_prompt)
        )
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                this,
                getString(R.string.speech_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == RESULT_OK && null != data) {
                    svCustomer?.setIconified(false);
                    var result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    svCustomer?.setQuery(result?.get(0), false)
                }
            }
        }
    }
}