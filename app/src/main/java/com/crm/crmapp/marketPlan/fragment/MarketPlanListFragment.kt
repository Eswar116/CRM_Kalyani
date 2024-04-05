package com.crm.crmapp.marketPlan.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.databinding.FragmentMarketPlanBinding
import com.crm.crmapp.marketPlan.NewMarketPlanFragment
import com.crm.crmapp.marketPlan.adapters.MarketPlanListAdapter
import com.crm.crmapp.marketPlan.models.MarketPlanListRequestResponse
import com.crm.crmapp.order.activity.NewOrderActivity
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MarketPlanListFragment : Fragment(),
    androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private lateinit var viewOfLayout: View
    private lateinit var tvUsername: TextView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var marketPlanList: ArrayList<MarketPlanListRequestResponse.Result>
    private lateinit var progressDialog: ProgressDialog
    private lateinit var progressDialog_search: ProgressDialog
    private var preferencesHelper: PreferencesHelper? = null
    private lateinit var binding: FragmentMarketPlanBinding

//    lateinit var marketPlanInfoFrag: MarketPlanInfoFragment
        lateinit var marketPlanInfoFrag: BlankFragment

    var from: String = ""
    var searchedValue: String = ""
    var typeStr = "All"
    val myStrings = arrayOf("All", "Completed", "Rejected", "In Progress")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle?.getString(ConstantVariable.TAG_FROM) != null) {
            from = bundle.getString(ConstantVariable.TAG_FROM)!!;
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_market_plan,
            container,
            false
        )
