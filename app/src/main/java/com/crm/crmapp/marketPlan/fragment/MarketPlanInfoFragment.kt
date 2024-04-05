package com.crm.crmapp.marketPlan.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import androidx.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import android.util.DisplayMetrics
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.customer.adapter.CustomerAttachmentAdapter
import com.crm.crmapp.databinding.FragmentMarketPlanInfoBinding
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
import kotlin.collections.ArrayList

class MarketPlanInfoFragment : Fragment(), View.OnClickListener {

    var binding: FragmentMarketPlanInfoBinding? = null
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
        if (bundle != null && bundle.getSerializable(ConstantVariable.TAG_OBJECT) != null) {
            from = bundle!!.getString(ConstantVariable.TAG_FROM).toString();
            if (from.equals("from_marketPlan_list", true)
                && bundle!!.getSerializable(ConstantVariable.TAG_OBJECT) != null
            ) {
                marketplanListObj =
                    bundle.getSerializable(ConstantVariable.TAG_OBJECT) as MarketPlanListRequestResponse.Result
            } else if (from.equals("from_notification_detail", true)) {
                marketPlanIdObj = bundle!!.getInt(ConstantVariable.TAG_OBJECT)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        binding = DataBindingUtil.inflate<FragmentMarketPlanInfoBinding>( inflater, R.layout.fragment_market_plan_info, container,false)

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_market_plan_info,container,false)
        val view = binding!!.root
        imageBase64 = java.util.ArrayList()
        stageList = ArrayList();
        stageList.add("In Progress")
        stageList.add("Inactive")
        stageList.add("Completed")
        yyddmDate_fromDate = CustomDate()
        yyddmDate_toDate = CustomDate()
        getMarketPlanInfo()
        setListenersValue()
        return view
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

        var tvFromDate = mAlertDialogEditToDate.findViewById<TextView>(R.id.tvFromDate) as TextView
        var tvToDate = mAlertDialogEditToDate.findViewById<TextView>(R.id.tvToDate) as TextView
        var tvCancel = mAlertDialogEditToDate.findViewById<TextView>(R.id.tvCancel) as TextView
        var tvDone = mAlertDialogEditToDate.findViewById<TextView>(R.id.tvDone) as TextView

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

    private fun updateMarketPlanStage(view: View, msg: String, remark: String) {
        progressDialog3 = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        resultList = ArrayList();
        val result1 = UpdateMarketPlanStateRequestResponseModel.Result()
        val result2 = UpdateMarketPlanStateRequestResponseModel()

        result1?._mkp_id = marketplanListObj.id!!.toIntOrNull()!!
        result1?.stage = msg
        result1?.approvedBy = preferencesHelper!!.userId!!.toIntOrNull()!!
        if (msg.equals("In Progress", true)) {
            result1?.isApproved = "1"
            result1?._reject_reason = ""
        } else {
            result1?.isApproved = "0"
            result1?._reject_reason = remark
        }
        resultList.add(result1)
        result2.result = resultList;

        var call: Call<UpdateMarketPlanStateRequestResponseModel>? =
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
                        //binding!!.contentMain!!.tvstatus.text = msg;
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

    private fun updateMarketPlanToDate(view: View, fromdate: String, todate: String) {
        progressDialog4 = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        resultList = ArrayList();
        val result1 = UpdateMarketPlanStateRequestResponseModel.Result()
        val result2 = UpdateMarketPlanStateRequestResponseModel()

        result1?.mkt_pln_id = marketplanListObj.id!!.toIntOrNull()!!
        result1?.user_id = preferencesHelper!!.userId!!.toIntOrNull()!!
        result1?.from_date = fromdate
        result1?.to_date = todate
        resultList.add(result1)
        result2.result = resultList;

        var call: Call<UpdateMarketPlanStateRequestResponseModel>? =
            apiInterface!!.UpdateMarketPlanToDate(result2)

        call!!.enqueue(object : Callback<UpdateMarketPlanStateRequestResponseModel> {
            override fun onFailure(
                call: Call<UpdateMarketPlanStateRequestResponseModel>,
                t: Throwable
            ) {
                progressDialog4!!.dismiss()
                ConstantVariable.onSNACK(view, "Something went wrong")
            }

            override fun onResponse(
                call: Call<UpdateMarketPlanStateRequestResponseModel>,
                response: Response<UpdateMarketPlanStateRequestResponseModel>
            ) {
                progressDialog4!!.dismiss()
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

    private fun getMarketPlanInfo() {
        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var id: Int = 0
        if (from.equals("from_marketPlan_list", true)) {
            id = marketplanListObj.id!!.toIntOrNull()!!
        } else if (from.equals("from_notification_detail", true)) {
            id = marketPlanIdObj;
        }

        var call: Call<MarketPlanInfoRequestResponse>? =
            apiInterface!!.getMarketPlanInfo(id, preferencesHelper!!.userId!!.toIntOrNull()!!)
        call!!.enqueue(object : Callback<MarketPlanInfoRequestResponse> {
            override fun onFailure(call: Call<MarketPlanInfoRequestResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(binding!!.contentMain!!.tvCity, "Something went wrong")
            }

            override fun onResponse(
                call: Call<MarketPlanInfoRequestResponse>,
                response: Response<MarketPlanInfoRequestResponse>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
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

    private fun setMarketPlanDetail(marketPlanDetailList: ArrayList<MarketPlanInfoRequestResponse.Result_1>) {

        binding!!.contentMain!!.tvMarketPlanName.setText(
            marketPlanDetailList.get(0).market_plan_name + " (id:" + marketPlanDetailList.get(
                0
            ).id + ")"
        )
        binding!!.contentMain!!.tvstatus.setText("Status : " + marketPlanDetailList.get(0).stage)
        binding!!.contentMain!!.tv_planOwner.setText(marketPlanDetailList.get(0).owner_full_name)
        binding!!.contentMain!!.tv_ownerprofile.setText(marketPlanDetailList.get(0).owner_user_cat)
        binding!!.contentMain!!.tvPeriod.setText(
            ConstantVariable.parseDateToddMMyyyyWithoutTime(marketPlanDetailList.get(0).startdate!!) + " to " + ConstantVariable.parseDateToddMMyyyyWithoutTime(
                marketPlanDetailList.get(0).enddate!!
            )
        )
        binding!!.contentMain!!.tvstate.setText(marketPlanDetailList.get(0).state_name)
        binding!!.contentMain!!.tvCity.setText(marketPlanDetailList.get(0).city_name)
        binding!!.contentMain!!.tvScheme.setText(marketPlanDetailList.get(0).scheme_name)
        binding!!.contentMain!!.tvCategory.setText(marketPlanDetailList.get(0).cat_name)
        binding!!.contentMain!!.tvRemarks.setText(marketPlanDetailList.get(0).remarks)
        binding!!.contentMain!!.tvDistributor.setText(marketPlanDetailList.get(0).distributor_name)
    }

    private fun setAttachmentList(docDetailList: ArrayList<MarketPlanInfoRequestResponse.Result_2>) {
        if (docDetailList.size > 0) {
            progressDialog2 = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
            var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
            for (i: Int in docDetailList!!.indices) {
                var imageRequest = ImageRequest()
                imageBase64?.clear()
                imageRequest.setImgPath(docDetailList.get(i).url!!)
                var call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
                call!!.enqueue(object : Callback<ImageRequestResponse> {
                    override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                        progressDialog2!!.dismiss()
                        ConstantVariable.onSNACK(binding!!.contentMain!!.scrollView, t.toString())
                    }

                    override fun onResponse(
                        call: Call<ImageRequestResponse>,
                        response: Response<ImageRequestResponse>
                    ) {
                        progressDialog2!!.dismiss()
                        if (response?.body()?.getMsg().equals("success")) {
                            var imageUrl = response.body()?.getImgString() as String
                            imageBase64!!.add(imageUrl)
                            binding!!.contentMain!!.rvAttachment.layoutManager =
                                LinearLayoutManager(activity)
                            binding!!.contentMain!!.rvAttachment.adapter =
                                CustomerAttachmentAdapter(context, imageBase64, false)
                        }
                    }
                })
            }
        }
    }

    private fun setAssignedUserList(assignedUserList: ArrayList<MarketPlanInfoRequestResponse.Result_3>) {
        binding!!.contentMain!!.rvUsers.layoutManager = LinearLayoutManager(activity)
        binding!!.contentMain!!.rvUsers.adapter =
            MarketPlanInfoUsersAdapter(context!!, assignedUserList, null)
    }

    private fun setRetailerList(
        retailerList: ArrayList<MarketPlanInfoRequestResponse.Result_4>,
        marketPlanDetailList: ArrayList<MarketPlanInfoRequestResponse.Result_1>
    ) {
        binding!!.contentMain!!.rvPlan.layoutManager = LinearLayoutManager(activity)
        binding!!.contentMain!!.rvPlan.adapter =
            MarketPlanInfoRetailerAdapter(
                context!!,
                retailerList,
                marketPlanDetailList.get(0),
                null
            )
        if (retailerList.size > 0) {
            binding!!.contentMain!!.svRetailers.visibility = View.VISIBLE;
            binding!!.contentMain!!.svRetailers.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.length > 2) {
                        filter(newText!!);
                    } else if (newText!!.length == 0) {
                        (binding!!.contentMain!!.rvPlan.adapter as MarketPlanInfoRetailerAdapter).setItemList(
                            globalRetailerList
                        );
                    }
                    return false
                }
            })

        } else {
            binding!!.contentMain!!.svRetailers.visibility = View.GONE;
        }
    }

    fun filter(text: String) {
        var temp: ArrayList<MarketPlanInfoRequestResponse.Result_4> = ArrayList()
        if (globalRetailerList != null && globalRetailerList.size > 0) {
            for (d in globalRetailerList) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
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
                (binding!!.contentMain!!.rvPlan.adapter as MarketPlanInfoRetailerAdapter).setItemList(
                    temp
                );
            } else {
                (binding!!.contentMain!!.rvPlan.adapter as MarketPlanInfoRetailerAdapter).setItemList(
                    globalRetailerList
                );
            }
        }
    }


    private fun setSalesOrderList(salesOrderList: ArrayList<MarketPlanInfoRequestResponse.Result_5>) {
        binding!!.contentMain!!.rvSalesOrder.layoutManager = LinearLayoutManager(activity)
        binding!!.contentMain!!.rvSalesOrder.adapter =
            MarketPlanInfoSalesOrderAdapter(context!!, salesOrderList, null)
    }

    @SuppressLint("RestrictedApi")
    private fun setEditApproveList(editApproveList: ArrayList<MarketPlanInfoRequestResponse.Result_6>) {
        if (editApproveList[0].approve.equals("Y", true)) {
            binding!!.contentMain!!.btnapprove.visibility = View.VISIBLE
        } else {
            binding!!.contentMain!!.btnapprove.visibility = View.GONE
        }

        if (editApproveList[0].approve.equals("Y", true)) {
            binding!!.contentMain!!.btnreject.visibility = View.VISIBLE
        } else {
            binding!!.contentMain!!.btnreject.visibility = View.GONE
        }

        if (editApproveList[0].edit.equals("Y", true)) {
            binding!!.contentMain!!.tv_edtToDate.visibility = View.VISIBLE
        } else {
            binding!!.contentMain!!.tv_edtToDate.visibility = View.GONE
        }

        if (editApproveList[0].status.equals("Y", true)) {
            binding!!.fabStage!!.visibility = View.VISIBLE
        } else {
            binding!!.fabStage!!.visibility = View.GONE
        }
    }

    private fun setListenersValue() {
        binding!!.contentMain!!.ivCall.setOnClickListener(this)
        binding!!.contentMain!!.ivEmail.setOnClickListener(this)
        binding!!.contentMain!!.btnreject.setOnClickListener(this)
        binding!!.contentMain!!.btnapprove.setOnClickListener(this)
        binding!!.fabStage.setOnClickListener(this)
        binding!!.contentMain!!.tv_edtToDate.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding!!.contentMain!!.ivCall -> {
                ConstantVariable.sendPhoneCall(
                    this!!.activity!!,
                    marketPlanDetailList.get(0).owner_mobile_no!!
                )
                ConstantVariable.animationEffect(binding!!.contentMain!!.ivCall, activity!!)
            }
            binding!!.contentMain!!.ivEmail -> {
                ConstantVariable.sendEmail(
                    this!!.activity!!,
                    marketPlanDetailList.get(0).owner_email_id!!,
                    "9672655444"
                )
                ConstantVariable.animationEffect(binding!!.contentMain!!.ivEmail, activity!!)
            }
            binding!!.contentMain!!.btnreject -> {
                ConstantVariable.animationEffect(binding!!.contentMain!!.btnreject, activity!!)
                showDialog(context as Activity, "Rejected")
            }
            binding!!.contentMain!!.btnapprove -> {
                ConstantVariable.animationEffect(binding!!.contentMain!!.btnapprove, activity!!)
                updateMarketPlanStage(v!!, "In Progress", "")
            }

            binding!!.contentMain!!.tv_edtToDate -> {
                ConstantVariable.animationEffect(binding!!.contentMain!!.tv_edtToDate, activity!!)
                setEditToDateDialogue()
            }

            binding!!.fabStage -> {
                ConstantVariable.animationEffect(binding!!.fabStage, activity!!)
                setStageDialogue()
            }

        }
    }

    fun showDialog(activity: Activity, msg: String) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_alert_reject_expense_layout)

        val tvRejectedOrApproved = dialog.findViewById(R.id.tvRejectedOrApproved) as TextView
        if (msg != null && msg.equals("rejected", ignoreCase = true)) {
            tvRejectedOrApproved.setTextColor(resources.getColor(R.color.colorRed))
        } else if (msg != null && msg.equals("approved", ignoreCase = true)) {
            tvRejectedOrApproved.setTextColor(resources.getColor(R.color.colorGreen))
        }
        tvRejectedOrApproved.setText(msg)
        val text = dialog.findViewById(R.id.edt_comment) as EditText

        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView
        val tvDone = dialog.findViewById(R.id.tvDone) as TextView

        tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        tvDone.setOnClickListener {
            dialog.dismiss()
            if (!text.text.toString().equals("")) {
                updateMarketPlanStage(it, "Rejected", text.text.toString())
            } else {
                ConstantVariable.onSNACK(it, "Please fill remark")
            }
        }

        dialog.show()
    }


}
