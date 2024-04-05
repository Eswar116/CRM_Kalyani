package com.crm.crmapp.order.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R
import android.widget.ImageView
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.util.PreferencesHelper


class SortActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvNameAsc: TextView
    private lateinit var tvDesAsc: TextView
    private lateinit var tvDateAsc: TextView
    private lateinit var tvApply: TextView
    private lateinit var tvCancel: TextView
    private lateinit var llName: LinearLayout
    private lateinit var llDistributiveName: LinearLayout
    private lateinit var llDate: LinearLayout
    private lateinit var ivIcon: ImageView
    private lateinit var ivIconDes: ImageView
    private lateinit var ivIconDate: ImageView
    private lateinit var preferencesHelper: PreferencesHelper
    var booleanName :Boolean?=true
    var booleanDesc :Boolean?=true
    var booleanDate :Boolean?=true
    var byofSort:String=""
    var typeOfSort:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sort)

         preferencesHelper = PreferencesHelper(this)

        setIds()
        setListeners()
        checkPreviousData()

    }

    private fun checkPreviousData() {
        if (preferencesHelper.bySort.equals("cust_name",ignoreCase = true)){
            llName.performClick()
            if (preferencesHelper.typeOfSort.equals("Asc",ignoreCase = true)){
                booleanName =false
                tvNameAsc.performClick()
            }
            else{
                booleanName =true
                tvNameAsc.performClick()
            }

        }
        else if(preferencesHelper.bySort.equals("main_cust_name",ignoreCase = true)){
            llDistributiveName.performClick()

            if (preferencesHelper.typeOfSort.equals("Asc",ignoreCase = true)){
                booleanDesc =false
                tvDesAsc.performClick()
            }
            else{
                booleanDesc =true
                tvDesAsc.performClick()
            }
        }
        else{
            llDate.performClick()
            if (preferencesHelper.typeOfSort.equals("Asc",ignoreCase = true)){
                booleanDate =false
                tvDateAsc.performClick()
            }
            else{
                booleanDate =true
                tvDateAsc.performClick()
            }

        }
        ConstantVariable.BY_OF_SORT=byofSort
        ConstantVariable.TYPE_OF_SORT=typeOfSort
    }

    private fun setIds(){
        tvNameAsc=findViewById<TextView>(R.id.tvNameAsc)
        tvDateAsc=findViewById<TextView>(R.id.tvDateAsc)
        tvApply=findViewById<TextView>(R.id.tvApply)
        tvCancel=findViewById<TextView>(R.id.tvCancel)
        llName=findViewById<LinearLayout>(R.id.llName)
        llDistributiveName=findViewById<LinearLayout>(R.id.llDistributiveName)
        llDate=findViewById<LinearLayout>(R.id.llDate)
        ivIcon=findViewById<ImageView>(R.id.ivIcon)
        ivIconDes=findViewById<ImageView>(R.id.ivIconDes)
        ivIconDate=findViewById<ImageView>(R.id.ivIconDate)
        tvDesAsc=findViewById<TextView>(R.id.tvDesAsc)


    }

    private fun setListeners (){
        tvNameAsc.setOnClickListener(this)
        tvDateAsc.setOnClickListener(this)
        tvDesAsc.setOnClickListener(this)
        llDistributiveName.setOnClickListener(this)
        llName.setOnClickListener(this)
        llDate.setOnClickListener(this)
        tvApply.setOnClickListener(this)
        tvCancel.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when(v){
            llName->{

                ConstantVariable.animationEffect(llName, this!!)
                tvDateAsc.visibility=View.INVISIBLE
                tvDesAsc.visibility=View.INVISIBLE
                tvNameAsc.visibility=View.VISIBLE
                ivIconDes.visibility=View.INVISIBLE
                ivIconDate.visibility=View.INVISIBLE
                ivIcon.visibility=View.VISIBLE
                byofSort="cust_name"


            } llDistributiveName->{

            ConstantVariable.animationEffect(llDistributiveName, this!!)
            tvDateAsc.visibility=View.INVISIBLE
            tvDesAsc.visibility=View.VISIBLE
            tvNameAsc.visibility=View.INVISIBLE

            ivIconDes.visibility=View.VISIBLE
            ivIconDate.visibility=View.INVISIBLE
            ivIcon.visibility=View.INVISIBLE

            byofSort="main_cust_name"


        }llDate->{

            ConstantVariable.animationEffect(llDate, this!!)
            tvDateAsc.visibility=View.VISIBLE
            tvDesAsc.visibility=View.INVISIBLE
            tvNameAsc.visibility=View.INVISIBLE
            ivIconDes.visibility=View.INVISIBLE
            ivIconDate.visibility=View.VISIBLE
            ivIcon.visibility=View.INVISIBLE
            byofSort="order_date"

            }

            tvNameAsc->{

                ConstantVariable.animationEffect(tvNameAsc, this!!)
                if (booleanName==true){
                    ivIcon.animate().rotation(180f)
                    tvNameAsc.setText("Descending")
                    booleanName=false
                    typeOfSort="Desc"
                }
                else {
                    ivIcon.animate().rotation(360f)
                    tvNameAsc.setText("Ascending")
                    booleanName=true
                    typeOfSort="Asc"
                }

            }

            tvDesAsc->{

                ConstantVariable.animationEffect(tvDesAsc, this!!)
                if (booleanDesc==true){
                    ivIconDes.animate().rotation(180f)
                    tvDesAsc.setText("Descending")
                    booleanDesc=false
                    typeOfSort="Desc"

                }
                else {
                    ivIconDes.animate().rotation(360f)
                    tvDesAsc.setText("Ascending")
                    booleanDesc=true;
                    typeOfSort="Asc"
                }
            }

            tvDateAsc->{
                ConstantVariable.animationEffect(tvDateAsc, this!!)
                if (booleanDate==true){
                    ivIconDate.animate().rotation(180f)
                    tvDateAsc.setText("Descending")
                    booleanDate=false
                    typeOfSort="Desc"
                }
                else {
                    ivIconDate.animate().rotation(360f)
                    tvDateAsc.setText("Ascending")
                    booleanDate=true;
                    typeOfSort="Asc"
                }
            }

            tvApply->{

                preferencesHelper.bySort =byofSort
                preferencesHelper.typeOfSort=typeOfSort
                ConstantVariable.animationEffect(tvApply, this!!)
                ConstantVariable.BY_OF_SORT=byofSort
                ConstantVariable.TYPE_OF_SORT=typeOfSort
                finish()
            }

            tvCancel->{
                ConstantVariable.animationEffect(tvCancel, this!!)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish()
            }
        }
    }

}