//        binding.frag = this
        binding.lifecycleOwner = this
        viewOfLayout = binding.root
        setId()
        setSpinner()

        getMarketPlanList()

        return viewOfLayout
    }

    private fun setId() {
        preferencesHelper = PreferencesHelper(context!!)
//        marketPlanInfoFrag = MarketPlanInfoFragment()
        marketPlanInfoFrag = BlankFragment()
        binding!!.svMarketPlan.setOnQueryTextListener(this)
        binding!!.svMarketPlan.setOnClickListener(View.OnClickListener {
            binding!!.svMarketPlan.setIconified(false);
        })
        binding!!.tvNew.setOnClickListener {
            binding!!.svMarketPlan.setQuery("", false)
            var intent = Intent(context, NewMarketPlanFragment::class.java)
            startActivity(intent)
        }
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding!!.rvList.layoutManager = linearLayoutManager

        if (from != null && from.equals("from_new_order", true)) {
            binding!!.llHeaderMP.visibility = View.GONE
        } else {
            binding!!.llHeaderMP.visibility = View.VISIBLE
        }

        var result = preferencesHelper!!.getObject()
        if (result.getCategory().equals("Area Sales Manager", true)) {
            binding!!.tvNew.visibility = View.VISIBLE
            binding!!.llSearch.visibility = View.VISIBLE
        } else {
            binding!!.tvNew.visibility = View.GONE
            binding!!.llSearch.visibility = View.GONE
        }
    }

    private fun getMarketPlanList() {

        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<MarketPlanListRequestResponse> =
            apiInterface!!.getSearchMarketPlanList(
                preferencesHelper!!.userId!!.toIntOrNull()!!,
                "All",
                "All"
            )

        call.enqueue(object : Callback<MarketPlanListRequestResponse> {
            override fun onFailure(call: Call<MarketPlanListRequestResponse>, t: Throwable) {
                progressDialog.dismiss()
                ConstantVariable.onSNACK(binding.marketPlan, "Something went wrong")
            }

            override fun onResponse(
                call: Call<MarketPlanListRequestResponse>,
                response: Response<MarketPlanListRequestResponse>
            ) {
                progressDialog.dismiss()
                if (response.body() != null) {
                    marketPlanList = response.body()
                        ?.getResult() as ArrayList<MarketPlanListRequestResponse.Result>

                    if (from != null && from == "from_new_order") {
                        var othersObj = MarketPlanListRequestResponse.Result()
                        othersObj.startdate = ""
                        othersObj.stage = ""
                        othersObj.distributor_name = ""
                        othersObj.owner_full_name = ""
                        othersObj.market_plan_name = "Others"
                        othersObj.enddate = ""
                        othersObj.id = "0"
                        othersObj.state_name = ""
                        marketPlanList.add(0, othersObj);
                    }
                    setRecyclerView();

//                    if (!marketPlanList.isEmpty() && marketPlanList.size > 0) {
//                        binding.listEmpty.visibility = View.INVISIBLE
//                        setRecyclerView();
//                    } else {
//                        binding.listEmpty.visibility = View.VISIBLE
//                    }


                }
            }
        })
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        searchedValue = p0!!
        if (!searchedValue.equals("") && searchedValue.length > 2) {
            binding!!.btnSearch!!.isEnabled = true
        } else {
            binding!!.btnSearch!!.isEnabled = true
        }
        return false
    }

    private fun getSearchMarketPlanList() {
        progressDialog_search = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        val apiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        if (searchedValue.equals("")) {
            searchedValue = "All";
        }
        val call: Call<MarketPlanListRequestResponse> =
            apiInterface.getSearchMarketPlanList(
                preferencesHelper!!.userId!!.toIntOrNull()!!,
                searchedValue,
                typeStr
            )
        call.enqueue(object : Callback<MarketPlanListRequestResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<MarketPlanListRequestResponse>?,
                response: Response<MarketPlanListRequestResponse>
            ) {
                progressDialog_search.dismiss()
                if (response?.body() != null) {
                    if (response?.body()?.getStatus().equals("success")) {
                        marketPlanList.clear();
                        var marketPlanList_local =
                            (response?.body()
                                ?.getResult() as ArrayList<MarketPlanListRequestResponse.Result>?)!!

                        marketPlanList.addAll(marketPlanList_local)
                        //setRecyclerView()
                        if (from != null && from.equals("from_new_order", true)) {
                            var othersObj = MarketPlanListRequestResponse.Result()
                            othersObj.startdate = ""
                            othersObj.stage = ""
                            othersObj.distributor_name = ""
                            othersObj.owner_full_name = ""
                            othersObj.market_plan_name = "Others"
                            othersObj.enddate = ""
                            othersObj.id = "0"
                            othersObj.state_name = ""
                            marketPlanList.add(0, othersObj);
                        }
                        binding!!.rvList.adapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<MarketPlanListRequestResponse>?, t: Throwable?) {
                progressDialog_search.dismiss()
                ConstantVariable.onSNACK(binding!!.rvList, "Something went wrong")
            }
        })
    }

    private fun setRecyclerView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.rvList.adapter =
                MarketPlanListAdapter(context!!, marketPlanList, object : onItemClick {
                    override fun getPositionOfList(position: Int) {
                        binding.svMarketPlan.setQuery("", false)
                        if (from != null && from.equals("from_new_order", true)) {
                            val intent = Intent(context, NewOrderActivity::class.java)
                            intent.putExtra(
                                "MarketPlanPosition",
                                marketPlanList[position].market_plan_name
                            )
                            intent.putExtra("MarketPlanId", marketPlanList[position].id)
                            activity!!.setResult(Activity.RESULT_OK, intent)
                            activity!!.finish()
                        } else {
                            val bundle = Bundle()
                            bundle.putString(ConstantVariable.TAG_FROM, "from_marketPlan_list")
                            bundle.putSerializable(
                                ConstantVariable.TAG_OBJECT,
                                marketPlanList[position]
                            )
                            marketPlanInfoFrag.arguments = bundle
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.fragment_container, marketPlanInfoFrag)
                                ?.addToBackStack(null)
                                ?.commit()

                        }
                    }

                })
        }
    }

    private fun setSpinner() {
        binding!!.spType!!.adapter =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, myStrings)
        binding!!.spType!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != 0) {
                    typeStr = myStrings[p2].substring(0, 3).trim()
                } else {
                    typeStr = "All"
                }
            }
        }

        binding.btnSearch.setOnClickListener {
            getSearchMarketPlanList()
        }
    }
}