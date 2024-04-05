package com.crm.crmapp.favourite.activity

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import kotlinx.android.synthetic.main.row_recent_customer_adapter.view.*

class FavrtCustomerAdapter (var context: Context, var data: ArrayList<FavrtResponseModel.Result>, var onItemClick: onItemClick) :
    RecyclerView.Adapter<FavrtCustomerAdapter.MyViewHolder>() {
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
        private var ivEmail: ImageView? = null
        private var llDetail: LinearLayout? = null
        private var ll_edit: LinearLayout? = null

        fun dataBind(
            item: FavrtResponseModel.Result,
            context: Context,
            int: Int
        ) {
            tvRecentCust = itemView.findViewById<TextView>(R.id.tvRecentCust)
            tvdistributor = itemView.findViewById<TextView>(R.id.tvdistributor)
            tvCustomerGroupName = itemView.findViewById<TextView>(R.id.tvCustomerGroupName)
            ivCall = itemView.findViewById<ImageView>(R.id.ivCall)
            ivEmail = itemView.findViewById<ImageView>(R.id.ivEmail)
            ivCall!!.visibility =View.GONE

            llDetail = itemView.findViewById<LinearLayout>(R.id.llDetail)
            ll_edit = itemView.findViewById<LinearLayout>(R.id.ll_edit)
            ivEmail!!.visibility =View.GONE
            ll_edit!!.visibility =View.GONE


            itemView.tvRecentCust.setText(item.getLine1())

            if (item.getLine2() == null || item.getLine2().equals("", true)) {
                itemView.tvdistributor.setText("")
            } else {
                itemView.tvdistributor.setText("(" + item.getBeanType() + ")")
            }

            if (item.getLine2() == null || item.getLine2().equals("", true)) {
                itemView.tvCustomerGroupName.setText("")
            } else {
                itemView.tvCustomerGroupName.setText("Distributer : " + item.getLine2())
            }



            itemView.llDetail.setOnClickListener {
                ConstantVariable.animationEffect(itemView.llDetail, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                if (onItemClickValue != null) {
                    onItemClickValue?.getPositionOfList(item.getBeanId()!!)
                }
            }
        }
    }
}