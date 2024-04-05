package com.crm.crmapp.marketPlan.adapters

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.databinding.RowResponsibleBinding
import com.crm.crmapp.marketPlan.ResponsiblePersoneResponse
import kotlinx.android.synthetic.main.row_responsible.view.*


class ResponsiblePersonListAdapter(
    var context: Context,
    var responsibleData: ArrayList<ResponsiblePersoneResponse.Result>?
) : RecyclerView.Adapter<ResponsiblePersonListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RowResponsibleBinding.inflate(inflater)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return responsibleData!!.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.dataBind(
            context, responsibleData!!, p1
        )
        p0.itemView.ivDelete.setOnClickListener {
            if (responsibleData!!.size > 0) {
                this.responsibleData!!.removeAt(p1)
                notifyItemRemoved(p1);
                notifyItemRangeChanged(p1, getItemCount() - p1);
            }
        }
    }

    class MyViewHolder(itemView: RowResponsibleBinding) : RecyclerView.ViewHolder(itemView.root) {

        fun dataBind(
            context: Context,
            complaintResultList: ArrayList<ResponsiblePersoneResponse.Result>,
            p1: Int
        ) {
            itemView.tvName.text = complaintResultList.get(p1).getUserName()
            itemView.tvDesc.text = "Reports To : " + complaintResultList.get(p1).getReportsToFullname()
            itemView.ivDelete.visibility = View.VISIBLE


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

            itemView.ivDelete.setOnClickListener {
                ConstantVariable.animationEffect(itemView.ivDelete, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }


        }
    }
}