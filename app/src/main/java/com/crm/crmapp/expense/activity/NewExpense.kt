package com.crm.crmapp.expense.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.activity.CameraActivity
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.SpannableStringBuilder
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.*
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.customer.adapter.CustomerAttachmentAdapter
import com.crm.crmapp.expense.adapter.ExpenseTravellingAdapter
import com.crm.crmapp.expense.model.*
import com.crm.crmapp.order.model.*
import com.crm.crmapp.order.util.CustomDate
import com.crm.crmapp.order.util.PreferencesHelper
import kotlinx.android.synthetic.main.layout_expense_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class NewExpense : AppCompatActivity(), View.OnClickListener {


    private lateinit var rvList: RecyclerView
    private lateinit var Apply: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvHintOrder: TextView
    private lateinit var tvHintName: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvHintOrderDetail: TextView
    private lateinit var tvHintDate: TextView
    private lateinit var tvHintDoc: TextView
    private lateinit var ivAdd: ImageView
    private lateinit var etRemarks: EditText
    private lateinit var tvType: TextView
    private lateinit var tvname: TextView
    private lateinit var EdtAmountm: EditText
    private lateinit var context: Context
    private lateinit var mAlertDialog: AlertDialog
    private lateinit var rvTravelling: RecyclerView
    private lateinit var operation: String
    private lateinit var expDetailResult: ExpenseDetailByIDResult1
    private var msg: String = ""
    private var userId: Int = 0
    private var typeId: Int = 0
   // private lateinit var progressDialog: ProgressDialog
   // private lateinit var progressDialog2: ProgressDialog
    private lateinit var expenseTypeList: ArrayList<ExpenseTypeResult>
    private var preferencesHelper: PreferencesHelper? = null
    private var docDetailList: ArrayList<DocDetailModel>? = null
    private var resultList1: ArrayList<SaveExpenseResult1>? = null
    private var resultList2: ArrayList<SaveExpenseResult2>? = null
    var imageBase64: ArrayList<String>? = null
    lateinit var yyddmDate: CustomDate
    var expId_global: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expense)
        context = this@NewExpense
        docDetailList = ArrayList()
        expenseTypeList = ArrayList()
        resultList1 = ArrayList()
        resultList2 = ArrayList()
        imageBase64 = ArrayList()
        yyddmDate = CustomDate()
        setId()
        preferencesHelper = PreferencesHelper(this)

        getExpenseTypeList()
        setCurrentDate()
        if (intent != null && intent.getStringExtra(ConstantVariable.TAG_FROM) != null) {
            if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals("edit", true)) {
                operation = intent.getStringExtra(ConstantVariable.TAG_FROM) as String
                tvTitle.setText("Update Expense")
                Apply.setText("Update")

                if (intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) != null) {
                    expDetailResult =
                            intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) as ExpenseDetailByIDResult1
                    var expDetailResult2 =
                        intent.getSerializableExtra(ConstantVariable.TAG_OBJECT2) as ArrayList<ExpenseDetailByIDResult2>
                    if (expDetailResult != null) {
                        expId_global = expDetailResult.getId()
                        var indiandate = ConstantVariable.parseDateToyyyyMMdd(expDetailResult.expDate)
                        yyddmDate.setDate(indiandate)
                        tvDate.setText(ConstantVariable.parseDateToddMMyyyyWithoutTime(expDetailResult.expDate))
                        EdtAmountm.setText(expDetailResult.getExpAmount().toString())
                        etRemarks.setText(expDetailResult.getRemarks().toString())
                        typeId = expDetailResult.getExpTypeId()
                        getUserDetailByUserId(expDetailResult.getUserId())
                    }
                    if (expDetailResult2 != null && expDetailResult2.size > 0) {
                        getRequestApiForImage(expDetailResult2)
                    }
                }
            } else if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals("new", true)) {
                operation = intent.getStringExtra(ConstantVariable.TAG_FROM) as String
                tvTitle.setText("New Expense")
                Apply.setText("Save")
                if (preferencesHelper!!.getObject() != null) {
                    var userData = preferencesHelper!!.getObject()
                    tvname?.setText(userData.getFName() + " " + userData.getLName())
                    userId = userData.getUserId()!!
                }
                setAttachmentAdapter()
            }
        }
        //   setSpinner()
    }

    private fun setId() {
        rvList = findViewById<RecyclerView>(R.id.rvList)
        tvTitle = findViewById<TextView>(R.id.tvTitle)
        Apply = findViewById<TextView>(R.id.Apply)
        tvCancel = findViewById<TextView>(R.id.tvCancel)
        tvHintName = findViewById<TextView>(R.id.tvHintName)
        addStar(tvHintName.text.toString(), tvHintName)
        tvHintOrder = findViewById<TextView>(R.id.tvHintOrder)
        tvHintDate = findViewById<TextView>(R.id.tvHintDate)
        tvname = findViewById<TextView>(R.id.tvname)
        EdtAmountm = findViewById<EditText>(R.id.EdtAmount)
        addStar(tvHintDate.text.toString(), tvHintDate)
        tvHintDoc = findViewById<TextView>(R.id.tvHintDoc)
        etRemarks = findViewById<EditText>(R.id.etRemarks)
        tvType = findViewById<TextView>(R.id.tvType)
        addStar(tvHintDoc?.text.toString(), tvHintDoc)
        ivAdd = findViewById<ImageView>(R.id.ivAdd)
        tvHintOrderDetail = findViewById<TextView>(R.id.tvHintOrderDetail)
        tvDate = findViewById<TextView>(R.id.tvDate)

        // tvHintName.setText(R.string.expenseBy)
        tvHintOrder.setText(R.string.expenseBy)
        //click listeners
        ivAdd.setOnClickListener(this)
        tvDate.setOnClickListener(this)
        Apply.setOnClickListener(this)
        tvType.setOnClickListener(this)
        tvCancel.setOnClickListener(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ConstantVariable.resultCode) {
            if (resultCode == Activity.RESULT_OK) {
                var docListDetail = data?.extras?.get(ConstantVariable.docDetailModel) as DocDetailModel
                if (docListDetail!=null) {
                    docDetailList?.add(docListDetail)

                }else {
                    docListDetail=preferencesHelper!!.getObjectImage()
                    docDetailList?.add( preferencesHelper!!.getObjectImage())
                }

                Log.e("docDetailList", "docDetailList" + docDetailList?.size)
                //imageBase64?.clear()
                if (imageBase64 != null) {
                    imageBase64!!.add(docListDetail.uri)
                }
                setAttachmentAdapter()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            ivAdd -> {

                ConstantVariable.animationEffect(ivAdd, this!!)
                val intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, ConstantVariable.resultCode)
            }
            tvDate -> {
                ConstantVariable.animationEffect(tvDate, this!!)
//                ConstantVariable.datePickerWithYYDDMMRequest(this, tvDate, yyddmDate)   // Previous
                ConstantVariable.selectDatePickerForNewExpense(this, tvDate, yyddmDate)   // Anil
            }
            Apply -> {
                ConstantVariable.animationEffect(Apply, this!!)
                if (ConstantVariable.verifyAvailableNetwork(context as Activity)) {
                    if (isValid()) {
                        if (operation.equals("edit", ignoreCase = true)) {
                            if (expId_global != 0) {
                                updateExpense(v, expId_global, 1)
                            } else {
                                ConstantVariable.onSNACK(v, "Invalid Customer Id")
                            }

                        } else if (operation.equals("new", ignoreCase = true)) {
                            saveExpense(v)
                        }
                    } else {
                        ConstantVariable.onSNACK(v, msg)
                    }
                } else {
                    ///  insertCustomerInDB()
                }
            }
            tvType -> {
                ConstantVariable.animationEffect(tvType, this!!)
                setDialogue()
            }
            tvCancel -> {
                ConstantVariable.animationEffect(tvCancel, this!!)
                finish()
            }
        }
    }

    private fun updateExpense(view: View, expid: Int, isActive: Int) {
        resultList1?.clear()
        resultList2?.clear()
        val progressDialog =  ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        val result1 = SaveExpenseResult1()
        result1?.setExpId(expid)
        result1?.setUserId(userId)
        result1?.setExpType(typeId)
        result1?.setExpDate(yyddmDate.getDate())
        result1?.setExpAmount(Integer.parseInt(EdtAmountm.text.toString()))
        result1?.setRemarks(etRemarks.text.toString())
        result1?.setIsactive(isActive)
        resultList1!!.add(result1)

        var saveExpenseRequest = SaveExpenseRequestModel()
        saveExpenseRequest.setResult1(this!!.resultList1!!)

        for (i: Int in imageBase64!!.indices) {
            var result2 = SaveExpenseResult2()
            if (imageBase64 != null)
                result2!!.setExpAttachment(imageBase64!!.get(i))
            result2!!.setType("image")
            result2!!.setExtension(".jpg")
            resultList2!!.add(result2!!)
        }
        saveExpenseRequest.setResult2(this!!.resultList2!!)

        var call: Call<SaveExpenseResponseModel>? = apiInterface!!.SaveExpense(saveExpenseRequest)
        call!!.enqueue(object : Callback<SaveExpenseResponseModel> {
            override fun onFailure(call: Call<SaveExpenseResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(view, "Something went wrong")
            }

            override fun onResponse(
                call: Call<SaveExpenseResponseModel>,
                response: Response<SaveExpenseResponseModel>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        ConstantVariable.onToastSuccess(context, response.body()!!.getMessage()!!)
                        finish()
                    } else {
                        ConstantVariable.onSNACK(view, response.body()!!.getMsg()!!)
                    }
                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(view, "Something went wrong!!")
                }
            }
        })
    }

    private fun saveExpense(view: View) {
        resultList1?.clear()
        resultList2?.clear()
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        val result1 = SaveExpenseResult1()
        result1?.setExpId(0)
        result1?.setUserId(userId)
        result1?.setExpType(typeId)
        result1?.setExpDate(yyddmDate.getDate())
        result1?.setExpAmount(Integer.parseInt(EdtAmountm.text.toString()))
        result1?.setRemarks(etRemarks.text.toString())
        result1?.setIsactive(1)

        resultList1?.add(result1!!)

        var saveExpenseRequest = SaveExpenseRequestModel()
        saveExpenseRequest.setResult1(this!!.resultList1!!)

        for (i: Int in imageBase64!!.indices) {
            var result2 = SaveExpenseResult2()
            if (imageBase64 != null)
                result2!!.setExpAttachment(imageBase64!!.get(i))
            result2!!.setType("image")
            result2!!.setExtension(".jpg")
            resultList2!!.add(result2!!)
        }

        saveExpenseRequest.setResult2(this!!.resultList2!!)

        var call: Call<SaveExpenseResponseModel>? = apiInterface!!.SaveExpense(saveExpenseRequest)

        call!!.enqueue(object : Callback<SaveExpenseResponseModel> {
            override fun onFailure(call: Call<SaveExpenseResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(view, "Something went wrong")
            }

            override fun onResponse(
                call: Call<SaveExpenseResponseModel>,
                response: Response<SaveExpenseResponseModel>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        /*   preferencesHelper!!.orderId=(response?.body()?.getOrderid())
                           ConstantVariable.onSNACK(tvDate, response.body()!!.getMsg()!!)*/
                        ConstantVariable.onToastSuccess(context, response.body()!!.getMessage()!!)
                        finish()
                    } else {
                        ConstantVariable.onSNACK(view, response.body()!!.getMsg()!!)
                    }
                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(view, "Something went wrong!!")
                }
            }
        })
    }

    fun isValid(): Boolean {
        if (tvType.text == null || tvType.text.toString().equals("") || tvType.text.toString().equals("Select")) {
            msg = "Please Select Expense Type"
            return false
        } else if (tvDate.text.toString() == null || tvDate.text.toString().contains("--/--/--")) {
            msg = getString(R.string.date_empty)
            return false
        } else if (yyddmDate.getDate() == null || yyddmDate.getDate().toString().equals("")) {
            msg = "Please Select Date"
            return false
        } else if (EdtAmountm.text == null || EdtAmountm.text.toString().equals("")) {
            msg = "Please fill Amount"
            return false
        }

        return true
    }

    fun addStar(simple: String, tvHintName: TextView) {

        val colored = "*"
        val builder = SpannableStringBuilder()
        builder.append(simple)
        val start = builder.length
        builder.append(colored)
        val end = builder.length

        builder.setSpan(
            ForegroundColorSpan(Color.RED), simple.length, builder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvHintName.setText(builder)
    }

    private fun setDialogue() {
        val builder = AlertDialog.Builder(context!!)
        val alertDialog: AlertDialog
        // Set the alert dialog title
        // builder.setTitle("App")
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_expense_travelling, null)
        val mBuilder = AlertDialog.Builder(context!!)
            .setView(mDialogView)
        //show dialog
        mAlertDialog = mBuilder.show()
        val displayMetric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetric)
        var width = displayMetric.widthPixels
        var height = displayMetric.heightPixels        //AlertDialogBuilder

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(mAlertDialog.getWindow()!!.getAttributes());

        /*val dialogWindowWidth = (width * 0.9f.toInt())
        // Set alert dialog height equal to screen height 70%
        val dialogWindowHeight = (height * 0.9f.toInt())

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        mAlertDialog.getWindow().setAttributes(layoutParams)*/

        rvTravelling = mAlertDialog.findViewById<RecyclerView>(R.id.rvTravelling)!!
        rvTravelling.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvTravelling.adapter =
                ExpenseTravellingAdapter(context, expenseTypeList, object : ExpenseTravellingAdapter.BtnClickListener {
                    override fun onBtnClick(position: Int, llRow: LinearLayout, type: String) {
                        llRow.setBackgroundColor(Color.parseColor("#d6d6d6"))
                        mAlertDialog.dismiss()
                        tvType.setText(type)
                        typeId = expenseTypeList.get(position).getId()
                        //   Toast.makeText(activity,"aa",Toast.LENGTH_LONG).show()
                    }
                })
    }

    private fun getExpenseTypeList() {
       val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<ExpenseTypeResponseModel>? = apiInterface!!.getExpenseTypeList()
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<ExpenseTypeResponseModel> {
            override fun onFailure(call: Call<ExpenseTypeResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvCancel, "Something went wrong")
            }

            override fun onResponse(
                call: Call<ExpenseTypeResponseModel>,
                response: Response<ExpenseTypeResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    expenseTypeList = response.body()?.getResult() as ArrayList<ExpenseTypeResult>
                    if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals(
                            "edit",
                            true
                        ) && expDetailResult != null
                    ) {
                        for (i in expenseTypeList.indices) {
                            if (expenseTypeList[i].id == expDetailResult.expTypeId) {
                                tvType.setText(expenseTypeList[i].expName)
                                break;
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setCurrentDate() {

        val c = Calendar.getInstance()
        val yy = c.get(Calendar.YEAR)
        val mm = c.get(Calendar.MONTH)
        val dd = c.get(Calendar.DAY_OF_MONTH)
        val mFormat = DecimalFormat("00");
        // set current date into textview
        tvDate.setText(
            StringBuilder()
                // Month is 0 based, just add 1
                .append(mFormat.format(dd)).append("-").append(mFormat.format(mm + 1)).append("-")
                .append(yy)
        )
        yyddmDate.setDate("" + yy + "-" + mFormat.format(mm + 1) + "-" + mFormat.format(dd))
    }

    fun setAttachmentAdapter() {
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = CustomerAttachmentAdapter(this, imageBase64, true)
    }

    private fun getRequestApiForImage(docDetailList: ArrayList<ExpenseDetailByIDResult2>) {
        val progressDialog2 = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        for (i: Int in docDetailList.indices) {
            val imageRequest = ImageRequest()
            //imageBase64?.clear()
            imageRequest.setImgPath(docDetailList.get(i).getUrl()!!)
            val call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
            call.enqueue(object : Callback<ImageRequestResponse> {
                override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                    progressDialog2.dismiss()
                    ConstantVariable.onSNACK(rvList, t.toString())
                }

                override fun onResponse(call: Call<ImageRequestResponse>, response: Response<ImageRequestResponse>) {
                    progressDialog2.dismiss()
                    if (response.body()?.getMsg().equals("success")) {
                        val imageUrl = response.body()?.getImgString() as String
                        imageBase64!!.add(imageUrl)
                        setAttachmentAdapter()
                    }
                }
            })
        }
    }

    fun getUserDetailByUserId(userid: Int) {
        //  progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var searchExpenseRequestModel = SearchExpenseResult()
        searchExpenseRequestModel.setUserId(userid)
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
                            var userDetailResult = userDetails.get(0)
                            tvname.text = userDetailResult.getFName() + " " + userDetailResult.getLName()
                            userId = userDetailResult.getUserId()!!
                        }
                    } else {
                        ConstantVariable.onSNACK(scrollView, "Something went wrong")
                    }
                }
            }
        })
    }

}