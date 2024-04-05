package com.crm.crmapp.customer.adapter

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.customer.model.OrderResult
import java.util.ArrayList

class CustomerOrderAdapter(var context: Context?, val list: ArrayList<OrderResult>, var onItemClick: onItemClick) :
    RecyclerView.Adapter<CustomerOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        var v = LayoutInflater.from(p0.context).inflate(R.layout.row_order, p0, false)
        return CustomerOrderAdapter.ViewHolder(v)
    }

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        if (context != null) {
            holder.bindData(context!!, p1, list)
            onItemClickValue = onItemClick
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var tvOrderNo: TextView
        lateinit var tvDate: TextView
        lateinit var tvVisited: TextView
        lateinit var tvEdit: TextView
        lateinit var tvPlan: TextView
        lateinit var ll_row: LinearLayout

        fun bindData(
            context: Context,
            int: Int,
            alResult_global: ArrayList<OrderResult>
        ) {
            tvOrderNo = itemView.findViewById(R.id.tvOrderNo) as TextView
            tvDate = itemView.findViewById(R.id.tvDate) as TextView
            tvVisited = itemView.findViewById(R.id.tvVisited) as TextView
            tvEdit = itemView.findViewById(R.id.tvEdit) as TextView
            tvPlan = itemView.findViewById(R.id.tvPlan) as TextView
            ll_row = itemView.findViewById(R.id.ll_row) as LinearLayout

            tvOrderNo.setText("Order No." + alResult_global.get(int).orderId)
            tvDate.setText(
                ConstantVariable.parseDateTo_ddMMMyyyy_From_yyyyMMDD_WithoutTime(alResult_global.get(int).orderDate)
            )
            tvEdit.setText(alResult_global.get(int).statusText)
            tvPlan.setText(alResult_global.get(int).mainCustName)
            tvVisited.setText(alResult_global.get(int).custName)

            ll_row.setOnClickListener {
                ConstantVariable.animationEffect(ll_row, context as Activity)
                onItemClickValue!!.getPositionOfList(alResult_global.get(int).orderId)
            }

        }
    }

}