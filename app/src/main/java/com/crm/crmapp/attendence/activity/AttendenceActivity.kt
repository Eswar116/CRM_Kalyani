package com.crm.crmapp.attendence.activity


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.attendence.model.AttendanceDetailRequest
import com.crm.crmapp.attendence.model.AttendanceResponse
import com.crm.crmapp.attendence.model.Result
import com.crm.crmapp.expense.model.ExpenseTypeResponseModel
import com.crm.crmapp.expense.model.ExpenseTypeResult
import com.crm.crmapp.order.activity.CameraActivity
import com.crm.crmapp.order.model.DocDetailModel
import com.crm.crmapp.order.model.ImageRequest
import com.crm.crmapp.order.model.ImageRequestResponse
import com.crm.crmapp.order.util.CustomDate
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.test.StoreListActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.snackbar.Snackbar
import com.mikhaellopez.circularimageview.CircularImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class AttendenceActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {
    private lateinit var tvDate: TextView
    private lateinit var tvEmployeeName: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvApply: TextView
    private lateinit var etRemarks: EditText
    private lateinit var progressDialog: ProgressDialog
    private lateinit var progressDialog_out: ProgressDialog
    private lateinit var attendanceResponse: ArrayList<Result>
    private lateinit var spCategory: Spinner
    private lateinit var ivImage: CircularImageView
    private lateinit var ivImageIcon: ImageView
    private lateinit var ivImage_out: CircularImageView
    private lateinit var ivImageIcon_out: ImageView
    private lateinit var btn_punchIn: Button
    private lateinit var btn_punchOut: Button
    var selectedSpinnerValue: String? = ""
    var latitude: Double? = null
    var longitude: Double? = null
    var preferencesHelper: PreferencesHelper? = null
    lateinit var yyddmDate: CustomDate

    //val myStrings = arrayOf("Travelling", "Market Plan", "Sales Order", "Office", "Leave")
    lateinit var myStrings: ArrayList<String>;
    var str_punchin: String? = "";
    var str_punchout: String? = "";
    var imageBase64: String? = "";
    var imageBase64_out: String? = "";
    private lateinit var context: Context
    /*  var mapView: MapView? = null
      var map: GoogleMap? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendence)
        yyddmDate = CustomDate()
        setId()
        setCurrentDate()
        getStatusList()
        if (intent != null && intent.getStringExtra(ConstantVariable.SELECTED_DATE) != null) {
            if (ConstantVariable.verifyAvailableNetwork(this)) {
                getAttendanceDetailApi(intent.getStringExtra(ConstantVariable.SELECTED_DATE))
            } else {
                ConstantVariable.onToast(context, "No Internet Connection")
            }
        }
        /*     if (ConstantVariable.verifyAvailableNetwork(this!!.activity!!)){
                 if(selectedDate.equals("",ignoreCase = true)){
                     getAttendanceDetailApi(currentDate())
                 }
                 else {
                     getAttendanceDetailApi(this!!.selectedDate!!)
                 }
             }
             */
    }
    private fun setSpinner() {
        spCategory.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, myStrings)
        spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedSpinnerValue = p0!!.getItemAtPosition(p2).toString()
            }
        }
    }

    private fun getAttendanceDetailApi(selectedDate: String) {
        attendanceResponse = ArrayList<Result>()
        progressDialog = ConstantVariable.showProgressDialog(this@AttendenceActivity, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var attendanceDetailRequest = AttendanceDetailRequest()
        attendanceDetailRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull())
        attendanceDetailRequest.setAttenDate(selectedDate)
        var call: Call<AttendanceResponse>? =
            apiInterface!!.getAttendanceDetail(attendanceDetailRequest)
        call?.enqueue(object : Callback<AttendanceResponse> {
            override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onToast(context, "NO Record Found")
            }

            @SuppressLint("NewApi")
            override fun onResponse(
                call: Call<AttendanceResponse>,
                response: Response<AttendanceResponse>
            ) {
                progressDialog!!.dismiss()
                try {
                    attendanceResponse = response.body()?.getResult() as ArrayList<Result>
                    //var indiandate = ConstantVariable.parseDateToyyyyMMdd(attendanceResponse.get(0).getAttenDate()!!)
                    yyddmDate.setDate(attendanceResponse.get(0).getAttenDate()!!)
                    tvDate.setText(attendanceResponse.get(0).getAttenDate())
                    str_punchin = attendanceResponse.get(0).getPunchInTime().toString().trim()
                    btn_punchIn.setText(
                        if (!attendanceResponse.get(0).getPunchInTime().toString().trim().equals(
                                "",
                                ignoreCase = true
                            )
                        ) ConstantVariable.parseDateTimeToTime(
                            attendanceResponse.get(0).getPunchInTime().toString()
                        ) else "PUNCH IN"
                    )
                    str_punchout = attendanceResponse.get(0).getPunchOutTime().toString()
                    btn_punchOut.setText(
                        if (!attendanceResponse.get(0).getPunchOutTime().toString().trim().equals(
                                "",
                                ignoreCase = true
                            )
                        ) ConstantVariable.parseDateTimeToTime(
                            attendanceResponse.get(0).getPunchOutTime().toString()
                        ) else "PUNCH OUT"
                    )

                    etRemarks.setText(attendanceResponse.get(0).getRemarks())
                    myStrings.indexOf(attendanceResponse.get(0).getState().toString())
                    spCategory?.setSelection(
                        myStrings.indexOf(
                            attendanceResponse.get(0).getState().toString()
                        )
                    )

                    if (!attendanceResponse.get(0).getPunchInTime().toString().trim().equals("")) {
                        btn_punchOut.isEnabled = true
                        btn_punchIn.isEnabled = false
                        btn_punchIn.backgroundTintList == (resources.getColorStateList(R.color.grey_10))
                    }

                    if (!attendanceResponse.get(0).getPunchOutTime().toString().trim().equals("")) {
                        btn_punchIn.isEnabled = false
                        btn_punchOut.isEnabled = false
                        btn_punchIn.backgroundTintList =
                            (resources.getColorStateList(R.color.grey_10))
                        btn_punchOut.backgroundTintList =
                            (resources.getColorStateList(R.color.grey_10))
                    }

                    if (!attendanceResponse.get(0).getImageServer().toString().trim().equals("")) {
                        progressDialog =
                            ConstantVariable.showProgressDialog(context as Activity, "Loading...")
                        getRequestApiForImage(
                            attendanceResponse.get(0).getImageServer().toString(),
                            "in"
                        )
                    }
                    if (!attendanceResponse.get(0).getImageServerOut().toString().trim()
                            .equals("")
                    ) {
                        getRequestApiForImage(
                            attendanceResponse.get(0).getImageServerOut().toString(), "out"
                        )
                    }


                } catch (e: Exception) {
                    ConstantVariable.onToast(context, "NO Record Found/Something went wrong")
                }
                //  tvEmployeeName.setText( attendanceResponse.get(0).get())
            }
        })

    }


    private fun getRequestApiForImage(docDetailList: String, from: String) {
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var imageRequest = ImageRequest()
        imageRequest.setImgPath(docDetailList!!)
        var call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
        call!!.enqueue(object : Callback<ImageRequestResponse> {
            override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onToast(context, "NO Record Found/Something went wrong")
            }

            override fun onResponse(
                call: Call<ImageRequestResponse>,
                response: Response<ImageRequestResponse>
            ) {
                progressDialog!!.dismiss()
                if (response?.body()?.getMsg().equals("success")) {

                    if (from.equals("in")) {
                        var imageUrl = response.body()?.getImgString() as String
                        ivImage.setImageBitmap(ConstantVariable.StringToBitMap(imageUrl))
                        ivImage.visibility = View.VISIBLE
                        ivImageIcon.visibility = View.GONE
                        imageBase64 = imageUrl
                    } else if (from.equals("out")) {
                        var imageUrl = response.body()?.getImgString() as String
                        ivImage_out.setImageBitmap(ConstantVariable.StringToBitMap(imageUrl))
                        ivImage_out.visibility = View.VISIBLE
                        ivImageIcon_out.visibility = View.GONE
                        imageBase64_out = imageUrl
                    }
                }
            }
        })

    }

    private fun getStatusList() {
        var progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<ExpenseTypeResponseModel>? =
            apiInterface!!.getStatusList(preferencesHelper!!.userId!!.toIntOrNull()!!)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<ExpenseTypeResponseModel> {
            override fun onFailure(call: Call<ExpenseTypeResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvCancel, "Something went wrong")
            }

            override fun onResponse(
                call: Call<ExpenseTypeResponseModel>,
                response: Response<ExpenseTypeResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    var statusList = response.body()?.getResult() as ArrayList<ExpenseTypeResult>
                    if (statusList != null && statusList.size > 0) {
                        myStrings.clear()
                        for (i in statusList.indices) {
                            myStrings.add(statusList[i].type)
                        }
                        setSpinner();
                    }
                }
            }
        })
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
        yyddmDate.setDate("" + yy + "-" + mFormat.format(mm + 1) + "-" + mFormat.format(dd))
    }

    private fun getLatLong() {
        var intent = Intent(this@AttendenceActivity, StoreListActivity::class.java)
        intent.putExtra(ConstantVariable.TAG_FROM, "from_attendance")
        startActivityForResult(intent, ConstantVariable.latlong)
    }

    private fun setId() {
        getLatLong()
        context = this
        preferencesHelper = PreferencesHelper(this@AttendenceActivity)
        /*  val mapFragment = supportFragmentManager
              .findFragmentById(R.id.mapView) as SupportMapFragment
             mapFragment.getMapAsync(this)*/
        spCategory = findViewById<Spinner>(R.id.spCategory)
        tvDate = findViewById<TextView>(R.id.tvDate)
        tvCancel = findViewById<TextView>(R.id.tvCancel)
        tvApply = findViewById<TextView>(R.id.tvApply)
        tvEmployeeName = findViewById<TextView>(R.id.tvEmployeeName)
        tvTitle = findViewById<TextView>(R.id.tvTitle)
        etRemarks = findViewById<EditText>(R.id.etRemarks)
        ivImage = findViewById<CircularImageView>(R.id.ivImage)
        ivImage_out = findViewById<CircularImageView>(R.id.ivImage_out)
        ivImageIcon = findViewById<ImageView>(R.id.ivImageIcon)
        ivImageIcon_out = findViewById<ImageView>(R.id.ivImageIcon_out)
        btn_punchIn = findViewById<Button>(R.id.btn_punchIn)
        btn_punchOut = findViewById<Button>(R.id.btn_punchOut)
        myStrings = ArrayList<String>();

        tvEmployeeName.setText(preferencesHelper!!.userName)
        tvTitle.setText("Attendance Detail")
        tvApply.setText("SAVE")
        btn_punchIn.setOnClickListener(this)
        btn_punchOut.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
        tvApply.setOnClickListener(this)
        ivImage.setOnClickListener(this)
        ivImageIcon.setOnClickListener(this)
        ivImage_out.setOnClickListener(this)
        ivImageIcon_out.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            btn_punchIn -> {
                pressPunchInBtn()
            }
            btn_punchOut -> {
                pressPunchOutBtn()
            }
            tvCancel -> {
                ConstantVariable.animationEffect(tvCancel, this!!)
                finish()
            }
            tvApply -> {
                ConstantVariable.animationEffect(tvApply, this!!)
                checkValidation()
            }

            ivImage -> {
                ConstantVariable.animationEffect(ivImage, this!!)
                val intent = Intent(this, CameraActivity::class.java)

                intent.putExtra("AttendanceActivity", "AttendanceActivity")
                startActivityForResult(intent, ConstantVariable.resultCode)

            }
            ivImageIcon -> {
                ConstantVariable.animationEffect(ivImageIcon, this!!)
                val intent = Intent(this, CameraActivity::class.java)

                intent.putExtra("AttendanceActivity", "AttendanceActivity")
                startActivityForResult(intent, ConstantVariable.resultCode)
            }
            ivImage_out -> {
                if (checkPunchInPunchOutBeforeOutImageClick()) {
                    if (ConstantVariable.verifyAvailableNetwork(this)) {
                        ConstantVariable.animationEffect(ivImage_out, this!!)
                        val intent = Intent(this, CameraActivity::class.java)
                        intent.putExtra("AttendanceActivity", "AttendanceActivity")
                        startActivityForResult(intent, ConstantVariable.resultCode2)
                    } else {
                        ConstantVariable.onToast(context, "No Internet Connection")
                    }
                }
            }
            ivImageIcon_out -> {
                if (checkPunchInPunchOutBeforeOutImageClick()) {
                    if (ConstantVariable.verifyAvailableNetwork(this)) {
                        ConstantVariable.animationEffect(ivImageIcon_out, this!!)
                        val intent = Intent(this, CameraActivity::class.java)
                        intent.putExtra("AttendanceActivity", "AttendanceActivity")
                        startActivityForResult(intent, ConstantVariable.resultCode2)
                    } else {
                        ConstantVariable.onToast(context, "No Internet Connection")
                    }
                }
            }
        }
    }

    private fun pressPunchInBtn() {
        if (!str_punchin.equals("")) {
            ConstantVariable.onToast(context, "Punch In already done")
        } else if (imageBase64.equals("")) {
            ConstantVariable.onToast(context, "Punchin Selfie is Mandatory.")
        } else {
            ConstantVariable.animationEffect(btn_punchIn, this!!)
            str_punchin = ConstantVariable.defaultTime(btn_punchIn, this)
        }
    }

    private fun pressPunchOutBtn() {
        if (str_punchin.equals("")) {
            ConstantVariable.onToast(context, "Please Punch in first.")
        }  else {
            ConstantVariable.animationEffect(btn_punchOut, this!!)
            str_punchout = ConstantVariable.defaultTime(btn_punchOut, this)
        }
    }

    private fun checkPunchInPunchOutBeforeOutImageClick(): Boolean {
        if (str_punchin.equals("")) {
            ConstantVariable.onToast(context, "Please Punch in first.")
            return false;
        } else if (str_punchout.equals("")) {
            ConstantVariable.onToast(context, "Please Punch out first.")
            return false;
        } else {
            return true;
        }
    }

    private fun checkValidation() {
        if (str_punchin.toString().trim().equals("")) {
            ConstantVariable.onToast(context, "Please Punch in first")
        } else {
            saveAttendanceDetail()
        }
    }

    private fun saveAttendanceDetail() {
        var attendanceRequestList = java.util.ArrayList<Result>()
        progressDialog = ConstantVariable.showProgressDialog(this!!, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var attendanceRequest = Result()
        var resultlist = AttendanceResponse()
        if (str_punchin.toString().trim()!!.equals("", ignoreCase = true)) {
            attendanceRequest.setPunchInTime("null")
        } else {
            attendanceRequest.setPunchInTime(str_punchin.toString().trim())
        }
        if (str_punchout.toString().trim()!!.equals("", ignoreCase = true)) {
            attendanceRequest.setPunchOutTime("null")
        } else {
            attendanceRequest.setPunchOutTime(str_punchout.toString())
        }
        attendanceRequest.setRemarks(etRemarks!!.text.toString())
        attendanceRequest.setAttenDate(yyddmDate.getDate())
        attendanceRequest.setState(this!!.selectedSpinnerValue!!)
        attendanceRequest.setUserId(preferencesHelper!!.userId?.toIntOrNull())

        if (!str_punchin.toString().trim().equals("", ignoreCase = true)) {
            attendanceRequest.setGeoLocationInLat(latitude.toString())
            attendanceRequest.setGeoLocationInLon(longitude.toString())
        } else {
            attendanceRequest.setGeoLocationInLat("")
            attendanceRequest.setGeoLocationInLon("")
        }
        if (!str_punchout.toString().trim().equals("", ignoreCase = true)) {
            attendanceRequest.setGeoLocationOutLat(latitude.toString())
            attendanceRequest.setGeoLocationOutLon(longitude.toString())
        } else {
            attendanceRequest.setGeoLocationOutLat("")
            attendanceRequest.setGeoLocationOutLon("")
        }
        attendanceRequest.setImage(this!!.imageBase64!!)
        attendanceRequest.setImageOut(this!!.imageBase64_out!!)

        attendanceRequestList!!.add(attendanceRequest)
        resultlist.setResult(attendanceRequestList!!)
        var call: Call<AttendanceResponse>? = apiInterface!!.saveAttendance(resultlist)
        call?.enqueue(object : Callback<AttendanceResponse> {
            override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(tvCancel, t.toString())
            }

            override fun onResponse(
                call: Call<AttendanceResponse>,
                response: Response<AttendanceResponse>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        ConstantVariable.onSNACKSuccess(tvCancel, "Record saved successfully")
                        finish()
                    } else {
                        ConstantVariable.onSNACK(tvCancel, "Record not saved")
                    }
                } catch (e: Exception) {
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ConstantVariable.resultCode) {
            if (resultCode == Activity.RESULT_OK) {
                val docListDetail =
                    data?.extras?.get(ConstantVariable.docDetailModel) as DocDetailModel

                var bitmap = ConstantVariable.StringToBitMap(docListDetail.uri)
                var bitmap2 = ConstantVariable.rotateBitmap(docListDetail.docPath!!, bitmap!!)

                ConstantVariable.overwriteBitmapToImage(docListDetail.docPath!!, bitmap2)

                ivImage.setImageBitmap(bitmap2)
                ivImage.visibility = View.VISIBLE
                ivImageIcon.visibility = View.GONE
                imageBase64 = docListDetail.uri
            }
        } else if (requestCode == ConstantVariable.resultCode2) {
            if (resultCode == Activity.RESULT_OK) {
                val docListDetail =
                    data?.extras?.get(ConstantVariable.docDetailModel) as DocDetailModel

                var bitmap = ConstantVariable.StringToBitMap(docListDetail.uri)
                var bitmap2 = ConstantVariable.rotateBitmap(docListDetail.docPath!!, bitmap!!)

                ivImage_out.setImageBitmap(bitmap2)
                ivImage_out.visibility = View.VISIBLE
                ivImageIcon_out.visibility = View.GONE
                imageBase64_out = docListDetail.uri
                //region This is added to autamate save when user click, out image.Added - 29 June 2022
                checkValidation();
                //endregion
            }
        } else if (requestCode == ConstantVariable.latlong) {
            if (resultCode == Activity.RESULT_OK) {

                latitude = data?.extras?.get(ConstantVariable.Latitude) as Double
                longitude = data?.extras?.get(ConstantVariable.Longitude) as Double

                Log.e("latitude" + latitude, "longitude" + longitude)

            }
        }
        else if (requestCode==Activity.RESULT_CANCELED){

        }
    }

    fun CheckPermission() {
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, 100)
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                Snackbar.make(
                    this.findViewById(android.R.id.content),
                    "Please Grant Permissions to get lat long",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    "ENABLE"
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission
                                    .ACCESS_FINE_LOCATION, Manifest.permission
                                    .ACCESS_COARSE_LOCATION
                            ),
                            100
                        )
                    }
                }.show()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        100
                    )
                }
            }
        } else {
            //    map!!.setMyLocationEnabled(true)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            100 -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //  map!!.setMyLocationEnabled(true)
                }
                else {
                    Toast.makeText(
                        getApplicationContext(),
                        "location faild, please try again.", Toast.LENGTH_LONG
                    ).show();




                }
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        /*   map = googleMap;
           map!!.getUiSettings().setMyLocationButtonEnabled(false)


           if (map != null) {
             ///CheckPermission()
           }


           //in old Api Needs to call MapsInitializer before doing any CameraUpdateFactory call
            try {
                MapsInitializer.initialize(this);
            } catch ( e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace();
            }

           // Updates the location and zoom of the MapView
    *//*      var  cameraUpdate = CameraUpdateFactory.newLatLngZoom( LatLng(43.1, -87.9), 10);
        map!!.animateCamera(cameraUpdate);*//*
        val position = LatLng(28.5355, 77.3910)
        map!!.addMarker(MarkerOptions().position(position).title("Hi Noida"))
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(position,10f));*/
    }
}


