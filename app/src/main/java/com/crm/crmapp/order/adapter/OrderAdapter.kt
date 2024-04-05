package com.crm.crmapp.order.adapter

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
import com.crm.crmapp.order.adapter.OrderAdapter.MyViewHolder
import com.crm.crmapp.order.model.Result


class OrderAdapter(
    val context: Context?,
    var orderAllModelList: ArrayList<Result>,
    var onItemClick: onItemClick
) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {

        p0.dataBind(orderAllModelList!![p1], context)
        onItemClickValue = onItemClick
    }

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderAdapter.MyViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.fragment_row_order, p0, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return orderAllModelList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tv_name: TextView
        lateinit var tvDistibutor_name: TextView
        lateinit var tvDate: TextView
        lateinit var yvTime: TextView
        lateinit var erpId: TextView
        lateinit var tvTotalBox: TextView
        lateinit var llRow: LinearLayout
        lateinit var tvMarketPlan: TextView

        fun dataBind(orderModel: Result, context: Context?) {
            tv_name = itemView.findViewById<TextView>(R.id.tv_name)
            tvDistibutor_name = itemView.findViewById(R.id.tvDistibutor_name) as TextView
            tvDate = itemView.findViewById(R.id.tvDate) as TextView
            yvTime = itemView.findViewById(R.id.yvTime) as TextView
            tvMarketPlan = itemView.findViewById(R.id.tvMarketPlan) as TextView
            llRow = itemView.findViewById(R.id.llRow) as LinearLayout
            erpId = itemView.findViewById(R.id.erpId) as TextView
            tvTotalBox = itemView.findViewById(R.id.tvTotalBox) as TextView
            tv_name.setText(orderModel.getCustName())
            tvDistibutor_name.setText(orderModel.getMainCustName())
            tvDate.setText(ConstantVariable.parseDateTo_ddMMMyyyy_From_yyyyMMDD_WithoutTime(orderModel.getOrderDate()!!))
            yvTime.setText(orderModel.getStatusText())
            erpId.setText("ERP Ref : " + orderModel.getErpRefId())


            if (orderModel.getStatusText().equals("New", ignoreCase = true)) {
                yvTime.setBackgroundResource(R.drawable.bg_pill_info)
            } else if (orderModel.getStatusText().equals("CANCELLED", ignoreCase = true)) {
                if (context != null) {
                    yvTime.setBackgroundResource(R.drawable.bg_pill_danger)
                }
            } else {
                if (context != null) {
                    yvTime.setBackgroundResource(R.drawable.bg_pill_success)
                }
            }

            llRow.setOnClickListener {
                ConstantVariable.animationEffect(llRow, context as Activity)
                onItemClickValue!!.getPositionOfList(orderModel.getOrderId()!!)
            }

            if (orderModel.getmarketPlanId() != null && orderModel.getmarketPlanId() != 0) {
                tvMarketPlan.setText("Market Plan : " + orderModel.getMarketPlanName())
                tvMarketPlan.visibility = View.VISIBLE
            } else {
                tvMarketPlan.visibility = View.GONE
            }

            if (orderModel.getNoOfBoxes() != null) {
                tvTotalBox.setText("Total Box : " + orderModel.getNoOfBoxes())
                tvTotalBox.visibility = View.VISIBLE
            } else {
                tvTotalBox.visibility = View.GONE
            }
        }
    }

}