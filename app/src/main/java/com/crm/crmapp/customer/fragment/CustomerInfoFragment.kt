package com.crm.crmapp.customer.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import com.crm.crmapp.customer.adapter.CustomerOrderAdapter
import com.crm.crmapp.order.model.DocDetailModel
import java.util.ArrayList
import android.app.ProgressDialog
import android.util.Log
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.order.util.ConstantVariable.Companion.TAG_FROM
import com.crm.crmapp.order.util.ConstantVariable.Companion.TAG_OBJECT
import com.crm.crmapp.competition.fragment.CompetitionDetailActivity
import com.crm.crmapp.competition.model.CompetitionListResult
import com.crm.crmapp.complaint.activity.ComplaintDetailActivity
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.customer.adapter.ComplaintCompetitionAdapter
import com.crm.crmapp.customer.adapter.CustomerAttachmentAdapter
import com.crm.crmapp.customer.adapter.CustomerCompetitionAdapter
import com.crm.crmapp.customer.model.*
import com.crm.crmapp.order.activity.NewOrderActivity
import com.crm.crmapp.order.activity.OrderDetailActivity
import com.crm.crmapp.order.model.ImageRequest
import com.crm.crmapp.order.model.ImageRequestResponse
import com.crm.crmapp.order.model.SearchCustomerResultModel
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.search.SearchMainResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerInfoFragment : Fragment(), View.OnClickListener {
    private lateinit var viewOfLayout: View
    private lateinit var tvName: TextView
    private lateinit var tvMob: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvType: TextView
    private lateinit var tvOrder: TextView
    private lateinit var tvDistributor: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvRemarks: TextView
    private lateinit var tvComplaints: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var tvCompetition: TextView
    private lateinit var ivCall: ImageView
    private lateinit var ivEmail: ImageView
    private lateinit var ivMap: ImageView
    private lateinit var ivFavrt: ImageView
    private lateinit var imgNewDistributor: ImageView
    private lateinit var llDt: LinearLayout
    private lateinit var llBranding: LinearLayout
    private lateinit var rvDistributor: RecyclerView
    private lateinit var rvOrder: RecyclerView
    private lateinit var rvMarketPlans: RecyclerView
    private lateinit var rvComplaints: RecyclerView
    private lateinit var rvFeedback: RecyclerView
    private var docDetailList: ArrayList<DocDetailModel>? = null
    var progressDialog: ProgressDialog? = null
    private lateinit var customerDetailList: ArrayList<CustomerDetailByIDResult>
    var preferencesHelper: PreferencesHelper? = null
    var customerResult: CustomerResult? = null
    var imageBase64: ArrayList<String>? = null
    private var isFavrt: Boolean = false
    var orderResultList: ArrayList<OrderResult>? = null
    var complaintResultList: ArrayList<ComplaintResult>? = null
    var competitionResultList: ArrayList<CompetitionListResult>? = null
    private var animShow: Animation? = null
    private var animHide: Animation? = null
    private var grp_cust_id: String? = "0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater?.inflate(R.layout.activity_customer_info, container, false)
        setIds()
        docDetailList = ArrayList()
        customerDetailList = ArrayList()
        imageBase64 = ArrayList()

        val bundle = this.arguments
        if (bundle != null && bundle.getString(TAG_FROM) != null) {

            if (bundle.getString(TAG_FROM).equals("from_recent_customer", true)
                && bundle.getSerializable(TAG_OBJECT) != null
            ) {
                customerResult = bundle.getSerializable(TAG_OBJECT) as CustomerResult
                if (customerResult != null) {
                    getCustomerDetail("")
                    getCustomerOtherDetail("")
                }
            } else if (bundle.getString(TAG_FROM).equals("from_search_customer", true)
                && bundle.getSerializable(TAG_OBJECT) != null
            ) {
                val searchCustomerResult = bundle.getSerializable(TAG_OBJECT) as SearchCustomerResultModel
                if (searchCustomerResult != null) {
                    getSearchCustomerDetail(searchCustomerResult)
                    customerResult = CustomerResult()
                    customerResult!!.setCustId(searchCustomerResult.custId)
                    customerResult!!.setCustName(searchCustomerResult.custName)
                    getCustomerOtherDetail("")
                }
            } else if (bundle.getString(TAG_FROM).equals("from_search_main_customer", true)
                && bundle.getSerializable(TAG_OBJECT) != null
            ) {
                var searchCustomerResult = bundle.getSerializable(TAG_OBJECT) as SearchMainResponse.Result
                // var searchCustomerResult = bundle.getSerializable(TAG_OBJECT) as String
                if (searchCustomerResult != null) {
                    customerResult = CustomerResult()
                    customerResult!!.setCustId(searchCustomerResult.getId())
                    customerResult!!.setCustName(searchCustomerResult.getH1())
                    getMainSearchApi(searchCustomerResult.getId().toString())
                    //getCustomerOtherDetail("")
                }
            }
            else if (bundle.getString(TAG_FROM).equals("from_search_main_distributor", true)
                && bundle.getSerializable(TAG_OBJECT) != null
            ) {
                //var searchCustomerResult = bundle.getSerializable(TAG_OBJECT) as SearchMainResponse.Result
                var searchCustomerResult = bundle.getSerializable(TAG_OBJECT) as String
                getMainSearchApi(searchCustomerResult)
               // getCustomerOtherDetail("")
            }

        } else
            if (bundle != null && bundle.getString(ConstantVariable.firebase_bean_id) != null) {
                //getExpenseDetailById(ConstantVariable.FirebaseExpense!!,bundle.getString(ConstantVariable.firebase_bean_id))
                getCustomerDetail(bundle.getString(ConstantVariable.firebase_bean_id)!!)
                getCustomerOtherDetail(bundle.getString(ConstantVariable.firebase_bean_id)!!)
            }

        setListenersValue()
        setTouchListener()
        return viewOfLayout
    }

    private fun getMainSearchApi(searchCustomerResult: String) {
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var customerListRequest = RecentCustomerModel()
        customerListRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        customerListRequest.setCustId(searchCustomerResult.toIntOrNull()!!)

        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<CustomerDetailByIDModel>? = apiInterface!!.getCustomersDtlbyId(customerListRequest)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<CustomerDetailByIDModel> {
            override fun onFailure(call: Call<CustomerDetailByIDModel>, t: Throwable) {
                progressDialog!!.dismiss()
                //ConstantVariable.onSNACK(rvRecentCustomer,t.toString())
                //tvNoData.visibility=View.VISIBLE
                //rvList.visibility=View.GONE
            }

            override fun onResponse(call: Call<CustomerDetailByIDModel>, response: Response<CustomerDetailByIDModel>) {
                progressDialog!!.dismiss()
                //tvNoData.visibility=View.GONE
                //rvList.visibility=View.VISIBLE

                if (response.body() != null) {
                    customerDetailList = response.body()?.getResult() as ArrayList<CustomerDetailByIDResult>
                    var customerDetailList2 = response.body()?.getResult2() as ArrayList<CustomerDetailByIDResult2>
                    if (customerDetailList.size > 0) {
                        tvName.setText(customerDetailList.get(0).custName)
                        tvMob.setText(customerDetailList.get(0).mobileno)
                        //tvType.setText(customerDetailList.get(0).custType)
                        tvCategory.setText(customerDetailList.get(0).custType)
                        tvEmail.setText(customerDetailList.get(0).emailid)
                        tvDistributor.setText(customerDetailList.get(0).grpcustname)
                        grp_cust_id = customerDetailList.get(0).group_cust_id.toString()
                        /*tvAddress.setText(
                            customerDetailList.get(0).address + "," + customerDetailList.get(0).addrCity + ","
                                    + customerDetailList.get(0).addrState + ",\n" + customerDetailList.get(0).addrCountry + ",\n"
                                    + customerDetailList.get(0).addrPin + "."
                        )*/
                        tvAddress.setText(customerDetailList.get(0).address)
                        tvRemarks.setText(customerDetailList.get(0).remarks)

                        if (customerDetailList.get(0).fav == 0) {
                            isFavrt = false
                            ivFavrt.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off))

                        } else {
                            isFavrt = true
                            ivFavrt.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on))
                        }
                        if (customerDetailList2 != null && customerDetailList2.size > 0) {
                            getRequestApiForImage(customerDetailList2)
                        }
                        Log.e("result>>", "" + customerDetailList.get(0))
                        getCustomerOtherDetail("")
                    }

                }
            }
        })
    }

    fun getCustomerDetail(type: String) {
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var customerListRequest = RecentCustomerModel()
        customerListRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)

        if (type.equals("", ignoreCase = true)) {
            customerListRequest.setCustId(customerResult!!.custId)

        } else {
            customerListRequest.setCustId(type.toIntOrNull()!!)
        }
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<CustomerDetailByIDModel>? = apiInterface!!.getCustomersDtlbyId(customerListRequest)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<CustomerDetailByIDModel> {
            override fun onFailure(call: Call<CustomerDetailByIDModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvCategory, "Something went wrong")
                //ConstantVariable.onSNACK(rvRecentCustomer,t.toString())
                //tvNoData.visibility=View.VISIBLE
                //rvList.visibility=View.GONE
            }

            override fun onResponse(call: Call<CustomerDetailByIDModel>, response: Response<CustomerDetailByIDModel>) {
                progressDialog!!.dismiss()
                //tvNoData.visibility=View.GONE
                //rvList.visibility=View.VISIBLE

                if (response.body() != null) {
                    customerDetailList = response.body()?.getResult() as ArrayList<CustomerDetailByIDResult>
                    var customerDetailList2 = response.body()?.getResult2() as ArrayList<CustomerDetailByIDResult2>
                    if (customerDetailList.size > 0) {
                        tvName.setText(customerDetailList.get(0).custName)
                        tvMob.setText(customerDetailList.get(0).mobileno)
                        //tvType.setText(customerDetailList.get(0).custType)
                        tvCategory.setText(customerDetailList.get(0).custType)
                        tvEmail.setText(customerDetailList.get(0).emailid)
                        tvDistributor.setText(customerDetailList.get(0).grpcustname)
                        grp_cust_id = customerDetailList.get(0).group_cust_id.toString()
                        /*tvAddress.setText(
                            customerDetailList.get(0).address + "," + customerDetailList.get(0).addrCity + ","
                                    + customerDetailList.get(0).addrState + ",\n" + customerDetailList.get(0).addrCountry + ",\n"
                                    + customerDetailList.get(0).addrPin + "."
                        )*/
                        tvAddress.setText(customerDetailList.get(0).address)
                        tvRemarks.setText(customerDetailList.get(0).remarks)

                        if (customerDetailList.get(0).fav == 0) {
                            isFavrt = false
                            ivFavrt.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off))

                        } else {
                            isFavrt = true
                            ivFavrt.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on))
                        }
                        if (customerDetailList2 != null && customerDetailList2.size > 0) {
                            getRequestApiForImage(customerDetailList2)
                        }
                       /* if(grp_cust_id!=null)
                        {
                            getCustomerOtherDetail("")
                        }*/


                    }
                    // Log.e("result>>", "" + customerDetailList.get(0))
                }
            }
        })
    }

    fun getCustomerOtherDetail(type: String) {
        var customerListRequest = RecentCustomerModel()
        customerListRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        if (type.equals("", ignoreCase = true)) {
            customerListRequest.setCustId(customerResult!!.custId!!.toInt())

        } else {
            customerListRequest.setCustId(type.toIntOrNull()!!)
        }
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<CustomerOtherDtlResponseModel>? = apiInterface!!.getCustomersOtherDtlbyId(customerListRequest)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<CustomerOtherDtlResponseModel> {
            override fun onFailure(call: Call<CustomerOtherDtlResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvCategory, "Something went wrong")
            }
            override fun onResponse(
                call: Call<CustomerOtherDtlResponseModel>,
                response: Response<CustomerOtherDtlResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null && response.body()?.status.equals("success", ignoreCase = true)) {
                    var competition = response.body()?.competition as Competition
                    var orders = response.body()?.orders as Orders
                    var complaints = response.body()?.complaints as Complaints

                    if (competition != null && competition.status.equals("success", ignoreCase = true)) {
                        if (competition.result != null && competition.result.size > 0) {
                            competitionResultList = competition.result as ArrayList<CompetitionListResult>
                            if (competitionResultList != null && competitionResultList!!.size > 0) {
                                setCompetitionDetailAdapter(competitionResultList!!)
                            }
                        }
                    }

                    if (orders != null && orders.status.equals("success", ignoreCase = true)) {

                        if (orders.result != null && orders.result.size > 0) {
                            orderResultList = orders.result as ArrayList<OrderResult>
                            if (orderResultList != null && orderResultList!!.size > 0) {
                                setOrderDetailAdapter(orderResultList!!)
                            }
                        }

                    }

                    if (complaints != null && complaints.getStatus().equals("success", ignoreCase = true)) {
                        if (complaints.getResult() != null && complaints.getResult().size > 0) {

                            complaintResultList = complaints.getResult() as ArrayList<ComplaintResult>
                            if (complaintResultList != null && complaintResultList!!.size > 0) {
                                setComplaintDetailAdapter(complaintResultList!!)
                            }
                        }
                    }

                } else {

                }
            }
        })
    }

    fun getSearchCustomerDetail(customerResult: SearchCustomerResultModel) {
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var customerListRequest = RecentCustomerModel()
        customerListRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        customerListRequest.setCustId(customerResult.getCustId()!!)

        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<CustomerDetailByIDModel>? = apiInterface!!.getCustomersDtlbyId(customerListRequest)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<CustomerDetailByIDModel> {
            override fun onFailure(call: Call<CustomerDetailByIDModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvCategory, "Something went wrong")

                //ConstantVariable.onSNACK(rvRecentCustomer,t.toString())
                //tvNoData.visibility=View.VISIBLE
                //rvList.visibility=View.GONE
            }

            override fun onResponse(call: Call<CustomerDetailByIDModel>, response: Response<CustomerDetailByIDModel>) {
                progressDialog!!.dismiss()
                //tvNoData.visibility=View.GONE
                //rvList.visibility=View.VISIBLE

                if (response.body() != null) {
                    customerDetailList = response.body()?.getResult() as ArrayList<CustomerDetailByIDResult>
                    var customerDetailList2 = response.body()?.getResult2() as ArrayList<CustomerDetailByIDResult2>
                    if (customerDetailList.size > 0) {
                        tvName.setText(customerDetailList.get(0).custName)
                        tvMob.setText(customerDetailList.get(0).mobileno)
                        //tvType.setText(customerDetailList.get(0).custType)
                        tvCategory.setText(customerDetailList.get(0).custType)
                        tvEmail.setText(customerDetailList.get(0).emailid)
                        tvDistributor.setText(customerDetailList.get(0).grpcustname)
                        grp_cust_id = customerDetailList.get(0).group_cust_id.toString()
                        /*tvAddress.setText(
                            customerDetailList.get(0).address + "," + customerDetailList.get(0).addrCity + ","
                                    + customerDetailList.get(0).addrState + ",\n" + customerDetailList.get(0).addrCountry + ",\n"
                                    + customerDetailList.get(0).addrPin + "."
                        )*/
                        tvAddress.setText(customerDetailList.get(0).address)
                        tvRemarks.setText(customerDetailList.get(0).remarks)


                        if (customerDetailList.get(0).fav == 0) {
                            isFavrt = false
                            ivFavrt.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off))

                        } else {
                            isFavrt = true
                            ivFavrt.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on))
                        }
                        if (customerDetailList2 != null && customerDetailList2.size > 0) {
                            getRequestApiForImage(customerDetailList2)
                        }
                    }
                    // Log.e("result>>", "" + customerDetailList.get(0))
                }
            }
        })
    }

    private fun setAdapters() {
        rvOrder.layoutManager = LinearLayoutManager(activity)
        //rvMarketPlans.layoutManager = LinearLayoutManager(activity)
        ///rvMarketPlans.adapter = CustomerOrderAdapter(context, "marketOrder")
        rvDistributor.layoutManager = LinearLayoutManager(activity)
        //rvDistributor.adapter = DistributionAdapter(context, docDetailList)
        rvDistributor.adapter = CustomerAttachmentAdapter(context, imageBase64, false)
    }

    private fun setOrderDetailAdapter(list: ArrayList<OrderResult>) {
        rvOrder.layoutManager = LinearLayoutManager(activity)
        rvOrder.adapter = CustomerOrderAdapter(context, list, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                val intent = Intent(activity, OrderDetailActivity::class.java)
                intent.putExtra(ConstantVariable.online_order_id, position)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        })
    }

    private fun setCompetitionDetailAdapter(list: ArrayList<CompetitionListResult>) {
        rvMarketPlans.layoutManager = LinearLayoutManager(activity)
        rvMarketPlans.adapter = CustomerCompetitionAdapter(context, list, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                val intent = Intent(activity, CompetitionDetailActivity::class.java)
                intent.putExtra(ConstantVariable.TAG_FROM, "edit")
                intent.putExtra(ConstantVariable.TAG_OBJECT, list.get(position))
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

        })
    }

    private fun setComplaintDetailAdapter(list: ArrayList<ComplaintResult>) {
        rvComplaints.layoutManager = LinearLayoutManager(activity)
        rvComplaints.adapter = ComplaintCompetitionAdapter(context, list, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                val intent = Intent(activity, ComplaintDetailActivity::class.java)
                intent.putExtra(ConstantVariable.online_complaint_id, position)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        })
    }

    private fun setListenersValue() {
        ivCall.setOnClickListener(this)
        ivEmail.setOnClickListener(this)
        ivMap.setOnClickListener(this)
        llDt.setOnClickListener(this)
        llBranding.setOnClickListener(this)
        tvComplaints.setOnClickListener(this)
        tvFeedback.setOnClickListener(this)
        tvOrder.setOnClickListener(this)
        tvCompetition.setOnClickListener(this)
        ivFavrt.setOnClickListener(this)
        imgNewDistributor.setOnClickListener(this)

    }

    private fun setTouchListener() {

        tvOrder.setOnTouchListener(onTouch)
        tvCompetition.setOnTouchListener(onTouch)
        tvComplaints.setOnTouchListener(onTouch)
    }

    @SuppressLint("ClickableViewAccessibility")
    val onTouch = View.OnTouchListener { v, event ->

        var DRAWABLE_RIGHT: Int = 2
        when (v) {
            tvOrder -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (tvOrder.getRight() - tvOrder.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        ConstantVariable.animationEffect(tvOrder, activity!!)
                        var intent = Intent(activity, NewOrderActivity::class.java) as Intent
                        intent.putExtra(TAG_FROM, "from_customerInfo")
                        intent.putExtra(TAG_OBJECT, customerResult)
                        startActivity(intent)
                        activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                        true
                    }
                }
            }
            tvCompetition -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (tvCompetition.getRight() - tvCompetition.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        ConstantVariable.animationEffect(tvCompetition, activity!!)
                        var intent = Intent(activity, CompetitionDetailActivity::class.java) as Intent
                        intent.putExtra(TAG_FROM, "from_customerInfo")
                        intent.putExtra(TAG_OBJECT, customerResult)
                        startActivity(Intent(intent))
                        activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                        true
                    }
                }
            }
            tvComplaints -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (tvComplaints.getRight() - tvComplaints.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        ConstantVariable.animationEffect(tvComplaints, activity!!)
                        var intent = Intent(activity, ComplaintDetailActivity::class.java) as Intent
                        intent.putExtra(TAG_FROM, "from_customerInfo")
                        intent.putExtra(TAG_OBJECT, customerResult)
                        startActivity(Intent(intent))
                        activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                        true
                    }
                }
            }
        }
        false
    }

    private fun setIds() {
        preferencesHelper = PreferencesHelper(this!!.activity!!)
        tvName = viewOfLayout.findViewById(R.id.tvName) as TextView
        tvMob = viewOfLayout.findViewById(R.id.tvMob) as TextView
        tvType = viewOfLayout.findViewById(R.id.tvType) as TextView
        tvCategory = viewOfLayout.findViewById(R.id.tvCategory) as TextView
        tvEmail = viewOfLayout.findViewById(R.id.tvEmail) as TextView
        tvDistributor = viewOfLayout.findViewById(R.id.tvDistributor) as TextView
        tvAddress = viewOfLayout.findViewById(R.id.tvAddress) as TextView
        tvRemarks = viewOfLayout.findViewById(R.id.tvRemarks) as TextView
        tvOrder = viewOfLayout.findViewById(R.id.tvOrder) as TextView
        tvComplaints = viewOfLayout.findViewById(R.id.tvComplaints) as TextView
        tvFeedback = viewOfLayout.findViewById(R.id.tvFeedback) as TextView
        tvCompetition = viewOfLayout.findViewById(R.id.tvCompetition) as TextView
        ivCall = viewOfLayout.findViewById(R.id.ivCall) as ImageView
        ivFavrt = viewOfLayout.findViewById(R.id.ivFavrt) as ImageView
        ivEmail = viewOfLayout.findViewById(R.id.ivEmail) as ImageView
        ivMap = viewOfLayout.findViewById(R.id.ivMap) as ImageView
        imgNewDistributor = viewOfLayout.findViewById(R.id.imgNewDistributor) as ImageView
        llDt = viewOfLayout.findViewById(R.id.llDt) as LinearLayout
        llBranding = viewOfLayout.findViewById(R.id.llBranding) as LinearLayout

        rvDistributor = viewOfLayout.findViewById(R.id.rvDistributor) as RecyclerView
        rvOrder = viewOfLayout.findViewById(R.id.rvOrder) as RecyclerView
        rvMarketPlans = viewOfLayout.findViewById(R.id.rvMarketPlans) as RecyclerView
        rvComplaints = viewOfLayout.findViewById(R.id.rvComplaints) as RecyclerView
        rvFeedback = viewOfLayout.findViewById(R.id.rvFeedback) as RecyclerView
        animShow = AnimationUtils.loadAnimation(context, R.anim.view_show)
        animHide = AnimationUtils.loadAnimation(context, R.anim.view_hide)

    }

    override fun onClick(v: View?) {
        when (v) {

            ivCall -> {
                ConstantVariable.sendPhoneCall(this!!.activity!!, tvMob.text.toString())
                ConstantVariable.animationEffect(ivCall, activity!!)
            }
            ivEmail -> {
                ConstantVariable.sendEmail(this!!.activity!!, tvEmail.text.toString(), "9672655444")
                ConstantVariable.animationEffect(ivEmail, activity!!)
            }

            ivFavrt -> {
//                if (isFavrt == false) {
//                    ConstantVariable.animationEffect(ivFavrt, activity!!)
//                    saveFavrt()
//                    isFavrt = true
//                } else {
//
//                    saveFavrt()
//                    isFavrt = false
//                }


                ConstantVariable.animationEffect(ivFavrt, activity!!)
                saveFavrt()
            }
            ivMap -> {
                ConstantVariable.animationEffect(ivMap, activity!!)
                if (ConstantVariable.getLocationFromAddress(tvAddress.text.toString(), this!!.activity!!) != null) {
                    try {
                        val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(tvAddress.text.toString()))
                        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        startActivity(mapIntent)
                        activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } catch (e: ActivityNotFoundException) {
                        ConstantVariable.onSNACK(tvAddress, "Please Install Google Maps")
                    }
                }
            }
            llDt -> {
                ConstantVariable.animationEffect(llDt, activity!!)
            }
            llBranding -> {
                ConstantVariable.animationEffect(llBranding, activity!!)
            }
            tvComplaints -> {
                ConstantVariable.animationEffect(tvComplaints, activity!!)
                if (complaintResultList != null && complaintResultList!!.size > 0) {
                    if (rvComplaints.visibility == View.VISIBLE) {
                        rvComplaints.visibility = View.GONE
                        rvComplaints.startAnimation(animHide)
                    } else {
                        rvComplaints.visibility = View.VISIBLE
                        rvComplaints.startAnimation(animShow)
                    }
                } else {
                    ConstantVariable.onToast(context!!, "No data")
                }
            }
            tvFeedback -> {
                ConstantVariable.animationEffect(tvFeedback, activity!!)
            }
            tvOrder -> {
                ConstantVariable.animationEffect(tvOrder, activity!!)
                if (orderResultList != null && orderResultList!!.size > 0) {
                    if (rvOrder.visibility == View.VISIBLE) {
                        rvOrder.visibility = View.GONE
                        rvOrder.startAnimation(animHide)
                    } else {
                        rvOrder.visibility = View.VISIBLE
                        rvOrder.startAnimation(animShow)
                    }
                } else {
                    ConstantVariable.onToast(context!!, "No data")
                }
            }

            tvCompetition -> {
                ConstantVariable.animationEffect(tvCompetition, activity!!)
                if (competitionResultList != null && competitionResultList!!.size > 0) {
                    if (rvMarketPlans.visibility == View.VISIBLE) {
                        rvMarketPlans.visibility = View.GONE
                        rvMarketPlans.startAnimation(animHide)
                    } else {
                        rvMarketPlans.visibility = View.VISIBLE
                        rvMarketPlans.startAnimation(animShow)
                    }
                } else {
                    ConstantVariable.onToast(context!!, "No data")
                }
            }

            imgNewDistributor -> {
                val fragmentManager = activity!!.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                var bundle = Bundle()
                bundle.putString(ConstantVariable.TAG_FROM, "from_search_main_distributor")
                bundle.putSerializable(ConstantVariable.TAG_OBJECT, grp_cust_id)
                var fragment: Fragment = CustomerInfoFragment()
                fragment.arguments = bundle
                fragmentTransaction!!.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                fragmentTransaction.replace(R.id.fragment_container, fragment)?.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
    }

    private fun saveFavrt() {
        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var favrtRequest = FavrtSaveRequest()
        var result = FavrtSaveRequest.Result()
        var list = ArrayList<FavrtSaveRequest.Result>()


        try {
            if (customerDetailList != null && customerDetailList.size > 0) {
                result.setBeanId(customerDetailList.get(0).custId)
                result.setBeanType("Customers")

                if (isFavrt == true) {
                    result.setIsactive(0)
                } else {
                    result.setIsactive(1)
                }
                result.setRemarks("")
                result.setUserId(preferencesHelper!!.userId!!)
                list.add(result)
                favrtRequest.setResult(list)

            }
        } catch (e: Exception) {
        }
        var call: Call<FavrtSaveResponseModel> = apiInterface!!.saveFavrtCustomer(favrtRequest)
        call!!.enqueue(object : Callback<FavrtSaveResponseModel> {
            override fun onFailure(call: Call<FavrtSaveResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvAddress, "Something went wrong")
            }

            override fun onResponse(call: Call<FavrtSaveResponseModel>, response: Response<FavrtSaveResponseModel>) {
                progressDialog!!.dismiss()
                var favrtSaveResponseModel: FavrtSaveResponseModel
                var favrtId = response.body()?.getFavouriteidid()

                if (favrtId == 0) {

                    isFavrt = false
                    ivFavrt.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off))
                } else {
                    isFavrt = true
                    ivFavrt.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on))

                }




                Toast.makeText(activity, response?.body()?.getMessage(), Toast.LENGTH_SHORT)
            }
        })


    }

    private fun getRequestApiForImage(docDetailList: ArrayList<CustomerDetailByIDResult2>) {
        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        for (i: Int in docDetailList!!.indices) {
            var imageRequest = ImageRequest()
            imageBase64?.clear()
            imageRequest.setImgPath(docDetailList.get(i).getUrl()!!)
            var call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
            call!!.enqueue(object : Callback<ImageRequestResponse> {
                override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                    progressDialog!!.dismiss()
                    ConstantVariable.onSNACK(tvAddress, "Something went wrong")
                }

                override fun onResponse(call: Call<ImageRequestResponse>, response: Response<ImageRequestResponse>) {
                    progressDialog!!.dismiss()
                    if (response?.body()?.getMsg().equals("success")) {
                        var imageUrl = response.body()?.getImgString() as String
                        imageBase64!!.add(imageUrl)
                        setAdapters()
                    }
                }
            })
        }
    }

}