package com.crm.crmapp.expense.fragment

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.adapter.CustomerAttachmentAdapter
import com.crm.crmapp.expense.activity.NewExpense
import com.crm.crmapp.expense.model.*
import com.crm.crmapp.order.model.ImageRequest
import com.crm.crmapp.order.model.ImageRequestResponse
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ExpenseInfoFragment : Fragment(), View.OnClickListener {

    private lateinit var viewOfLayout: View

    private lateinit var tv_expenseowner: TextView
    private lateinit var tv_ownerprofile: TextView
    private lateinit var tvType: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvAmount: TextView
    private lateinit var tvRemarks: TextView
    private lateinit var ivCall: ImageView
    private lateinit var ivEmail: ImageView
    private lateinit var ivuserImg: ImageView
    private var userId: Int = 0
    private var mobile: String = ""
    private var email: String = ""
    private lateinit var tvexpenseid: TextView
    private lateinit var commentByApprover: TextView
    private lateinit var tvstatus: TextView
    var imageBase64: ArrayList<String>? = null
    private lateinit var rvAttachment: RecyclerView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var btnedit: Button
    private lateinit var btnapprove: Button
    private lateinit var navigationView: BottomNavigationView
    private lateinit var scrollView: ScrollView
    private lateinit var newExpenseIntent: Intent
    private lateinit var expenseDetailByIdList: ArrayList<ExpenseDetailByIDResult1>
    private lateinit var expenseDetailByIdList2: ArrayList<ExpenseDetailByIDResult2>
    private lateinit var expenseTypeList: ArrayList<ExpenseTypeResult>
    //var progressDialog: ProgressDialog? = null
    var preferencesHelper: PreferencesHelper? = null
    var expenseResult: SearchExpenseResult? = null
    var userDetailResult: GetUserDetailResponseResult? = null
    var bundle: Bundle? = null
    private var resultList1: ArrayList<UpdateExpenseStateRequestResult>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater?.inflate(R.layout.activity_expense_info, container, false)
        expenseTypeList = ArrayList()
        imageBase64 = ArrayList()
        resultList1 = ArrayList()
        setIds()
        getExpenseTypeList()
        if (preferencesHelper!!.getObject() != null) {
            var userData = preferencesHelper!!.getObject()
            userId = userData.getUserId()!!
            mobile = userData.getMobileNo().toString()
            email = userData.getEmailId().toString()
            //getRequestApiForImage(userData.getProfilePicUrl()!!)
        }
        bundle = this.arguments
        if (bundle != null && bundle!!.getString(ConstantVariable.TAG_FROM) != null) {

            if (bundle!!.getString(ConstantVariable.TAG_FROM).equals("from_all_expense", true)
                && bundle!!.getSerializable(ConstantVariable.TAG_OBJECT) != null
            ) {
                expenseResult = bundle!!.getSerializable(ConstantVariable.TAG_OBJECT) as SearchExpenseResult
                if (expenseResult != null) {
                    getExpenseDetailById("expenseResult", "")
                }
            } else if (bundle!!.getString(ConstantVariable.TAG_FROM).equals("from_notification_detail", true)) {
                var expenseId = bundle!!.getInt(ConstantVariable.TAG_OBJECT)
                getExpenseDetailById(
                    ConstantVariable.FirebaseExpense!!,
                    expenseId.toString()
                )
            }
        } else
            if (bundle != null && bundle!!.getString(ConstantVariable.firebase_bean_id) != null) {
                bundle!!.getString(ConstantVariable.firebase_bean_id!!)?.let {
                    getExpenseDetailById(
                        ConstantVariable.FirebaseExpense!!,
                        it
                    )
                }

            }
        setListenersValue()
        return viewOfLayout
    }

    private fun getRequestApiForImage(docDetailList: String) {
        var progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var imageRequest = ImageRequest()
        imageRequest.setImgPath(docDetailList!!)
        var call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
        call!!.enqueue(object : Callback<ImageRequestResponse> {
            override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                progressDialog!!.dismiss()
            }

            override fun onResponse(call: Call<ImageRequestResponse>, response: Response<ImageRequestResponse>) {
                progressDialog!!.dismiss()
                if (response?.body()?.getMsg().equals("success")) {
                    var imageUrl = response.body()?.getImgString() as String
                    preferencesHelper?.image = imageUrl
                    ivuserImg?.setImageBitmap(ConstantVariable.StringToBitMap(imageUrl!!))
                }
            }
        })
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
                updateExpenseState(it, msg, text.text.toString())
            } else {
                ConstantVariable.onSNACK(it, "Please fill remark")
            }

        }

        dialog.show()
    }

    private fun updateExpenseState(view: View, msg: String, comment: String) {
        resultList1?.clear()
        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        val result1 = UpdateExpenseStateRequestResult()
        if (bundle!!.getString(ConstantVariable.TAG_FROM) != null
            && bundle!!.getString(ConstantVariable.TAG_FROM).equals("from_all_expense", true)
        ) {
            var expense = bundle!!.getSerializable(ConstantVariable.TAG_OBJECT) as SearchExpenseResult
            result1?.setExpId(expense.id)
        } else if (bundle!!.getString(ConstantVariable.TAG_FROM) != null
            && bundle!!.getString(ConstantVariable.TAG_FROM).equals("from_notification_detail", true)
        ) {
            var expenseId = bundle!!.getInt(ConstantVariable.TAG_OBJECT)
            result1?.setExpId(expenseId)
        } else {
            result1?.setExpId(0)
        }

        result1?.setFrmUserId(userDetailResult!!.userId)
        result1?.setManagerUserId(userId)
        result1?.setApprovalStage(msg)
        result1?.setCommentsbyApprover(comment)
        resultList1?.add(result1!!)

        var updateExpenseStateRequestModel = UpdateExpenseStateRequestModel()
        updateExpenseStateRequestModel.setResult(this!!.resultList1!!)

        var call: Call<UpdateExpenseStateResponseModel>? =
            apiInterface!!.UpdateExpenseState(updateExpenseStateRequestModel)

        call!!.enqueue(object : Callback<UpdateExpenseStateResponseModel> {
            override fun onFailure(call: Call<UpdateExpenseStateResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(view, "Something went wrong")
            }

            override fun onResponse(
                call: Call<UpdateExpenseStateResponseModel>,
                response: Response<UpdateExpenseStateResponseModel>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        ConstantVariable.onToastSuccess(context!!, response.body()!!.getMessage()!!)
                        navigationView.visibility = View.GONE
                        btnapprove.visibility = View.GONE
                        btnedit.visibility = View.GONE
                        //finish()
                    } else {
                        ConstantVariable.onSNACK(view, response.body()!!.getMessage()!!)
                    }
                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(view, "Something went wrong!!")
                }
            }
        })
    }

    private fun getExpenseTypeList() {
        // progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<ExpenseTypeResponseModel>? = apiInterface!!.getExpenseTypeList()
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<ExpenseTypeResponseModel> {
            override fun onFailure(call: Call<ExpenseTypeResponseModel>, t: Throwable) {
                //  progressDialog!!.dismiss()
                ConstantVariable.onSNACK(scrollView, t.toString())
            }

            override fun onResponse(
                call: Call<ExpenseTypeResponseModel>,
                response: Response<ExpenseTypeResponseModel>
            ) {
                // progressDialog!!.dismiss()
                if (response.body() != null) {
                    expenseTypeList = response.body()?.getResult() as ArrayList<ExpenseTypeResult>
                }
            }
        })
    }

    fun getExpenseDetailById(type: String, id: String) {
        //  progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var searchExpenseRequestModel = SearchExpenseRequestModel()
        if (type.equals(ConstantVariable.FirebaseExpense, ignoreCase = true)) {
            searchExpenseRequestModel.setExpid(id.toIntOrNull())
        } else {
            searchExpenseRequestModel.setExpid(expenseResult?.getId())
        }
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<ExpenseDetailByIDModel>? = apiInterface!!.getExpenseDetailById(searchExpenseRequestModel)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<ExpenseDetailByIDModel> {
            override fun onFailure(call: Call<ExpenseDetailByIDModel>, t: Throwable) {
                // progressDialog!!.dismiss()
                ConstantVariable.onSNACK(scrollView, "Something went wrong")

            }

            override fun onResponse(call: Call<ExpenseDetailByIDModel>, response: Response<ExpenseDetailByIDModel>) {
                // progressDialog!!.dismiss()
                if (response.body() != null) {
                    expenseDetailByIdList = response.body()?.getResult1() as ArrayList<ExpenseDetailByIDResult1>
                    expenseDetailByIdList2 = response.body()?.getResult2() as ArrayList<ExpenseDetailByIDResult2>
                    if (expenseDetailByIdList.size > 0) {

                        tvexpenseid.setText("Expense id :" + expenseDetailByIdList.get(0).id)
                        tvstatus.setText("Status :" + expenseDetailByIdList.get(0).approvalStage)
                        for (i in expenseTypeList!!.indices) {
                            if (expenseTypeList!![i].id == expenseDetailByIdList.get(0).expTypeId) {
                                tvType.setText(expenseTypeList!![i].expName)
                            }
                        }

                        //tvDate.setText(ConstantVariable.parseDateToddMMyyyy(expenseDetailByIdList.get(0).expDate))
                        tvDate.setText(
                            ConstantVariable.parseDateTo_ddMMMyyyy_From_yyyyMMDD_WithoutTime(
                                expenseDetailByIdList.get(0).expDate
                            )
                        )
                        tvAmount.setText(expenseDetailByIdList.get(0).expAmount.toString())
                        tvRemarks.setText(expenseDetailByIdList.get(0).remarks)
                        commentByApprover.setText(expenseDetailByIdList.get(0).commentsbyapprover)

                        getUserDetailByUserId(expenseDetailByIdList.get(0).userId)

                        checkForDifferentUserId(
                            expenseDetailByIdList.get(0).getUserId(),
                            expenseDetailByIdList.get(0).approvalStage.toString()
                        )
                    }
                    if (expenseDetailByIdList2 != null && expenseDetailByIdList2.size > 0) {
                        getRequestApiForImage(expenseDetailByIdList2)
                    }
                }
            }
        })
    }

    fun getUserDetailByUserId(userId: Int) {
        //  progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var searchExpenseRequestModel = SearchExpenseResult()
        searchExpenseRequestModel.setUserId(userId)
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<GetUserDetailResponseModel>? = apiInterface!!.GetUserDetail(searchExpenseRequestModel)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<GetUserDetailResponseModel> {
            override fun onFailure(call: Call<GetUserDetailResponseModel>, t: Throwable) {
                // progressDialog!!.dismiss()
                ConstantVariable.onSNACK(scrollView, "Something went wrong")
            }

            override fun onResponse(
                call: Call<GetUserDetailResponseModel>,
                response: Response<GetUserDetailResponseModel>
            ) {
                // progressDialog!!.dismiss()
                if (response.body() != null) {

                    if (response?.body()?.getStatus().equals("success")) {
                        var userDetails = response.body()?.getResult() as List<GetUserDetailResponseResult>
                        if (userDetails.size > 0) {
                            userDetailResult = userDetails.get(0)
                            tv_expenseowner?.setText(userDetailResult!!.getFName())
                            tv_ownerprofile?.setText(userDetailResult!!.getCategory())
                            getRequestApiForProfileImage(userDetailResult!!.getProfilePicUrl())
                        }
                    } else {
                        ConstantVariable.onSNACK(scrollView, "Something went wrong")
                    }

                }
            }
        })
    }

    private fun setAdapters() {
        rvAttachment.layoutManager = LinearLayoutManager(activity)
        rvAttachment.adapter = CustomerAttachmentAdapter(context, imageBase64, false)
    }

    private fun setListenersValue() {
        ivCall.setOnClickListener(this)
        ivEmail.setOnClickListener(this)
        btnedit.setOnClickListener(this)
        btnapprove.setOnClickListener(this)
    }

    private fun setIds() {
        preferencesHelper = PreferencesHelper(this!!.activity!!)

        tv_expenseowner = viewOfLayout.findViewById(R.id.tv_expenseowner) as TextView
        tv_ownerprofile = viewOfLayout.findViewById(R.id.tv_ownerprofile) as TextView
        tvexpenseid = viewOfLayout.findViewById(R.id.tvexpenseid) as TextView
        tvstatus = viewOfLayout.findViewById(R.id.tvstatus) as TextView
        tvType = viewOfLayout.findViewById(R.id.tvType) as TextView
        tvDate = viewOfLayout.findViewById(R.id.tvDate) as TextView
        tvAmount = viewOfLayout.findViewById(R.id.tvAmount) as TextView
        tvRemarks = viewOfLayout.findViewById(R.id.tvRemarks) as TextView
        commentByApprover = viewOfLayout.findViewById(R.id.commentByApprover) as TextView
        rvAttachment = viewOfLayout.findViewById(R.id.rvAttachment) as RecyclerView
        navigationView = viewOfLayout.findViewById(R.id.navigationView) as BottomNavigationView

        newExpenseIntent = Intent(activity, NewExpense::class.java)
        scrollView = viewOfLayout.findViewById(R.id.scrollView) as ScrollView

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = scrollView.getScrollY() // For ScrollView
            val scrollX = scrollView.getScrollX() // For HorizontalScrollView

            if (scrollY > 0) {
                slideDown(navigationView)
            } else if (scrollY == 0) {
                slideUp(navigationView)
            }
        }
        ivCall = viewOfLayout.findViewById(R.id.ivCall) as ImageView
        ivEmail = viewOfLayout.findViewById(R.id.ivEmail) as ImageView
        ivuserImg = viewOfLayout.findViewById(R.id.ivuserImg) as ImageView
        btnedit = viewOfLayout.findViewById(R.id.btnedit) as Button
        btnapprove = viewOfLayout.findViewById(R.id.btnapprove) as Button
    }

    private fun checkForDifferentUserId(userid: Int, approvalStage: String) {
        if (userId == 0) {
            ConstantVariable.onToast(this!!.context!!, "Invalid Login UserId")
        } else if (userid == 0) {
            ConstantVariable.onToast(this!!.context!!, "Invalid UserId")
        } else if (userid == userId) {
            btnapprove.visibility = View.GONE
            navigationView.visibility = View.GONE
            if (approvalStage.equals("Approved", true)
                || approvalStage.equals("Rejected", true)
            ) {
                btnedit.visibility = View.GONE
                btnapprove.visibility = View.GONE
                navigationView.visibility = View.GONE
            } else {
                btnedit.visibility = View.VISIBLE
                btnapprove.visibility = View.GONE
                navigationView.visibility = View.GONE
            }

        } else {

            if (approvalStage.equals("Approved", true)
                || approvalStage.equals("Rejected", true)
            ) {
                btnedit.visibility = View.GONE
                btnapprove.visibility = View.GONE
                navigationView.visibility = View.GONE
            } else {
                btnedit.visibility = View.VISIBLE
                btnapprove.visibility = View.VISIBLE
                navigationView.visibility = View.VISIBLE
            }


        }
    }

    private fun slideUp(child: BottomNavigationView) {
        child.clearAnimation()
        child.animate().translationY(0f).duration = 200
    }

    private fun slideDown(child: BottomNavigationView) {
        child.clearAnimation()
        child.animate().translationY(navigationView.height.toFloat()).duration = 200
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_reject -> {
                showDialog(context as Activity, "Rejected")
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_approve -> {
                // startActivity(scanIntent)
                showDialog(context as Activity, "Approved")
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_edit -> {
                newExpenseIntent.putExtra(ConstantVariable.TAG_FROM, "edit")
                newExpenseIntent.putExtra(ConstantVariable.TAG_OBJECT, expenseDetailByIdList.get(0))
                newExpenseIntent.putExtra(ConstantVariable.TAG_OBJECT2, expenseDetailByIdList2)
                startActivity(newExpenseIntent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onClick(v: View?) {
        when (v) {
            ivCall -> {
                ConstantVariable.sendPhoneCall(this!!.activity!!, mobile)
                ConstantVariable.animationEffect(ivCall, activity!!)
            }
            ivEmail -> {
                ConstantVariable.sendEmail(this!!.activity!!, email, "9672655444")
                ConstantVariable.animationEffect(ivEmail, activity!!)
            }
            btnedit -> {
                ConstantVariable.animationEffect(btnedit, activity!!)
                newExpenseIntent.putExtra(ConstantVariable.TAG_FROM, "edit")
                newExpenseIntent.putExtra(ConstantVariable.TAG_OBJECT, expenseDetailByIdList.get(0))
                newExpenseIntent.putExtra(ConstantVariable.TAG_OBJECT2, expenseDetailByIdList2)
                startActivity(newExpenseIntent)
                getActivity()!!.onBackPressed();
            }
            btnapprove -> {
                ConstantVariable.animationEffect(btnapprove, activity!!)
                showDialog(context as Activity, "Approved")
            }
        }
    }

    private fun getRequestApiForImage(docDetailList: ArrayList<ExpenseDetailByIDResult2>) {
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
                    ConstantVariable.onSNACK(scrollView, t.toString())
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

    private fun getRequestApiForProfileImage(url: String) {
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var imageRequest = ImageRequest()
        imageRequest.setImgPath(url)
        var call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
        call!!.enqueue(object : Callback<ImageRequestResponse> {
            override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                ConstantVariable.onSNACK(scrollView, t.toString())
            }

            override fun onResponse(call: Call<ImageRequestResponse>, response: Response<ImageRequestResponse>) {
                if (response?.body()?.getMsg().equals("success")) {
                    var imageUrl = response.body()?.getImgString() as String
                    ivuserImg?.setImageBitmap(ConstantVariable.StringToBitMap(imageUrl!!))
                }
            }
        })

    }


}