package com.crm.crmapp.marketPlan.adapters

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
import com.crm.crmapp.marketPlan.models.MarketPlanInfoRequestResponse

class MarketPlanInfoSalesOrderAdapter(
    var c: Context,
    var salesOrderList: ArrayList<MarketPlanInfoRequestResponse.Result_5>,
    var onItemClick: onItemClick?
) : RecyclerView.Adapter<MarketPlanInfoSalesOrderAdapter.ViewHolder>() {
    private lateinit var v: View

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        v = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_row_order, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return salesOrderList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.dataBind(this!!.c!!, salesOrderList[position], position)
        onItemClickValue = onItemClick
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var tv_name: TextView
        lateinit var tvDistibutor_name: TextView
        lateinit var tvDate: TextView
        lateinit var tvMarketPlan: TextView
        lateinit var yvTime: TextView
        lateinit var erpId: TextView
        lateinit var llRow: LinearLayout
        fun dataBind(
            context: Context,
            salesOrderListObj: MarketPlanInfoRequestResponse.Result_5,
            position: Int
        ) {
            tv_name = itemView.findViewById<TextView>(R.id.tv_name)
            tvDistibutor_name = itemView.findViewById(R.id.tvDistibutor_name) as TextView
            tvDate = itemView.findViewById(R.id.tvDate) as TextView
            tvMarketPlan = itemView.findViewById(R.id.tvMarketPlan) as TextView
            yvTime = itemView.findViewById(R.id.yvTime) as TextView
            erpId = itemView.findViewById(R.id.erpId) as TextView
            llRow = itemView.findViewById(R.id.llRow) as LinearLayout
            tv_name.setText(salesOrderListObj.cust_name)
            tvDistibutor_name.setText(salesOrderListObj.main_cust_name)

            //tvDate.setText(recentOrderList.getOrderDate())
            tvDate.setText(ConstantVariable.parseDateTo_ddMMMyyyy_From_yyyyMMDD_WithoutTime(salesOrderListObj.order_date!!))

            if (salesOrderListObj.status_text.equals("New", ignoreCase = true)) {
                yvTime.setBackgroundResource(R.drawable.bg_pill_info)
            } else if (salesOrderListObj.status_text.equals("CANCELLED", ignoreCase = true)) {
                if (context != null) {
                    yvTime.setBackgroundResource(R.drawable.bg_pill_danger)
                }
            } else {
                if (context != null) {
                    yvTime.setBackgroundResource(R.drawable.bg_pill_success)
                }
            }
            tvMarketPlan.visibility = View.GONE
            yvTime.setText(salesOrderListObj.status_text)
            erpId.setText("ERP Ref : " + salesOrderListObj.erp_ref_id)
            llRow.setOnClickListener {
                if (onItemClickValue != null) {
                    ConstantVariable.animationEffect(llRow, context as Activity)
                    onItemClickValue?.getPositionOfList(salesOrderListObj.order_id!!)
                }
            }

            /*if (recentOrderList.getmarketPlanId() != null && recentOrderList.getmarketPlanId() != 0) {
                tvMarketPlan.setText("Market Plan : "+recentOrderList.getMarketPlanName())
                tvMarketPlan.visibility = View.VISIBLE
            } else {
                tvMarketPlan.visibility = View.GONE
            }*/
        }
    }

}