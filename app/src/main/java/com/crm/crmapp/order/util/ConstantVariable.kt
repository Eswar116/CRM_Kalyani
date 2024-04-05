package com.crm.crmapp.order.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.location.Address
import android.location.Geocoder
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.telephony.SmsManager
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crm.crmapp.R
import com.crm.crmapp.order.activity.CameraActivity.DATE_FORMAT
import com.crm.crmapp.order.activity.CameraActivity.IMAGE_DIRECTORY
import com.crm.crmapp.order.model.DocDetailModel
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.lang.System.currentTimeMillis
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("UNREACHABLE_CODE")
class ConstantVariable {
    companion object {
        val resultCode = 101
        val resultCode2 = 1011
        val ResponsilberesultCode = 101
        var OTP_REQUEST_CODE = 201
        val docDetailModel = "docDetailModel"
        val selectedResponsibleData = "selectedResponsibleData"
        var TYPE_OF_SORT = ""
        var BY_OF_SORT = ""
        var FILTER_VALUE = ""

        var checkActiveInActive="fail"
        var FILTER_CUSTOMER_NAME = "c.cust_name"
        var FILTER_DIST_NAME = "maincus.cust_name"
        val orderId: String = "orderId"
        val resultCode_customer = 105
        val resultCode_marketPlan = 104
        val resultCode_distributor = 103
        val latlong = 102
        val offline_order_id = "offline_order_ide"
        val online_order_id: String? = "online_order_id"
        val SELECTED_DATE: String? = "SELECTED_DATE"
        val TAG_OTP: String? = "otp"
        val TAG_NUMBER: String? = "number"
        val TAG_OBJECT: String? = "object"
        val TAG_OBJECT2: String? = "object2"
        val TAG_FROM: String? = "from"
        val TAG_LAST_SYNC: String? = "Last_sync"
        val online_complaint_id: String = "online_complaint_id"
        var firebaseType: String? = "firebaseType"
        val TAG_STATE_CODE: String? = "state_code"
        var FLAG_NEW = 0
        var FLAG_EDIT = 1
        var FLAG_VIEW = 2
        var Latitude: String? = "latitude"
        var Longitude: String? = "longitude"
        var homeMenu: String? = "homeMenu"

        var firebase_bean_id: String? = "firebase_bean_id"
        var FirebaseExpense: String? = "FirebaseExpense"
        var NotificationCount: Int? = 0
        var NotificationIntentFilter: String? = "NotificationIntentFilter"
        var mScrollY: Int = 0
        var BaseUrl: String? = ""


        fun datePicker(context: Context, tvDate: TextView) {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val hh: Int = c.get(Calendar.HOUR_OF_DAY)
            val mm: Int = c.get(Calendar.MINUTE)
            val ss: Int = c.get(Calendar.SECOND)
            val dpd =
                DatePickerDialog(
                    context,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in textbox
                        //tvDate.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth + " " + hh + ":" + mm + ":" + ss)
                        tvDate.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year + " " + hh + ":" + mm + ":" + ss)

                    },
                    year,
                    month,
                    day
                )
            dpd.show()
        }

        fun datePickerWithYYDDMMRequest(
            context: Context,
            tvDate: TextView,
            date: CustomDate
        ): DatePickerDialog {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val hh: Int = c.get(Calendar.HOUR_OF_DAY)
            val mm: Int = c.get(Calendar.MINUTE)
            val ss: Int = c.get(Calendar.SECOND)
            var mFormat = DecimalFormat("00");
            val dpd =
                DatePickerDialog(
                    context,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in textbox
                        //tvDate.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth + " " + hh + ":" + mm + ":" + ss)
                        //tvDate.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year + " " + hh + ":" + mm + ":" + ss)
                        tvDate.setText(
                            "" + mFormat.format(dayOfMonth) + "-" + mFormat.format(
                                monthOfYear + 1
                            ) + "-" + year
                        )
                        date.setDate(
                            "" + year + "-" + mFormat.format(monthOfYear + 1) + "-" + mFormat.format(
                                dayOfMonth
                            )
                        )
                    },
                    year,
                    month,
                    day
                )
            dpd.show()
            return dpd;
        }


        @SuppressLint("SetTextI18n")
        fun selectDatePickerForNewExpense(
            context: Context,
            tvDate: TextView,
            date: CustomDate
        ): DatePickerDialog {

            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val hh: Int = c.get(Calendar.HOUR_OF_DAY)
            val mm: Int = c.get(Calendar.MINUTE)
            val ss: Int = c.get(Calendar.SECOND)
            val mFormat = DecimalFormat("00");


            val dpd =
                DatePickerDialog(
                    context,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in textbox
                        tvDate.text =
                            "" + mFormat.format(dayOfMonth) + "-" + mFormat.format(monthOfYear + 1) + "-" + year
                        date.date =
                            "" + year + "-" + mFormat.format(monthOfYear + 1) + "-" + mFormat.format(
                                dayOfMonth
                            ) // yyyy-mm-dd


                    },
                    year,
                    month,
                    day
                )

