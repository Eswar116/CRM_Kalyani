package com.crm.crmapp.order.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.DataBase.CRMdatabase
import com.crm.crmapp.DataBase.NewOrder
import com.crm.crmapp.DataBase.OrderImage
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.util.ConstantVariable.Companion.TAG_OBJECT
import com.crm.crmapp.order.util.ConstantVariable.Companion.TAG_OBJECT2
import com.crm.crmapp.customer.model.CustomerResult
import com.crm.crmapp.marketPlan.models.MarketPlanInfoRequestResponse
import com.crm.crmapp.order.adapter.NewOrderAdapter
import com.crm.crmapp.order.model.*
import com.crm.crmapp.order.util.CustomDate
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.order.util.doAsync
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.test.StoreListActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_new_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

class NewOrderActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var rvList: RecyclerView
    private lateinit var tvApply: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvHintName: TextView
    private lateinit var tvName: TextView
    private lateinit var tvHintOrder: TextView
    private lateinit var tvHintMarketPlan: TextView
    private lateinit var tvHintDate: TextView
    private lateinit var tvHintDoc: TextView
    private lateinit var Apply: TextView
    private lateinit var tvDate: TextView
    private lateinit var ivAdd: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var ivSearchMP: ImageView
    private lateinit var svCustomer: TextView
    private lateinit var svMarketPlan: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvNoBox: TextView
    private lateinit var etBox: EditText
    private lateinit var spnBoxes: Spinner
    private lateinit var cvOrder: CheckBox
    private lateinit var llUpload: LinearLayout
    private var docDetailList: ArrayList<DocDetailModel>? = null
    private var mDb: CRMdatabase? = null
    private var long: Long? = null
    private var progressDialog: ProgressDialog? = null
    private var result1: NewOrderResult1? = null
    private var resultList1: ArrayList<NewOrderResult1>? = null
    private var result2: NewOrderResult2? = null
    private var resultList2: ArrayList<NewOrderResult2>? = null
    private var customerId: String? = "0"
    private var marketPlanId: String? = "0"
    private var isNoOrder: Boolean? = false
    private val MY_PERMISSIONS_REQUEST_SEND_SMS = 0
    private var orderResponseList: ArrayList<NewOrderResponse.Result>? = null
    lateinit var yyddmDate: CustomDate
    var latitude: Double? = null
    var longitude: Double? = null

    var preferencesHelper: PreferencesHelper? = null
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)
        docDetailList = ArrayList()
        resultList1 = ArrayList()
        resultList2 = ArrayList()
        orderResponseList = ArrayList()
        yyddmDate = CustomDate()
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog!!.setContentView(R.layout.dialog_dark)
        dialog!!.setCancelable(true)
        setIds()
        setCurrentDate()
        if (intent.getStringExtra(ConstantVariable.TAG_FROM) != null && intent.getStringExtra(
                ConstantVariable.TAG_FROM
            ).equals(
                "from_customerInfo",
                ignoreCase = true
            )
        ) {
            var customerResult = intent.getSerializableExtra(TAG_OBJECT) as CustomerResult
            svCustomer!!.setText(customerResult.custName)
            customerId = customerResult.custId.toString()
        } else if (intent.getStringExtra(ConstantVariable.TAG_FROM) != null && intent.getStringExtra(
                ConstantVariable.TAG_FROM
            ).equals(
                "from_MarketPlanInfo",
                ignoreCase = true
            )
        ) {
            var marketPlanInfoRetailerResult =
                intent.getSerializableExtra(TAG_OBJECT) as MarketPlanInfoRequestResponse.Result_4
            var marketPlanInfoResult =
                intent.getSerializableExtra(TAG_OBJECT2) as MarketPlanInfoRequestResponse.Result_1

            if (marketPlanInfoRetailerResult != null) {
                svCustomer!!.setText(marketPlanInfoRetailerResult.cust_name)
                customerId = marketPlanInfoRetailerResult.id.toString()
            }

            if (marketPlanInfoResult != null) {
                svMarketPlan!!.setText(marketPlanInfoResult.market_plan_name)
                marketPlanId = marketPlanInfoResult.id.toString()
            }
        }
    }

    private fun getLatLong() {
        var intent = Intent(this@NewOrderActivity, StoreListActivity::class.java)
        intent.putExtra(ConstantVariable.TAG_FROM, "from_new_sales_order")
        startActivityForResult(intent, ConstantVariable.latlong)
    }


    private fun setIds() {
        getLatLong()
        preferencesHelper = PreferencesHelper(this@NewOrderActivity)
        rvList = findViewById<RecyclerView>(R.id.rvList)
        llUpload = findViewById<LinearLayout>(R.id.llUpload)
        tvApply = findViewById<TextView>(R.id.tvApply)
        tvName = findViewById<TextView>(R.id.tvName)

        tvName.setText("Order Owner : " + preferencesHelper!!.userName)
        Apply = findViewById<TextView>(R.id.Apply)
        tvCancel = findViewById<TextView>(R.id.tvCancel)
        tvNoBox = findViewById<TextView>(R.id.tvNoBox)
        spnBoxes = findViewById<Spinner>(R.id.spnBoxes)
        ivAdd = findViewById<ImageView>(R.id.ivAdd)
        ivSearch = findViewById<ImageView>(R.id.ivSearch)
        ivSearchMP = findViewById<ImageView>(R.id.ivSearchMP)
        tvTitle = findViewById(R.id.tvTitle) as TextView
        etBox = findViewById(R.id.etBox) as EditText
        tvHintName = findViewById<TextView>(R.id.tvHintName)
        addStar(tvHintName.text.toString(), tvHintName)
        tvHintOrder = findViewById<TextView>(R.id.tvHintOrder)
        tvHintMarketPlan = findViewById<TextView>(R.id.tvHintMarketPlan)
        tvHintDate = findViewById<TextView>(R.id.tvHintDate)
        tvDate = findViewById<TextView>(R.id.tvDate)
        cvOrder = findViewById<CheckBox>(R.id.cvOrder)
        addStar(tvHintDate.text.toString(), tvHintDate)
        tvHintDoc = findViewById<TextView>(R.id.tvHintDoc)
        addStar(tvHintDoc?.text.toString(), tvHintDoc)
        // todo set recyclerview
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = NewOrderAdapter(this, docDetailList)
        svCustomer = findViewById(R.id.svCustomer)
        svMarketPlan = findViewById(R.id.svMarketPlan)
        tvTitle.setText("New Order")
        svMarketPlan.setText("Others")
        marketPlanId = "0";

        //todo CLICK LISTENER
        svCustomer.setOnClickListener(this)
        svMarketPlan.setOnClickListener(this)
        ivAdd.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        ivSearchMP.setOnClickListener(this)
        tvDate.setOnClickListener(this)
        Apply.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ConstantVariable.resultCode) {
            if (resultCode == Activity.RESULT_OK) {
                val docListDetail =
                    data!!.extras!!.get(ConstantVariable.docDetailModel) as DocDetailModel
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
        } else if (requestCode == ConstantVariable.resultCode_marketPlan) {
            if (data?.extras?.get("MarketPlanPosition") != null) {
                svMarketPlan!!.setText(data?.extras?.get("MarketPlanPosition").toString())
                marketPlanId = data?.extras?.get("MarketPlanId").toString()
            }
        } else if (requestCode == ConstantVariable.latlong) {
            if (resultCode == Activity.RESULT_OK) {
                latitude = data?.extras?.get(ConstantVariable.Latitude) as Double
                longitude = data?.extras?.get(ConstantVariable.Longitude) as Double
                Log.e("latitude" + latitude, "longitude" + longitude)
            }
        }


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
                intent.putExtra("NewOrderKey", "NewOrderWithMarketPlanId")
                intent.putExtra("marketPlanId", marketPlanId)
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

            tvDate -> {
                ConstantVariable.animationEffect(tvDate, this!!)
                //ConstantVariable.datePicker(this, tvDate)
                ConstantVariable.datePickerWithYYDDMMRequest(this, tvDate, yyddmDate)
            }
            Apply -> {
                checkValidation()
            }
            tvCancel -> {
                ConstantVariable.animationEffect(tvCancel, this!!)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish()
            }
            ivSearch -> {
                svCustomer.performClick()
            }
            ivSearchMP -> {
                svMarketPlan.performClick()
            }

        }
    }


    fun CheckPermission() {
        val permissions = arrayOf(android.Manifest.permission.SEND_SMS)
        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_SEND_SMS)
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.SEND_SMS
                )
            ) {

                Snackbar.make(
                    this.findViewById(android.R.id.content),
                    "Please Grant Permissions to upload profile photo",
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

                    ConstantVariable.hideKeyPad(this)

                    showCustomDialog(
                        orderResponseList!!.get(0).getMobileno().toString(),
                        orderResponseList!!.get(0).getOrderId().toString(),
                        orderResponseList!!.get(0).getNoOfBoxes().toString()
                    )
                }
            } catch (e: Exception) {
                Log.e("sms exception>>", e.message)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_SEND_SMS -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    try {
                        ConstantVariable.hideKeyPad(this)
                        showCustomDialog(
                            orderResponseList!!.get(0).getMobileno().toString(),
                            orderResponseList!!.get(0).getOrderId().toString(),
                            orderResponseList!!.get(0).getNoOfBoxes().toString()
                        )

                    } catch (e: Exception) {
                        Log.e("sms exception", e.message)
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
        tvDate.setText(
            StringBuilder()
                // Month is 0 based, just add 1
                .append(mFormat.format(dd)).append("-").append(mFormat.format(mm + 1)).append("-")
                .append(yy)
        )
        yyddmDate.setDate("" + yy + "-" + mFormat.format(mm + 1) + "-" + dd)
    }

    //todo insert into db
    private fun insertInTable() {

        if (rvList.visibility == View.VISIBLE) {
            isNoOrder = true
        } else {
            isNoOrder = false
        }
        val order = NewOrder(
            yyddmDate.getDate().toString()!!,
            tvName.text.toString()!!,
            svMarketPlan.text.toString()!!,
            svCustomer.text.toString()!!,
            etBox.text.toString().toInt()!!,
            marketPlanId!!.toInt(),
            customerId!!.toInt(),
            1,
            etRemarks.text.toString()!!,
            this!!.isNoOrder!!,
            true
        )
        if (mDb != null) {
            progressDialog = ConstantVariable.showProgressDialog(this@NewOrderActivity, "Loading")
            doAsync {
                long = mDb!!.crmDoa().insertOrder(order)
                Log.e("ValueOfOrder", "" + long)
                if (docDetailList != null && docDetailList!!.size > 0) {
                    insertImageDataBase(docDetailList!!)
                } else {
                    progressDialog!!.dismiss()
                    finish()
                }
            }
        }
    }

    //todo insert into db
    private fun insertImageDataBase(docDetailList: ArrayList<DocDetailModel>) {
        for (item: Int in docDetailList!!.indices) {
            var image = OrderImage(
                ConstantVariable.convertPathToBlob(
                    ConstantVariable.StringToBitMap(
                        docDetailList!!.get(
                            item
                        ).uri
                    )!!
                ),
                long!!.toInt()
            )
            AsyncTask.execute {
                mDb!!.crmDoa().insertImage(image)
                progressDialog!!.dismiss()
                finish()
            }
        }
    }

    //todo api to send order data
    private fun saveOrderApi() {
        resultList1?.clear()
        resultList2?.clear()
        progressDialog = ConstantVariable.showProgressDialog(this@NewOrderActivity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var orderDetailRequest = NewOrderRequest()
        orderDetailRequest.setMessage("Record Found")
        orderDetailRequest.setStatus("success")
        result1 = NewOrderResult1()
        result1?.setMarketPlanId(marketPlanId?.toInt())
        result1?.setCustId(customerId?.toInt())
        result1?.setUserId(preferencesHelper!!.userId!!.toIntOrNull())
        result1?.setNoOfBoxes(etBox.text.toString() + "_" + spnBoxes.selectedItem.toString())
        result1?.setOrderDate(yyddmDate.getDate())
        result1?.setRemarks(etRemarks.text.toString())
        result1?.setIsEnteredErp(0)
        if (latitude != null && longitude != null) {
            result1?.setLatitude(latitude.toString())
            result1?.setLongitude(longitude.toString())
        } else {
            result1?.setLatitude("0.0")
            result1?.setLongitude("0.0")
        }

        if (rvList.visibility == View.VISIBLE) {
            result1?.setIsNoOrder(0)
        } else {
            result1?.setIsNoOrder(1)
        }
        result1?.setStatusText("New")
        resultList1?.add(this!!.result1!!)
        orderDetailRequest.setResult1(this!!.resultList1!!)
        for (i: Int in docDetailList!!.indices) {
            result2 = NewOrderResult2()
            result2!!.setOrderAttachmentUrl(
                ConstantVariable.convertByteArray(
                    ConstantVariable.StringToBitMap(
                        docDetailList!!.get(i).uri
                    )!!
                )
            )
            resultList2!!.add(result2!!)

        }
        orderDetailRequest.setResult2(this!!.resultList2!!)
        var call: Call<NewOrderResponse>? = apiInterface!!.sendOrderDetail(orderDetailRequest)
        call!!.enqueue(object : Callback<NewOrderResponse> {
            override fun onFailure(call: Call<NewOrderResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvDate, "Something went wrong")
            }

            override fun onResponse(
                call: Call<NewOrderResponse>,
                response: Response<NewOrderResponse>
            ) {
                progressDialog!!.dismiss()
                try {

                    orderResponseList =
                        response.body()?.getResult() as ArrayList<NewOrderResponse.Result>

                    preferencesHelper!!.orderId =
                        (orderResponseList!!.get(0).getOrderId().toString())
                    /*  ConstantVariable.onSNACKSuccess(tvDate, response.body()!!.getMsg()!!)
                     // finish()*/
                    CheckPermission()

                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(tvDate, "Something went wrong!!")
                }
            }
        })
    }


    private fun showCustomDialog(phone: String, orderId: String, noOfBoxes: String) {

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        var textTitle = (dialog!!.findViewById(R.id.title) as TextView)
        var dropdownValue: String;
        if (spnBoxes.selectedItem.toString().equals("pc", true)) {
            dropdownValue = if (!noOfBoxes.equals("") && noOfBoxes.toInt()>1) "pieces" else "piece"
        } else {
            dropdownValue = if (!noOfBoxes.equals("") && noOfBoxes.toInt()>1) "boxes" else "box"
        }

        textTitle.setText(
            Html.fromHtml("Dear Customer, " + "<br>" + "<font color=\"#7986CB\">Order No." + orderId + " </font> of " + noOfBoxes + " " + dropdownValue +" for "+"<font color=\"#7986CB\">"+svCustomer.text.toString()+"</font> "+" has been generated on "+"<font color=\"#7986CB\">"+tvDate.text.toString()+"</font>"+" by "+"<font color=\"#7986CB\">"+preferencesHelper!!.userName+"</font>"+" is under process."+"</br>"+"<br>"+" Thanks for your Order."),
        TextView.BufferType.SPANNABLE
        )

        (dialog!!.findViewById(R.id.bt_close) as ImageView).setOnClickListener {
            dialog!!.dismiss()

            ConstantVariable.animationEffect(tvCancel, this!!)
            this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            finish()
        }

        (dialog!!.findViewById(R.id.bt_follow) as TextView)
        (dialog!!.findViewById(R.id.llMessage) as LinearLayout).setOnClickListener {

            ConstantVariable.sendSMS(this@NewOrderActivity, phone, textTitle.text.toString())
            ConstantVariable.animationEffect(tvCancel, this!!)
            this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            finish()
        }

        (dialog!!.findViewById(R.id.llWhatsapp) as LinearLayout).setOnClickListener {
            if (docDetailList!!.size > 0) {
                ConstantVariable.sendWhatsAppWithImage(
                    this@NewOrderActivity,
                    textTitle.text.toString(),
                    phone,
                    docDetailList!!.get(0)
                )
            } else {
                ConstantVariable.sendWhatsApp(
                    this@NewOrderActivity,
                    textTitle.text.toString(),
                    phone
                )
            }

            ConstantVariable.animationEffect(tvCancel, this!!)
            this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            finish()
        }

        dialog!!.show()
        dialog!!.window!!.attributes = lp
    }

    private fun checkValidation() {
        if (svCustomer.text.toString().equals("", ignoreCase = true)) {
            ConstantVariable.onSNACK(tvDate, "Please enter customer name !!")
        } else if (yyddmDate.getDate() == null || yyddmDate.getDate().toString().equals("")) {
            ConstantVariable.onSNACK(tvDate, "Please Select Date")
        } else if (!cvOrder.isChecked && etBox.text.toString().equals("", ignoreCase = true)) {
            ConstantVariable.onSNACK(tvDate, "Please fill No Of Boxes")
        } else if (spnBoxes.selectedItemPosition == 0) {
            ConstantVariable.onSNACK(tvDate, "Please Select pc or bx from dropdown")
        } else if (!cvOrder.isChecked &&
            !etBox.text.toString().equals("", ignoreCase = true) &&
            !((etBox.text.toString().toInt()) > 0)
        ) {
            ConstantVariable.onSNACK(tvDate, "Please fill No Of Boxes greater than 0")
        } else if (!cvOrder.isChecked &&
            !etBox.text.toString().equals("", ignoreCase = true) &&
            ((etBox.text.toString().toInt()) > 0 && docDetailList!!.size == 0)
        ) {
            ConstantVariable.onSNACK(tvDate, "SaleOrder attachment is mandatory")
        } else {
            if (cvOrder.isChecked) {
                etBox.setText("0")
            }
            ConstantVariable.animationEffect(Apply, this!!)
            if (ConstantVariable.verifyAvailableNetwork(this@NewOrderActivity)) {
                saveOrderApi()
            } else {
                mDb = CRMdatabase.getInstance(applicationContext)
                insertInTable()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

}