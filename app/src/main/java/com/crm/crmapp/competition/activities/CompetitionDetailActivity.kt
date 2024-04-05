package com.crm.crmapp.competition.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.competition.model.*
import com.crm.crmapp.complaint.model.ComplaintDetailRequest
import com.crm.crmapp.complaint.model.ComplaintDetailResponse
import com.crm.crmapp.complaint.model.ComplaintTypeResponse
import com.crm.crmapp.customer.model.CustomerResult
import com.crm.crmapp.order.activity.CameraActivity
import com.crm.crmapp.order.activity.SearchCustomer
import com.crm.crmapp.order.adapter.NewOrderAdapter
import com.crm.crmapp.order.model.DocDetailModel
import com.crm.crmapp.order.model.ImageRequest
import com.crm.crmapp.order.model.ImageRequestResponse
import com.crm.crmapp.order.model.NewOrderResponse
import com.crm.crmapp.order.util.CustomDate
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

class CompetitionDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var rvList: RecyclerView
    private lateinit var save: TextView
    private lateinit var tvHintName: TextView
    private lateinit var tvHintOrder: TextView
    private lateinit var tvHintDate: TextView
    private lateinit var tvHintDoc: TextView
    private lateinit var tvName: TextView
    private lateinit var tvDate: TextView
    private lateinit var ivAdd: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var svCustomer: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvCancel: TextView
    private lateinit var llUpload: LinearLayout
    private lateinit var progressDialog: ProgressDialog
    private var docDetailList: ArrayList<DocDetailModel>? = null
    private var complaintType: ArrayList<ComplaintTypeResponse.Result>? = null
    private var complaintResultList: ArrayList<ComplaintDetailResponse.Result1>? = null
    private var complaintResultList2: ArrayList<ComplaintDetailResponse.Result2>? = null
    val myStrings = arrayOf("Low", "Medium", "High")
    private var complaintType_value: ArrayList<String>? = null
    val source = arrayOf("Email", "Call", "Visit", "Others")
    private var msg: String = ""
    private var customerId: String = "0"
    private var operation: String = ""
    private lateinit var EdtCompetitor: EditText
    private lateinit var etRemarks: EditText
    private lateinit var context: Context
    private var global_competitorId: Int = 0
    private var resultList1: ArrayList<SaveCompetitorResult1>? = null
    private var resultList2: ArrayList<SaveCompetitorResult2>? = null
    private var preferencesHelper: PreferencesHelper? = null
    var imageBase64: ArrayList<String>? = null
    private lateinit var navigationView: BottomNavigationView
    private lateinit var scrollView: ScrollView
    lateinit var delete_menuitem: MenuItem
    lateinit var yyddmDate: CustomDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_competition_detail)
        docDetailList = ArrayList()
        complaintType_value = ArrayList()
        resultList1 = ArrayList()
        complaintType = ArrayList()
        complaintResultList = ArrayList()
        complaintResultList2 = ArrayList()
        resultList2 = ArrayList()
        imageBase64 = ArrayList()
        yyddmDate = CustomDate()
        context = this
        setIds()

        if (intent != null && intent.getStringExtra(ConstantVariable.TAG_FROM) != null) {
            if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals("edit", true)) {
                operation = intent.getStringExtra(ConstantVariable.TAG_FROM) as String
                tvTitle.setText("Update Competitor")
                save.setText("Update")
                if (intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) != null) {
                    var competitorDetailResult =
                        intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) as CompetitionListResult
                    if (competitorDetailResult != null) {
                        getCompetitorDetail(competitorDetailResult.getId())
                    }

                }
                navigationView.visibility = View.VISIBLE
            } else if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals("new", true)) {
                operation = intent.getStringExtra(ConstantVariable.TAG_FROM) as String
                tvTitle.setText("New Competitor")
                save.setText("Save")
                navigationView.visibility = View.GONE

                //setAttachmentAdapter()
            } else if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals(
                    "from_customerInfo",
                    ignoreCase = true
                )
            ) {
                var customerResult = intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) as CustomerResult
                operation = "new"
                svCustomer!!.setText(customerResult.custName)
                customerId = customerResult.custId.toString()
                tvTitle.setText("New Competitor")
                save.setText("Save")
                navigationView.visibility = View.GONE
            }
        }
        setCurrentDate()

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_delete -> {
                updateCompetitor(navigationView, global_competitorId, 0)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    fun getCompetitorDetail(id: Int) {
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var customerListRequest = ComplaintDetailRequest()
        customerListRequest.setCompid(id)
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        var call: Call<CompetitionDetailByIdResponseModel>? =
            apiInterface!!.getCompetitionDetailById(customerListRequest)

        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<CompetitionDetailByIdResponseModel> {
            override fun onFailure(call: Call<CompetitionDetailByIdResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvCancel, t.toString())
                //tvNoData.visibility=View.VISIBLE
                //rvList.visibility=View.GONE
            }

            override fun onResponse(
                call: Call<CompetitionDetailByIdResponseModel>,
                response: Response<CompetitionDetailByIdResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    var customerDetailList = response.body()?.getResult1() as ArrayList<CompetitionDetailByIdResult1>
                    var customerDetailList2 = response.body()?.getResult2() as ArrayList<CompetitionDetailByIdResult2>
                    if (customerDetailList.size > 0) {
                        global_competitorId = customerDetailList.get(0).id
                        customerId = customerDetailList.get(0).custId.toString()
                        svCustomer.setText(customerDetailList.get(0).custName)
                        EdtCompetitor.setText(customerDetailList.get(0).competitorName)
                        var indiandate = ConstantVariable.parseDateToyyyyMMdd(customerDetailList.get(0).compDate)
                        yyddmDate.setDate(indiandate)
                        tvDate.setText(
                            ConstantVariable.parseDateTo_ddMMMyyyy_From_yyyyMMDD_WithoutTime(
                                customerDetailList.get(0).compDate
                            )
                        )
                        etRemarks.setText(customerDetailList.get(0).remarks)
                        if (customerDetailList2 != null && customerDetailList2.size > 0) {
                            getRequestApiForImage(customerDetailList2)
                        }
                    }
                }
            }
        })
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
        tvName = findViewById<TextView>(R.id.tvName)
        tvTitle = findViewById(R.id.tvTitle) as TextView
        tvHintName = findViewById<TextView>(R.id.tvHintName)
        addStar(tvHintName.text.toString(), tvHintName)
        tvHintOrder = findViewById<TextView>(R.id.tvHintOrder)
        tvHintDate = findViewById<TextView>(R.id.tvHintDate)
        tvDate = findViewById<TextView>(R.id.tvDate)
        addStar(tvHintDate.text.toString(), tvHintDate)
        tvHintDoc = findViewById<TextView>(R.id.tvHintDoc)
        addStar(tvHintDoc?.text.toString(), tvHintDoc)
        svCustomer = findViewById(R.id.svCustomer)
        EdtCompetitor = findViewById(R.id.EdtCompetitor)
        etRemarks = findViewById(R.id.etRemarks)
        navigationView = findViewById(R.id.navigationView) as BottomNavigationView
        scrollView = findViewById(R.id.scrollView) as ScrollView
        //todo set text
        tvTitle.setText(getString(R.string.competition_detail))
        save.setText(getString(R.string.update))
        //todo CLICK LISTENER
        svCustomer.setOnClickListener(this)
        ivAdd.setOnClickListener(this)
        tvDate.setOnClickListener(this)
        save.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
        ivSearch.setOnClickListener(this)

        tvName.setText(preferencesHelper!!.userName)
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
        val nav_Menu = navigationView.menu
        delete_menuitem = nav_Menu.findItem(R.id.menu_delete)
    }

    private fun slideUp(child: BottomNavigationView) {
        child.clearAnimation()
        child.animate().translationY(0f).duration = 200
    }

    private fun slideDown(child: BottomNavigationView) {
        child.clearAnimation()
        child.animate().translationY(navigationView.height.toFloat()).duration = 200
    }


    private fun setAdapter() {
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = NewOrderAdapter(this, docDetailList)

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
                imageBase64?.clear()
                Log.e("docDetailList", "docDetailList" + docDetailList?.size)
                if (imageBase64 != null) {
                    imageBase64!!.add(docListDetail.uri)
                }
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
                if (ConstantVariable.verifyAvailableNetwork(context as Activity)) {
                    if (isValid()) {
                        if (operation.equals("edit", ignoreCase = true)
                        ) {
                            if (global_competitorId != 0) {
                                updateCompetitor(v, global_competitorId, 1)
                            } else {
                                ConstantVariable.onSNACK(v, "Invalid Customer Id")
                            }

                        } else if (operation.equals("new", ignoreCase = true)) {
                            saveCompetitor(v)
                        }
                    } else {
                        ConstantVariable.onSNACK(v, msg)
                    }
                } else {
                    // insertCustomerInDB()
                }
            }
            tvCancel -> {
                ConstantVariable.animationEffect(tvCancel, this!!)
                finish()
            }

            ivSearch -> {
                svCustomer.performClick()
            }
        }
    }

    fun isValid(): Boolean {
        if (tvDate.text == null || tvDate.text.toString().equals("")) {
            msg = "Please fill Date"
            return false
        } else if (svCustomer.text == null || svCustomer.text.equals("")) {
            msg = "Please Select Customer"
            return false
        } else if (customerId == null || customerId.equals("0")) {
            msg = "Please Select Customer"
            return false
        } else if (yyddmDate.getDate() == null || yyddmDate.getDate().toString().equals("")) {
            msg = "Please Select Date"
            return false
        } else if (EdtCompetitor.text == null || EdtCompetitor.text.toString().equals("")) {
            msg = "Please Fill Competitor Name"
            return false
        }
        return true
    }


    private fun saveCompetitor(view: View) {
        resultList1?.clear()
        resultList2?.clear()
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val result1 = SaveCompetitorResult1()
        val preferencesHelper = PreferencesHelper(context)
        result1?.setCompId(0)
        result1?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        result1?.setCustId(Integer.parseInt(customerId))
        result1?.setCompetitorName(EdtCompetitor.text.toString())
        result1?.setCDate(yyddmDate.getDate())
        result1?.setRemarks(etRemarks.text.toString())
        result1?.setIsActive(0)
        resultList1?.add(result1!!)

        var saveCustomerRequest = SaveCompetitorRequestModel()
        saveCustomerRequest.setResult1(this!!.resultList1!!)

        for (i: Int in imageBase64!!.indices) {
            var result2 = SaveCompetitorResult2()
            if (imageBase64 != null)
                result2!!.setCompAttachment(imageBase64!!.get(i))
            result2!!.setType("image")
            result2!!.setExtension(".jpg")
            resultList2!!.add(result2!!)
        }

        saveCustomerRequest.setResult2(this!!.resultList2!!)
        var call: Call<NewOrderResponse>? = apiInterface!!.saveCompetitor(saveCustomerRequest)
        call!!.enqueue(object : Callback<NewOrderResponse> {
            override fun onFailure(call: Call<NewOrderResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(view, t.toString())
            }

            override fun onResponse(call: Call<NewOrderResponse>, response: Response<NewOrderResponse>) {
                progressDialog!!.dismiss()
                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        ConstantVariable.onToastSuccess(context, response.body()!!.getMessage()!!)
                        finish()
                    } else {
                        ConstantVariable.onSNACK(view, response.body()!!.getMessage()!!)
                    }
                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(view, "Something went wrong!!")
                }
            }
        })
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

    private fun updateCompetitor(view: View, global_competitorId: Int, isActive: Int) {
        resultList1?.clear()
        resultList2?.clear()
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val result1 = SaveCompetitorResult1()
        val preferencesHelper = PreferencesHelper(context)
        result1?.setCompId(global_competitorId)
        result1?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        result1?.setCustId(Integer.parseInt(customerId))
        result1?.setCompetitorName(EdtCompetitor.text.toString())
        result1?.setCDate(yyddmDate.getDate().toString())
        result1?.setRemarks(etRemarks.text.toString())
        result1?.setIsActive(isActive)
        resultList1?.add(result1!!)

        var saveCustomerRequest = SaveCompetitorRequestModel()
        saveCustomerRequest.setResult1(this!!.resultList1!!)

        for (i: Int in imageBase64!!.indices) {
            var result2 = SaveCompetitorResult2()
            if (imageBase64 != null)
                result2!!.setCompAttachment(imageBase64!!.get(i))
            result2!!.setType("image")
            result2!!.setExtension(".jpg")
            resultList2!!.add(result2!!)
        }
        saveCustomerRequest.setResult2(this!!.resultList2!!)
        var call: Call<NewOrderResponse>? = apiInterface!!.saveCompetitor(saveCustomerRequest)
        call!!.enqueue(object : Callback<NewOrderResponse> {
            override fun onFailure(call: Call<NewOrderResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(view, t.toString())
            }

            override fun onResponse(call: Call<NewOrderResponse>, response: Response<NewOrderResponse>) {
                progressDialog!!.dismiss()
                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        /*   preferencesHelper!!.orderId=(response?.body()?.getOrderid())
                           ConstantVariable.onSNACK(tvDate, response.body()!!.getMsg()!!)*/
                        ConstantVariable.onToast(context, response.body()!!.getMessage()!!)
                        finish()
                    } else {
                        ConstantVariable.onSNACK(view, response.body()!!.getMessage()!!)
                    }
                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(view, "Something went wrong!!")
                }
            }
        })
    }


    private fun getRequestApiForImage(docDetailListResult2: ArrayList<CompetitionDetailByIdResult2>) {
        val progressDialog = ConstantVariable.showProgressDialog(this@CompetitionDetailActivity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        for (i: Int in docDetailListResult2!!.indices) {
            var imageRequest = ImageRequest()
            docDetailList?.clear()
            imageRequest.setImgPath(docDetailListResult2.get(i).getUrl()!!)
            var call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
            call!!.enqueue(object : Callback<ImageRequestResponse> {
                override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                    progressDialog!!.dismiss()
                    ConstantVariable.onSNACK(tvDate, t.toString())
                }

                override fun onResponse(call: Call<ImageRequestResponse>, response: Response<ImageRequestResponse>) {
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

}
