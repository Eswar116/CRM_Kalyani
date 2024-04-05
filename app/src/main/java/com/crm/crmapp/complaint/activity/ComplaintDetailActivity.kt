package com.crm.crmapp.complaint.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.complaint.model.*
import com.crm.crmapp.customer.model.CustomerResult
import com.crm.crmapp.order.activity.CameraActivity
import com.crm.crmapp.order.activity.SearchCustomer
import com.crm.crmapp.order.adapter.NewOrderAdapter
import com.crm.crmapp.order.model.DocDetailModel
import com.crm.crmapp.order.model.ImageRequest
import com.crm.crmapp.order.model.ImageRequestResponse
import com.crm.crmapp.order.util.CustomDate
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import kotlinx.android.synthetic.main.activity_new_order.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

class ComplaintDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var rvList: RecyclerView
    private lateinit var save: TextView
    private lateinit var tvHintName: TextView
    private lateinit var tvHintOrder: TextView
    private lateinit var tv_name: TextView
    private lateinit var tvHintDate: TextView
    private lateinit var tvHintDoc: TextView
    private lateinit var tvDate: TextView
    private lateinit var ivAdd: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var svCustomer: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvCancel: TextView
    private lateinit var llUpload: LinearLayout
    private lateinit var spType: Spinner
    private lateinit var spSource: Spinner
    private lateinit var spPriority: Spinner
    private lateinit var tvStartDate: TextView
    private lateinit var progressDialog: ProgressDialog
    private var docDetailList: ArrayList<DocDetailModel>? = null
    private var complaintType: ArrayList<ComplaintTypeResponse.Result>? = null
    private var complaintResultList: ArrayList<ComplaintDetailResponse.Result1>? = null
    private var complaintResultList2: ArrayList<ComplaintDetailResponse.Result2>? = null
    val myStrings = arrayOf("Low", "Medium", "High")
    private var complaintType_value: ArrayList<String>? = null
    val source = arrayOf("Email", "Call", "Visit", "Others")
    private var compType: String = ""
    private var compSource: String = ""
    private var compPriority: String = ""
    private var customerId: String = "0"
    private var selectedTypeId: Int? = null
    private var result1: ArrayList<ComplaintSaveRequest.Result1>? = null
    private var resultList2: ArrayList<ComplaintSaveRequest.Result2>? = null
    private var preferencesHelper: PreferencesHelper? = null
    lateinit var yyddmDate: CustomDate
    lateinit var yyddmDate_frmDate: CustomDate
    val myStrings_status = arrayOf("OPEN", "CLOSED")
    private lateinit var spStatus: Spinner
    private var statusStr: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint)
        docDetailList = ArrayList()
        complaintType_value = ArrayList()
        result1 = ArrayList()
        complaintType = ArrayList()
        complaintResultList = ArrayList()
        complaintResultList2 = ArrayList()
        resultList2 = ArrayList()
        yyddmDate = CustomDate()
        yyddmDate_frmDate = CustomDate()
        setIds()

        if (ConstantVariable.verifyAvailableNetwork(this)) {
            getComplaintTypeApi()
        } else {
            ConstantVariable.onSNACK(tvApply, "No Internet Connection")
        }

        setCurrentDate()
        if (intent.getStringExtra(ConstantVariable.TAG_FROM) != null && intent.getStringExtra(
                ConstantVariable.TAG_FROM
            ).equals(
                "from_customerInfo",
                ignoreCase = true
            )
        ) {
            var customerResult =
                intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) as CustomerResult
            svCustomer!!.setText(customerResult.custName)
            customerId = customerResult.custId.toString()
        }
    }

    private fun getComplaintTypeApi() {
        complaintType?.clear()
        complaintType_value?.clear()
        progressDialog = ConstantVariable.showProgressDialog(this, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var jsonObject = JSONObject()
        var call: Call<ComplaintTypeResponse>? = apiInterface!!.complaintList(jsonObject)
        call?.enqueue(object : Callback<ComplaintTypeResponse> {
            override fun onFailure(call: Call<ComplaintTypeResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(ivAdd, "NO Record Found")
            }

            override fun onResponse(
                call: Call<ComplaintTypeResponse>,
                response: Response<ComplaintTypeResponse>
            ) {
                progressDialog!!.dismiss()
                try {
                    complaintType =
                        response.body()?.getResult() as ArrayList<ComplaintTypeResponse.Result>
                    if (complaintType!!.size > 0) {
                        for (i: Int in complaintType?.indices!!) {
                            complaintType_value?.add(complaintType!!.get(i).getTypeName()!!)
                        }
                    }
                    setSpinner()
                } catch (e: Exception) {
                    ConstantVariable.onSNACK(ivAdd, "NO Record Found")
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    private fun setSpinner() {
        spPriority.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, myStrings)
        spType.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, complaintType_value as List<String>)
        spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                try {
                    compType = p0?.getItemAtPosition(0).toString()
                } catch (e: Exception) {
                }
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                compType = p0?.getItemAtPosition(p2).toString()
            }
        }

        spSource.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, source)
        spSource.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                try {
                    compSource = p0?.getItemAtPosition(0).toString()
                } catch (e: Exception) {
                }
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                compSource = p0?.getItemAtPosition(p2).toString()
            }
        }

        spPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                try {
                    compPriority = p0?.getItemAtPosition(0).toString()
                } catch (e: Exception) {
                }
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                compPriority = p0?.getItemAtPosition(p2).toString()
            }
        }

        spStatus.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, myStrings_status)
        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                /* if (p2 != 0) {
                     statusStr = myStrings_status[p2]
                 } else {
                     statusStr = ""
                 }*/
                statusStr = myStrings_status[p2]
            }
        }

        if (ConstantVariable.verifyAvailableNetwork(this)) {
            getDetail()
        } else {
            ConstantVariable.onSNACK(tvDate!!, "No Internet Connection")
        }


    }

    @SuppressLint("SetTextI18n")
    private fun setIds() {
        preferencesHelper = PreferencesHelper(this)
        rvList = findViewById<RecyclerView>(R.id.rvList)
        llUpload = findViewById<LinearLayout>(R.id.llUpload)
        save = findViewById<TextView>(R.id.save)
        ivAdd = findViewById<ImageView>(R.id.ivAdd)
        ivSearch = findViewById<ImageView>(R.id.ivSearch)
        tvCancel = findViewById<TextView>(R.id.tvCancel)
        tvTitle = findViewById(R.id.tvTitle) as TextView
        tv_name = findViewById(R.id.tv_name) as TextView
        tvHintName = findViewById<TextView>(R.id.tvHintName)
        addStar(tvHintName.text.toString(), tvHintName)
        tvHintOrder = findViewById<TextView>(R.id.tvHintOrder)
        tvHintDate = findViewById<TextView>(R.id.tvHintDate)
        tvDate = findViewById<TextView>(R.id.tvDate)
        addStar(tvHintDate.text.toString(), tvHintDate)
        tvHintDoc = findViewById<TextView>(R.id.tvHintDoc)
        addStar(tvHintDoc?.text.toString(), tvHintDoc)
        svCustomer = findViewById(R.id.svCustomer)
        spType = findViewById<Spinner>(R.id.spType)
        spSource = findViewById<Spinner>(R.id.spSource)
        spPriority = findViewById<Spinner>(R.id.spPriority)
        spStatus = findViewById<Spinner>(R.id.spStatus)
        //todo set text
        tvTitle.setText(getString(R.string.complaint_detail))
        save.setText(getString(R.string.update))

        tv_name.setText(preferencesHelper!!.userName)
        tvStartDate = findViewById(R.id.tvStartDate) as TextView
        //todo CLICK LISTENER
        svCustomer.setOnClickListener(this)
        ivAdd.setOnClickListener(this)
        tvDate.setOnClickListener(this)
        save.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        tvStartDate.setOnClickListener(this)
    }

    private fun setAdapter() {
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = NewOrderAdapter(this, docDetailList)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ConstantVariable.resultCode) {
            if (resultCode == Activity.RESULT_OK) {
                val docListDetail =
                    data?.extras?.get(ConstantVariable.docDetailModel) as DocDetailModel
                if (docListDetail != null) {
                    docDetailList?.add(docListDetail)

                } else {
                    docDetailList?.add(preferencesHelper!!.getObjectImage())
                }
                Log.e("docDetailList", "docDetailList" + docDetailList?.size)
                setAdapter()
            }
        } else if (requestCode == ConstantVariable.resultCode_customer) {

            if (data?.extras?.get("CustomerPosition") != null) {
                svCustomer!!.setText(data?.extras?.get("CustomerPosition").toString())
                customerId = data?.extras?.get("CustomerId").toString()


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
            svCustomer -> {
                ConstantVariable.animationEffect(svCustomer, this!!)
                val intent = Intent(this, SearchCustomer::class.java)
                intent.putExtra("NewOrderKey", "NewOrderKey")
                startActivityForResult(intent, ConstantVariable.resultCode_customer)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
            tvDate -> {
                ConstantVariable.animationEffect(tvDate, this!!)
                //ConstantVariable.datePicker(this, tvDate)
                ConstantVariable.datePickerWithYYDDMMRequest(this, tvDate, yyddmDate)
            }
            save -> {
                ConstantVariable.animationEffect(save, this!!)
                //todo to save data api
                checkValidation()
            }
            tvCancel -> {
                ConstantVariable.animationEffect(tvCancel, this!!)
                finish()
            }
            ivSearch -> {
                svCustomer.performClick()
            }
            tvStartDate -> {
                ConstantVariable.animationEffect(tvStartDate, this!!)
                ConstantVariable.datePickerWithYYDDMMRequest(
                    this,
                    tvStartDate,
                    yyddmDate_frmDate
                )
            }
        }
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
        tvHintName?.setText(builder)
    }

    private fun setCurrentDate() {
        val c = Calendar.getInstance()
        var yy = c.get(Calendar.YEAR)
        var mm = c.get(Calendar.MONTH)
        var dd = c.get(Calendar.DAY_OF_MONTH)
        var mFormat = DecimalFormat("00");
        // set current date into textview
        tvDate.setText(
            StringBuilder()
                // Month is 0 based, just add 1
                .append(mFormat.format(dd)).append("-").append(mFormat.format(mm + 1)).append("-")
                .append(yy)
        )
        yyddmDate.setDate("" + yy + "-" + mFormat.format(mm + 1) + "-" + mFormat.format(dd))
    }

    private fun getDetail() {
        complaintResultList?.clear()
        complaintResultList2?.clear()
        if (progressDialog == null) {
            progressDialog = ConstantVariable.showProgressDialog(this, "Loading...")
        }
        var jsonObject = ComplaintDetailRequest()
        if (intent != null) {
            jsonObject.setCompid(intent.getIntExtra(ConstantVariable.online_complaint_id, 0))
        }
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<ComplaintDetailResponse>? = apiInterface!!.getComplaintDetail(jsonObject)
        call?.enqueue(object : Callback<ComplaintDetailResponse> {
            override fun onFailure(call: Call<ComplaintDetailResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(ivAdd, "Something went wrong")
            }

            override fun onResponse(
                call: Call<ComplaintDetailResponse>,
                response: Response<ComplaintDetailResponse>
            ) {
                progressDialog!!.dismiss()
                try {
                    complaintResultList =
                        response.body()?.getResult1() as ArrayList<ComplaintDetailResponse.Result1>
                    complaintResultList2 =
                        response.body()?.getResult2() as ArrayList<ComplaintDetailResponse.Result2>
                    //tvDate.setText(complaintResultList!!.get(0).getComplaintDate())
                    var indiandate =
                        ConstantVariable.parseDateToyyyyMMdd(
                            complaintResultList!!.get(0).getComplaintDate()!!
                        )
                    yyddmDate.setDate(indiandate)
                    tvDate.setText(
                        ConstantVariable.parseDateToddMMyyyyWithoutTime(
                            complaintResultList!!.get(0).getComplaintDate()!!
                        )
                    )

                    var startdate =
                        ConstantVariable.parseDateToyyyyMMdd(complaintResultList!!.get(0).startdate!!)
                    yyddmDate_frmDate.setDate(startdate)
                    tvStartDate.setText(
                        ConstantVariable.parseDateToddMMyyyyWithoutTime(
                            complaintResultList!!.get(0).startdate!!
                        )
                    )

                    etRemarks.setText(complaintResultList!!.get(0).getRemarks())
                    svCustomer.setText(complaintResultList!!.get(0).getCustName())
                    var typeId = complaintResultList!!.get(0).getTypeId()
                    customerId = complaintResultList!!.get(0).getCustId().toString()
                    var complaintSource = complaintResultList!!.get(0).getSourceText()
                    var complaintPriority = complaintResultList!!.get(0).getPriorityText()
                    var status = complaintResultList!!.get(0).status
                    if (typeId != null) {
                        for (item: Int in complaintType!!.indices) {
                            if (complaintType!!.get(item).getId() == typeId) {
                                spType.setSelection(item)
                                break;
                            }
                        }
                    }
                    spPriority.setSelection(myStrings.indexOf(complaintPriority))
                    spSource.setSelection(source.indexOf(complaintSource))
                    spStatus.setSelection(myStrings_status.indexOf(status))
                    statusStr = complaintResultList!!.get(0).status!!

                    if (status.equals("CLOSED", true)) {
                        save.visibility = View.GONE
                    } else {
                        save.visibility = View.VISIBLE
                    }


                    if (complaintResultList2 != null && complaintResultList2!!.size > 0) {
                        getRequestApiForImage(complaintResultList2!!)
                    }
                } catch (e: Exception) {
                    ConstantVariable.onSNACK(ivAdd, "NO Record Found")
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    private fun getRequestApiForImage(docDetailListResult2: ArrayList<ComplaintDetailResponse.Result2>) {
        progressDialog =
            ConstantVariable.showProgressDialog(this@ComplaintDetailActivity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        for (i: Int in docDetailListResult2!!.indices) {
            var imageRequest = ImageRequest()
            docDetailList?.clear()
            imageRequest.setImgPath(docDetailListResult2.get(i).getCompAttachment()!!)
            var call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
            call!!.enqueue(object : Callback<ImageRequestResponse> {
                override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                    progressDialog!!.dismiss()
                    ConstantVariable.onSNACK(tvDate, "Something went wrong")

                }

                override fun onResponse(
                    call: Call<ImageRequestResponse>,
                    response: Response<ImageRequestResponse>
                ) {
                    progressDialog!!.dismiss()
                    if (response?.body()?.getMsg().equals("success")) {
                        var imageUrl = response.body()?.getImgString() as String
                        if (imageUrl != null) {
                            var model = DocDetailModel(imageUrl, "", 0.0, "")
                            docDetailList?.add(model)
                        }
                        setAdapter()
                    }
                }
            })
        }
    }

    private fun saveComplaintData() {
        try {
            result1?.clear()
            resultList2?.clear()
            if (progressDialog == null) {
                progressDialog = ConstantVariable.showProgressDialog(this, "Loading...")
            }
            var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
            var complaintSaveRequest = ComplaintSaveRequest()
            var result1Const = ComplaintSaveRequest.Result1()
            result1Const.setCustId(customerId.toInt())
            result1Const?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
            if (intent != null) {
                result1Const?.setCompId(intent.getIntExtra(ConstantVariable.online_complaint_id, 0))
                Log.e("Comp_id", "" + intent.getIntExtra(ConstantVariable.online_complaint_id, 0))
            }
            result1Const?.setPriorityText(compPriority)
            result1Const?.setRemarks(etRemarks.text.toString())
            result1Const?.setCompDate(yyddmDate.getDate())
            result1Const?.setSourceText(compSource)
            result1Const?.start_date = yyddmDate_frmDate.date.toString()
            result1Const?.status = statusStr
            if (checkTypeId() != null) {
                result1Const?.setTypeId(checkTypeId())
            }
            result1?.add(result1Const)
            complaintSaveRequest.setResult1(this!!.result1!!)
            if (docDetailList?.size!! > 0) {
                for (i: Int in docDetailList!!.indices) {
                    var result2 = ComplaintSaveRequest.Result2()
                    result2!!.setCompAttachment(
                        ConstantVariable.convertByteArray(
                            ConstantVariable.StringToBitMap(
                                docDetailList!!.get(i).uri
                            )!!
                        )
                    )
                    result2.setExtension("")
                    result2.setType("")
                    resultList2!!.add(result2!!)
                }
            }
            complaintSaveRequest.setResult2(this!!.resultList2!!)
            var call: Call<ComplaintSaveResponse>? =
                apiInterface!!.saveComplaint(complaintSaveRequest)
            call?.enqueue(object : Callback<ComplaintSaveResponse> {
                override fun onFailure(call: Call<ComplaintSaveResponse>, t: Throwable) {
                    progressDialog!!.dismiss()
                    ConstantVariable.onSNACK(tvDate, "NO Record Found")

                }

                override fun onResponse(
                    call: Call<ComplaintSaveResponse>,
                    response: Response<ComplaintSaveResponse>
                ) {
                    progressDialog!!.dismiss()
                    try {
                        ConstantVariable.onToastSuccess(baseContext, response.body()?.getMsg()!!)
                        finish()
                    } catch (e: Exception) {
                        ConstantVariable.onSNACK(tvDate, "NO Record Found")
                    }
                }
            })
        } catch (e: Exception) {
            ConstantVariable.onSNACK(tvDate, "Server Error")
        }
    }

    private fun checkTypeId(): Int {
        for (i: Int in complaintType?.indices!!) {
            if (complaintType!!.get(i).getTypeName().equals(compType, ignoreCase = true)) {
                selectedTypeId = complaintType!!.get(i).getId()!!
            }
        }
        return this!!.selectedTypeId!!
    }

    private fun checkValidation() {
        if (svCustomer.text.toString().equals("", ignoreCase = true)) {
            ConstantVariable.onSNACK(tvDate, "Please enter customer name !!")
        } else {
            saveComplaintData()
        }
    }
}