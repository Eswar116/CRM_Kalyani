package com.crm.crmapp.marketPlan.adapters

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.databinding.RowResponsibleBinding
import com.crm.crmapp.marketPlan.ResponsiblePersoneResponse
import kotlinx.android.synthetic.main.row_responsible.view.*

class ResponsiblePersonAdapter(
    var context: Context,
    var complaintResultList: List<ResponsiblePersoneResponse.Result>?,
    var onItemClick: onItemClick?
) : RecyclerView.Adapter<ResponsiblePersonAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RowResponsibleBinding.inflate(inflater)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return complaintResultList!!.size
    }

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.dataBind(
            context, complaintResultList as ArrayList<ResponsiblePersoneResponse.Result>, p1
        )
        onItemClickValue = onItemClick
    }

    class MyViewHolder(binding: RowResponsibleBinding) : RecyclerView.ViewHolder(binding.root) {


        fun dataBind(
            context: Context,
            complaintResultList: ArrayList<ResponsiblePersoneResponse.Result>,
            p1: Int
        ) {
            itemView.tvName.text = complaintResultList.get(p1).getFName() + " " + complaintResultList.get(p1).getLName()
            itemView.tvDesc.text = "Reports To : " + complaintResultList.get(p1).getReportsToFullname()

            itemView.ivCall.setOnClickListener {
                ConstantVariable.sendPhoneCall(context, complaintResultList?.get(p1)?.getMobileNo().toString())
                ConstantVariable.animationEffect(itemView.ivCall, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            itemView.ivEmail.setOnClickListener {
                ConstantVariable.sendEmail(context, complaintResultList?.get(p1)?.getEmailId().toString(), "")
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