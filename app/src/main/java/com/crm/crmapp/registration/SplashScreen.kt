package com.crm.crmapp.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.crm.crmapp.R
import com.crm.crmapp.order.activity.Main2Activity
import com.crm.crmapp.order.util.PreferencesHelper
import com.google.firebase.iid.FirebaseInstanceId
import android.view.animation.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    lateinit var background: ImageView
    lateinit var tvCrm: TextView
    lateinit var tvEss: TextView
    lateinit var context: Context
    private var preferencesHelper: PreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        context = this@SplashScreen
        background = findViewById(R.id.background)
        tvCrm = findViewById(R.id.tvCrm)
        tvEss = findViewById(R.id.tvEss)
        preferencesHelper = PreferencesHelper(applicationContext)
        val startAnimation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.fadein) as Animation
        background.startAnimation(startAnimation)
        val startAnimation2 =
            AnimationUtils.loadAnimation(applicationContext, R.anim.anim_right_to_left) as Animation
        tvCrm.startAnimation(startAnimation2)
        val startAnimation3 =
            AnimationUtils.loadAnimation(applicationContext, R.anim.anim_left_to_right) as Animation
        tvEss.startAnimation(startAnimation3)

        startAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                //TODO: Run when animation start
            }
            override fun onAnimationEnd(animation: Animation) {
                //TODO: Run when animation end
                if (!preferencesHelper!!.userId.equals("", ignoreCase = true)) {
                    var tok = preferencesHelper!!.deviceToken
                    Log.e("TOKEN", "DEVICE TOKEN FOR PUSH NOTIFICATION $tok")
                    var intent = Intent(this@SplashScreen, Main2Activity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                    (context as SplashScreen).overridePendingTransition(
                        R.anim.slide_in,
                        R.anim.slide_out
                    );
                } else {
                    var devId = preferencesHelper!!.deviceToken
                    if (devId != null && devId.length > 0 && !devId.equals("", ignoreCase = true)) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            var intent = Intent(this@SplashScreen, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                            (context as SplashScreen).overridePendingTransition(
                                R.anim.slide_in,
                                R.anim.slide_out
                            )
                        }, 3000)
                    } else {
                        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this@SplashScreen,
                            { instanceIdResult ->
                                var newToken = instanceIdResult.token
                                Log.e("newToken>", newToken)
                                preferencesHelper!!.deviceToken = newToken
                                var intent = Intent(this@SplashScreen, LoginActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                                finish()
                                (context as SplashScreen).overridePendingTransition(
                                    R.anim.slide_in,
                                    R.anim.slide_out
                                );
                            })
                    }
                }
            }

            override fun onAnimationRepeat(animation: Animation) {
                //TODO: Run when animation repeat
            }
        })
    }
}


