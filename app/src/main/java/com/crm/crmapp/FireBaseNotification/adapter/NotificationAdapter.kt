package com.crm.crmapp.fireBaseNotification.adapter

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crm.crmapp.fireBaseNotification.model.NotificationResponseModel
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import java.util.ArrayList

class NotificationAdapter (var context: Context, var data: ArrayList<NotificationResponseModel.Result>?, var onItemClick: onItemClick) :

    RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {
    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.row_notification, p0, false)
        return MyViewHolder(view, p1)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dataBind(data!![position], context, position)
        onItemClickValue = onItemClick
    }

    class MyViewHolder(itemView: View, p1: Int) : RecyclerView.ViewHolder(itemView) {
        private var tvRecentCust: TextView? = null
        private var tvdistributor: TextView? = null
        private var tvCustomerGroupName: TextView? = null
        private var tvPlan: TextView? = null
        private var tvDate: TextView? = null
        private var llRow: androidx.cardview.widget.CardView? = null

        fun dataBind(
            item: NotificationResponseModel.Result,
            context: Context,
            int: Int
        ) {
            tvRecentCust = itemView.findViewById<TextView>(R.id.tvOrderNo)
            tvdistributor = itemView.findViewById<TextView>(R.id.tvdistributor)
            tvCustomerGroupName = itemView.findViewById<TextView>(R.id.tvT)
            tvPlan = itemView.findViewById<TextView>(R.id.tvPlan)
            tvDate = itemView.findViewById<TextView>(R.id.tvDate)
            llRow = itemView.findViewById<androidx.cardview.widget.CardView>(R.id.llRow)


            tvRecentCust?.setText(item.getBeanType())
            tvDate?.setText(item.getNDate())
            tvPlan?.setText(item.getNBody())
            tvCustomerGroupName?.setText(item.getNTitle())

            if (item.getIsRead()==false){
                llRow!!.setBackgroundResource(R.color.unread_color)
            }
            else{
                llRow!!.setBackgroundResource(android.R.color.white)
            }

            llRow?.setOnClickListener {
                ConstantVariable.animationEffect(llRow!!, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

                if (onItemClickValue != null) {
                    onItemClickValue?.getPositionOfList(int!!)
                }
            }
        }
    }
}