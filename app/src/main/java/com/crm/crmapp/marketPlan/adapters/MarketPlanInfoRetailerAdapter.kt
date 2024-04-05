package com.crm.crmapp.marketPlan.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.databinding.RowResponsibleBinding
import com.crm.crmapp.marketPlan.models.MarketPlanInfoRequestResponse
import com.crm.crmapp.order.activity.NewOrderActivity
import kotlinx.android.synthetic.main.row_responsible.view.*

class MarketPlanInfoRetailerAdapter(
    var context: Context,
    var retailerList: ArrayList<MarketPlanInfoRequestResponse.Result_4>,
    var marketPlanObj: MarketPlanInfoRequestResponse.Result_1,
    var onItemClick: onItemClick?
) : RecyclerView.Adapter<MarketPlanInfoRetailerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowResponsibleBinding.inflate(inflater)
        intent = Intent(context, NewOrderActivity::class.java)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return retailerList.size
    }

    companion object {
        var onItemClickValue: onItemClick? = null
        var intent: Intent? = null
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.dataBind(
            context, retailerList, marketPlanObj, p1
        )
        onItemClickValue = onItemClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(imageList: ArrayList<MarketPlanInfoRequestResponse.Result_4>) {
        if (retailerList != null) {
            retailerList.clear()
            retailerList.addAll(imageList)
            notifyDataSetChanged()
        } else {
            retailerList = imageList
            notifyDataSetChanged()
        }
    }

    class MyViewHolder(binding: RowResponsibleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun dataBind(
            context: Context,
            retailerList: ArrayList<MarketPlanInfoRequestResponse.Result_4>,
            marketPlanObj: MarketPlanInfoRequestResponse.Result_1,
            p1: Int
        ) {
            itemView.tvName.text = retailerList.get(p1).cust_name
            itemView.tvDesc.text = retailerList.get(p1).grpcustname
            itemView.tvAddress.text = retailerList.get(p1).address

            itemView.ivCall.visibility = View.GONE
            itemView.ivEmail.visibility = View.GONE
            itemView.btn_placeOrder.visibility = View.VISIBLE
            itemView.tvAddress.visibility = View.VISIBLE

            itemView.btn_placeOrder.isEnabled = marketPlanObj.is_approved == 1

            itemView.ivCall.setOnClickListener {
                ConstantVariable.sendPhoneCall(context, retailerList[p1].mobileno!!)
                ConstantVariable.animationEffect(itemView.ivCall, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            itemView.ivEmail.setOnClickListener {
                ConstantVariable.sendEmail(context, retailerList[p1].emailid!!, "")
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

            itemView.btn_placeOrder.setOnClickListener {
                intent!!.putExtra(ConstantVariable.TAG_FROM, "from_MarketPlanInfo")
                intent!!.putExtra(ConstantVariable.TAG_OBJECT, retailerList.get(p1))
                intent!!.putExtra(ConstantVariable.TAG_OBJECT2, marketPlanObj)
                context.startActivity(intent)
                ConstantVariable.animationEffect(itemView.btn_placeOrder, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

        }
    }
}