package com.crm.crmapp.customer.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import kotlinx.android.synthetic.main.activity_verification_header.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit


class VerificationHeader : AppCompatActivity() {

    private var tv_coundown: TextView? = null
    private var txt_correctotp: TextView? = null
    private var img_cancel: ImageView? = null
    private var imgbtn_greentick: ImageButton? = null
    private var edt_otp1: com.google.android.material.textfield.TextInputEditText? = null
    private var edt_otp2: com.google.android.material.textfield.TextInputEditText? = null
    private var edt_otp3: com.google.android.material.textfield.TextInputEditText? = null
    private var edt_otp4: com.google.android.material.textfield.TextInputEditText? = null
    private var edt_number: com.google.android.material.textfield.TextInputEditText? = null
    private var btn_continue: androidx.appcompat.widget.AppCompatButton? = null
    public var btn_resend_code: androidx.appcompat.widget.AppCompatButton? = null
    private lateinit var context: Context
    private lateinit var globalOtp: String
    private lateinit var globalnumber: String
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_header)
        setSupportActionBar(toolbar)
        if (intent != null && intent.getStringExtra(ConstantVariable.TAG_OTP) != null) {
            globalOtp = intent.getStringExtra(ConstantVariable.TAG_OTP)
            globalnumber = intent.getStringExtra(ConstantVariable.TAG_NUMBER)
        }
        initToolbarAndVariable();
        setListeners()
        countDownTimer();
    }

    private fun setListeners() {
        btn_continue!!.setOnClickListener {
            ConstantVariable.animationEffect(btn_continue!!, this!!)
            var otpStr: String =
                edt_otp1?.text.toString() + edt_otp2?.text.toString() + edt_otp3?.text.toString() + edt_otp4?.text.toString()

            if (otpStr.equals(globalOtp, true)) {
                val intent = Intent(context, NewCustomerActivity::class.java)
                intent.putExtra("otp", otpStr)
                setResult(Activity.RESULT_OK, intent)
                (context as Activity).finish()
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            } else {
                txt_correctotp?.visibility = View.VISIBLE
            }

        }
        btn_resend_code!!.setOnClickListener {
            ConstantVariable.animationEffect(btn_resend_code!!, this!!)
            var str_otp = generateOTP(4);
            globalOtp = str_otp.joinToString("");
            sendOtpTask(context, str_otp, globalnumber).execute();
            this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        img_cancel!!.setOnClickListener {
            ConstantVariable.animationEffect(img_cancel!!, this!!)
            (context as Activity).finish()
            this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        edt_otp1?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (edt_otp1?.getText().toString().length === 1)
                //size as per your requirement
                {
                    checkotp()
                    txt_correctotp?.visibility = View.GONE
                    edt_otp2?.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })

        edt_otp2?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (edt_otp2?.getText().toString().length === 1)
                //size as per your requirement
                {
                    checkotp()
                    txt_correctotp?.visibility = View.GONE
                    edt_otp3?.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })

        edt_otp3?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (edt_otp3?.getText().toString().length === 1)
                //size as per your requirement
                {
                    checkotp()
                    txt_correctotp?.visibility = View.GONE
                    edt_otp4?.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })

        edt_otp4?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (edt_otp4?.getText().toString().length === 1)
                //size as per your requirement
                {
                    checkotp()
                    txt_correctotp?.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })
    }


    private fun initToolbarAndVariable() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        // supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setSystemBarColor(this)
        context = this
        tv_coundown = findViewById(R.id.tv_coundown)
        txt_correctotp = this.findViewById<TextView>(R.id.txt_correctotp)
        img_cancel = findViewById(R.id.img_cancel)
        imgbtn_greentick = this.findViewById<ImageButton>(R.id.imgbtn_greentick)
        btn_continue = findViewById(R.id.btn_continue)
        btn_resend_code =
            findViewById(R.id.btn_resend_code)
        edt_otp1 = findViewById(R.id.edt_otp1)
        edt_otp2 = findViewById(R.id.edt_otp2)
        edt_otp3 = findViewById(R.id.edt_otp3)
        edt_otp4 = findViewById(R.id.edt_otp4)
        edt_number = this.findViewById(R.id.edt_number)
        btn_continue?.isEnabled = false
        txt_correctotp?.visibility = View.GONE
        edt_number?.setText(globalnumber);
        btn_resend_code?.visibility = View.GONE
        imgbtn_greentick?.visibility = View.VISIBLE
    }


    public fun countDownTimer() {
        countDownTimer = object : CountDownTimer((1000 * 60 * 2).toLong(), 1000) {
            override fun onTick(l: Long) {
                val text = String.format(
                    Locale.getDefault(), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(l) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(l) % 60
                )
                tv_coundown?.text = text
            }

            override fun onFinish() {
                btn_resend_code?.visibility = View.VISIBLE
                tv_coundown?.text = "00:00"
            }
        }
        (countDownTimer as CountDownTimer).start()
    }

    fun setSystemBarColor(act: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = act.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = act.resources.getColor(R.color.colorPrimaryDark)
        }
    }

    fun checkotp() {
        var otpStr: String =
            edt_otp1?.text.toString() + edt_otp2?.text.toString() + edt_otp3?.text.toString() + edt_otp4?.text.toString()

        if (otpStr.equals(globalOtp, true)) {
            btn_continue?.isEnabled = true
            // imgbtn_greentick?.visibility = View.VISIBLE
        } else {
            // imgbtn_greentick?.visibility = View.GONE
        }
    }

    inner class sendOtpTask(context1: Context, otpStr1: CharArray, number1: String) :
        AsyncTask<String, Void, String>() {

        var context = context1
        var otpStr = otpStr1
        var number = number1

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String? {
            var sb: StringBuilder? = null;
            var reader: BufferedReader? = null;
            var serverResponse: String? = null;

            try {
                val otp: String = otpStr.joinToString("");
                var str: String =
                    "http://perfectbulksms.com/Sendsmsapi.aspx?USERID=kalyaniinnerwear&PASSWORD=59661250&SENDERID=Klyani&TO=" + number + "&MESSAGE=Greeting%20from%20Kalyani.%20Your%20OTP%20for%20registration%20is%20" + otp + ".";
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
            Log.d("result_verification", result.toString());
            btn_resend_code!!.visibility = View.GONE
            countDownTimer()
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


}