//package com.crm.crmapp.registration
//
//import android.app.Activity
//import android.app.ProgressDialog
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.util.Log
//import android.view.animation.*
//import android.widget.ImageView
//import android.widget.RelativeLayout
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.crm.crmapp.R
//import com.crm.crmapp.order.activity.Main2Activity
//import com.crm.crmapp.order.model.CheckUserActiveResponse
//import com.crm.crmapp.order.model.LogoutMode
//import com.crm.crmapp.order.util.ConstantVariable
//import com.crm.crmapp.order.util.PreferencesHelper
//import com.crm.crmapp.retrofit.ApiClient.Companion.checkClientActiveInActive
//import com.crm.crmapp.retrofit.ApiInterface
//import com.google.firebase.iid.FirebaseInstanceId
//import com.google.firebase.messaging.FirebaseMessaging
//
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import android.hardware.display.DisplayManager
//import android.view.Display
//
//
//class SplashScreen : AppCompatActivity() {
//    private lateinit var progressDialog: ProgressDialog
//    lateinit var background: ImageView
//    lateinit var tvCrm: TextView
//    lateinit var tvEss: TextView
//    lateinit var context: Context
//    private var preferencesHelper: PreferencesHelper? = null
//    lateinit var activity_splash_screen: RelativeLayout
//    var variable_name: String? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//        context = this@SplashScreen
//
//        background = findViewById(R.id.background)
//        tvCrm = findViewById(R.id.tvCrm)
//        tvEss = findViewById(R.id.tvEss)
//        activity_splash_screen = findViewById(R.id.activity_splash_screen)
//
//        preferencesHelper = PreferencesHelper(context)
//        val startAnimation =
//            AnimationUtils.loadAnimation(applicationContext, R.anim.fadein) as Animation
//        background.startAnimation(startAnimation)
//
//        val startAnimation2 =
//            AnimationUtils.loadAnimation(applicationContext, R.anim.anim_right_to_left) as Animation
//        tvCrm.startAnimation(startAnimation2)
//        val startAnimation3 =
//            AnimationUtils.loadAnimation(applicationContext, R.anim.anim_left_to_right) as Animation
//        tvEss.startAnimation(startAnimation3)
//
//        startAnimation.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation) {
//                //TODO: Run when animation start
//            }
//            override fun onAnimationEnd(animation: Animation) {
//                if (!preferencesHelper!!.userId.equals("", ignoreCase = true)) {
//                    var intent = Intent(this@SplashScreen, Main2Activity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    startActivity(intent)
//                    finish()
//                    (context as SplashScreen).overridePendingTransition(
//                        R.anim.slide_in,
//                        R.anim.slide_out
//                    );
//                } else {
//                    var devId = preferencesHelper!!.deviceToken
//                    if (devId != null && devId.length > 0 && !devId.equals("", ignoreCase = true)) {
//                        Handler().postDelayed({
//                            var intent = Intent(this@SplashScreen, LoginActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            startActivity(intent)
//                            finish()
//                            (context as SplashScreen).overridePendingTransition(
//                                R.anim.slide_in,
//                                R.anim.slide_out
//                            );
//                        }, 2000)
//                    } else {
//                        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this@SplashScreen) { instanceIdResult ->
//                            val newToken = instanceIdResult.token
//                            if (newToken != null) {
//                                Log.e("newToken>", newToken)
//                                preferencesHelper!!.deviceToken = newToken
//                                val intent = Intent(this@SplashScreen, LoginActivity::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                startActivity(intent)
//                                finish()
//                                (context as SplashScreen).overridePendingTransition(
//                                    R.anim.slide_in,
//                                    R.anim.slide_out
//                                )
//                            } else {
//                                Log.e("newToken>", "Token is null")
//                                // Handle the case where the token is null
//                            }
//                        }
//
//                    }
//                }
//            }
//
//            override fun onAnimationRepeat(animation: Animation) {
//                //TODO: Run when animation repeat
//            }
//        })
//
//    }
//
//    fun checkActiveInactive(user_name: String) {
//        progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
//        val apiInterface = checkClientActiveInActive().create(ApiInterface::class.java)
//        val call: Call<CheckUserActiveResponse> =
//            apiInterface!!.checkUserActive(user_name)
//        call.enqueue(object : Callback<CheckUserActiveResponse> {
//
//            override fun onFailure(call: Call<CheckUserActiveResponse>, t: Throwable) {
//                progressDialog.dismiss()
//                variable_name = "fail"
//            }
//
//            override fun onResponse(
//                call: Call<CheckUserActiveResponse>,
//                response: Response<CheckUserActiveResponse>
//            ) {
//                progressDialog.dismiss()
//                try {
//                    if (response.body() != null) {
//                        val message = response.body()!!.getMessage()
//                        if (message.equals("success")) {
//                            ConstantVariable.checkActiveInActive = "success"
//                        } else {
//                            ConstantVariable.checkActiveInActive = "fail"
//                        }
//
//                    }
//                } catch (e: NullPointerException) {
//                    ConstantVariable.onSNACK(activity_splash_screen, "Something went wrong!!")
//                }
//            }
//        })
//
//
//    }
//
//
//    private fun logoutUser(user_name: String) {
//
//        val apiInterface = checkClientActiveInActive().create(ApiInterface::class.java)
//        val call: Call<LogoutMode>? =
//            apiInterface!!.logoutUser(user_name)
//        Log.e("CHECK USER INACTIVE>>", "" + call?.request()?.url())
//        call!!.enqueue(object : Callback<LogoutMode> {
//            override fun onFailure(call: Call<LogoutMode>, t: Throwable) {
//
//
//            }
//
//            override fun onResponse(
//                call: Call<LogoutMode>,
//                response: Response<LogoutMode>
//            ) {
//                if (response.body() != null) {
//
//
//                }
//            }
//        })
//    }
//
//}