package com.crm.crmapp.attendence.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.attendence.activity.AttendenceActivity
import com.crm.crmapp.attendence.model.AttendanceDetailRequest
import com.crm.crmapp.attendence.model.AttendanceResponse
import com.crm.crmapp.attendence.model.Result
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AttendenceFragment : Fragment(), View.OnClickListener, CalendarView.OnDateChangeListener {
    var viewOfLayout: View? = null
    var calenderView: CalendarView? = null
    var tvCustomerName: TextView? = null
    var tvEdit: FloatingActionButton? = null
    var tvPunchIn: TextView? = null
    var tvPunchOut: TextView? = null
    var tvApply: TextView? = null
    var etRemarks: TextView? = null
    var progressDialog: ProgressDialog? = null
    private lateinit var tvState: TextView
    var selectedDate: String? = ""
    private lateinit var attendanceResponse: ArrayList<Result>
    private var preferencesHelper: PreferencesHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater?.inflate(R.layout.fragment_attendence, container, false)
        setIds()
        setListeners()
        return viewOfLayout
    }


    @SuppressLint("RestrictedApi")
    private fun setIds() {
        preferencesHelper = PreferencesHelper(this!!.activity!!)
        calenderView = viewOfLayout!!.findViewById(R.id.calenderView) as CalendarView
        tvState = viewOfLayout!!.findViewById(R.id.tvState) as TextView
        tvCustomerName = viewOfLayout!!.findViewById(R.id.tvCustomerName) as TextView
        etRemarks = viewOfLayout!!.findViewById(R.id.etRemarks) as TextView
        tvPunchIn = viewOfLayout!!.findViewById(R.id.tvPunchIn) as TextView
        tvPunchOut = viewOfLayout!!.findViewById(R.id.tvPunchOut) as TextView
        tvEdit =
            viewOfLayout!!.findViewById(R.id.tvEdit) as FloatingActionButton
        tvEdit!!.visibility = View.VISIBLE
        calenderView?.setMaxDate(System.currentTimeMillis())
        tvCustomerName!!.setText("Employee : " + preferencesHelper!!.userName)
    }


    private fun setListeners() {
        tvEdit!!.setOnClickListener(this)
        tvPunchIn!!.setOnClickListener(this)
        tvPunchOut!!.setOnClickListener(this)
        calenderView!!.setOnDateChangeListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tvEdit -> {
                ConstantVariable.animationEffect(this!!.tvEdit!!, activity!!)
                val intent = Intent(activity, AttendenceActivity::class.java);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                if (!selectedDate.equals("", ignoreCase = true)) {
                    intent.putExtra(ConstantVariable.SELECTED_DATE, selectedDate)
                } else {
                    intent.putExtra(ConstantVariable.SELECTED_DATE, currentDate())
                }
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
            tvPunchIn -> {
                ConstantVariable.animationEffect(this!!.tvPunchIn!!, activity!!)
                ConstantVariable.onTimeSeclect(this!!.tvPunchIn!!, activity!!)
            }
            tvPunchOut -> {
                ConstantVariable.animationEffect(this!!.tvPunchOut!!, activity!!)
                ConstantVariable.onTimeSeclect(this!!.tvPunchOut!!, activity!!)
            }
        }
    }


    private fun getAttendanceDetailApi(selectedDate: String) {
        attendanceResponse = ArrayList<Result>()
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var attendanceDetailRequest = AttendanceDetailRequest()

        val preferencesHelper = PreferencesHelper(this!!.activity!!)
        if (preferencesHelper!!.userId != null) {
            attendanceDetailRequest?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        }
        attendanceDetailRequest.setAttenDate(selectedDate)
        var call: Call<AttendanceResponse>? =
            apiInterface!!.getAttendanceDetail(attendanceDetailRequest)
        call?.enqueue(object : Callback<AttendanceResponse> {
            override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                // ConstantVariable.onSNACK(activity!!.tvEdit,"NO Record Found")
                tvState.setText("")
                tvPunchIn?.setText("")
                tvPunchOut?.setText("")
                etRemarks?.setText("")
            }

            override fun onResponse(
                call: Call<AttendanceResponse>,
                response: Response<AttendanceResponse>
            ) {
                progressDialog!!.dismiss()
                try {
                    attendanceResponse = response.body()?.getResult() as ArrayList<Result>
                    tvState.setText(attendanceResponse.get(0).getState())
                    tvPunchIn?.setText(attendanceResponse.get(0).getPunchInTime())
                    tvPunchOut?.setText(attendanceResponse.get(0).getPunchOutTime())
                    etRemarks?.setText(attendanceResponse.get(0).getRemarks())
                } catch (e: Exception) {
                    //  ConstantVariable.onSNACK(activity!!.tvEdit,"NO Record Found")
                }
            }
        })
    }

    // todo get current date
    @SuppressLint("RestrictedApi")
    private fun currentDate(): String {
        tvEdit!!.visibility = View.VISIBLE
        val c = Calendar.getInstance()
        var yy = c.get(Calendar.YEAR)
        var mm = c.get(Calendar.MONTH)
        var dd = c.get(Calendar.DAY_OF_MONTH)
        val formattedDate = StringBuilder()
            .append(yy).append("-").append(mm + 1).append("-")
            .append(dd)
        return formattedDate.toString()
    }


    override fun onResume() {
        super.onResume()

        if (ConstantVariable.verifyAvailableNetwork(this!!.activity!!)) {
            if (selectedDate.equals("", ignoreCase = true)) {
                getAttendanceDetailApi(currentDate())
            } else {
                getAttendanceDetailApi(this!!.selectedDate!!)
            }
        }
    }

    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
        var yy = year
        var mm = month
        var dd = dayOfMonth
        var selectedDate = StringBuilder()
            .append(yy).append("-").append(mm + 1).append("-")
            .append(dd).toString()
        if (!selectedDate.equals("", ignoreCase = true)) {
            getAttendanceDetailApi(selectedDate)
        }
    }
}