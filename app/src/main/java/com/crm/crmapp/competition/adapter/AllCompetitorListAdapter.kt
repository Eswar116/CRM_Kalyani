package com.crm.crmapp.competition.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.competition.model.CompetitionListResult
import com.crm.crmapp.customer.Interface.onItemClick

class AllCompetitorListAdapter(
    var context: Context,
    var competitorResultList: ArrayList<CompetitionListResult>?,
    var onItemClick: onItemClick
) : RecyclerView.Adapter<AllCompetitorListAdapter.MyViewHolder>() {

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.row_all_complaints, p0, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return competitorResultList!!.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.dataBind(competitorResultList!![p1], p1, context)
        onItemClickValue = onItemClick
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tv_name: TextView
        lateinit var tvDistibutor_name: TextView
        lateinit var tvPriority: TextView
        lateinit var yvTime: TextView
        lateinit var llRow: LinearLayout

        fun dataBind(complaintResult: CompetitionListResult, position: Int, context: Context) {
            tv_name = itemView.findViewById<TextView>(R.id.tv_name)
            llRow = itemView.findViewById<LinearLayout>(R.id.llRow)
            tvDistibutor_name = itemView.findViewById(R.id.tvDistibutor_name) as TextView
            tvPriority = itemView.findViewById(R.id.tvPriority) as TextView
            tv_name.setText("Customer : " + complaintResult.getCustName())
            tvDistibutor_name.setText(complaintResult.getCompetitorName())
            tvPriority.setText(ConstantVariable.parseDateTo_ddMMMyyyy_From_yyyyMMDD_WithoutTime(complaintResult.getCDate()))
            llRow.setOnClickListener {
                if (onItemClickValue != null) {
                    ConstantVariable.animationEffect(llRow, context as Activity)
                    onItemClickValue?.getPositionOfList(position)
                }
            }

        }


    }
}