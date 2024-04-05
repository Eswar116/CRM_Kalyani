package com.crm.crmapp.registration

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.activity.Main2Activity
import com.crm.crmapp.order.model.CheckUserActiveResponse
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.utils.PreferenceHelper
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener   {
    protected lateinit var mainActivity: LoginActivity
    private lateinit var tvUsername: TextInputEditText

    //    private lateinit var tvPassword: TextInputEditText
    private lateinit var tvPassword: EditText
    private lateinit var cbSignIn: AppCompatCheckBox
    private lateinit var llLgoin: LinearLayout
    private var progressDialog: ProgressDialog? = null

    private lateinit var progressDialog1: ProgressDialog
    private var preferencesHelper: PreferencesHelper? = null
    private lateinit var loginResultModel: ArrayList<LoginResponse.Result>
    private var signInMe: Int? = null
    private var addUrl: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setIds()
        setListeners()
    }

    private fun setListeners() {
        cbSignIn.setOnCheckedChangeListener(this)
        llLgoin.setOnClickListener {
            ConstantVariable.animationEffect(llLgoin, this!!)
            this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            checkValidation()
        }

        addUrl!!.setOnClickListener {
            showCustomDialog()
        }
    }

    private fun checkValidation() {
        if (tvUsername.text.toString().isEmpty()) {
            ConstantVariable.onSNACK(tvUsername, "Please enter user name.")

        } else if (tvPassword.text.toString().isEmpty()) {
            ConstantVariable.onSNACK(tvUsername, "Please enter password.")
        } else {
            if (ConstantVariable.verifyAvailableNetwork(this)) {
                checkActiveInactive(tvUsername.text!!.trim().toString())
//                if (ConstantVariable.checkActiveInActive.equals("success")) {
//                    getLoginApi()
//                } else {
//                    ConstantVariable.onSNACK(tvUsername, "Please Contact the Admin!")
//                }
            } else {
                ConstantVariable.onSNACK(tvUsername, "Please check your internet connection")
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            signInMe = 0
        } else {
            signInMe = 1
        }
    }

    private fun getLoginApi() {
        Log.e("BaseUrl>>", ConstantVariable.BaseUrl)
        if (progressDialog == null) {
            progressDialog = ConstantVariable.showProgressDialog(this, "Loading...")
        }
//        val progressDialog = CustomProgressDialog(this)
//        progressDialog.show()

        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var loginRequest = LoginRequest()

        loginRequest.setUserName(tvUsername.text.toString())
        loginRequest?.setUserPassword(tvPassword.text.toString())

        if (preferencesHelper!!.deviceToken != null) {
            loginRequest.setDeviceKey(preferencesHelper!!.deviceToken!!)
        } else {
            loginRequest.setDeviceKey("")
        }
        loginRequest?.setIsRemember(signInMe)


        var sdkVersion: Int
        var android_id: String
        try {
            sdkVersion = android.os.Build.VERSION.SDK_INT
            android_id = Secure.getString(
                this.getContentResolver(),
                Secure.ANDROID_ID
            )
            var deviceDesc = android_id + sdkVersion.toString()
            loginRequest?.setDeviceDesc(deviceDesc)
        } catch (e: Exception) {
        }

        var call: Call<LoginResponse>? = apiInterface!!.loginApi(loginRequest)
        call?.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(
                    tvUsername,
                    "NO Record Found so please check your username and password."
                )
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                progressDialog!!.dismiss()
                try {

                    loginResultModel =
                        response.body()!!.getResult() as ArrayList<LoginResponse.Result>
                    val result = LoginResponse.Result()
//                    PreferenceHelper.setPreLoginPreferences(
//                        this@LoginActivity, USERNAME, loginResultModel[0].getUserName()!!
//                    )
                    result.setEmailId(loginResultModel.get(0).getEmailId()!!)
                    result.setFName(loginResultModel.get(0).getFName()!!)
                    result.setLName(loginResultModel.get(0).getLName()!!)
                    result.setProfilePicUrl(loginResultModel.get(0).getProfilePicUrl()!!)
                    result.setUserId(loginResultModel.get(0).getUserId()!!)
                    result.setCategory(loginResultModel.get(0).getCategory()!!)
                    result.setMobileNo(loginResultModel.get(0).getMobileNo()!!)
                    result.setDepartment(loginResultModel.get(0).getDepartment()!!)
                    result.setUserName(loginResultModel.get(0).getUserName()!!)
                    result.setReportsToFullname(loginResultModel.get(0).getReportsToFullname()!!)
                    preferencesHelper!!.userId = loginResultModel.get(0).getUserId().toString()
                    preferencesHelper!!.mobileId = loginResultModel.get(0).getMobileNo().toString()
                    preferencesHelper!!.userName = loginResultModel.get(0).getFName() + " " +
                            loginResultModel.get(0).getLName()!!
                    preferencesHelper!!.saveObject(this@LoginActivity, result)
                    ConstantVariable.onSNACK(tvUsername, response.body()?.getMessage()!!)
                    val intent = Intent(this@LoginActivity, Main2Activity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } catch (e: Exception) {
                    ConstantVariable.onSNACK(tvUsername, "NO Record Found")
                }
            }
        })
    }

    private fun setIds() {
        tvUsername = findViewById<TextInputEditText>(R.id.tvUsername)
        tvPassword = findViewById<EditText>(R.id.tvPassword)
        cbSignIn = findViewById<AppCompatCheckBox>(R.id.cbSignIn)
        addUrl = findViewById<TextView>(R.id.addUrl)
        llLgoin = findViewById<LinearLayout>(R.id.llLgoin)
        preferencesHelper = PreferencesHelper(this)
        loginResultModel = ArrayList()
        signInMe = 1
        /*tvUsername.setText("shrik_nt")
        tvPassword.setText("1234")*/
    }


    private fun showCustomDialog() {
        var dialog: Dialog? = null
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog!!.setContentView(R.layout.dialog_addurl)
        dialog!!.setCancelable(true)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        var tvUrl = (dialog?.findViewById(R.id.tvUrl) as TextInputEditText)


        (dialog!!.findViewById(R.id.llUrl) as LinearLayout).setOnClickListener {
            if (!ConstantVariable.BaseUrl.equals("")) {

                ConstantVariable.BaseUrl = ""
                ConstantVariable.BaseUrl =
                    "http://" + tvUrl.text.toString() + "/CRMMobileAPI/webresources/"

                Log.e("BaseUrl", ConstantVariable.BaseUrl)
            } else {
                ConstantVariable.BaseUrl =
                    "http://" + tvUrl.text.toString() + "/CRMMobileAPI/webresources/"
            }

            //  preferencesHelper!!.Base_URL = "http://"+ tvUrl.text.toString()+"/CRMMobileAPI/webresources/"

            dialog.dismiss()

        }
        dialog!!.show()
        dialog!!.window!!.attributes = lp
    }

    fun checkActiveInactive(user_name: String) {
        progressDialog1 = ConstantVariable.showProgressDialog(this, "Loading")
        val apiInterface = ApiClient.checkClientActiveInActive().create(ApiInterface::class.java)
        val call: Call<CheckUserActiveResponse> =
            apiInterface!!.checkUserActive(user_name)
        call.enqueue(object : Callback<CheckUserActiveResponse> {

            override fun onFailure(call: Call<CheckUserActiveResponse>, t: Throwable) {
                progressDialog1.dismiss()
            }

            override fun onResponse(
                call: Call<CheckUserActiveResponse>,
                response: Response<CheckUserActiveResponse>
            ) {
                9
                progressDialog1.dismiss()
                try {
                    if (response.body() != null) {
                        val message = response.body()!!.getMessage()
                        if (message.equals("success")) {
                            ConstantVariable.checkActiveInActive = "success"
                            getLoginApi()
                        } else {
                            ConstantVariable.checkActiveInActive = "fail"
                            ConstantVariable.onSNACK(tvUsername, message!!)
                        }

                    }
                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(tvUsername, "Something went wrong!!")
                }

            }
        })


    }


}