//            dpd.datePicker.minDate =c.timeInMillis-  10*86400*1000L  // Date is not visible before 10days
            dpd.datePicker.minDate =
                currentTimeMillis() - 3 * 86400 * 1000L  // Date is not visible before 3 days (3 days, 24*60*60=86400, 1000L-> used to convert time in millis second
            dpd.datePicker.maxDate = c.timeInMillis
            dpd.show()
            return dpd;
        }


        fun datePickerWithYYDDMMRequest_date_minimum_date(
            context: Context,
            tvDate: TextView,
            date: CustomDate
        ) {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val hh: Int = c.get(Calendar.HOUR_OF_DAY)
            val mm: Int = c.get(Calendar.MINUTE)
            val ss: Int = c.get(Calendar.SECOND)
            var mFormat = DecimalFormat("00");
            val dpd =
                DatePickerDialog(
                    context,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in textbox
                        //tvDate.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth + " " + hh + ":" + mm + ":" + ss)
                        //tvDate.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year + " " + hh + ":" + mm + ":" + ss)
                        tvDate.setText(
                            "" + mFormat.format(dayOfMonth) + "-" + mFormat.format(
                                monthOfYear + 1
                            ) + "-" + year
                        )
                        date.setDate(
                            "" + year + "-" + mFormat.format(monthOfYear + 1) + "-" + mFormat.format(
                                dayOfMonth
                            )
                        )
                    },
                    year,
                    month,
                    day
                )

            dpd.datePicker.minDate = c.timeInMillis
            dpd.show()
        }


        fun onSNACK(view: View, text: String) {
//Snackbar(view)
            try {
                val snackbar = Snackbar.make(
                    view, text,
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.setActionTextColor(Color.BLUE)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.RED)
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                textView.textSize = 14f
                snackbar.show()
            } catch (e: Exception) {
            }
        }
        fun onSNACK_GREY(view: View, text: String) {
//Snackbar(view)
            try {
                val snackbar = Snackbar.make(
                    view, text,
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.setActionTextColor(Color.BLUE)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.DKGRAY)
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                textView.textSize = 14f
                snackbar.show()
            } catch (e: Exception) {
            }
        }

        fun onSNACKSuccess(view: View, text: String) {
//Snackbar(view)
            val snackbar = Snackbar.make(
                view, text,
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snackbar.setActionTextColor(Color.BLUE)
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(Color.BLUE)
            val textView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            textView.textSize = 14f
            snackbar.show()
        }

        fun onToast(context: Context, text: String) {

            val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
            val snackbarView = toast.view
            //snackbarView.setBackgroundColor(Color.RED)
            val textView = snackbarView.findViewById(android.R.id.message) as TextView
            textView.setTextColor(Color.BLACK)
            textView.textSize = 14f
            toast.show()
        }

        fun onToastSuccess(context: Context, text: String) {

            val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
            val snackbarView = toast.view
            snackbarView.setBackgroundColor(Color.DKGRAY)
            val textView = snackbarView.findViewById(android.R.id.message) as TextView
            textView.setTextColor(Color.WHITE)
            textView.textSize = 14f
            toast.show()
        }

        fun onTimeSeclect(view: TextView, context: Context) {

            val c = Calendar.getInstance()
            var yy = c.get(Calendar.YEAR)
            var mm = c.get(Calendar.MONTH)
            var dd = c.get(Calendar.DAY_OF_MONTH)
            var am_pm: String? = null
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                if (cal.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "AM";
                else if (cal.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "PM";

//todo set current date
                view.setText(
                    StringBuilder()
                        .append(yy).append("-").append(mm + 1).append("-")
                        .append(dd)
                )

// todo selected time from timepicker
                view.text =
                    view.text.toString() + " " + SimpleDateFormat("HH:mm").format(cal.time) +
                            ":00" /*+ " " + am_pm*/
            }
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),

                true
            ).show()
        }

