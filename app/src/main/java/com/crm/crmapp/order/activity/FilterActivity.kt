package com.crm.crmapp.order.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.adapter.FilterAdapter
import com.crm.crmapp.order.model.FilterModel
import com.crm.crmapp.order.util.PreferencesHelper

class FilterActivity : BaseActivity(), View.OnClickListener {

    private lateinit var tvCancel: TextView
    private lateinit var tvApply: TextView
    private lateinit var tvAddFilter: TextView
    private lateinit var tvClearAll: TextView
    private lateinit var context: FilterActivity
    private lateinit var rvFilterList: RecyclerView
    private var filterAdapter: FilterAdapter? = null
    private lateinit var rv: RecyclerView
    private lateinit var alDialogData: ArrayList<String>
    private var alData: ArrayList<FilterModel>? = null
    private lateinit var tvDistributer: TextView
    private lateinit var tvCustomerName: TextView
    private lateinit var tvOrderDate: TextView
    private lateinit var mAlertDialog: AlertDialog
    private lateinit var filterModel: FilterModel
    private var isValid: Boolean = false
    private var filterValue: String = ""
    private var preferencesHelper: PreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        context = this@FilterActivity
        findId()
        initVariable()
        setListener()
        checkReferenceArray()
        setRecyclerView()

    }

    private fun checkReferenceArray() {
        if (alData != null) {
            alData!!.clear()

            try {
                if (preferencesHelper!!.getTasksFromSharedPrefs(this@FilterActivity) != null) {
                    alData!!.addAll(preferencesHelper!!.getTasksFromSharedPrefs(this@FilterActivity))
                    setRecyclerView()
                    filterAdapter = FilterAdapter(context, alData, 2)
                    rvFilterList.adapter = filterAdapter
                    filterAdapter!!.notifyDataSetChanged()
                }
            } catch (e: Exception) {
            }
        } else {
            alData = ArrayList()
            try {
                if (preferencesHelper!!.getTasksFromSharedPrefs(this@FilterActivity) != null) {
                    alData!!.addAll(preferencesHelper!!.getTasksFromSharedPrefs(this@FilterActivity))
                    setRecyclerView()
                    filterAdapter = FilterAdapter(context, alData, 2)
                    rvFilterList.adapter = filterAdapter
                    filterAdapter!!.notifyDataSetChanged()
                }
            } catch (e: NullPointerException) {
            }
        }
    }

    private fun findId() {
        tvCancel = findViewById<TextView>(R.id.tvCancel)
        tvApply = findViewById<TextView>(R.id.tvApply)
        tvAddFilter = findViewById<TextView>(R.id.tvAddFilter)
        tvClearAll = findViewById<TextView>(R.id.tvClearAll)
        rvFilterList = findViewById<RecyclerView>(R.id.rvFilterList)
        preferencesHelper = PreferencesHelper(this)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("list", alData)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun initVariable() {
        alDialogData = ArrayList()
        alDialogData.add("Distributer Name")
        alDialogData.add("Customer Name")
        alDialogData.add("Oreder Date")
    }

    private fun setListener() {
        tvCancel.setOnClickListener(context)
        tvApply.setOnClickListener(context)
        tvAddFilter.setOnClickListener(context)
        tvClearAll.setOnClickListener(context)
    }

    private fun setRecyclerView() {
        rvFilterList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onClick(data: View?) {
        when (data) {
            tvCancel -> {
                ConstantVariable.animationEffect(tvCancel, this!!)
                this!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish()
            }
            tvApply -> {
                ConstantVariable.animationEffect(tvApply, this!!)
                for (item: Int in this!!.alData!!.indices) {
                    if (item == 0) {
                        if (alData!!.get(item).tvName.equals("Customer Name", ignoreCase = true)) {
                            filterValue =
                                ConstantVariable.FILTER_CUSTOMER_NAME + " like" + " '" + "%" +
                                        alData!!.get(item).tvNameValue +
                                        "%" + "'"
                            print(filterValue)
                        } else {
                            filterValue = ConstantVariable.FILTER_DIST_NAME + " like" + " '" + "%" +
                                    alData!!.get(item).tvNameValue +
                                    "%" + "'"
                            print(filterValue)
                        }
                    } else {
                        if (alData!!.get(item).tvName.equals("Customer Name", ignoreCase = true)) {
                            filterValue =
                                filterValue + "," + ConstantVariable.FILTER_CUSTOMER_NAME + " like" + " '" + "%" +
                                        alData!!.get(item).tvNameValue +
                                        "%" + "'"
                            print(filterValue)
                        } else {
                            filterValue =
                                filterValue + "," + ConstantVariable.FILTER_DIST_NAME + " like" + " '" + "%" +
                                        alData!!.get(item).tvNameValue +
                                        "%" + "'"
                            print(filterValue)
                        }
                    }
                }
                preferencesHelper!!.filterValue = filterValue!!
                preferencesHelper!!.saveTasksToSharedPrefs(this@FilterActivity, this!!.alData!!)
                finish()
            }

            tvAddFilter -> {
                ConstantVariable.animationEffect(tvAddFilter, this!!)
                setDialogue()
            }
            tvClearAll -> {
                ConstantVariable.animationEffect(tvClearAll, this!!)
                alData?.clear()
                preferencesHelper!!.filterValue = ""
                if (filterAdapter != null)
                    filterAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun setDialogue() {
        val builder = AlertDialog.Builder(context)
        val alertDialog: AlertDialog
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.filter_dialog, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        mAlertDialog = mBuilder.show()
        tvDistributer = mAlertDialog.findViewById<TextView>(R.id.tvDistributerName)!!
        tvCustomerName = mAlertDialog.findViewById<TextView>(R.id.tvCustomerName)!!
        setDialogListeners()
    }

    private fun setDialogListeners() {
        tvDistributer.setOnClickListener {
            setDistributerData()
        }
        tvCustomerName.setOnClickListener {
            setCustomerDataData()
        }
        Log.e("alData", "alData" + alData?.size)
    }

    private fun setDistributerData() {
        mAlertDialog.dismiss()
        if (alData == null || alData?.size == 0) {
            filterModel = FilterModel(tvDistributer.text.toString(), "")
            alData = ArrayList()
            alData!!.add(filterModel)
            filterAdapter = FilterAdapter(context, alData, 1)
            rvFilterList.adapter = filterAdapter
            filterAdapter!!.notifyDataSetChanged();

        } else {
            for (i: Int in alData!!.indices) {
                if (alData!!.get(i).tvNameValue.equals("")) {
                    Toast.makeText(context, "Please fill the value", Toast.LENGTH_LONG).show()
                    isValid = false
                } else {
                    isValid = true
                }
            }
            if (isValid) {
                filterModel = FilterModel(tvDistributer.text.toString(), "")
                alData!!.add(filterModel)
                filterAdapter = FilterAdapter(context, alData, 1)
                rvFilterList.adapter = filterAdapter
                filterAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun setCustomerDataData() {
        mAlertDialog.dismiss()
        if (alData == null || alData?.size == 0) {
            filterModel = FilterModel(tvCustomerName.text.toString(), "")
            alData = ArrayList()
            alData!!.add(filterModel)
            filterAdapter = FilterAdapter(context, alData, 2)
            rvFilterList.adapter = filterAdapter
            filterAdapter!!.notifyDataSetChanged();

        } else {
            for (i: Int in alData!!.indices) {
                if (alData!!.get(i).tvNameValue.equals("")) {
                    Toast.makeText(context, "Please fill the value", Toast.LENGTH_LONG).show()
                    isValid = false
                } else {
                    isValid = true
                }
            }
            if (isValid) {
                filterModel = FilterModel(tvCustomerName.text.toString(), "")
                alData!!.add(filterModel)
                filterAdapter = FilterAdapter(context, alData, 2)
                rvFilterList.adapter = filterAdapter
                filterAdapter!!.notifyDataSetChanged()
            }
        }
    }
}
