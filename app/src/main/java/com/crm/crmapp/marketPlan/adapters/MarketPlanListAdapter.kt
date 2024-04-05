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
import com.crm.crmapp.marketPlan.adapters.MarketPlanListAdapter.MyViewHolder
import com.crm.crmapp.marketPlan.models.MarketPlanListRequestResponse

class MarketPlanListAdapter(
    var context: Context,
    var marketPlanList: ArrayList<MarketPlanListRequestResponse.Result>,
    var onItemClick: onItemClick
) : RecyclerView.Adapter<MyViewHolder>() {

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.row_market_plan_list, p0, false)
        return MyViewHolder(v, p1)
    }

    override fun getItemCount(): Int {
        return marketPlanList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dataBind(context, position)
        holder.tv_MarketPlanName!!.setText(marketPlanList[position].market_plan_name)
        holder.tv_DistributorName!!.setText("" + marketPlanList[position].distributor_name)

        if (marketPlanList[position].owner_full_name != null && !marketPlanList[position].owner_full_name.equals(
                "",
                true
            )
        ) {
            holder.tv_CreatedBy!!.setText("Created By : " + marketPlanList[position].owner_full_name)
        } else {
            holder.tv_CreatedBy!!.visibility = View.GONE
        }

        if (marketPlanList[position].stage != null && !marketPlanList[position].stage.equals("", true)) {
            holder.tv_status!!.setText(marketPlanList[position].stage)
            holder.tv_status!!.setBackgroundResource(R.drawable.bg_pill_info)
        } else {
            holder.tv_status!!.visibility = View.GONE
        }

        if (marketPlanList[position].startdate != null && !marketPlanList[position].startdate.equals("", true)) {
            holder.tv_date!!.setText(
                ConstantVariable.parseDateToddMMyyyyWithoutTime(marketPlanList[position].startdate!!)
                        + " to " + ConstantVariable.parseDateToddMMyyyyWithoutTime(marketPlanList[position].enddate!!)
            )
        } else {
            holder.tv_date!!.visibility = View.GONE
        }

        onItemClickValue = onItemClick
    }

    class MyViewHolder(itemView: View, p1: Int) : RecyclerView.ViewHolder(itemView) {

        var tv_MarketPlanName: TextView? = null
        var tv_CreatedBy: TextView? = null
        var tv_DistributorName: TextView? = null
        var tv_status: TextView? = null
        var tv_date: TextView? = null

        var llRow: LinearLayout? = null
        fun dataBind(
            context: Context,
            int: Int
        ) {
            llRow = itemView.findViewById<LinearLayout>(R.id.llRow)
            tv_MarketPlanName = itemView.findViewById<TextView>(R.id.tv_MarketPlanName)
            tv_CreatedBy = itemView.findViewById<TextView>(R.id.tv_CreatedBy)
            tv_DistributorName = itemView.findViewById<TextView>(R.id.tv_DistributorName)
            tv_status = itemView.findViewById<TextView>(R.id.tv_status)
            tv_date = itemView.findViewById<TextView>(R.id.tv_date)

            llRow!!.setOnClickListener {
                ConstantVariable.animationEffect(llRow!!, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                if (onItemClickValue != null) {
                    onItemClickValue?.getPositionOfList(int)
                }
            }
        }

    }


}