//        // METHOD TO GET DEFAULT TIME
//        fun defaultTime(view: TextView, context: Context) {
//            var am_pm: String? = null
//            val cal = Calendar.getInstance()
//
//            if (cal.get(Calendar.AM_PM) == Calendar.AM)
//                am_pm = "AM";
//            else if (cal.get(Calendar.AM_PM) == Calendar.PM)
//                am_pm = "PM";
//            view.text = SimpleDateFormat("HH:mm").format(cal.time) + " " + am_pm
//        }

        fun sendEmail(context: Context, mailTo: String, phone: String) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailTo))
            intent.putExtra(Intent.EXTRA_SUBJECT, "")
            intent.putExtra(Intent.EXTRA_TEXT, "")
            try {
                context.startActivity(Intent.createChooser(intent, "Send mail..."))

            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT)
                    .show()
            }


        }

        fun sendSMS(context: Context, phoneNo: String, msg: String) {
            try {
                var smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                Toast.makeText(
                    context, "Message Sent",
                    Toast.LENGTH_LONG
                ).show();
            } catch (ex: Exception) {
                Log.e("SMSEXCEPTION", ex.message)
                ex.printStackTrace()
            }
        }

        fun sendWhatsApp(context: Context, mailTo: String, phone: String) {

            var whatsappIntent = Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, mailTo)
            try {
                context.startActivity(whatsappIntent);
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(context, "Please install whatsapp.", Toast.LENGTH_SHORT).show()
            }
        }

        fun sendWhatsAppWithImage(
            context: Context,
            mailTo: String,
            phone: String,
            docs: DocDetailModel
        ) {

            var imgUri = Uri.parse(docs.docPath) as Uri

            var whatsappIntent = Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, mailTo)
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            whatsappIntent.setType("image/jpeg");
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(whatsappIntent);
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(context, "Please install whatsapp.", Toast.LENGTH_SHORT).show()
            }
        }


        @SuppressLint("MissingPermission")
        fun sendPhoneCall(context: Context, phone: String) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            context.startActivity(intent)
        }

        fun getLocationFromAddress(maddress: String, context: Context): Barcode.GeoPoint? {
            val coder = Geocoder(context)
            val address: List<Address>
            var p1: Barcode.GeoPoint? = null
            try {
                address = coder.getFromLocationName(maddress, 5);
                if (address == null) {
                    return null
                }
                if (address.isNotEmpty()) {
                    val location: Address = address[0];
                    location.latitude;
                    location.longitude;
                    p1 = Barcode.GeoPoint(
                        (location.latitude * 1E6).toDouble(),
                        (location.longitude * 1E6).toDouble()
                    )
                }

            } catch (ex: IOException) {
                ex.printStackTrace();
            }
            return p1
        }

        // todo set Image Using Glide
        fun setImage(context: Context, imageView: ImageView, url: String) {
            Glide.with(context)
                .load(Base64.decode(url, Base64.DEFAULT)) // or URI/path
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .skipMemoryCache(false)
                .into(imageView);
        }

        // TODO PROGRESS BAR
        fun showProgressDialog(activity: Activity, message: String): ProgressDialog {

            var m_Dialog = ProgressDialog(activity);

            m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_Dialog.setCancelable(false);
            m_Dialog.show();
            return m_Dialog;

        }


        fun hideKeyPad(context: Activity) {

            context.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            )
        }

        private fun decodeFile(f: File): Bitmap? {
            var file = File(
                (Environment.getExternalStorageDirectory()).toString()
                        + "/" + IMAGE_DIRECTORY
            )
            if (!file.exists()) {
                file.mkdirs()
            }

            var dateFormatter = SimpleDateFormat(
                DATE_FORMAT, Locale.US
            )


            var b: Bitmap? = null
            //Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = false
            var fis: FileInputStream? = null
            try {
                fis = FileInputStream(f)
                BitmapFactory.decodeStream(fis, null, o)
                fis.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val IMAGE_MAX_SIZE = 1024
            var scale = 1
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = Math.pow(
                    2.0,
                    Math.ceil(
                        Math.log(
                            IMAGE_MAX_SIZE / Math.max(
                                o.outHeight,
                                o.outWidth
                            ).toDouble()
                        ) / Math.log(0.5)
                    ).toInt().toDouble()
                ).toInt()
            }

            //Decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            try {
                fis = FileInputStream(f)
                b = BitmapFactory.decodeStream(fis, null, o2)
                Log.d("", "Width :" + b!!.width + " Height :" + b.height)
                fis.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            var destFile = File(
                file, "img_"
                        + dateFormatter.format(Date()).toString() + ".png"
            )
            try {
                val out = FileOutputStream(destFile)
                b!!.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return b
        }

        fun animationEffect(tvNew: View, activity: Activity) {
            val shake = AnimationUtils.loadAnimation(activity, R.anim.abc_grow_fade_in_from_bottom)
            tvNew.startAnimation(shake)
        }

        fun verifyAvailableNetwork(activity: Activity): Boolean {
            /* val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
             val networkInfo = connectivityManager.activeNetworkInfo
             return networkInfo != null && networkInfo.isConnected*/

            var connection = ConnectionDetector(activity)
            var returnInfo = connection.isConnectingToInternet
            return returnInfo
        }

        fun StringToBitMap(encodedString: String): Bitmap? {

            try {
                val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
                return BitmapFactory.decodeByteArray(
                    encodeByte, 0,
                    encodeByte.size
                )
            } catch (e: Exception) {
                e.message
                return null
            }
        }

        fun overwriteBitmapToImage(photoPath: String, rotatedBitmap: Bitmap) {
            val pictureFile = photoPath
            if (pictureFile == null) {
                return
            }
            try {
                var file = File(photoPath);
                if (file.exists()) file.delete();
                val fileOutputStream = FileOutputStream(pictureFile)
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close()
            } catch (error: FileNotFoundException) {
                Log.e(TAG, "File not found: " + error.message)
            } catch (error: IOException) {
                Log.e(TAG, "Error accessing file: " + error.message)
            } catch (error: Throwable) {
                Log.e(TAG, "Error saving file: " + error.message)
            }
        }


        fun rotateBitmap(photoPath: String, bitmap: Bitmap): Bitmap {
            var ei = ExifInterface(photoPath);
            var orientation =
                ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                ) as Int

            var rotatedBitmap: Bitmap

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90F);

                ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180F);

                ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270F);

                ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap

                else -> rotatedBitmap = bitmap;
            }
            return rotatedBitmap;
        }

        fun rotateImage(source: Bitmap, angle: Float): Bitmap {
            var matrix = Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true
            );
        }

        fun convertByteArray(bmp: Bitmap): String {
            var stream = ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            var byteArray = stream.toByteArray()
            var encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return encoded
        }

        fun convertPathToBlob(bmp: Bitmap): ByteArray {
            var stream = ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            var byteArray = stream.toByteArray()
            return byteArray
        }

        fun convertByteArrayToBase64(byteArray: ByteArray): String {
            var imgString = Base64.encodeToString(
                byteArray,
                Base64.NO_WRAP
            )
            return imgString;
        }

        fun showDateInIndianFormat(yydddMMdate: String): String {
            var indianDate: String = ""

            if (yydddMMdate != null && !yydddMMdate.equals("")) {
                val splitdate =
                    yydddMMdate.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (splitdate.size == 3) {
                    indianDate = splitdate[2] + "-" + splitdate[1] + "-" + splitdate[0]
                } else {
                    indianDate = "invalid date"
                }
            } else {
                indianDate = "invalid date"
            }

            return indianDate
        }

        fun parseDateToddMMyyyy(time: String): String? {
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
            val outputPattern = "dd-MMM-yyyy h:mm a"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return str
        }

        fun parseDateToyyyyMMdd(time: String): String? {
            val outputPattern = "yyyy-MM-dd"
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return str
        }

        fun parseDateToddMMyyyyWithoutTime(time: String): String? {
            val outputPattern = "dd-MM-yyyy"
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return str
        }


        @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.O)
        fun endPlanDate(dateString: String): Boolean {
//            Log.e(TAG, "endPlanDate: $dateString")


            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = format.parse(dateString)
            val endmilliseconds = date.time + (5 * 24 * 60 * 60 * 1000)

            val currentDate_ = currentTimeMillis()

//            val timeRemaining = endmilliseconds - currentDate_
//            println("Current Date Milisecond $currentDate_  end time   $dateString  $endmilliseconds")
            return currentDate_ < endmilliseconds
        }


        fun defaultTime(view: TextView, context: Context): String {
            val cal = Calendar.getInstance()
            val yy = cal.get(Calendar.YEAR)
            val mm = cal.get(Calendar.MONTH)
            val dd = cal.get(Calendar.DAY_OF_MONTH)
            var am_pm: String? = null
            var date: StringBuilder? = null

            if (cal.get(Calendar.AM_PM) == Calendar.AM) {
                am_pm = "AM";
            } else if (cal.get(Calendar.AM_PM) == Calendar.PM) {
                am_pm = "PM";
            }

            date = StringBuilder()
                .append(yy).append("-").append(mm + 1).append("-")
                .append(dd);

            var str_punch = date.toString() + " " + SimpleDateFormat("HH:mm").format(cal.time) +
                    ":00"
            view.text = parseDateTimeToTime(
                date.toString() + " " + SimpleDateFormat("HH:mm").format(cal.time) +
                        ":00"
            ) /*+ " " + am_pm*/

            return str_punch;
        }

        fun defaultTimeWithDate(view: TextView, context: Context): String {
            val cal = Calendar.getInstance()
            var yy = cal.get(Calendar.YEAR)
            var mm = cal.get(Calendar.MONTH)
            var dd = cal.get(Calendar.DAY_OF_MONTH)
            var am_pm: String? = null
            var date: StringBuilder? = null

            if (cal.get(Calendar.AM_PM) == Calendar.AM) {
                am_pm = "AM";
            } else if (cal.get(Calendar.AM_PM) == Calendar.PM) {
                am_pm = "PM";
            }

            date = StringBuilder()
                .append(yy).append("-").append(mm + 1).append("-")
                .append(dd);

            var str_punch = date.toString() + " " + SimpleDateFormat("HH:mm").format(cal.time) +
                    ":00"
            view.text =
                date.toString() + " " + SimpleDateFormat("HH:mm").format(cal.time) +
                        ":00"
            /*+ " " + am_pm*/
            return str_punch;
        }

        fun parseDateTimeToTime(time: String): String? {
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
            val outputPattern = "HH:mm:ss"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return str
        }

        fun parseDateTo_ddMMMyyyy_From_yyyyMMDD_WithoutTime(inputdate: String): String? {
            val outputPattern = "dd-MMM-yyyy"
            val inputPattern = "yyyy-MM-dd"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(inputdate)
                str = outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return str
        }


        fun parseDateTo_ddMMyyyy_From_yyyyMMDD_WithoutTime(inputdate: String): String? {
            val outputPattern = "dd/MM/yyyy"
            val inputPattern = "yyyy-MM-dd"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(inputdate)
                str = outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return str
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
            tvHintName.setText(builder)
        }

        fun parseDateToyyyyMMdd2(time: String): String? {
            val outputPattern = "yyyy-MM-dd"
            val inputPattern = "MMM dd,yyyy HH:mm:ss"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return str
        }

        fun parseDateToddMMyyyyWithoutTime2(time: String): String? {
            val outputPattern = "dd-MM-yyyy"
            val inputPattern = "MMM dd,yyyy HH:mm:ss"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return str
        }


        fun isNetworkConnected(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }

        fun isNetworkConnectedNew(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        }
    }

}