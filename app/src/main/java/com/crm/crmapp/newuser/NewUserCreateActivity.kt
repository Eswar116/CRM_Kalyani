package com.crm.crmapp.newuser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.DataBase.CRMdatabase
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.expense.model.GetUserDetailResponseModel
import com.crm.crmapp.expense.model.GetUserDetailResponseResult
import com.crm.crmapp.expense.model.SearchExpenseResult
import com.crm.crmapp.marketPlan.adapters.CategoryListAdapter
import com.crm.crmapp.marketPlan.adapters.StateListAdapter
import com.crm.crmapp.marketPlan.models.CategoryListRequestResponseModel
import com.crm.crmapp.marketPlan.models.StateListRequestResponseModel
import com.crm.crmapp.order.model.DocDetailModel
import com.crm.crmapp.order.model.NewOrderResponse
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_user_info.*
import kotlinx.android.synthetic.main.layout_new_customer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class NewUserCreateActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var edtFirstname: EditText
    private lateinit var edtLastname: EditText
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtMobileNo: EditText
    private lateinit var edtEmail: EditText
    private lateinit var tvTitle: TextView
    private lateinit var Apply: TextView
    private lateinit var tvCancel: TextView
    private lateinit var ivAdd: ImageView
    private lateinit var rvList: RecyclerView
    private lateinit var spCategory: TextView
    private lateinit var tvReportsto: TextView
    private lateinit var navigationView: BottomNavigationView
    private lateinit var llCategory: LinearLayout
    private var docDetailList: ArrayList<DocDetailModel>? = null
    var msg = ""
    var operation = ""
    var custId_global: Int = 0
    private lateinit var context: Context
    var imageBase64: ArrayList<String>? = null
    lateinit var delete_menuitem: MenuItem
    private var mDb: CRMdatabase? = null
    private var preferencesHelper: PreferencesHelper? = null
    private var userId: Int = 0
    private var isMobileValid = false
    private var isEdit = false
    private lateinit var str_otp: CharArray
    private lateinit var mAlertDialog: AlertDialog
    private lateinit var mAlertDialogCategory: AlertDialog
    private lateinit var mAlertDialogReportsTo: AlertDialog
    private lateinit var rvStateList: RecyclerView
    private lateinit var rvCategory: RecyclerView
    private lateinit var rvReportsTo: RecyclerView
    lateinit var categoryList: ArrayList<CategoryListRequestResponseModel.Result>
    lateinit var stateList: ArrayList<StateListRequestResponseModel.Result>
    lateinit var reportsToUserList: ArrayList<UserResult>
    lateinit var final_stateList: ArrayList<StateListRequestResponseModel.Result>
    private var stateId: String = ""
    private var stateName: String = ""
    private var categoryName: String = ""
    private var reportsToName: String = ""
    private var reportsToID: Int = 0
    private lateinit var mAlertDialogState: AlertDialog
    var userDetailResult: GetUserDetailResponseResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user_create)
        docDetailList = ArrayList()
        context = this
        setIds()
        getCategoryList()
        getStateList(0)
        preferencesHelper = PreferencesHelper(this)
        if (preferencesHelper!!.getObject() != null) {
            var userData = preferencesHelper!!.getObject()
            userId = userData.getUserId()!!
        }
        if (intent != null && intent.getStringExtra(ConstantVariable.TAG_FROM) != null) {
            if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals("edit", true)) {
                operation = intent.getStringExtra(ConstantVariable.TAG_FROM) as String
                tvTitle.setText("Update User")
                Apply.setText("Update")
                delete_menuitem.isVisible = true
                isMobileValid = true
                navigationView.visibility = View.VISIBLE
                if (intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) != null) {
                    val userResult =
                        intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) as UserResult
                    if (userResult != null) {
                        getUserDetailByUserId(userResult!!.getUserId()!!)
                        getStateList(userResult!!.getUserId()!!)
                        custId_global = userResult!!.getUserId()!!
                    }
                }
            } else if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals("new", true)) {
                operation = intent.getStringExtra(ConstantVariable.TAG_FROM) as String
                tvTitle.setText("New User")
                Apply.setText("Save")
                delete_menuitem.isVisible = false
                navigationView.visibility = View.GONE
            }
        }

        spCategory.setOnClickListener {
            ConstantVariable.animationEffect(it, this!!)
            setCategoryDialogue()
        }

        spCategory.setOnClickListener {
            ConstantVariable.animationEffect(it, this!!)
            setCategoryDialogue()
        }

        tvReportsto.setOnClickListener {
            ConstantVariable.animationEffect(it, this!!)
            if (categoryName != null) {
                setReportsToDialogue()
            } else {
                ConstantVariable.onToast(context as Activity, "Please select category first")
            }
        }

        rvList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val stateAdapter = FinalStateListAdapter(
            context,
            final_stateList,
            object : FinalStateListAdapter.BtnClickListener {
                override fun onRemoveClick(
                    position: Int,
                    result: StateListRequestResponseModel.Result
                ) {
                    final_stateList.remove(result);
                    rvList.adapter!!.notifyDataSetChanged()
                }

            })
        rvList.adapter = stateAdapter;

    }

    private fun setCategoryDialogue() {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_expense_travelling, null)
        val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
        //show dialog
        val displayMetric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetric)
        val layoutParams = WindowManager.LayoutParams()
        mAlertDialogCategory = mBuilder.show()

        layoutParams.copyFrom(mAlertDialogCategory.getWindow()!!.getAttributes());
        rvCategory = mAlertDialogCategory.findViewById<RecyclerView>(R.id.rvTravelling)!!
        rvCategory.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvCategory.adapter =
            CategoryListAdapter(
                context,
                categoryList,
                object : CategoryListAdapter.BtnClickListener {
                    override fun onBtnClick(position: Int, llRow: LinearLayout, type: String) {
                        llRow.setBackgroundColor(Color.parseColor("#d6d6d6"))
                        mAlertDialogCategory.dismiss()
                        spCategory.setText(type)
                        categoryName = categoryList.get(position).category!!
                        getReportsToList(categoryName)
                    }
                })
    }

    private fun setReportsToDialogue() {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_expense_travelling, null)
        val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
        //show dialog
        val displayMetric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetric)
        val layoutParams = WindowManager.LayoutParams()
        mAlertDialogReportsTo = mBuilder.show()

        layoutParams.copyFrom(mAlertDialogReportsTo.getWindow()!!.getAttributes());
        rvReportsTo = mAlertDialogReportsTo.findViewById(R.id.rvTravelling)!!
        rvReportsTo.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvReportsTo.adapter = ReportsToUserAdapter(
            context,
            reportsToUserList,
            object : ReportsToUserAdapter.BtnClickListener {
                override fun onBtnClick(position: Int, llRow: LinearLayout, type: String) {
                    llRow.setBackgroundColor(Color.parseColor("#d6d6d6"))
                    mAlertDialogReportsTo.dismiss()
                    tvReportsto.setText(type)
                    reportsToName = reportsToUserList.get(position).getUserFullName()!!
                    reportsToID = reportsToUserList.get(position).getUserId()!!
                }
            })
    }

    private fun getReportsToList(category: String) {
        var progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<RecentUserGetterSetter>? = apiInterface!!.GetReportingUser(category)
        Log.e("GetReportingUser>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<RecentUserGetterSetter> {
            override fun onFailure(call: Call<RecentUserGetterSetter>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RecentUserGetterSetter>,
                response: Response<RecentUserGetterSetter>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    var recentUserList = response.body()?.getResult() as ArrayList<UserResult>
                    if (recentUserList.size > 0) {
                        reportsToUserList.clear()
                        reportsToUserList.addAll(recentUserList)
                    } else {
                        Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun setStateDialogue() {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_expense_travelling, null)
        val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
        //show dialog
        val displayMetric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetric)
        val layoutParams = WindowManager.LayoutParams()
        mAlertDialogState = mBuilder.show()
        layoutParams.copyFrom(mAlertDialogState.getWindow()!!.getAttributes());
        rvStateList = mAlertDialogState.findViewById<RecyclerView>(R.id.rvTravelling)!!
        rvStateList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val stateListAdapter =
            StateListAdapter(context, stateList, object : StateListAdapter.BtnClickListener {
                override fun onBtnClick(position: Int, llRow: LinearLayout, type: String) {
                    llRow.setBackgroundColor(Color.parseColor("#d6d6d6"))
                    mAlertDialogState.dismiss()
                    stateId = stateList.get(position).state_code!!
                    stateName = stateList.get(position).state_name!!
                    var isContain: Boolean = false
                    for (model in final_stateList) {
                        if (model.state_name.equals(stateName, true)) {
                            isContain = true
                        }
                    }
                    if (isContain == false) {
                        final_stateList.add(stateList.get(position))
                        rvList.adapter!!.notifyDataSetChanged()
                    } else {
                        ConstantVariable.onToast(context, "state already selected")
                    }
                }
            })
        rvStateList.adapter = stateListAdapter;
    }

    private fun getCategoryList() {
        var progressDialog2 = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<CategoryListRequestResponseModel>? = apiInterface!!.GetUserCategory()
        Log.e("<<GetUserCategory>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<CategoryListRequestResponseModel> {
            override fun onFailure(call: Call<CategoryListRequestResponseModel>, t: Throwable) {
                progressDialog2!!.dismiss()
                ConstantVariable.onSNACK(tvCancel, "Something went wrong")
            }

            override fun onResponse(
                call: Call<CategoryListRequestResponseModel>,
                response: Response<CategoryListRequestResponseModel>
            ) {
                progressDialog2!!.dismiss()
                if (response.body() != null) {
                    categoryList = response.body()
                        ?.getResult() as ArrayList<CategoryListRequestResponseModel.Result>
                }
            }
        })
    }

    private fun getStateList(userid: Int) {
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<StateListRequestResponseModel>? =
            apiInterface!!.getStateList(userid)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<StateListRequestResponseModel> {
            override fun onFailure(call: Call<StateListRequestResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvCancel, "Something went wrong")
            }

            override fun onResponse(
                call: Call<StateListRequestResponseModel>,
                response: Response<StateListRequestResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    var stateListlocal = response.body()
                        ?.getResult() as ArrayList<StateListRequestResponseModel.Result>
                    if (userid != 0 && operation.equals(
                            "edit",
                            true
                        )
                    ) {
                        final_stateList.clear()
                        final_stateList.addAll(stateListlocal)
                        rvList.adapter!!.notifyDataSetChanged()
                    } else if (userid == 0) {
                        stateList.clear()
                        stateList.addAll(stateListlocal)
                    }
                }
            }
        })
    }

    private fun setIds() {
        edtFirstname = findViewById(R.id.edtFirstname) as EditText
        edtLastname = findViewById(R.id.edtLastname) as EditText
        edtUsername = findViewById(R.id.edtUsername) as EditText
        edtPassword = findViewById(R.id.edtPassword) as EditText
        edtMobileNo = findViewById(R.id.edtMobileNo) as EditText
        edtEmail = findViewById(R.id.edtEmail) as EditText
        tvTitle = findViewById(R.id.tvTitle) as TextView
        Apply = findViewById(R.id.Apply) as TextView
        tvCancel = findViewById(R.id.tvCancel) as TextView
        ivAdd = findViewById(R.id.ivAdd) as ImageView
        rvList = findViewById(R.id.rvList) as RecyclerView
        spCategory = findViewById(R.id.spCategory) as TextView
        tvReportsto = findViewById(R.id.tvReportsto) as TextView
        navigationView = findViewById(R.id.navigationView) as BottomNavigationView
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
        stateList = ArrayList();
        categoryList = ArrayList();
        final_stateList = ArrayList();
        reportsToUserList = ArrayList()
        val nav_Menu = navigationView.menu
        delete_menuitem = nav_Menu.findItem(R.id.menu_delete)
        llCategory = findViewById(R.id.llCategory)
        ivAdd.setOnClickListener(this)
        Apply.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
    }

    private fun slideUp(child: BottomNavigationView) {
        child.clearAnimation()
        child.animate().translationY(0f).duration = 200
    }

    private fun slideDown(child: BottomNavigationView) {
        child.clearAnimation()
        child.animate().translationY(navigationView.height.toFloat()).duration = 200
    }

    private fun setDialogue() {
        val mBuilder = AlertDialog.Builder(this)
        mAlertDialog = mBuilder.show()
        mAlertDialog.setMessage("Invalid OTP Found")
        mAlertDialog.setTitle("OTP Mismatch")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ConstantVariable.resultCode_distributor) {
            if (data?.extras?.get("CustomerPosition") != null) {
                tvDistributor!!.setText(data?.extras?.get("CustomerPosition").toString())
            }
        } else if (requestCode == ConstantVariable.OTP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data?.extras?.get("otp") != null) {
                    //var resultOtp: String = data?.extras?.get("otp").toString()
                    if (ConstantVariable.verifyAvailableNetwork(context as Activity)) {
                        if (isValid()) {
                            if (operation.equals("new", ignoreCase = true)) {
                                if (isMobileValid == true) {
                                    //saveCustomer(navigationView)
                                } else {
                                    ConstantVariable.onSNACK(
                                        navigationView,
                                        "Mobile No is invalid"
                                    )
                                }
                            }
                        } else {
                            ConstantVariable.onSNACK(navigationView, msg)
                        }
                    } else {
                        // insertCustomerInDB()
                    }

                }
            }
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            ivAdd -> {
                ConstantVariable.animationEffect(v, this!!)
                setStateDialogue()
            }
            Apply -> {
                ConstantVariable.animationEffect(Apply, this!!)
                if (ConstantVariable.verifyAvailableNetwork(context as Activity)) {
                    if (isValid()) {
                        if (operation.equals("edit", ignoreCase = true)
                            || operation.equals("search", ignoreCase = true)
                        ) {
                            if (custId_global != 0) {
                                //if (isMobileValid == true) {
                                if (true) {
                                    updateCustomer(v, custId_global, 1)
                                } else {
                                    ConstantVariable.onSNACK(v, "Invalid mobile no.")
                                }
                            }
                        } else if (operation.equals("new", ignoreCase = true)) {
                            //if (isMobileValid == true) {
                            if (true) {
                                saveCustomer(v)
                                /*str_otp = generateOTP(4); // commented because api is not there for responsible user and distributer and otp link not working on 15 June 2020
                                sendOtpTask(
                                    context,
                                    str_otp,
                                    edtMobileNo.getText().toString()
                                ).execute();*/
                            } else {
                                ConstantVariable.onSNACK(v, "Mobile No is invalid")
                            }
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
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish()
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

    fun isValid(): Boolean {
        if (edtFirstname.text == null || edtFirstname.text.toString().equals("")) {
            msg = "Please fill First Name"
            return false
        } else if (edtLastname.text == null || edtLastname.text.toString().equals("")) {
            msg = "Please fill Last Name"
            return false
        } else if (edtUsername.text == null || edtUsername.text.toString().equals("")) {
            msg = "Please fill Username"
            return false
        } else if (edtPassword.text == null || edtPassword.text.toString().equals("")) {
            msg = "Please fill User password"
            return false
        } else if (categoryName.equals("", ignoreCase = true)) {
            msg = "Please select category"
            return false
        } else if (edtMobileNo.text == null || edtMobileNo.text.equals("")) {
            msg = "Please fill Mobile No."
            return false
        } /*else if (edtEmail.text == null || edtEmail.text.equals("")) {
            msg = "Please fill Email Address."
            return false
        } else if (!isValidEmail(edtEmail.text.toString())) {
            msg = "Please fill valid email address."
            return false
        }*/ else if (final_stateList!!.size == 0) {
            msg = "State Allotment is mandatory."
            return false
        }
        return true
    }

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches();
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_delete -> {
                    if (item.title.contains("deactivate", true)) {
                        updateCustomer(navigationView, custId_global, 0)
                    } else {
                        updateCustomer(navigationView, custId_global, 1)
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun saveCustomer(view: View) {
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val result1 = GetUserDetailResponseResult()
        result1.setUserId(0)
        result1.userName = edtUsername.text.toString()
        result1.user_password = edtPassword.text.toString()
        result1.category = spCategory.text.toString()
        result1.fName = edtFirstname.text.toString()
        result1.lName = edtLastname.text.toString()
        result1.reportsTo = reportsToID
        result1.mobileNo = edtMobileNo.text.toString()
        result1.emailId = edtEmail.text.toString()
        result1.isactive = "1"
        val statelist = ArrayList<GetUserDetailResponseResult.Result>()
        for (i: Int in final_stateList.indices) {
            val obj = GetUserDetailResponseResult.Result();
            obj.state_code = final_stateList[i].state_code
            statelist.add(obj)
        }
        result1.allotedStates = statelist

        val call: Call<NewOrderResponse> = apiInterface!!.SaveUser(result1)

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
                        ConstantVariable.onToastSuccess(context, response.body()!!.getMessage()!!)
                        finish()
                    } else {
                        ConstantVariable.onSNACK(view, response.body()!!.getMsg()!!)
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    ConstantVariable.onSNACK(view, "Something went wrong!!")
                }
            }
        })
    }

    private fun updateCustomer(view: View, userId: Int, isActive: Int) {
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        val result1 = GetUserDetailResponseResult()
        result1.setUserId(userId)
        result1.userName = edtUsername.text.toString()
        result1.user_password = edtPassword.text.toString()
        result1.category = spCategory.text.toString()
        result1.fName = edtFirstname.text.toString()
        result1.lName = edtLastname.text.toString()
        result1.reportsTo = reportsToID
        result1.mobileNo = edtMobileNo.text.toString()
        result1.emailId = edtEmail.text.toString()
        result1.isactive = isActive.toString()
        val statelist = ArrayList<GetUserDetailResponseResult.Result>()
        for (i: Int in final_stateList.indices) {
            val obj = GetUserDetailResponseResult.Result();
            obj.state_code = final_stateList[i].state_code
            statelist.add(obj)
        }
        result1.allotedStates = statelist

        val call: Call<NewOrderResponse> = apiInterface!!.SaveUser(result1)

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
                        ConstantVariable.onToastSuccess(context, response.body()!!.getMessage()!!)
                        finish()
                    } else {
                        ConstantVariable.onSNACK(view, response.body()!!.getMsg()!!)
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    ConstantVariable.onSNACK(view, "Something went wrong!!")
                }
            }
        })
    }

    fun getUserDetailByUserId(userId: Int) {
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        val searchExpenseRequestModel = SearchExpenseResult()
        searchExpenseRequestModel.setUserId(userId)
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<GetUserDetailResponseModel>? =
            apiInterface!!.GetUserDetail(searchExpenseRequestModel)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<GetUserDetailResponseModel> {
            override fun onFailure(call: Call<GetUserDetailResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(edtUsername, "Something went wrong")
            }

            override fun onResponse(
                call: Call<GetUserDetailResponseModel>,
                response: Response<GetUserDetailResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {

                    if (response?.body()?.getStatus().equals("success")) {
                        var userDetails =
                            response.body()?.getResult() as List<GetUserDetailResponseResult>
                        if (userDetails.size > 0) {
                            userDetailResult = userDetails.get(0)
                            edtFirstname?.setText(userDetailResult!!.fName)
                            edtLastname?.setText(userDetailResult!!.lName)
                            edtUsername?.setText(userDetailResult!!.userName)
                            edtPassword?.setText(userDetailResult!!.user_password)
                            edtMobileNo?.setText(userDetailResult!!.mobileNo)
                            edtEmail?.setText(userDetailResult!!.emailId)
                            spCategory?.setText(userDetailResult!!.category)
                            tvReportsto?.setText(userDetailResult!!.reportsToFullname)
                            categoryName = userDetailResult!!.category!!
                            reportsToName = userDetailResult!!.reportsToFullname!!
                            reportsToID = userDetailResult!!.reportsToId!!
                            if (userDetailResult!!.isactive != null && userDetailResult!!.isactive.equals(
                                    "true",
                                    true
                                )
                            ) {
                                delete_menuitem.setTitle("Deactivate User")
                            } else {
                                delete_menuitem.setTitle("Activate User")
                            }
                            getReportsToList(categoryName)
                            //getRequestApiForProfileImage(userDetailResult!!.getProfilePicUrl())
                        }
                    } else {
                        ConstantVariable.onSNACK(edtUsername, "Something went wrong")
                    }

                }
            }
        })
    }

}