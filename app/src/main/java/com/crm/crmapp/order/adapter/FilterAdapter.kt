package com.crm.crmapp.order.adapter

import android.app.DatePickerDialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.model.FilterModel
import kotlinx.android.synthetic.main.fragment_row_filter.view.*
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.collections.ArrayList

class FilterAdapter(var context:Context,var alData:ArrayList<FilterModel>?,var type:Int): RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

     private lateinit var v:View
     private lateinit var activityContext:Context
     var alData1=alData



    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {


        v = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_row_filter, viewGroup, false)
        return FilterAdapter.ViewHolder(v,alData,p1)
    }

    override fun getItemCount(): Int {
         return  alData!!.size

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindItems(alData!![position],alData,position,context,type)

    }


    class ViewHolder(itemView: View,alData: ArrayList<FilterModel>?,position:Int) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: FilterModel,alData: ArrayList<FilterModel>?,position:Int,context: Context,type:Int) {
            var tvName = itemView.findViewById(R.id.tvName) as TextView
            tvName.text=user.tvName
            var etNameValue = itemView.findViewById<EditText>(R.id.etNameValue)
            etNameValue.setText(user.tvNameValue)

         if(type.equals(3)){


             etNameValue.isCursorVisible=false

             etNameValue.setOnClickListener {

               /*  val inputManager:InputMethodManager =context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                 inputManager.hideSoftInputFromWindow(etNameValue.windowToken, InputMethodManager.SHOW_FORCED)*/

                 etNameValue.requestFocus()

                 datePicker(context,etNameValue,alData!!,position)

             }
         }
         else {
             etNameValue.addTextChangedListener(object : TextWatcher {
                 override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                     etNameValue.requestFocus()

                 }

                 override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                 }

                 override fun afterTextChanged(s: Editable) {
                     alData?.get(position)!!.tvNameValue = s.toString().trim()
                 }
             })
         }
        }



        fun View.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etNameValue.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

fun datePicker(context: Context,etNameValue:EditText,alData:ArrayList<FilterModel>,position:Int){

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)


    val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // Display Selected date in textbox
        etNameValue.setText("" + dayOfMonth + " " + monthOfYear+ ", " + year)
        alData?.get(position).tvNameValue=etNameValue.text.toString().trim()

    }, year, month, day)
    dpd.show()
}
    }

    }



