package com.crm.crmapp.marketPlan.adapters

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.databinding.RowResponsibleBinding
import com.crm.crmapp.marketPlan.models.MarketPlanInfoRequestResponse
import kotlinx.android.synthetic.main.row_responsible.view.*

class MarketPlanInfoUsersAdapter(
    var context: Context,
    var assignedUserList: ArrayList<MarketPlanInfoRequestResponse.Result_3>,
    var onItemClick: onItemClick?
) : RecyclerView.Adapter<MarketPlanInfoUsersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowResponsibleBinding.inflate(inflater)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return assignedUserList!!.size
    }

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.dataBind(
            context, assignedUserList, p1
        )
        onItemClickValue = onItemClick
    }

    class MyViewHolder(binding: RowResponsibleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun dataBind(
            context: Context,
            assignedUserList: ArrayList<MarketPlanInfoRequestResponse.Result_3>,
            p1: Int
        ) {
            itemView.tvName.text = assignedUserList.get(p1).user_full_name
            itemView.tvDesc.visibility = View.GONE
            itemView.btn_placeOrder.visibility = View.GONE

            itemView.ivCall.setOnClickListener {
                ConstantVariable.sendPhoneCall(context, assignedUserList[p1].mobile_no!!)
                ConstantVariable.animationEffect(itemView.ivCall, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            itemView.ivEmail.setOnClickListener {
                ConstantVariable.sendEmail(context, assignedUserList[p1].email_id!!, "")
                ConstantVariable.animationEffect(itemView.ivEmail, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

            itemView.llRow.setOnClickListener {
                if (onItemClickValue != null) {
                    onItemClickValue?.getPositionOfList(p1)
                }
                ConstantVariable.animationEffect(itemView.llRow, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

        }
    }
}