package com.crm.crmapp.marketPlan.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.R
import com.crm.crmapp.customer.adapter.CustomerAttachmentAdapter
import com.crm.crmapp.databinding.FragmentBlankBinding
import com.crm.crmapp.marketPlan.adapters.MarketPlanInfoRetailerAdapter
import com.crm.crmapp.marketPlan.adapters.MarketPlanInfoSalesOrderAdapter
import com.crm.crmapp.marketPlan.adapters.MarketPlanInfoUsersAdapter
import com.crm.crmapp.marketPlan.adapters.StringListAdapter
import com.crm.crmapp.marketPlan.models.MarketPlanInfoRequestResponse
import com.crm.crmapp.marketPlan.models.MarketPlanListRequestResponse
import com.crm.crmapp.marketPlan.models.UpdateMarketPlanStateRequestResponseModel
import com.crm.crmapp.order.model.ImageRequest
import com.crm.crmapp.order.model.ImageRequestResponse
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.util.CustomDate
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import kotlinx.android.synthetic.main.layout_market_plan_info.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BlankFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentBlankBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var progressDialog2: ProgressDialog
    private lateinit var progressDialog3: ProgressDialog
    private lateinit var progressDialog4: ProgressDialog
    private var preferencesHelper: PreferencesHelper? = null
    var imageBase64: java.util.ArrayList<String>? = null
    private lateinit var marketplanListObj: MarketPlanListRequestResponse.Result
    private lateinit var marketPlanDetailList: ArrayList<MarketPlanInfoRequestResponse.Result_1>
    private lateinit var attachmentList: ArrayList<MarketPlanInfoRequestResponse.Result_2>
    private lateinit var assignedUserList: ArrayList<MarketPlanInfoRequestResponse.Result_3>
    private lateinit var retailerList: ArrayList<MarketPlanInfoRequestResponse.Result_4>
    private val globalRetailerList: ArrayList<MarketPlanInfoRequestResponse.Result_4> = ArrayList()
    private lateinit var salesOrderList: ArrayList<MarketPlanInfoRequestResponse.Result_5>
    private lateinit var editApproveList: ArrayList<MarketPlanInfoRequestResponse.Result_6>
    private lateinit var mAlertDialogStage: AlertDialog
    private lateinit var mAlertDialogEditToDate: AlertDialog
    private lateinit var rvStage: RecyclerView
    private lateinit var stageList: ArrayList<String>
    private lateinit var resultList: ArrayList<UpdateMarketPlanStateRequestResponseModel.Result>
    private var stage_global: String = ""
    private var from: String = ""
    private var marketPlanIdObj: Int = 0
    lateinit var yyddmDate_fromDate: CustomDate
    lateinit var yyddmDate_toDate: CustomDate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesHelper = PreferencesHelper(context!!)
        val bundle = this.arguments
        if (bundle?.getSerializable(ConstantVariable.TAG_OBJECT) != null) {
            from = bundle!!.getString(ConstantVariable.TAG_FROM).toString();
            if (from.equals("from_marketPlan_list", true)
                && bundle!!.getSerializable(ConstantVariable.TAG_OBJECT) != null
            ) {
                marketplanListObj =
                    bundle.getSerializable(ConstantVariable.TAG_OBJECT) as MarketPlanListRequestResponse.Result
            } else if (from.equals("from_notification_detail", true)) {
                marketPlanIdObj = bundle.getInt(ConstantVariable.TAG_OBJECT)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_blank, container, false)
        preferencesHelper = PreferencesHelper(requireContext())

        imageBase64 = java.util.ArrayList()
        stageList = ArrayList();
        stageList.add("In Progress")
        stageList.add("Inactive")
        stageList.add("Completed")
        yyddmDate_fromDate = CustomDate()
        yyddmDate_toDate = CustomDate()
        getMarketPlanInfo()
        setListenersValue()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setMarketPlanDetail(marketPlanDetailList: ArrayList<MarketPlanInfoRequestResponse.Result_1>) {

        binding.tvMarketPlanName.text =
            marketPlanDetailList[0].market_plan_name + " (id:" + marketPlanDetailList[0].id + ")"
        binding.tvstatus.text = "Status : " + marketPlanDetailList[0].stage
        binding.tvPlanOwner.text = marketPlanDetailList[0].owner_full_name
        binding.tvOwnerprofile.text = marketPlanDetailList[0].owner_user_cat
        binding.tvPeriod.text = ConstantVariable.parseDateToddMMyyyyWithoutTime(
            marketPlanDetailList[0].startdate!!
        ) + " to " + ConstantVariable.parseDateToddMMyyyyWithoutTime(marketPlanDetailList[0].enddate!!)
        binding.tvstate.text = marketPlanDetailList[0].state_name
        binding.tvCity.text = marketPlanDetailList[0].city_name
        binding.tvScheme.text = marketPlanDetailList[0].scheme_name
        binding.tvCategory.text = marketPlanDetailList[0].cat_name
        binding.tvRemarks.text = marketPlanDetailList[0].remarks
        binding.tvDistributor.text = marketPlanDetailList[0].distributor_name
    }

    private fun setListenersValue() {
        binding.ivCall.setOnClickListener(this)
        binding.ivEmail.setOnClickListener(this)
        binding.btnreject.setOnClickListener(this)
        binding.btnapprove.setOnClickListener(this)
        binding.fabStage.setOnClickListener(this)
        binding.tvEdtToDate.setOnClickListener(this)
    }


    private fun getMarketPlanInfo() {
        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var id: Int = 0
        if (from.equals("from_marketPlan_list", true)) {
            id = marketplanListObj.id!!.toIntOrNull()!!
        } else if (from.equals("from_notification_detail", true)) {
            id = marketPlanIdObj;
        }

        val call: Call<MarketPlanInfoRequestResponse>? =
            apiInterface!!.getMarketPlanInfo(id, preferencesHelper!!.userId!!.toIntOrNull()!!)
        call!!.enqueue(object : Callback<MarketPlanInfoRequestResponse> {
            override fun onFailure(call: Call<MarketPlanInfoRequestResponse>, t: Throwable) {
                progressDialog.dismiss()
                ConstantVariable.onSNACK(binding.tvCity, "Something went wrong")
            }

            override fun onResponse(
                call: Call<MarketPlanInfoRequestResponse>,
                response: Response<MarketPlanInfoRequestResponse>
            ) {
                progressDialog.dismiss()
                if (response.body() != null) {
                    marketPlanDetailList =
                        response.body()?.result_1 as ArrayList<MarketPlanInfoRequestResponse.Result_1>
                    marketPlanDetailList =
                        response.body()?.result_1 as ArrayList<MarketPlanInfoRequestResponse.Result_1>
                    attachmentList =
                        response.body()?.result_2 as ArrayList<MarketPlanInfoRequestResponse.Result_2>
                    assignedUserList =
                        response.body()?.result_3 as ArrayList<MarketPlanInfoRequestResponse.Result_3>
                    retailerList =
                        response.body()?.result_4 as ArrayList<MarketPlanInfoRequestResponse.Result_4>
                    globalRetailerList.clear()
                    globalRetailerList.addAll(retailerList)
                    salesOrderList =
                        response.body()?.result_5 as ArrayList<MarketPlanInfoRequestResponse.Result_5>
                    editApproveList =
                        response.body()?.result_6 as ArrayList<MarketPlanInfoRequestResponse.Result_6>

                    setMarketPlanDetail(marketPlanDetailList)
                    setAttachmentList(attachmentList)
                    setAssignedUserList(assignedUserList)
                    setRetailerList(retailerList, marketPlanDetailList)
                    setSalesOrderList(salesOrderList)
                    setEditApproveList(editApproveList)
                }
            }
        })
    }
    private fun setRetailerList(
        retailerList: ArrayList<MarketPlanInfoRequestResponse.Result_4>,
        marketPlanDetailList: ArrayList<MarketPlanInfoRequestResponse.Result_1>
    ) {
        binding.rvPlan.layoutManager = LinearLayoutManager(activity)
        binding.rvPlan.adapter =
            MarketPlanInfoRetailerAdapter(
                context!!,
                retailerList,
                marketPlanDetailList.get(0),
                null
            )
        if (retailerList.size > 0) {
            binding.svRetailers.visibility = View.VISIBLE;
            binding.svRetailers.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.length > 2) {
                        filter(newText);
                    } else if (newText.length == 0) {
                        (binding.rvPlan.adapter as MarketPlanInfoRetailerAdapter).setItemList(
                            globalRetailerList
                        );
                    }
                    return false
                }
            })

        } else {
            binding.svRetailers.visibility = View.GONE;
        }
    }

    private fun setSalesOrderList(salesOrderList: ArrayList<MarketPlanInfoRequestResponse.Result_5>) {
        binding.rvSalesOrder.layoutManager = LinearLayoutManager(activity)
        binding.rvSalesOrder.adapter =
            MarketPlanInfoSalesOrderAdapter(context!!, salesOrderList, null)
    }


    fun filter(text: String) {
        val temp: ArrayList<MarketPlanInfoRequestResponse.Result_4> = ArrayList()
        if (globalRetailerList != null && globalRetailerList.size > 0) {
            for (d in globalRetailerList) {
                if (d.cust_name!!.toLowerCase()!!.contains(text.toLowerCase())) {
                    temp.add(d)
                } else if (d.address != null && d.address!!.toLowerCase()!!
                        .contains(text.toLowerCase())
                ) {
                    temp.add(d)
                }
            }
            //update recyclerview
            if (temp.size > 0) {
                (binding.rvPlan.adapter as MarketPlanInfoRetailerAdapter).setItemList(
                    temp
                );
            } else {
                (binding.rvPlan.adapter as MarketPlanInfoRetailerAdapter).setItemList(
                    globalRetailerList
                );
            }
        }
    }


    private fun setAssignedUserList(assignedUserList: ArrayList<MarketPlanInfoRequestResponse.Result_3>) {
        binding.rvUsers.layoutManager = LinearLayoutManager(activity)
        binding.rvUsers.adapter =
            MarketPlanInfoUsersAdapter(context!!, assignedUserList, null)
    }

    private fun setAttachmentList(docDetailList: ArrayList<MarketPlanInfoRequestResponse.Result_2>) {
        if (docDetailList.size > 0) {
            progressDialog2 = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
            val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
            for (i: Int in docDetailList.indices) {
                val imageRequest = ImageRequest()
                imageBase64?.clear()
                imageRequest.setImgPath(docDetailList.get(i).url!!)
                val call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
                call.enqueue(object : Callback<ImageRequestResponse> {
                    override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                        progressDialog2.dismiss()
                        ConstantVariable.onSNACK(binding.contentMain.scrollView, t.toString())
                    }

                    override fun onResponse(
                        call: Call<ImageRequestResponse>,
                        response: Response<ImageRequestResponse>
                    ) {
                        progressDialog2.dismiss()
                        if (response.body()?.getMsg().equals("success")) {
                            val imageUrl = response.body()?.getImgString() as String
                            imageBase64!!.add(imageUrl)
                            binding.rvAttachment.layoutManager =
                                LinearLayoutManager(activity)
                            binding.rvAttachment.adapter =
                                CustomerAttachmentAdapter(context, imageBase64, false)
                        }
                    }
                })
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setEditApproveList(editApproveList: ArrayList<MarketPlanInfoRequestResponse.Result_6>) {
        if (editApproveList[0].approve.equals("Y", true)) {
            binding.btnapprove.visibility = View.VISIBLE
        } else {
            binding.btnapprove.visibility = View.GONE
        }

        if (editApproveList[0].approve.equals("Y", true)) {
            binding.btnreject.visibility = View.VISIBLE
        } else {
            binding.btnreject.visibility = View.GONE
        }

        if (editApproveList[0].edit.equals("Y", true)) {
            binding.tvEdtToDate.visibility = View.VISIBLE
        } else {
            binding.tvEdtToDate.visibility = View.GONE
        }

        if (editApproveList[0].status.equals("Y", true)) {
            binding.fabStage.visibility = View.VISIBLE
        } else {
            binding.fabStage.visibility = View.GONE
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.ivCall -> {
                ConstantVariable.sendPhoneCall(
                    this.activity!!,
                    marketPlanDetailList[0].owner_mobile_no!!
                )
                ConstantVariable.animationEffect(binding.ivCall, activity!!)
            }
            binding.ivEmail -> {
                ConstantVariable.sendEmail(
                    this.activity!!,
                    marketPlanDetailList[0].owner_email_id!!,
                    "9672655444"
                )
                ConstantVariable.animationEffect(binding.ivEmail, activity!!)
            }
            binding.btnreject -> {
                ConstantVariable.animationEffect(binding.btnreject, activity!!)
//                showDialog(context as Activity, "Rejected")
            }
            binding.btnapprove -> {
                ConstantVariable.animationEffect(binding.btnapprove, activity!!)
                updateMarketPlanStage(v!!, "In Progress", "")
            }

            binding.tvEdtToDate -> {
                ConstantVariable.animationEffect(binding.tvEdtToDate, activity!!)
                setEditToDateDialogue()
            }

            binding.fabStage -> {
                ConstantVariable.animationEffect(binding.fabStage, activity!!)
                setStageDialogue()
            }

        }
    }

    private fun setStageDialogue() {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_expense_travelling, null)
        val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
        //show dialog
        val displayMetric = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetric)
        val layoutParams = WindowManager.LayoutParams()
        mAlertDialogStage = mBuilder.show()

        layoutParams.copyFrom(mAlertDialogStage.getWindow()!!.getAttributes());
        rvStage = mAlertDialogStage.findViewById<RecyclerView>(R.id.rvTravelling)!!
        rvStage.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvStage.adapter =
            StringListAdapter(context, stageList, object : StringListAdapter.BtnClickListener {
                override fun onBtnClick(position: Int, llRow: LinearLayout, type: String) {
                    llRow.setBackgroundColor(Color.parseColor("#d6d6d6"))
                    mAlertDialogStage.dismiss()
                    updateMarketPlanStage(rvStage, type, "")
                    stage_global = type;
                }
            })
    }
    private fun updateMarketPlanStage(view: View, msg: String, remark: String) {
        progressDialog3 = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        resultList = ArrayList();
        val result1 = UpdateMarketPlanStateRequestResponseModel.Result()
        val result2 = UpdateMarketPlanStateRequestResponseModel()

        result1._mkp_id = marketplanListObj.id!!.toIntOrNull()!!
        result1.stage = msg
        result1.approvedBy = preferencesHelper!!.userId!!.toIntOrNull()!!
        if (msg.equals("In Progress", true)) {
            result1.isApproved = "1"
            result1._reject_reason = ""
        } else {
            result1.isApproved = "0"
            result1._reject_reason = remark
        }
        resultList.add(result1)
        result2.result = resultList;

        val call: Call<UpdateMarketPlanStateRequestResponseModel>? =
            apiInterface!!.UpdateMarketPlanState(result2)

        call!!.enqueue(object : Callback<UpdateMarketPlanStateRequestResponseModel> {
            override fun onFailure(
                call: Call<UpdateMarketPlanStateRequestResponseModel>,
                t: Throwable
            ) {
                progressDialog3!!.dismiss()
                ConstantVariable.onSNACK(view, "Something went wrong")
            }

            override fun onResponse(
                call: Call<UpdateMarketPlanStateRequestResponseModel>,
                response: Response<UpdateMarketPlanStateRequestResponseModel>
            ) {
                progressDialog3!!.dismiss()
                try {
                    if (response?.body()?.status.equals("Success", ignoreCase = true)) {
                        ConstantVariable.onToastSuccess(context!!, response.body()!!.message!!)
                        getMarketPlanInfo()
                    } else {
                        ConstantVariable.onToast(context!!, response.body()!!.message!!)
                    }
                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(view, "Something went wrong!!")
                }
            }
        })
    }

    private fun setEditToDateDialogue() {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.custom_alert_edit_todate_layout, null)
        val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
        //show dialog
        val displayMetric = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetric)
        val layoutParams = WindowManager.LayoutParams()
        mAlertDialogEditToDate = mBuilder.show()

        layoutParams.copyFrom(mAlertDialogEditToDate.getWindow()!!.getAttributes());

        val tvFromDate = mAlertDialogEditToDate.findViewById<TextView>(R.id.tvFromDate) as TextView
        val tvToDate = mAlertDialogEditToDate.findViewById<TextView>(R.id.tvToDate) as TextView
        val tvCancel = mAlertDialogEditToDate.findViewById<TextView>(R.id.tvCancel) as TextView
        val tvDone = mAlertDialogEditToDate.findViewById<TextView>(R.id.tvDone) as TextView

        tvFromDate.setText(
            ConstantVariable.parseDateToddMMyyyyWithoutTime(
                marketPlanDetailList.get(
                    0
                ).startdate!!
            )
        )
        yyddmDate_fromDate.date = marketPlanDetailList.get(0).startdate!!
        tvToDate.setText(ConstantVariable.parseDateToddMMyyyyWithoutTime(marketPlanDetailList.get(0).enddate!!))
        yyddmDate_toDate.date = marketPlanDetailList.get(0).enddate!!

        tvFromDate.setOnClickListener {
            ConstantVariable.animationEffect(tvFromDate, activity!!)
            ConstantVariable.datePickerWithYYDDMMRequest_date_minimum_date(
                context!!,
                tvFromDate!!,
                yyddmDate_fromDate
            )
        }
        tvToDate.setOnClickListener {
            ConstantVariable.animationEffect(tvToDate, activity!!)
            ConstantVariable.datePickerWithYYDDMMRequest_date_minimum_date(
                context!!,
                tvToDate!!,
                yyddmDate_toDate
            )
        }
        tvCancel.setOnClickListener {
            mAlertDialogEditToDate.dismiss()
        }
        tvDone.setOnClickListener {
            if ((yyddmDate_fromDate.date != null && !yyddmDate_fromDate.date.equals(""))
                && (yyddmDate_toDate.date != null && !yyddmDate_toDate.date.equals(""))
            ) {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val fromdate: Date = sdf.parse(yyddmDate_fromDate.date)
                val todate: Date = sdf.parse(yyddmDate_toDate.date)
                if (todate.before(fromdate)) {
                    ConstantVariable.onToastSuccess(
                        context!!,
                        "To date should not be before From Date"
                    )
                } else {
                    updateMarketPlanToDate(it, yyddmDate_fromDate.date, yyddmDate_toDate.date)
                }
            } else {
                ConstantVariable.onToastSuccess(context!!, "Please Select End Date")
            }
        }

        if (editApproveList[0].edit_fromdate.equals("Y", true)) {
            tvFromDate.isEnabled = true
            tvFromDate.setTextColor(resources.getColor(R.color.colorBlack))
        } else {
            tvFromDate.isEnabled = false
            tvFromDate.setTextColor(resources.getColor(R.color.colorUnpressed))
        }
    }
    private fun updateMarketPlanToDate(view: View, fromdate: String, todate: String) {
        progressDialog4 = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        resultList = ArrayList();
        val result1 = UpdateMarketPlanStateRequestResponseModel.Result()
        val result2 = UpdateMarketPlanStateRequestResponseModel()

        result1.mkt_pln_id = marketplanListObj.id!!.toIntOrNull()!!
        result1.user_id = preferencesHelper!!.userId!!.toIntOrNull()!!
        result1.from_date = fromdate
        result1.to_date = todate
        resultList.add(result1)
        result2.result = resultList;

        val call: Call<UpdateMarketPlanStateRequestResponseModel> =
            apiInterface!!.UpdateMarketPlanToDate(result2)

        call!!.enqueue(object : Callback<UpdateMarketPlanStateRequestResponseModel> {
            override fun onFailure(
                call: Call<UpdateMarketPlanStateRequestResponseModel>,
                t: Throwable
            ) {
                progressDialog4.dismiss()
                ConstantVariable.onSNACK(view, "Something went wrong")
            }

            override fun onResponse(
                call: Call<UpdateMarketPlanStateRequestResponseModel>,
                response: Response<UpdateMarketPlanStateRequestResponseModel>
            ) {
                progressDialog4.dismiss()
                try {
                    if (response?.body()?.status.equals("Success", ignoreCase = true)) {
                        ConstantVariable.onToastSuccess(context!!, response.body()!!.message!!)
                        //binding!!.contentMain!!.tvstatus.text = msg;
                        if (mAlertDialogEditToDate != null) {
                            mAlertDialogEditToDate.dismiss()
                        }
                        getMarketPlanInfo()
                    } else {
                        ConstantVariable.onToast(context!!, response.body()!!.message!!!!)
                    }
                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(view, "Something went wrong!!")
                }
            }
        })
    }


}

