package com.crm.crmapp.complaint.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.complaint.model.ComplaintSaveRequest
import com.crm.crmapp.complaint.model.ComplaintSaveResponse
import com.crm.crmapp.complaint.model.ComplaintTypeResponse
import com.crm.crmapp.order.activity.CameraActivity
import com.crm.crmapp.order.activity.SearchCustomer
import com.crm.crmapp.order.adapter.NewOrderAdapter
import com.crm.crmapp.order.model.DocDetailModel
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
import kotlin.collections.ArrayList

class ComplaintActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var rvList: RecyclerView
    private lateinit var save: TextView
    private lateinit var tvHintName: TextView
    private lateinit var tvHintOrder: TextView
    private lateinit var tvHintDate: TextView
    private lateinit var tvHintDoc: TextView
    private lateinit var tv_name: TextView
    private lateinit var tvDate: TextView
    private lateinit var ivAdd: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var svCustomer: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvStartDate: TextView
    private lateinit var llUpload: LinearLayout
    private lateinit var spStatus: Spinner
    private lateinit var spType: Spinner
    private lateinit var spSource: Spinner
    private lateinit var spPriority: Spinner
    private lateinit var progressDialog: ProgressDialog
    lateinit var yyddmDate: CustomDate
    lateinit var yyddmDate_frmDate: CustomDate
    private var docDetailList: ArrayList<DocDetailModel>? = null
    private var complaintType: ArrayList<ComplaintTypeResponse.Result>? = null
    val myStrings = arrayOf("Low", "Medium", "High")
    private var complaintType_value: ArrayList<String>? = null
    val source = arrayOf("Email", "Call", "Visit", "Others")
    private var compType: String = ""
    private var compSource: String = ""
    private var compPriority: String = ""
    private var customerId: String = "0"
    private var statusStr: String = ""
    private var selectedTypeId: Int? = null
    private var result1: ArrayList<ComplaintSaveRequest.Result1>? = null
    private var resultList2: ArrayList<ComplaintSaveRequest.Result2>? = null
    private var preferencesHelper: PreferencesHelper? = null
    val myStrings_status = arrayOf("OPEN", "CLOSED")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint)
        docDetailList = ArrayList()
        complaintType_value = ArrayList()
        result1 = ArrayList()
        complaintType = ArrayList()
        resultList2 = ArrayList()
        yyddmDate = CustomDate()
        yyddmDate_frmDate = CustomDate()
        setIds()
        statusStr = "OPEN"
        if (ConstantVariable.verifyAvailableNetwork(this)) {
            getComplaintTypeApi()
        } else {
            ConstantVariable.onSNACK(tvApply, "No Internet Connection")
        }

        setCurrentDate()
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

            override fun onResponse(call: Call<ComplaintTypeResponse>, response: Response<ComplaintTypeResponse>) {
                progressDialog!!.dismiss()
                try {
                    complaintType = response.body()?.getResult() as ArrayList<ComplaintTypeResponse.Result>

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

        spPriority.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, myStrings)
        spType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, complaintType_value as List<String>)
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

        spStatus.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, myStrings_status)
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


    }

    private fun setIds() {
        preferencesHelper = PreferencesHelper(this)
        rvList = findViewById<RecyclerView>(R.id.rvList)
        llUpload = findViewById<LinearLayout>(R.id.llUpload)
        save = findViewById<TextView>(R.id.save)
        ivAdd = findViewById<ImageView>(R.id.ivAdd)
        ivSearch = findViewById<ImageView>(R.id.ivSearch)
        tvCancel = findViewById<TextView>(R.id.tvCancel)
        tv_name = findViewById<TextView>(R.id.tv_name)
        tvTitle = findViewById(R.id.tvTitle) as TextView
        tvStartDate = findViewById(R.id.tvStartDate) as TextView


        tvHintName = findViewById<TextView>(R.id.tvHintName)
        addStar(tvHintName.text.toString(), tvHintName)
        tvHintOrder = findViewById<TextView>(R.id.tvHintOrder)
        tvHintDate = findViewById<TextView>(R.id.tvHintDate)
        tvDate = findViewById<TextView>(R.id.tvDate)
        addStar(tvHintDate.text.toString(), tvHintDate)
        tvHintDoc = findViewById<TextView>(R.id.tvHintDoc)
        addStar(tvHintDoc?.text.toString(), tvHintDoc)

        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = NewOrderAdapter(this, docDetailList)
        svCustomer = findViewById(R.id.svCustomer)
        spType = findViewById<Spinner>(R.id.spType)
        spSource = findViewById<Spinner>(R.id.spSource)
        spPriority = findViewById<Spinner>(R.id.spPriority)
        spStatus = findViewById<Spinner>(R.id.spStatus)
        tv_name.setText(preferencesHelper!!.userName)

        //todo CLICK LISTENER

        svCustomer.setOnClickListener(this)
        ivAdd.setOnClickListener(this)
        tvDate.setOnClickListener(this)
        save.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        tvStartDate.setOnClickListener(this)
        tvStartDate.setText("Select date")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ConstantVariable.resultCode) {
            if (resultCode == Activity.RESULT_OK) {
                val docListDetail = data?.extras?.get(ConstantVariable.docDetailModel) as DocDetailModel
                if (docListDetail != null) {
                    docDetailList?.add(docListDetail)

                } else {
                    docDetailList?.add(preferencesHelper!!.getObjectImage())
                }
                Log.e("docDetailList", "docDetailList" + docDetailList?.size)
                rvList.adapter?.notifyDataSetChanged()

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
                //ConstantVariable.datePicker(this ,tvDate)
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
        tvStartDate.setText(
            StringBuilder()
                // Month is 0 based, just add 1
                .append(mFormat.format(dd)).append("-").append(mFormat.format(mm + 1)).append("-")
                .append(yy)
        )
        yyddmDate.setDate("" + yy + "-" + mFormat.format(mm + 1) + "-" + mFormat.format(dd))
        yyddmDate_frmDate.setDate("" + yy + "-" + mFormat.format(mm + 1) + "-" + mFormat.format(dd))
    }


    private fun saveComplaintData() {
        result1?.clear()
        resultList2?.clear()
        if (progressDialog == null) {
            progressDialog = ConstantVariable.showProgressDialog(this, "Loading...")
            save.isEnabled = false;
        }
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var complaintSaveRequest = ComplaintSaveRequest()
        var result1Const = ComplaintSaveRequest.Result1()
        result1Const.setCustId(customerId.toInt())

        val preferencesHelper = PreferencesHelper(this)
        result1Const?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        result1Const?.setCompId(0)
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
        var call: Call<ComplaintSaveResponse>? = apiInterface!!.saveComplaint(complaintSaveRequest)
        call?.enqueue(object : Callback<ComplaintSaveResponse> {
            override fun onFailure(call: Call<ComplaintSaveResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                save.isEnabled = true;
                ConstantVariable.onSNACK(ivAdd, "NO Record Found")
            }

            override fun onResponse(call: Call<ComplaintSaveResponse>, response: Response<ComplaintSaveResponse>) {
                progressDialog!!.dismiss()
                save.isEnabled = true;
                try {
                    ConstantVariable.onToastSuccess(applicationContext, response.body()?.getMsg()!!)
                    finish()
                } catch (e: Exception) {
                    ConstantVariable.onSNACK(ivAdd, "NO Record Found")
                }
            }
        })
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

            if (ConstantVariable.verifyAvailableNetwork(this)) {
                saveComplaintData()
            } else {
                ConstantVariable.onSNACK(tvDate!!, "No Internet Connection")
            }

        }
    }
}