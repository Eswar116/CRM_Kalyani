package com.crm.crmapp.customer.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.adapter.CustomerAttachmentAdapter
import com.crm.crmapp.customer.model.*
import com.crm.crmapp.DataBase.CRMdatabase
import com.crm.crmapp.DataBase.CustomerImage
import com.crm.crmapp.DataBase.NewCustomerDB
import com.crm.crmapp.ocrreader.TextExtractActivity
import com.crm.crmapp.order.activity.CameraActivity
import com.crm.crmapp.order.activity.SearchCustomer
import com.crm.crmapp.order.model.*
import com.crm.crmapp.order.util.EndlessRecyclerOnScrollListener.TAG
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.order.util.TextValidator
import com.crm.crmapp.order.util.doAsync
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import kotlinx.android.synthetic.main.layout_new_customer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class NewCustomerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var edtCustomerName: EditText
    private lateinit var edtMobileNo: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtAddress: EditText
    private lateinit var etRemarks: EditText
    private lateinit var tvTitle: TextView
    private lateinit var Apply: TextView
    private lateinit var tvCancel: TextView
    private lateinit var ivAdd: ImageView
    private lateinit var rvList: RecyclerView
    private lateinit var spCategory: Spinner
    private lateinit var tvDistributor: TextView
    private lateinit var navigationView: BottomNavigationView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var llDistributor: LinearLayout
    private lateinit var llCategory: LinearLayout
    private var docDetailList: ArrayList<DocDetailModel>? = null
    val myStrings = arrayOf("Select", "RETAILER", "DISTRIBUTER")
    var categoryStr = ""
    var distributorStr = ""
    var distributorId = "0"
    var msg = ""
    var operation = ""
    var custId_global: Int = 0
    private lateinit var context: Context
    private var resultList1: ArrayList<SaveCustomerResult1>? = null
    private var resultList2: ArrayList<SaveCustomerResult2>? = null
    var imageBase64: ArrayList<String>? = null
    lateinit var delete_menuitem: MenuItem
    lateinit var scan_menuitem: MenuItem
    lateinit var scanIntent: Intent
    private var mDb: CRMdatabase? = null
    private var preferencesHelper: PreferencesHelper? = null
    private var userId: Int = 0
    private var isMobileValid = false
    private var isEdit = false
    private lateinit var str_otp: CharArray
    private lateinit var mAlertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_customer)
        docDetailList = ArrayList()
        resultList1 = ArrayList()
        resultList2 = ArrayList()
        imageBase64 = ArrayList()
        context = this
        setIds()
        setDialogue()
        preferencesHelper = PreferencesHelper(this)
        if (preferencesHelper!!.getObject() != null) {
            var userData = preferencesHelper!!.getObject()
            userId = userData.getUserId()!!
        }

        //setSpinner()
        if (intent != null && intent.getStringExtra(ConstantVariable.TAG_FROM) != null) {
            if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals("edit", true)) {
                operation = intent.getStringExtra(ConstantVariable.TAG_FROM) as String
                tvTitle.setText("Update Customer")
                Apply.setText("Update")

                scan_menuitem.isVisible = true
                delete_menuitem.isVisible = true
                isMobileValid = true

                if (intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) != null) {
                    var customerResult =
                        intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) as CustomerResult
                    if (customerResult != null) {
                        getCustomerDetail(customerResult.custId)
                    }
                }
            } else if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals("search", true)) {
                isMobileValid = true
                operation = intent.getStringExtra(ConstantVariable.TAG_FROM) as String
                tvTitle.setText("Update Customer")
                Apply.setText("Update")
                scan_menuitem.isVisible = true
                delete_menuitem.isVisible = true
                if (intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) != null) {
                    var searchCustomerResult =
                        intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) as SearchCustomerResultModel
                    if (searchCustomerResult != null) {
                        getCustomerDetail(searchCustomerResult.getCustId()!!)
                    }
                }
            } else if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals("new", true)) {
                operation = intent.getStringExtra(ConstantVariable.TAG_FROM) as String
                tvTitle.setText("New Customer")
                Apply.setText("Save")
                scan_menuitem.isVisible = true
                delete_menuitem.isVisible = false
            }
        }


        if (intent != null && intent.getStringExtra("fullData") != null) {
            edtCustomerName.setText(intent.getStringExtra("ocrUserName"))
            edtMobileNo.setText(intent.getStringExtra("ocrPhoneNumber"))
            edtAddress.setText(intent.getStringExtra("ocrAddress"))
            edtEmail.setText(intent.getStringExtra("ocrEmail"))
            etRemarks.setText(intent.getStringExtra("fullData"))
        }
        setSpinner()
        setListeners()
        //setAdapter()
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_delete -> {
                    updateCustomer(navigationView, custId_global, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_scanvcard -> {
                    scanIntent.putExtra("isDeleteOption", delete_menuitem.isVisible)
                    startActivity(scanIntent)
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun setSpinner() {
        llDistributor.visibility = View.GONE
        spCategory.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, myStrings)
        spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != 0) {
                    categoryStr = myStrings[p2]
                    if (categoryStr.equals("Retailer", ignoreCase = true)) {
                        llDistributor.visibility = View.VISIBLE
                    } else {
                        llDistributor.visibility = View.GONE
                    }
                } else {
                    categoryStr = ""
                    llDistributor.visibility = View.GONE
                }
            }
        }
        spCategory.setSelection(1)
        llCategory.visibility = View.GONE
        llDistributor.visibility = View.VISIBLE
    }

    private fun setListeners() {
        ivAdd.setOnClickListener(this)
        Apply.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
        tvDistributor.setOnClickListener(this)
        edtMobileNo.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isEdit = false
                    }
                    MotionEvent.ACTION_UP -> {
                        isEdit = false
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        /*     edtMobileNo.setOnFocusChangeListener(object : View.OnFocusChangeListener() {
                 override fun onFocusChange(v: View?, hasFocus: Boolean) {
                   //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                 }

             });*/


        edtMobileNo.addTextChangedListener(object : TextValidator(edtMobileNo) {
            override fun validate(textView: EditText?, text: String?) {

                if (isEdit == false) {
                    if (text!!.length == 10) {
                        checkMobileValidation(text, textView);
                    } else {
                        isMobileValid = false
                        //textView!!.error="Invalid mobile no."
                    }
                }
            }
        })
    }

    private fun checkMobileValidation(text: String, textView: EditText?) {
        //  progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var request = RecentOrderRequest()
        request.setMobile(text)
        var call: Call<MobileValidationResponse>? = apiInterface!!.getMobileValidation(request)
        call!!.enqueue(object : Callback<MobileValidationResponse> {
            override fun onFailure(call: Call<MobileValidationResponse>, t: Throwable) {
                // progressDialog!!.dismiss()
            }

            override fun onResponse(
                call: Call<MobileValidationResponse>,
                response: Response<MobileValidationResponse>
            ) {
                // progressDialog!!.dismiss()
                try {
                    if (!response.body()!!.getMsg().equals("", ignoreCase = true)) {
                        isMobileValid = false
                        textView!!.error = response.body()!!.getMsg()
                    } else {
                        isMobileValid = true
                    }
                    ConstantVariable.onSNACK(edtMobileNo, response.body()!!.getMsg()!!)

                } catch (e: NullPointerException) {

                }
            }
        })

    }

    private fun setAdapter() {
        rvList.layoutManager = LinearLayoutManager(context)
        rvList.adapter = CustomerAttachmentAdapter(context, imageBase64, true)
    }

    private fun setIds() {
        edtCustomerName = findViewById(R.id.edtCustomerName) as EditText
        edtMobileNo = findViewById(R.id.edtMobileNo) as EditText
        edtEmail = findViewById(R.id.edtEmail) as EditText
        edtAddress = findViewById(R.id.edtAddress) as EditText
        etRemarks = findViewById(R.id.etRemarks) as EditText
        tvTitle = findViewById(R.id.tvTitle) as TextView
        Apply = findViewById(R.id.Apply) as TextView
        tvCancel = findViewById(R.id.tvCancel) as TextView
        ivAdd = findViewById(R.id.ivAdd) as ImageView
        rvList = findViewById(R.id.rvList) as RecyclerView
        spCategory = findViewById(R.id.spCategory) as Spinner
        tvDistributor = findViewById(R.id.tvDistributor) as TextView
        scanIntent = Intent(context, TextExtractActivity::class.java)
        navigationView = findViewById(R.id.navigationView) as BottomNavigationView
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigationView.visibility = View.GONE
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
        scan_menuitem = nav_Menu.findItem(R.id.menu_scanvcard)
        llDistributor = findViewById(R.id.llDistributor)
        llCategory = findViewById(R.id.llCategory)
    }

    private fun slideUp(child: BottomNavigationView) {
        child.clearAnimation()
        child.animate().translationY(0f).duration = 200
    }

    private fun slideDown(child: BottomNavigationView) {
        child.clearAnimation()
        child.animate().translationY(navigationView.height.toFloat()).duration = 200
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ConstantVariable.resultCode) {
            if (resultCode == Activity.RESULT_OK) {
                val docListDetail =
                    data?.extras?.get(ConstantVariable.docDetailModel) as DocDetailModel
                if (docListDetail != null) {
                    if (docListDetail != null) {
                        docDetailList?.add(docListDetail)
                    } else {
                        docDetailList?.add(preferencesHelper!!.getObjectImage())
                    }
                } else {
                    docDetailList?.add(preferencesHelper!!.getObjectImage())
                }
                if (imageBase64 != null) {
                    imageBase64!!.add(docListDetail.uri)
                }
                setAdapter()
            }
        } else if (requestCode == ConstantVariable.resultCode_distributor) {
            if (data?.extras?.get("CustomerPosition") != null) {
                tvDistributor!!.setText(data?.extras?.get("CustomerPosition").toString())
                distributorStr = data?.extras?.get("CustomerPosition").toString()
                distributorId = data?.extras?.get("CustomerId").toString()
            }
        } else if (requestCode == ConstantVariable.OTP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data?.extras?.get("otp") != null) {
                    //var resultOtp: String = data?.extras?.get("otp").toString()
                    if (ConstantVariable.verifyAvailableNetwork(context as Activity)) {
                        if (isValid()) {
                            if (operation.equals("new", ignoreCase = true)) {
                                if (isMobileValid == true) {
                                    saveCustomer(navigationView)
                                } else {
                                    ConstantVariable.onSNACK(navigationView, "Mobile No is invalid")
                                }
                            }
                        } else {
                            ConstantVariable.onSNACK(navigationView, msg)
                        }
                    } else {
                        insertCustomerInDB()
                    }

                }
            }
        }

    }

    private fun setDialogue() {
        val mBuilder = AlertDialog.Builder(this)
        mAlertDialog = mBuilder.show()
        mAlertDialog.setMessage("Invalid OTP Found")
        mAlertDialog.setTitle("OTP Mismatch")
    }

    override fun onClick(v: View?) {
        when (v) {
            ivAdd -> {
                ConstantVariable.animationEffect(ivAdd, this!!)
                val intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, ConstantVariable.resultCode)
            }

            Apply -> {
                ConstantVariable.animationEffect(Apply, this!!)
                if (ConstantVariable.verifyAvailableNetwork(context as Activity)) {
                    if (isValid()) {
                        if (operation.equals("edit", ignoreCase = true)
                            || operation.equals("search", ignoreCase = true)
                        ) {
                            if (custId_global != 0) {
                                if (isMobileValid == true) {
                                    updateCustomer(v, custId_global, 1)
                                } else {
                                    ConstantVariable.onSNACK(v, "Invalid mobile no.")
                                }
                            }
                        } else if (operation.equals("new", ignoreCase = true)) {
                            if (isMobileValid == true) {
                                //saveCustomer(v)
                                str_otp =
                                    generateOTP(4); // commented because api is not there for responsible user and distributer and otp link not working on 15 June 2020
                                sendOtpTask(
                                    context,
                                    str_otp,
                                    edtMobileNo.getText().toString()
                                ).execute();
                            } else {
                                ConstantVariable.onSNACK(v, "Mobile No is invalid")
                            }
                        }
                    } else {
                        ConstantVariable.onSNACK(v, msg)
                    }
                } else {
                    insertCustomerInDB()
                }
            }
            tvCancel -> {
                ConstantVariable.animationEffect(tvCancel, this!!)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish()
            }
            tvDistributor -> {
                ConstantVariable.animationEffect(tvDistributor, this!!)
                val intent = Intent(this, SearchCustomer::class.java)
                intent.putExtra("NewOrderKey", "NewDistributorKey")
                startActivityForResult(intent, ConstantVariable.resultCode_distributor)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }


        }
    }


    fun generateOTP(length: Int?): CharArray {
        var numbers: String = "1234567890"
        var random: Random = Random()
        var otp: CharArray = CharArray(length!!);

        for (i: Int in 0 until length!!) {
            otp[i] = numbers[(random.nextInt(numbers.length))];

        }
        return otp;
    }


    private fun insertCustomerInDB() {
        mDb = CRMdatabase.getInstance(applicationContext)
        val preferencesHelper = PreferencesHelper(this@NewCustomerActivity)
        var customer = NewCustomerDB(
            edtCustomerName.text.toString(),
            preferencesHelper!!.userId!!.toIntOrNull()!!,
            spCategory.selectedItem.toString(),
            edtMobileNo.text.toString().trim()!!,
            edtEmail.text.toString(),
            edtAddress.text.toString(),
            etRemarks.text.toString(),
            false
        )
        if (mDb != null) {
            progressDialog =
                ConstantVariable.showProgressDialog(this@NewCustomerActivity, "Loading")
            doAsync {
                var long = mDb!!.crmDoa().insertCustomer(customer)
                Log.e("ValueOfOrder", "" + long)
                if (docDetailList != null && docDetailList!!.size > 0) {
                    insertImageDataBase(docDetailList!!, long)
                } else {
                    progressDialog!!.dismiss()
                    finish()
                }
            }
        }
    }


    //todo insert into db
    private fun insertImageDataBase(
        docDetailList: ArrayList<DocDetailModel>,
        long: Long
    ) {
        for (item: Int in docDetailList!!.indices) {
            var image = CustomerImage(
                ConstantVariable.convertPathToBlob(
                    ConstantVariable.StringToBitMap(
                        docDetailList!!.get(item).uri
                    )!!
                ), long!!.toInt()
            )
            AsyncTask.execute {
                mDb!!.crmDoa().insertCustomerImage(image)
                progressDialog!!.dismiss()
                finish()
            }
        }
    }

    fun isValid(): Boolean {
        if (edtCustomerName.text == null || edtCustomerName.text.toString().equals("")) {
            msg = "Please fill Customer Name"
            return false
        }
        /*else if (spCategory.selectedItemPosition == 0 || categoryStr.equals("", ignoreCase = true)) {
            msg = "Please select category"
            return false
        }*/
        else if (spCategory.selectedItemPosition != 0 && categoryStr.equals(
                "Retailer",
                ignoreCase = true
            )
        ) {
            if (distributorId.equals("0")) {
                msg = "Please select distributor"
                return false
            }
        } //commented on 15 June 2020 for testing.
        else if (edtMobileNo.text == null || edtMobileNo.text.equals("")) {
            msg = "Please fill Mobile No."
            return false
        }
        /*else if (edtEmail.text == null || edtEmail.text.equals("")) { //here email validation is commented on 26-11-2019
            msg = "Please fill Email Address."
            return false
        } else if (!isValidEmail(edtEmail.text.toString())) {
            msg = "Please fill valid email address."
            return false
        }*/
        else if (imageBase64 != null && imageBase64!!.size == 0) {
            msg = "File Attachment is mandatory."
            return false
        }
        return true
    }


    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches();
    }

    private fun saveCustomer(view: View) {
        resultList1?.clear()
        resultList2?.clear()
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val result1 = SaveCustomerResult1()
        val preferencesHelper = PreferencesHelper(this@NewCustomerActivity)
        result1?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        result1?.setCustName(edtCustomerName.text.toString())
        result1?.setCustType(categoryStr)
        result1?.setGrp_cust_id(Integer.parseInt(distributorId))
        result1?.setAddress(edtAddress.text.toString())
        result1?.setMobileno(edtMobileNo.text.toString())
        result1?.setEmailid(edtEmail.text.toString())
        result1?.setRemarks(etRemarks.text.toString())
        resultList1?.add(result1!!)

        var saveCustomerRequest = SaveCustomerRequestModel()
        saveCustomerRequest.setResult1(this!!.resultList1!!)


        for (i: Int in imageBase64!!.indices) {
            var result2 = SaveCustomerResult2()
            if (imageBase64 != null)
                result2!!.setAttachment(imageBase64!!.get(i))
            result2!!.setType("image")
            result2!!.setExtension(".jpg")
            resultList2!!.add(result2!!)
        }

        saveCustomerRequest.setResult2(this!!.resultList2!!)

        var call: Call<NewOrderResponse>? = apiInterface!!.saveCustomer(saveCustomerRequest)

        call!!.enqueue(object : Callback<NewOrderResponse> {
            override fun onFailure(call: Call<NewOrderResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(view, "Something went wrong")
            }

            override fun onResponse(
                call: Call<NewOrderResponse>,
                response: Response<NewOrderResponse>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        /*   preferencesHelper!!.orderId=(response?.body()?.getOrderid())
                           ConstantVariable.onSNACK(tvDate, response.body()!!.getMsg()!!)*/
                        ConstantVariable.onToastSuccess(context, response.body()!!.getMsg()!!)
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

    private fun updateCustomer(view: View, custId: Int, isActive: Int) {
        resultList1?.clear()
        resultList2?.clear()
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        val result1 = SaveCustomerResult1()
        result1?.setUserId(userId)
        result1?.setCustId(custId)
        result1?.setCustName(edtCustomerName.text.toString())
        result1?.setCustType(categoryStr)
        result1?.setGrp_cust_id(Integer.parseInt(distributorId))
        result1?.setAddress(edtAddress.text.toString())
        result1?.setMobileno(edtMobileNo.text.toString())
        result1?.setEmailid(edtEmail.text.toString())
        result1?.setRemarks(etRemarks.text.toString())
        result1?.setIsactive(isActive)
        resultList1?.add(result1!!)

        var saveCustomerRequest = SaveCustomerRequestModel()
        saveCustomerRequest.setResult1(this!!.resultList1!!)

        for (i: Int in imageBase64!!.indices) {
            var result2 = SaveCustomerResult2()
            if (imageBase64 != null)
                result2!!.setAttachment(imageBase64!!.get(i))
            result2!!.setType("image")
            result2!!.setExtension(".jpg")
            resultList2!!.add(result2!!)
        }

        saveCustomerRequest.setResult2(this!!.resultList2!!)

        var call: Call<NewOrderResponse>? = apiInterface!!.updateCustomer(saveCustomerRequest)

        call!!.enqueue(object : Callback<NewOrderResponse> {
            override fun onFailure(call: Call<NewOrderResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(view, "Something went wrong")
            }

            override fun onResponse(
                call: Call<NewOrderResponse>,
                response: Response<NewOrderResponse>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        /*   preferencesHelper!!.orderId=(response?.body()?.getOrderid())
                           ConstantVariable.onSNACK(tvDate, response.body()!!.getMsg()!!)*/
                        ConstantVariable.onToastSuccess(context, response.body()!!.getMsg()!!)
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

    fun getCustomerDetail(custid: Int) {
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var customerListRequest = RecentCustomerModel()

        val preferencesHelper = PreferencesHelper(this@NewCustomerActivity)
        customerListRequest?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        customerListRequest.setCustId(custid)

        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<CustomerDetailByIDModel>? =
            apiInterface!!.getCustomersDtlbyId(customerListRequest)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<CustomerDetailByIDModel> {
            override fun onFailure(call: Call<CustomerDetailByIDModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(edtCustomerName, "Something went wrong")
                //tvNoData.visibility=View.VISIBLE
                //rvList.visibility=View.GONE
            }

            override fun onResponse(
                call: Call<CustomerDetailByIDModel>,
                response: Response<CustomerDetailByIDModel>
            ) {
                progressDialog!!.dismiss()
                //tvNoData.visibility=View.GONE
                //rvList.visibility=View.VISIBLE
                if (response.body() != null) {
                    isEdit = true
                    var customerDetailList =
                        response.body()?.getResult() as ArrayList<CustomerDetailByIDResult>
                    var customerDetailList2 =
                        response.body()?.getResult2() as ArrayList<CustomerDetailByIDResult2>
                    if (customerDetailList.size > 0) {
                        custId_global = customerDetailList.get(0).custId
                        edtCustomerName.setText(customerDetailList.get(0).custName)
                        edtMobileNo.setText(customerDetailList.get(0).mobileno)
                        //tvType.setText(customerDetailList.get(0).custType)
                        // tvCategory.setText(customerDetailList.get(0).custType)
                        edtEmail.setText(customerDetailList.get(0).emailid)
                        tvDistributor.setText(customerDetailList.get(0).grpcustname)
                        distributorId = customerDetailList.get(0).group_cust_id.toString()
                        for (i in myStrings.indices) {
                            if (myStrings[i].equals(
                                    customerDetailList.get(0).custType,
                                    ignoreCase = true
                                )
                            ) {
                                spCategory.setSelection(i)
                                break;
                            }
                        }
                        /*edtAddress.setText(
                            customerDetailList.get(0).address + "," + customerDetailList.get(0).addrCity + ","
                                    + customerDetailList.get(0).addrState + ",\n" + customerDetailList.get(0).addrCountry + ",\n"
                                    + customerDetailList.get(0).addrPin + "."
                        )*/
                        edtAddress.setText(customerDetailList.get(0).address)
                        etRemarks.setText(customerDetailList.get(0).remarks)
                        if (customerDetailList2 != null && customerDetailList2.size > 0) {
                            getRequestApiForImage(customerDetailList2)
                        }
                    }
                }
            }
        })
    }

    private fun getRequestApiForImage(docDetailList: ArrayList<CustomerDetailByIDResult2>) {
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        for (i: Int in docDetailList!!.indices) {
            var imageRequest = ImageRequest()
            imageRequest.setImgPath(docDetailList.get(i).getUrl()!!)
            var call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
            call!!.enqueue(object : Callback<ImageRequestResponse> {
                override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                    progressDialog!!.dismiss()
                    ConstantVariable.onSNACK(rvList, "Something went wrong")
                }

                override fun onResponse(
                    call: Call<ImageRequestResponse>,
                    response: Response<ImageRequestResponse>
                ) {
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


    class sendOtpTask(context1: Context, otpStr1: CharArray, number1: String) :
        AsyncTask<String, Void, String>() {

        var context = context1
        var otpStr = otpStr1
        var number = number1
        private lateinit var progressDialog: ProgressDialog


        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        }

        override fun doInBackground(vararg params: String?): String? {
            var sb: StringBuilder? = null;
            var reader: BufferedReader? = null;
            var serverResponse: String? = null;

            try {
                val otp: String = otpStr.joinToString("");
//                var str = "http://perfectbulksms.com/Sendsmsapi.aspx?USERID=kalyaniinnerwear&PASSWORD=59661250&SENDERID=Klyani&TO=" + number + "&MESSAGE=Greeting%20from%20Kalyani.%20Your%20OTP%20for%20registration%20is%20" + otp + ".";
                var str =
                    "https://www.perfectbulksms.co.in/Sendsms.aspx?ApiKey=bG1pZG1uYWdqeWVjeGdn&EntityID=1101603840000036083&SenderID=Klyani&TemplateID=1107160308659911468&Mobileno=$number&MESSAGE=Greeting%20from%20Kalyani.%20Your%20OTP%20for%20registration%20is%20$otp.";  //  Changes by anil when OTP not send

                Log.e(TAG, "doInBackground The String is and otp is  : $str")

                var url: URL = URL(str);
                var connection: HttpURLConnection = url.openConnection() as (HttpURLConnection)
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();

                var statusCode: Int = connection.getResponseCode();
                if (statusCode == 200) {
                    sb = StringBuilder();
                    reader = BufferedReader(InputStreamReader(connection.getInputStream()));
                    var line: String = ""
                    while ({ line = reader.readLine(); line }() != null) {
                        sb.append(line + "\n");
                    }
                }
                connection.disconnect();

                if (sb != null) {
                    serverResponse = sb.toString();
                }

            } catch (e: Exception) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (e: Exception) {
                        e.printStackTrace();
                    }
                }

            }

            return serverResponse;
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (progressDialog != null) {
                progressDialog.dismiss()
            }
            Log.d("result_verification", result.toString());
            var intentVerification: Intent? = Intent(context, VerificationHeader::class.java)
            intentVerification?.putExtra(ConstantVariable.TAG_FROM, "new")
            intentVerification?.putExtra(ConstantVariable.TAG_OTP, otpStr.joinToString(""))
            intentVerification?.putExtra(ConstantVariable.TAG_NUMBER, number)
            //context.startActivityForResult(intentVerification)
            (context as Activity).startActivityForResult(
                intentVerification,
                ConstantVariable.OTP_REQUEST_CODE
            );
            (context as Activity).overridePendingTransition(
                R.anim.abc_fade_in,
                R.anim.abc_fade_out
            );
        }
    }
}