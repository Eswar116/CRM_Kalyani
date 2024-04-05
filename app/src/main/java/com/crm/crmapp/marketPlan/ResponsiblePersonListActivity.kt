package com.crm.crmapp.marketPlan

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.marketPlan.adapters.ResponsiblePersonAdapter
import com.crm.crmapp.order.activity.NewOrderActivity


class ResponsiblePersonListActivity : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var activity: Activity;
    private lateinit var rvSearchCustomer: RecyclerView
    private lateinit var tvApply: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvTitle: TextView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var progressDialog: ProgressDialog? = null
    private var stateId: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_responsible)
        if (intent.getStringExtra(ConstantVariable.TAG_STATE_CODE) != null) {
            stateId = intent.getStringExtra(ConstantVariable.TAG_STATE_CODE)
        }
        var responsibleViewHolder: ResponsiblePersonViewHolder =
            ViewModelProviders.of(this@ResponsiblePersonListActivity).get(ResponsiblePersonViewHolder::class.java)
        responsibleViewHolder.getHeroes(stateId)
            .observe(this@ResponsiblePersonListActivity
            ) { t ->
                setRecyclerView(t)
                Log.e("Responsible", "" + t!!.size)
            }

        findId()
        setToolBar()
        setListener()
        // setListApi()
    }

    private fun setToolBar() {
        tvApply.visibility = View.INVISIBLE
        tvTitle.setText("Responsible Person")
    }

    //todo pagination of api
    private fun findId() {
        tvApply = findViewById<TextView>(R.id.tvApply)
        tvTitle = findViewById<TextView>(R.id.tvTitle)
        tvCancel = findViewById<TextView>(R.id.tvCancel)
        rvSearchCustomer = findViewById<RecyclerView>(R.id.rvSearchCustomer)

    }

    private fun setListener() {
        tvCancel.setOnClickListener {
            finish()
        }
    }

    private fun setRecyclerView(complaintResultList: List<ResponsiblePersoneResponse.Result>?) {
        linearLayoutManager = LinearLayoutManager(this)
        rvSearchCustomer.layoutManager = linearLayoutManager

        rvSearchCustomer.adapter = ResponsiblePersonAdapter(
            this,
            complaintResultList,
            object : onItemClick {
                override fun getPositionOfList(position: Int) {

                    Handler().postAtTime({
                        val intent = Intent(this@ResponsiblePersonListActivity, NewOrderActivity::class.java)
                        intent.putExtra(ConstantVariable.selectedResponsibleData, complaintResultList!!.get(position))
                        setResult(Activity.RESULT_OK, intent)
                        this@ResponsiblePersonListActivity.finish()
                    }, 1000)

                }
            })
    }

}









