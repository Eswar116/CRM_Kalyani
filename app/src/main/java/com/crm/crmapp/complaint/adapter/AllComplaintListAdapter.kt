package com.crm.crmapp.complaint.adapter

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
import com.crm.crmapp.complaint.model.ComplaintSearchListResponse
import com.crm.crmapp.customer.Interface.onItemClick

class AllComplaintListAdapter(
   var context: Context,
    var complaintResultList: ArrayList<ComplaintSearchListResponse.Result>?,
    var onItemClick: onItemClick
): RecyclerView.Adapter<AllComplaintListAdapter.MyViewHolder>() {

    companion object {
        var onItemClickValue :onItemClick ?=null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.row_all_complaints, p0, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  complaintResultList!!.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.dataBind( complaintResultList!![p1],p1,context)
        onItemClickValue =onItemClick
    }

    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        lateinit var tv_name: TextView
        lateinit var tvDistibutor_name: TextView
        lateinit var tvPriority: TextView
        lateinit var yvTime: TextView
        lateinit var llRow: LinearLayout

        fun dataBind(complaintResult: ComplaintSearchListResponse.Result, position: Int,context: Context){
            tv_name=itemView.findViewById<TextView>(R.id.tv_name)
            llRow=itemView.findViewById<LinearLayout>(R.id.llRow)
            tvDistibutor_name=itemView.findViewById(R.id.tvDistibutor_name) as TextView
            tvPriority=itemView.findViewById(R.id.tvPriority) as TextView
            tv_name.setText(complaintResult.getCustName())
            tvDistibutor_name.setText(complaintResult.getTypeName())
            tvPriority.setText(complaintResult.getPriorityText())
            llRow.setOnClickListener {
                if (onItemClickValue != null) {
                    ConstantVariable.animationEffect(llRow, context as Activity)
                    onItemClickValue?.getPositionOfList(complaintResult.getComplaintId()!!)
                }
            }

        }


    }


}