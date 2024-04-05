package com.crm.crmapp.customer.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.customer.activity.NewCustomerActivity
import com.crm.crmapp.customer.model.CustomerResult
import kotlinx.android.synthetic.main.row_recent_customer_adapter.view.*


class RecentCustomerAdapter(var context: Context, var data: ArrayList<CustomerResult>, var onItemClick: onItemClick) :
    RecyclerView.Adapter<RecentCustomerAdapter.MyViewHolder>() {
    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.row_recent_customer_adapter, p0, false)
        return MyViewHolder(view, p1)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dataBind(data[position], context, position)
        onItemClickValue = onItemClick
    }

    class MyViewHolder(itemView: View, p1: Int) : RecyclerView.ViewHolder(itemView) {
        private var tvRecentCust: TextView? = null
        private var tvdistributor: TextView? = null
        private var tvCustomerGroupName: TextView? = null
        private var ivCall: ImageView? = null
        private var llDetail: LinearLayout? = null

        fun dataBind(
            item: CustomerResult,
            context: Context,
            int: Int
        ) {
            tvRecentCust = itemView.findViewById<TextView>(R.id.tvRecentCust)
            tvdistributor = itemView.findViewById<TextView>(R.id.tvdistributor)
            tvCustomerGroupName = itemView.findViewById<TextView>(R.id.tvCustomerGroupName)
            ivCall = itemView.findViewById<ImageView>(R.id.ivCall)
            llDetail = itemView.findViewById<LinearLayout>(R.id.llDetail)

            itemView.tvRecentCust.setText(item.getCustName())

            if (item.getCustType() == null || item.getCustType().equals("", true)) {
                itemView.tvdistributor.setText("")
            } else {
                itemView.tvdistributor.setText("(" + item.getCustType() + ")")
            }

            if (item.getGrpCustName() == null || item.getGrpCustName().equals("", true)) {
                itemView.tvCustomerGroupName.setText("")
            } else {
                itemView.tvCustomerGroupName.setText("DISTRIBUTER : " + item.getGrpCustName())
            }

            if (item.isIs_new_entry() == null || item.isIs_new_entry() == true) {
                itemView.ll_edit.visibility = View.VISIBLE
            } else {
                itemView.ll_edit.visibility = View.GONE
            }

            itemView.ivCall.setOnClickListener {
                ConstantVariable.sendPhoneCall(context, item.getMobileno())
                ConstantVariable.animationEffect(itemView.ivCall, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            itemView.ivEmail.setOnClickListener {
                ConstantVariable.sendEmail(context, item.getEmailid(), "")
                ConstantVariable.animationEffect(itemView.ivEmail, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

            itemView.ll_edit.setOnClickListener {
                var intent = Intent(context, NewCustomerActivity::class.java)
                intent.putExtra(ConstantVariable.TAG_FROM, "edit")
                intent.putExtra(ConstantVariable.TAG_OBJECT, item)
                context.startActivity(intent)
                ConstantVariable.animationEffect(itemView.ll_edit, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

            itemView.llDetail.setOnClickListener {
                ConstantVariable.animationEffect(itemView.llDetail, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                if (onItemClickValue != null) {
                    onItemClickValue?.getPositionOfList(int)
                }
            }
        }
    }
}