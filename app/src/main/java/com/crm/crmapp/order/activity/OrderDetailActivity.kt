package com.crm.crmapp.order.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.DataBase.CRMdatabase
import com.crm.crmapp.order.adapter.OrderDetailAdapter
import com.crm.crmapp.order.model.*
import com.crm.crmapp.order.util.CustomDate
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.order.util.doAsync
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var rvList: RecyclerView
    private lateinit var tvApply: TextView
    private lateinit var tvHintName: TextView
    private lateinit var tvHintOrder: TextView
    private lateinit var tvHintDate: TextView
    private lateinit var Apply: TextView
    private lateinit var tvHintDoc: TextView
    private lateinit var tvDate: TextView
    private lateinit var ivAdd: ImageView
    private lateinit var svCustomer: TextView
    private lateinit var svMarketPlan: TextView
    private lateinit var tvName: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvNoBox: TextView
    private lateinit var etBox: EditText
    private lateinit var etRemarks: EditText
    private lateinit var cvOrder: CheckBox
    private lateinit var llUpload: LinearLayout
    private lateinit var ivDelete: ImageView
    private lateinit var ivShare: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var ivSearchMP: ImageView
    private lateinit var llDelete: LinearLayout
    private var docDetailListModel: ArrayList<DocDetailModel>? = null
    private var mDb: CRMdatabase? = null
    private lateinit var docDetailList: ArrayList<OrderDetailResult2>
    var orderDetailList: ArrayList<OrderDetailResult1>? = null
    var imageBase64: ArrayList<String>? = null
    //var progressDialog: ProgressDialog? = null
    private val MY_PERMISSIONS_REQUEST_SEND_SMS = 0
    private var result1: NewOrderResult1? = null
    private var resultList1: ArrayList<NewOrderResult1>? = null
    private var result2: NewOrderResult2? = null
    private var resultList2: ArrayList<NewOrderResult2>? = null
    private var customerId: String? = "0"
    private var order_id: Int? = 0
    var preferencesHelper: PreferencesHelper? = null
    private var dialog: Dialog? = null
    lateinit var yyddmDate: CustomDate
    private var marketPlanId: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)
        preferencesHelper = PreferencesHelper(this@OrderDetailActivity)
        docDetailList = ArrayList()
        orderDetailList = ArrayList()
        resultList1 = ArrayList()
        resultList2 = ArrayList()
        imageBase64 = ArrayList()
        docDetailListModel = ArrayList()
        yyddmDate = CustomDate()
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog!!.setContentView(R.layout.dialog_dark)
        dialog!!.setCancelable(true)
        setIds()
        setCurrentDate()
        checkDataofOffline()
    }

    //todo check data offline and internet connectivity
    private fun checkDataofOffline() {
        if (ConstantVariable.verifyAvailableNetwork(this@OrderDetailActivity)) {
            if (intent != null && getIntent().getIntExtra(ConstantVariable.offline_order_id, 0) != 0) {
                populateDataOffline(getIntent().getIntExtra(ConstantVariable.offline_order_id, 0))
                Apply.setText("SAVE")
            } else {
                if (intent != null && getIntent().getIntExtra(ConstantVariable.online_order_id, 0) != 0) {
                    order_id = getIntent().getIntExtra(ConstantVariable.online_order_id, 0)
                    getOrderDetailApi(getIntent().getIntExtra(ConstantVariable.online_order_id, 0))
                } else if (intent != null && getIntent().getStringExtra(ConstantVariable.firebase_bean_id) != null) {
                    getOrderDetailApi(getIntent().getStringExtra(ConstantVariable.firebase_bean_id).toIntOrNull()!!)
                }
            }
        } else {
            if (intent != null && getIntent().getIntExtra(ConstantVariable.offline_order_id, 0) != 0) {
                populateDataOffline(getIntent().getIntExtra(ConstantVariable.offline_order_id, 0))
                Apply.setText("SAVE")
            } else {
                ConstantVariable.onSNACK(tvApply, "No Internet Connection!!")
            }
        }
    }

    private fun populateDataOffline(orderId: Int) {
        mDb = CRMdatabase.getInstance(this)
        if (mDb != null) {
            val progressDialog = ConstantVariable.showProgressDialog(this@OrderDetailActivity, "Loading")
            AsyncTask.execute {
                var orderDetail = mDb!!.crmDoa().orderDetail(orderId)
                try {
                    tvDate.setText(yyddmDate.getDate())
                    etBox.setText(orderDetail.no_of_boxes.toString())
                    etRemarks.setText(orderDetail.remarks)
                    svCustomer.setText(orderDetail.cust_name)
                    svMarketPlan.setText(orderDetail.market_plan_name)
                    marketPlanId = orderDetail.market_plan_id.toString()
                    tvName.setText(orderDetail.user_name)
                    progressDialog!!.dismiss()
                    checkImageOffLine(mDb!!, orderId)
                } catch (e: NullPointerException) {
                    progressDialog!!.dismiss()
                }
            }
        }
    }

    private fun checkImageOffLine(mDb: CRMdatabase, orderId: Int) {
        imageBase64?.clear()
        doAsync {
            var listOrderImage = mDb!!.crmDoa().allOrderImage(orderId)
            for (i: Int in listOrderImage.indices) {
                imageBase64!!.add(ConstantVariable.convertByteArrayToBase64(listOrderImage.get(i).order_attachment_url!!)!!)
                runOnUiThread {
                    setAdapter()
                }
            }
        }
    }

    private fun setIds() {
        rvList = findViewById<RecyclerView>(R.id.rvList)
        llUpload = findViewById<LinearLayout>(R.id.llUpload)
        ivDelete = findViewById<ImageView>(R.id.ivDelete)
        ivShare = findViewById<ImageView>(R.id.ivShare)
        ivSearchMP = findViewById<ImageView>(R.id.ivSearchMP)
        ivSearch = findViewById<ImageView>(R.id.ivSearch)
        llDelete = findViewById<LinearLayout>(R.id.llDelete)
        tvApply = findViewById<TextView>(R.id.tvApply)
        tvNoBox = findViewById<TextView>(R.id.tvNoBox)
        Apply = findViewById<TextView>(R.id.Apply)
        tvName = findViewById<TextView>(R.id.tvName)
        ivAdd = findViewById<ImageView>(R.id.ivAdd)
        tvTitle = findViewById(R.id.tvTitle) as TextView
        tvCancel = findViewById(R.id.tvCancel) as TextView
        etBox = findViewById(R.id.etBox) as EditText
        etRemarks = findViewById(R.id.etRemarks) as EditText
        tvHintName = findViewById<TextView>(R.id.tvHintName)
        addStar(tvHintName.text.toString(), tvHintName)
        tvHintOrder = findViewById<TextView>(R.id.tvHintOrder)
        tvHintDate = findViewById<TextView>(R.id.tvHintDate)
        tvDate = findViewById<TextView>(R.id.tvDate)
        cvOrder = findViewById<CheckBox>(R.id.cvOrder)
        svCustomer = findViewById(R.id.svCustomer)
        svMarketPlan = findViewById(R.id.svMarketPlan)
        addStar(tvHintDate.text.toString(), tvHintDate)
        tvHintDoc = findViewById<TextView>(R.id.tvHintDoc)
        addStar(tvHintDoc?.text.toString(), tvHintDoc)
        /*    etRemarks.isEnabled=false
            etBox.isEnabled=false*/
        Apply.setText("Update")
        ivDelete.visibility = View.VISIBLE
        llDelete.visibility = View.VISIBLE
        tvTitle.setText(getString(R.string.order_detail))

        //todo CLICK LISTENER
        svCustomer.setOnClickListener(this)
        svMarketPlan.setOnClickListener(this)
        ivAdd.setOnClickListener(this)
        tvDate.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
        Apply.setOnClickListener(this)
        ivDelete.setOnClickListener(this)
        ivShare.setOnClickListener(this)
        ivSearchMP.setOnClickListener(this)
        cvOrder.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rvList.visibility = View.GONE
                tvNoBox.visibility = View.GONE
                etBox.visibility = View.GONE
                llUpload.visibility = View.GONE
            } else {
                rvList.visibility = View.VISIBLE
                tvNoBox.visibility = View.VISIBLE
                etBox.visibility = View.VISIBLE
                llUpload.visibility = View.VISIBLE
            }
        }
        svMarketPlan.isEnabled = false
        ivSearchMP.isEnabled = false
    }

    private fun setAdapter() {
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = OrderDetailAdapter(this@OrderDetailActivity, imageBase64)
    }

    override fun onClick(v: View?) {
        when (v) {
            ivAdd -> {
                ConstantVariable.animationEffect(ivAdd, this!!)
                val intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, ConstantVariable.resultCode)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            svCustomer -> {
                ConstantVariable.animationEffect(svCustomer, this!!)
                val intent = Intent(this, SearchCustomer::class.java)
                intent.putExtra("NewOrderKey", "NewOrderKey")
                startActivityForResult(intent, ConstantVariable.resultCode_customer)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            svMarketPlan -> {
                ConstantVariable.animationEffect(svMarketPlan, this!!)
                val intent = Intent(this, Main2Activity::class.java)
                intent.putExtra(ConstantVariable.TAG_FROM, "from_new_order")
                startActivityForResult(intent, ConstantVariable.resultCode_marketPlan)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            ivSearch -> {
                svCustomer.performClick()
            }
            tvDate -> {
                ConstantVariable.animationEffect(tvDate, this!!)
                //ConstantVariable.datePicker(this, tvDate)
                ConstantVariable.datePickerWithYYDDMMRequest(this, tvDate, yyddmDate)
            }

            tvCancel -> {
                ConstantVariable.animationEffect(tvCancel, this!!)
                finish()
            }
            Apply -> {
                ConstantVariable.animationEffect(Apply, this!!)
                deleteApi(0)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            ivDelete -> {
                ConstantVariable.animationEffect(ivDelete, this!!)
                if (ConstantVariable.verifyAvailableNetwork(this@OrderDetailActivity)) {
                    deleteApi(1)
                }
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            ivShare -> {
                ConstantVariable.animationEffect(ivShare, this!!)
                CheckPermission()
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            ivSearchMP -> {
                svMarketPlan.performClick()
            }
        }
    }

    private fun showCustomDialog() {
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        var textTitle = (dialog!!.findViewById(R.id.title) as TextView)
        textTitle.setText(
            Html.fromHtml("Dear Customer, " + "<br>" + "<font color=\"#7986CB\">Order No." + preferencesHelper!!.orderId + " </font> for " + "<font color=\"#7986CB\">" + svCustomer.text.toString() + "</font> " + " has been generated on " + "<font color=\"#7986CB\">" + tvDate.text.toString() + "</font>" + " by " + "<font color=\"#7986CB\">" + preferencesHelper!!.userName + "</font>" + " is under process." + "</br>" + "<br>" + " Thanks for your Order."),
            TextView.BufferType.SPANNABLE
        )
        (dialog!!.findViewById(R.id.bt_close) as ImageView).setOnClickListener { dialog!!.dismiss() }
        (dialog!!.findViewById(R.id.bt_follow) as TextView)
        (dialog!!.findViewById(R.id.llMessage) as LinearLayout).setOnClickListener {
            ConstantVariable.sendSMS(this@OrderDetailActivity, preferencesHelper!!.mobileId.toString(), textTitle.text.toString())
        }
        (dialog!!.findViewById(R.id.llWhatsapp) as LinearLayout).setOnClickListener {
            if (docDetailListModel!!.size > 0) {
                ConstantVariable.sendWhatsAppWithImage(
                    this@OrderDetailActivity,
                    textTitle.text.toString(),
                    preferencesHelper!!.mobileId.toString(),
                    docDetailListModel!!.get(0)
                )
            } else {
                ConstantVariable.sendWhatsApp(
                    this@OrderDetailActivity,
                    textTitle.text.toString(),
                    preferencesHelper!!.mobileId!!
                )
            }
        }
        dialog!!.show()
        dialog!!.window!!.attributes = lp
    }

    private fun deleteApi(isDelete: Int) {
        resultList1?.clear()
        resultList2?.clear()
        val progressDialog = ConstantVariable.showProgressDialog(this@OrderDetailActivity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var orderDetailRequest = NewOrderRequest()
        orderDetailRequest.setMessage("Record Found")
        orderDetailRequest.setStatus("success")
        result1 = NewOrderResult1()
        //result1?.setMarketPlanId(marketPlanId?.toInt())
        result1?.setCustId(customerId?.toInt())
        result1?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        result1?.setNoOfBoxes(etBox.text.toString())
        result1?.setOrderDate(yyddmDate.getDate())
        result1?.setRemarks(etRemarks.text.toString())
        result1?.setOrderId(order_id)
        result1?.setIsEnteredErp(0)
        result1?.setIsDeleted(isDelete)
        if (rvList.visibility == View.VISIBLE) {
            result1?.setIsNoOrder(0)
        } else {
            result1?.setIsNoOrder(1)
        }
        result1?.setStatusText("New")
        resultList1?.add(this!!.result1!!)
        orderDetailRequest.setResult1(this!!.resultList1!!)

        var strs: List<String> = ArrayList()
        for (i: Int in imageBase64!!.indices) {
            result2 = NewOrderResult2()
            if (imageBase64 != null)
            //  todo split image which contatin size , name , base 64 with word "EssCRM"
                strs = imageBase64!!.get(i)!!.split("EssCRM")
            if (strs.size > 1) {
                if (strs.get(0) != null && strs.get(1) != null && strs.get(2) != null) {
                    result2!!.setOrderAttachmentUrl(strs.get(0))
                }
            } else {
                result2!!.setOrderAttachmentUrl(imageBase64!!.get(i))
            }
            resultList2!!.add(result2!!)

        }
        orderDetailRequest.setResult2(this!!.resultList2!!)
        var call: Call<NewOrderResponse>? = apiInterface!!.updateOrder(orderDetailRequest)
        call!!.enqueue(object : Callback<NewOrderResponse> {
            override fun onFailure(call: Call<NewOrderResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvDate, "Something went wrong")
            }

            override fun onResponse(call: Call<NewOrderResponse>, response: Response<NewOrderResponse>) {
                progressDialog!!.dismiss()
                try {
                    ConstantVariable.onToastSuccess(applicationContext, response.body()!!.getStatus()!!)
                    finish()
                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(tvDate, "Something went wrong!!")
                }
            }
        })
    }

    fun CheckPermission() {
        val permissions = arrayOf(android.Manifest.permission.SEND_SMS)
        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_SEND_SMS)
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
            ) {

                Snackbar.make(
                    this.findViewById(android.R.id.content),
                    "Please Grant Permissions to send sms",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    "ENABLE"
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission
                                    .SEND_SMS
                            ),
                            MY_PERMISSIONS_REQUEST_SEND_SMS
                        )
                    }
                }.show()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.SEND_SMS),
                        MY_PERMISSIONS_REQUEST_SEND_SMS
                    )
                }
            }
        } else {
            try {
                if (!dialog?.isShowing!!) {
                    showCustomDialog()
                }
            } catch (e: Exception) {
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_SEND_SMS -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    try {
                        showCustomDialog()
                    } catch (e: Exception) {
                    }
                } else {
                    Toast.makeText(
                        getApplicationContext(),
                        "SMS faild, please try again.", Toast.LENGTH_LONG
                    ).show();
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ConstantVariable.resultCode) {
            if (resultCode == Activity.RESULT_OK) {
                var docListDetail = data?.extras?.get(ConstantVariable.docDetailModel) as DocDetailModel


                if (docListDetail != null) {
                    docDetailListModel?.add(docListDetail)

                } else {
                    docListDetail = preferencesHelper!!.getObjectImage()
                    docDetailListModel?.add(preferencesHelper!!.getObjectImage())
                }

                Log.e("docDetailList", "docDetailList" + docDetailList?.size)
                if (imageBase64 != null) {
                    imageBase64!!.add(docListDetail.uri + "EssCRM" + docListDetail.docName + "EssCRM" + docListDetail.docSize)
                }
                setAdapter()
            }
        } else if (requestCode == ConstantVariable.resultCode_customer) {
            if (data?.extras?.get("CustomerPosition") != null) {
                svCustomer!!.setText(data?.extras?.get("CustomerPosition").toString())
                customerId = data?.extras?.get("CustomerId").toString()
            }
        } else if (requestCode == ConstantVariable.resultCode_marketPlan) {
            if (data?.extras?.get("MarketPlanPosition") != null) {
                svMarketPlan!!.setText(data?.extras?.get("MarketPlanPosition").toString())
                marketPlanId = data?.extras?.get("MarketPlanId").toString()
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
        yyddmDate.setDate("" + yy + "-" + mFormat.format(mm + 1) + "-" + dd)
    }

    private fun getOrderDetailApi(intExtra: Int) {
        val progressDialog = ConstantVariable.showProgressDialog(this@OrderDetailActivity, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var orderDetailRequest = OrderDetailRequest()
        orderDetailRequest.setOrderId(intExtra)
        var call: Call<OrderDetailResponse>? = apiInterface!!.orderDetail(orderDetailRequest)
        call!!.enqueue(object : Callback<OrderDetailResponse> {
            override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvDate, t.toString())
            }

            override fun onResponse(call: Call<OrderDetailResponse>, response: Response<OrderDetailResponse>) {
                progressDialog!!.dismiss()
                try {
                    orderDetailList = response.body()?.getResult1() as ArrayList<OrderDetailResult1>
                    docDetailList = response.body()?.getResult2() as ArrayList<OrderDetailResult2>

                    var indiandate = ConstantVariable.parseDateToyyyyMMdd(orderDetailList!!.get(0).getOrderDate()!!)
                    yyddmDate.setDate(indiandate)
                    tvDate.setText(ConstantVariable.parseDateToddMMyyyyWithoutTime(orderDetailList!!.get(0).getOrderDate()!!))
                    //tvDate.setText(orderDetailList!!.get(0).getOrderDate())
                    etBox.setText(orderDetailList!!.get(0).getNoOfBoxes().toString())
                    etRemarks.setText(orderDetailList!!.get(0).getRemarks().toString())
                    svCustomer.setText(orderDetailList!!.get(0).getCustName().toString())
                    svMarketPlan.setText(orderDetailList!!.get(0).getMarketPlanName().toString())
                    marketPlanId = orderDetailList!!.get(0).getMarketPlanName().toString()
                    tvName.setText(orderDetailList!!.get(0).getUserName().toString())
                    customerId = orderDetailList!!.get(0).getCustId().toString()

                    if (orderDetailList!!.get(0).getIsEnteredErp() == true) {
                        Apply.isClickable = false
                        Apply.visibility = View.INVISIBLE
                        ivDelete.visibility = View.INVISIBLE
                        llUpload.isClickable = false
                    } else {
                        Apply.isClickable = true
                        Apply.visibility = View.INVISIBLE
                        ivDelete.visibility = View.INVISIBLE
                        llUpload.isClickable = true
                    }
                    if (orderDetailList!!.get(0).getIsNoOrder() == true) {
                        cvOrder.isChecked = true
                    } else {
                        cvOrder.isChecked = false
                    }
                    if (docDetailList != null && docDetailList.size > 0) {
                        getRequestApiForImage(docDetailList)
                    }

                } catch (e: Exception) {

                }
            }
        })
    }

    private fun getRequestApiForImage(docDetailList: ArrayList<OrderDetailResult2>) {
        val progressDialog = ConstantVariable.showProgressDialog(this@OrderDetailActivity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        for (i: Int in docDetailList!!.indices) {
            var imageRequest = ImageRequest()
            imageBase64?.clear()
            imageRequest.setImgPath(docDetailList.get(i).getOrderAttachmentUrl()!!)
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
                        imageBase64!!.add(imageUrl)
                        setAdapter()
                    }
                }
            })
        }
    }
}