package com.crm.crmapp.registration

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.model.CheckUserActiveResponse
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.utils.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
//    lateinit var background: ImageView
//    lateinit var tvCrm: TextView
//    lateinit var tvEss: TextView
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)
        context = this@SplashActivity
//        background = findViewById(R.id.background)
//        tvCrm = findViewById(R.id.tvCrm)
//        tvEss = findViewById(R.id.tvEss)
//        preferencesHelper = PreferencesHelper(context)
//        checkActiveInactive(PreferenceHelper.getPreLoginPreferences(context, Constants.USERNAME)!!)
//        val startAnimation =
//            AnimationUtils.loadAnimation(applicationContext, R.anim.fadein) as Animation
//        background.startAnimation(startAnimation)
//
//        val startAnimation2 =
//            AnimationUtils.loadAnimation(applicationContext, R.anim.anim_right_to_left) as Animation
//        tvCrm.startAnimation(startAnimation2)
//
//        val startAnimation3 =
//            AnimationUtils.loadAnimation(applicationContext, R.anim.anim_left_to_right) as Animation
//        tvEss.startAnimation(startAnimation3)
//
//        startAnimation.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation) {
//
//            }
//
//            override fun onAnimationEnd(animation: Animation) {
//
//                if (!preferencesHelper!!.userId.equals("", ignoreCase = true)) {
//                    Handler(Looper.myLooper()!!).postDelayed({
//                        var intent = Intent(this@SplashActivity, Main2Activity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        startActivity(intent)
//                        finish()
//                        (context as SplashScreen).overridePendingTransition(
//                            R.anim.slide_in,
//                            R.anim.slide_out
//                        )
//                    },200)
//                } else {
//                    var devId = preferencesHelper!!.deviceToken
//                    if (devId != null && devId.isNotEmpty() && !devId.equals(
//                            "",
//                            ignoreCase = true
//                        )
//                    ) {
//                        Handler().postDelayed({
//                            var intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            startActivity(intent)
//                            finish()
//                            (context as SplashScreen).overridePendingTransition(
//                                R.anim.slide_in,
//                                R.anim.slide_out
//                            );
//                        }, 2000)
//                    } else {
//                        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(
//                            this@SplashActivity
//                        ) { instanceIdResult ->
//                            val newToken = instanceIdResult.token
//                            Log.e("newToken>", newToken)
//                            preferencesHelper!!.deviceToken = newToken
//                            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            startActivity(intent)
//                            finish()
//                            (context as SplashScreen).overridePendingTransition(
//                                R.anim.slide_in,
//                                R.anim.slide_out
//                            );
//                        }
//                    }
//                }
//
//            }
//
//            override fun onAnimationRepeat(animation: Animation) {
//                //TODO: Run when animation repeat
//            }
//        })
    }
    fun checkActiveInactive(user_name: String)  {
        val apiInterface = ApiClient.checkClientActiveInActive().create(ApiInterface::class.java)
        val call: Call<CheckUserActiveResponse> =
            apiInterface!!.checkUserActive(user_name)
        call.enqueue(object : Callback<CheckUserActiveResponse> {
            override fun onFailure(call: Call<CheckUserActiveResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<CheckUserActiveResponse>,
                response: Response<CheckUserActiveResponse>
            ) {
                if (response.body() != null) {
                    val status = response.body()!!.getStatus()
                    val messgage = response.body()!!.getMessage()
                    val result = response.body()!!.getResult()
                    if (messgage.equals("success"))
                        ConstantVariable.checkActiveInActive = "success"
                    else {
                        ConstantVariable.checkActiveInActive  = "fail"
                    }

                }

            }
        })


    }


}
