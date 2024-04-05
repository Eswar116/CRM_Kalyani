package com.crm.crmapp.marketPlan

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.adapter.CustomerAttachmentAdapter
import com.crm.crmapp.databinding.FragmentMarketPlanNewBinding
import com.crm.crmapp.marketPlan.adapters.*
import com.crm.crmapp.marketPlan.models.CategoryListRequestResponseModel
import com.crm.crmapp.marketPlan.models.CityListRequestResponseModel
import com.crm.crmapp.marketPlan.models.SchemeListRequestResponseModel
import com.crm.crmapp.marketPlan.models.StateListRequestResponseModel
import com.crm.crmapp.order.activity.CameraActivity
import com.crm.crmapp.order.activity.SearchCustomer
import com.crm.crmapp.order.model.DocDetailModel
import com.crm.crmapp.order.util.CustomDate
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewMarketPlanFragment : Activity(), ViewNavigation {

    private var preferencesHelper: PreferencesHelper? = null
    private var docDetailList: ArrayList<DocDetailModel>? = null
    private var respList: ArrayList<ResponsiblePersoneResponse.Result>? = null
    var imageBase64: ArrayList<String>? = null
    var binding: FragmentMarketPlanNewBinding? = null
    private var resultList1: java.util.ArrayList<NewMarketPlanRequest.Result1>? = null
    private var resultList2: java.util.ArrayList<NewMarketPlanRequest.Result2>? = null
    private var resultList3: java.util.ArrayList<NewMarketPlanRequest.Result3>? = null
    private lateinit var progressDialog: ProgressDialog
    private lateinit var progressDialog2: ProgressDialog
    private lateinit var progressDialog3: ProgressDialog
    private var customerId: String? = "0"
    lateinit var yyddmDate_frmDate: CustomDate
    lateinit var yyddmDate_toDate: CustomDate
    lateinit var context: Context
    private lateinit var mAlertDialogState: AlertDialog
    private lateinit var mAlertDialogCity: AlertDialog
    private lateinit var mAlertDialogCategory: AlertDialog
    private lateinit var mAlertDialogScheme: AlertDialog
    private lateinit var rvStateList: RecyclerView
    private lateinit var rvCityList: RecyclerView
    private lateinit var rvCategory: RecyclerView
    private lateinit var rvScheme: RecyclerView
    private var stateId: String = ""
    private var cityId: String = ""
    private var categoryName: String = ""
    private var scheme_code: String = ""
    private var msg: String = ""
    lateinit var stateList: ArrayList<StateListRequestResponseModel.Result>
    lateinit var cityList: ArrayList<CityListRequestResponseModel.Result>
    lateinit var categoryList: ArrayList<CategoryListRequestResponseModel.Result>
    lateinit var schemeList: ArrayList<SchemeListRequestResponseModel.Result>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_market_plan_new)
        binding!!.marketNewViewModel = NewMarketPlanViewModel(this!!)
        context = this
        preferencesHelper = PreferencesHelper(context)
        docDetailList = ArrayList()
        resultList1 = ArrayList()
        resultList2 = ArrayList()
        resultList3 = ArrayList()
        respList = ArrayList()
        imageBase64 = ArrayList()
        yyddmDate_frmDate = CustomDate()
        yyddmDate_toDate = CustomDate()
        setListeners()
        setCurrentDate()
        setDataInIDs()
        getStateList()
        getCategoryList()
        setCurrentDate()
        getSchemeList()
    }

    private fun setCurrentDate() {
        val c = Calendar.getInstance()
        val yy = c.get(Calendar.YEAR)
        val mm = c.get(Calendar.MONTH)
        val dd = c.get(Calendar.DAY_OF_MONTH)
        val mFormat = DecimalFormat("00");
        // set current date into textview
        binding!!.tvFromDate.setText(
            StringBuilder()
                .append(mFormat.format(dd)).append("-").append(mFormat.format(mm + 1)).append("-")
                .append(yy)
        )
        yyddmDate_frmDate.date = ("" + yy + "-" + mFormat.format(mm + 1) + "-" + mFormat.format(dd))

        binding!!.tvToDate.setText(
            StringBuilder()
                .append(mFormat.format(dd)).append("-").append(mFormat.format(mm + 1)).append("-")
                .append(yy)
        )
        yyddmDate_toDate.date = ("" + yy + "-" + mFormat.format(mm + 1) + "-" + mFormat.format(dd))
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInIDs() {
        binding?.etResponsiblePerson?.text = "Plan Owner : " + preferencesHelper!!.userName
        binding?.tvFromDate?.text = "Select date"
        binding?.tvToDate?.text = "Select date"
        binding?.etState?.text = "Select state"
        binding?.etCity?.text = "Select city"
        binding?.etCategory?.text = "Select category"
        binding?.etScheme?.text = "Select scheme"
        stateList = ArrayList();
        cityList = ArrayList();
        schemeList = ArrayList();

        ConstantVariable.addStar("Period (From Date)", binding!!.lblFromDate)
        ConstantVariable.addStar("Period (To Date)", binding!!.lblToDate)
        ConstantVariable.addStar("State", binding!!.tvstate)
        ConstantVariable.addStar("Distributor", binding!!.tvDistributor)
        ConstantVariable.addStar("Market Plan Name", binding!!.lblMarketPlanName)
        ConstantVariable.addStar("Responsible Person", binding!!.tvResponsiblePerson)
        ConstantVariable.addStar("Category", binding!!.lblCategory)
        ConstantVariable.addStar("Scheme", binding!!.lblScheme)
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

        layoutParams.copyFrom(mAlertDialogState.window?.attributes);
        rvStateList = mAlertDialogState.findViewById<RecyclerView>(R.id.rvTravelling)!!
        rvStateList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvStateList.adapter =
            StateListAdapter(context, stateList, object : StateListAdapter.BtnClickListener {
                override fun onBtnClick(position: Int, llRow: LinearLayout, type: String) {
                    llRow.setBackgroundColor(Color.parseColor("#d6d6d6"))
                    mAlertDialogState.dismiss()
                    binding!!.etState.text = type
                    stateId = stateList.get(position).state_code!!
                    getCityList(stateId);
                }
            })
    }

    private fun setCityDialogue() {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_expense_travelling, null)
        val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
        //show dialog
        val displayMetric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetric)
        val layoutParams = WindowManager.LayoutParams()
        mAlertDialogCity = mBuilder.show()

        layoutParams.copyFrom(mAlertDialogCity.getWindow()!!.getAttributes());
        rvCityList = mAlertDialogCity.findViewById<RecyclerView>(R.id.rvTravelling)!!
        rvCityList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvCityList.adapter =
            CityListAdapter(context, cityList, object : CityListAdapter.BtnClickListener {
                override fun onBtnClick(position: Int, llRow: LinearLayout, type: String) {
                    llRow.setBackgroundColor(Color.parseColor("#d6d6d6"))
                    mAlertDialogCity.dismiss()
                    binding!!.etCity.setText(type)
                    cityId = cityList.get(position).city_code!!
                }
            })
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
                        binding!!.etCategory.setText(type)
                        categoryName = categoryList.get(position).cat_name!!
                    }
                })
    }

    private fun setSchemeDialogue() {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_expense_travelling, null)
        val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
        //show dialog
        val displayMetric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetric)
        val layoutParams = WindowManager.LayoutParams()
        mAlertDialogScheme = mBuilder.show()

        layoutParams.copyFrom(mAlertDialogScheme.getWindow()!!.getAttributes());
        rvScheme = mAlertDialogScheme.findViewById<RecyclerView>(R.id.rvTravelling)!!
        rvScheme.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvScheme.adapter =
            SchemeListAdapter(context, schemeList, object : SchemeListAdapter.BtnClickListener {
                override fun onBtnClick(position: Int, llRow: LinearLayout, type: String) {
                    llRow.setBackgroundColor(Color.parseColor("#d6d6d6"))
                    mAlertDialogScheme.dismiss()
                    binding!!.etScheme.setText(type)
                    scheme_code = schemeList.get(position).scheme_code!!
                }
            })
    }

    private fun getStateList() {
        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<StateListRequestResponseModel>? =
            apiInterface!!.getStateList(preferencesHelper!!.userId!!.toIntOrNull()!!)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<StateListRequestResponseModel> {
            override fun onFailure(call: Call<StateListRequestResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(binding!!.tvCancel, "Something went wrong")
            }

            override fun onResponse(
                call: Call<StateListRequestResponseModel>,
                response: Response<StateListRequestResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    stateList = response.body()
                        ?.getResult() as ArrayList<StateListRequestResponseModel.Result>
                    if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals(
                            "edit",
                            true
                        )
                    ) {
                        /*for (i in stateList.indices) {
                            if (stateList[i].state_code == expDetailResult.expTypeId) {
                                binding!!.etState.setText(stateList[i].state_name)
                                break;
                            }
                        }*/
                    }
                }
            }
        })
    }

    private fun getCityList(stateid: String) {
        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<CityListRequestResponseModel>? =
            apiInterface!!.getCityList(stateid)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<CityListRequestResponseModel> {
            override fun onFailure(call: Call<CityListRequestResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(binding!!.tvCancel, "Something went wrong")
            }

            override fun onResponse(
                call: Call<CityListRequestResponseModel>,
                response: Response<CityListRequestResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    cityList = response.body()
                        ?.getResult() as ArrayList<CityListRequestResponseModel.Result>
                    if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals(
                            "edit",
                            true
                        )
                    ) {
                        /*for (i in stateList.indices) {
                            if (stateList[i].state_code == expDetailResult.expTypeId) {
                                binding!!.etState.setText(stateList[i].state_name)
                                break;
                            }
                        }*/
                    }
                }
            }
        })
    }

    private fun getCategoryList() {
        progressDialog2 = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<CategoryListRequestResponseModel>? =
            apiInterface!!.getCategoryList(preferencesHelper!!.userId!!.toIntOrNull()!!)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<CategoryListRequestResponseModel> {
            override fun onFailure(call: Call<CategoryListRequestResponseModel>, t: Throwable) {
                progressDialog2!!.dismiss()
                ConstantVariable.onSNACK(binding!!.tvCancel, "Something went wrong")
            }

            override fun onResponse(
                call: Call<CategoryListRequestResponseModel>,
                response: Response<CategoryListRequestResponseModel>
            ) {
                progressDialog2!!.dismiss()
                if (response.body() != null) {
                    categoryList = response.body()
                        ?.getResult() as ArrayList<CategoryListRequestResponseModel.Result>
                    if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals(
                            "edit",
                            true
                        )
                    ) {
                        /*for (i in stateList.indices) {
                            if (stateList[i].state_code == expDetailResult.expTypeId) {
                                binding!!.etState.setText(stateList[i].state_name)
                                break;
                            }
                        }*/
                    }
                }
            }
        })
    }

    private fun getSchemeList() {
        progressDialog3 = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<SchemeListRequestResponseModel>? =
            apiInterface!!.getSchemeList(preferencesHelper!!.userId!!.toIntOrNull()!!)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<SchemeListRequestResponseModel> {
            override fun onFailure(call: Call<SchemeListRequestResponseModel>, t: Throwable) {
                progressDialog3!!.dismiss()
                ConstantVariable.onSNACK(binding!!.tvCancel, "Something went wrong")
            }

            override fun onResponse(
                call: Call<SchemeListRequestResponseModel>,
                response: Response<SchemeListRequestResponseModel>
            ) {
                progressDialog3!!.dismiss()
                if (response.body() != null) {
                    schemeList = response.body()
                        ?.getResult() as ArrayList<SchemeListRequestResponseModel.Result>
                    if (intent.getStringExtra(ConstantVariable.TAG_FROM).equals(
                            "edit",
                            true
                        )
                    ) {
                        /*for (i in stateList.indices) {
                            if (stateList[i].state_code == expDetailResult.expTypeId) {
                                binding!!.etState.setText(stateList[i].state_name)
                                break;
                            }
                        }*/
                    }
                }
            }
        })
    }

    private fun setListeners() {
        binding!!.svCustomer.setOnClickListener {
            if (!stateId.equals("", true)) {
                ConstantVariable.animationEffect(binding!!.svCustomer, this!!)
                val intent = Intent(this, SearchCustomer::class.java)
                intent.putExtra(ConstantVariable.TAG_STATE_CODE, stateId);
                intent.putExtra("NewOrderKey", "new_market_plan")
                startActivityForResult(intent, ConstantVariable.resultCode_customer)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            } else {
                ConstantVariable.onToast(this@NewMarketPlanFragment, "Please select state first.")
            }
        }
        binding!!.ivAdd.setOnClickListener {
            Log.e("clicked", "clicked>>")
            var intent = Intent(this, CameraActivity::class.java)
            startActivityForResult(intent, ConstantVariable.resultCode)
        }

        binding!!.tvFromDate.setOnClickListener {
            ConstantVariable.animationEffect(binding!!.tvFromDate, this!!)
            ConstantVariable.datePickerWithYYDDMMRequest_date_minimum_date(
                this,
                binding!!.tvFromDate,
                yyddmDate_frmDate
            )
        }
        binding!!.tvToDate.setOnClickListener {
            ConstantVariable.animationEffect(binding!!.tvToDate, this!!)
            ConstantVariable.datePickerWithYYDDMMRequest_date_minimum_date(
                this,
                binding!!.tvToDate,
                yyddmDate_toDate
            )
        }

        binding!!.etState.setOnClickListener {
            ConstantVariable.animationEffect(binding!!.etState, this!!)
            setStateDialogue()
        }

        binding!!.etCity.setOnClickListener {
            ConstantVariable.animationEffect(binding!!.etCity, this!!)
            if (cityList != null && cityList!!.size > 0) {
                setCityDialogue()
            } else {
                ConstantVariable.onSNACK(it, "Please select state first")
            }
        }

        binding!!.etCategory.setOnClickListener {
            ConstantVariable.animationEffect(binding!!.etCategory, this!!)
            setCategoryDialogue()
        }

        binding!!.etScheme.setOnClickListener {
            ConstantVariable.animationEffect(binding!!.etScheme, this!!)
            setSchemeDialogue()
        }

        binding!!.tvResponsiblePerson.setOnClickListener {
            if (!stateId.equals("", true)) {
                val intent = Intent(this, ResponsiblePersonListActivity::class.java)
                intent.putExtra(ConstantVariable.TAG_STATE_CODE, stateId);
                this.startActivityForResult(intent, ConstantVariable.ResponsilberesultCode)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            } else {
                ConstantVariable.onToast(this@NewMarketPlanFragment, "Please select state first.")
            }
        }

        binding!!.Apply.setOnClickListener {
            if (ConstantVariable.verifyAvailableNetwork(this)) {
                if (validate()) {
                    saveMP()
                } else {
                    ConstantVariable.onSNACK_GREY(it, msg)
                }
            } else {
                ConstantVariable.onToastSuccess(
                    this@NewMarketPlanFragment,
                    "No Internet Connection"
                )
            }
        }

        binding!!.tvCancel.setOnClickListener {
            ConstantVariable.animationEffect(binding!!.tvCancel, this!!)
            this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            finish()
        }

    }

    private fun validate(): Boolean {
        var isValid: Boolean = true
        if (binding!!.etMarketPlanName.text.toString().equals("", true)) {
            isValid = false
            msg = "Please fill Market Plan Name"
        } else if (stateId.equals("", true)) {
            isValid = false
            msg = "Please Select State"
        } else if (cityId.equals("", true)) {
            isValid = false
            msg = "Please Select City"
        } else if (categoryName.equals("", true)) {
            isValid = false
            msg = "Please Select Category"
        } else if (scheme_code.equals("", true)) {
            isValid = false
            msg = "Please Select Scheme"
        } else if (customerId.equals("0", true)) {
            isValid = false
            msg = "Please Choose a Distributor"
        } else if (respList!!.size == 0) {
            isValid = false
            msg = "Please Choose a Responsible Person"
        } else if (binding!!.etRemarks.text.toString().equals("", true)) {
            isValid = false
            msg = "Please Enter remarks"
        } else {
            //var fromdate = Date(ConstantVariable.parseDateTo_ddMMyyyy_From_yyyyMMDD_WithoutTime(yyddmDate_frmDate.date))
            //var todate = Date(ConstantVariable.parseDateTo_ddMMyyyy_From_yyyyMMDD_WithoutTime(yyddmDate_toDate.date))
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val fromdate: Date = sdf.parse(yyddmDate_frmDate.date)
            val todate: Date = sdf.parse(yyddmDate_toDate.date)

            if (todate.before(fromdate)) {
                isValid = false
                msg = "To date should not be before From Date"
            }
        }
        return isValid
    }

    private fun saveMP() {
        resultList3?.clear()
        resultList2?.clear()
        progressDialog = ConstantVariable.showProgressDialog(this, "Loading...")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        val result1 = NewMarketPlanRequest().Result1()

        result1.userId = preferencesHelper!!.userId!!.toIntOrNull()
        result1.mkyplnName = binding!!.etMarketPlanName.text.toString()
        result1.frmDate = yyddmDate_frmDate.date
        result1.toDate = yyddmDate_toDate.date
        result1.stateCode = stateId
        result1.cityCode = cityId
        result1.category = categoryName
        result1.scheme = scheme_code
        result1.custId = customerId?.toInt()
        result1.remarks = binding!!.etRemarks.text.toString()

        resultList1?.add(result1!!)

        var newMarketPlanRequest = NewMarketPlanRequest()
        newMarketPlanRequest.setResult1(resultList1)

        for (i: Int in imageBase64!!.indices) {
            var result2 = NewMarketPlanRequest().Result2()
            if (imageBase64 != null)
                result2.setMpAttachment(imageBase64?.get(i))

            resultList2?.add(result2)
        }
        newMarketPlanRequest?.setResult2(resultList2)

        for (i: Int in respList!!.indices) {
            var result3 = NewMarketPlanRequest().Result3()
            if (respList != null) {
                result3.responsiblePersonId = (respList!!.get(i).getUserId())
                resultList3?.add(result3)
            }
        }

        newMarketPlanRequest?.setResult3(resultList3)

        var call: Call<NewMarketPlanResponse>? = apiInterface!!.saveMP(newMarketPlanRequest)

        call!!.enqueue(object : Callback<NewMarketPlanResponse> {
            override fun onFailure(call: Call<NewMarketPlanResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(binding!!.rvResponsible, "Something went wrong")
            }

            override fun onResponse(
                call: Call<NewMarketPlanResponse>,
                response: Response<NewMarketPlanResponse>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        ConstantVariable.onToastSuccess(context, response.body()!!.getMsg()!!)
                        finish()
                    } else {
                        ConstantVariable.onSNACK(binding!!.rvList, response.body()!!.getMsg()!!)
                    }
                } catch (e: Exception) {
                    ConstantVariable.onToastSuccess(
                        this@NewMarketPlanFragment,
                        "Something went Wrong"
                    )
                }
            }
        })
    }

    private fun setRecyclerView(binding: FragmentMarketPlanNewBinding) {
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = CustomerAttachmentAdapter(this!!, imageBase64, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("docDetailList", "docDetailList")
        if (requestCode == ConstantVariable.resultCode) {
            if (resultCode == Activity.RESULT_OK) {

                if (data?.extras?.get(ConstantVariable.docDetailModel) != null) {
                    var docListDetail =
                        data?.extras?.get(ConstantVariable.docDetailModel) as DocDetailModel
                    if (docListDetail != null) {
                        docDetailList?.add(docListDetail)
                    } else {
                        docListDetail = preferencesHelper!!.getObjectImage()
                        docDetailList?.add(preferencesHelper!!.getObjectImage())
                    }
                    Log.e("docDetailList", "docDetailList" + docDetailList?.size)

                    if (imageBase64 != null) {
                        imageBase64!!.add(docListDetail.uri)
                    }
                    setRecyclerView(binding!!)
                } else if (data?.extras?.get(ConstantVariable.selectedResponsibleData) != null) {
                    var responsibleData =
                        data?.extras?.get(ConstantVariable.selectedResponsibleData) as ResponsiblePersoneResponse.Result

                    if (respList!!.size > 0) {
                        for (rs: ResponsiblePersoneResponse.Result in respList!!) {
                            if (rs.getUserName().equals(responsibleData.getUserName(), true)) {
                                ConstantVariable.onToast(context, "Duplicate Responsible person")
                                break;
                            } else {
                                respList!!.add(responsibleData)
                            }
                        }
                        //binding!!.rvResponsible.layoutManager = LinearLayoutManager(this)
                        binding!!.rvResponsible.adapter!!.notifyDataSetChanged()
                    } else {
                        respList!!.add(responsibleData)
                        binding!!.rvResponsible.layoutManager = LinearLayoutManager(this)
                        binding!!.rvResponsible.adapter =
                            ResponsiblePersonListAdapter(this!!, respList)
                    }
                }
            }
        } else if (requestCode == ConstantVariable.resultCode_customer) {
            if (data?.extras?.get("CustomerPosition") != null) {
                binding!!.svCustomer!!.setText(data?.extras?.get("CustomerPosition").toString())
                customerId = data?.extras?.get("CustomerId").toString()
            }
        }

    }

}


