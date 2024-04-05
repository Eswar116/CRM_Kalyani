package com.crm.crmapp.order.adapter

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
import com.crm.crmapp.order.model.OrderDetailResult1

class OffLineOrderAdapter (
    val context: Context?,
    var orderAllModelList: ArrayList<OrderDetailResult1>,
    var onItemClick: onItemClick
): RecyclerView.Adapter<OffLineOrderAdapter.MyViewHolder>() {
    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {

        p0.dataBind(orderAllModelList!![p1],context)
        onItemClickValue=onItemClick
    }

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OffLineOrderAdapter.MyViewHolder{
        val v = LayoutInflater.from(p0.context).inflate(R.layout.fragment_row_order, p0, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  orderAllModelList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var tv_name: TextView
        lateinit var tvDistibutor_name: TextView
        lateinit var tvDate: TextView
        lateinit var yvTime: TextView
        lateinit var llRow: LinearLayout

        fun dataBind(orderModel : OrderDetailResult1, context: Context?){
            tv_name=itemView.findViewById<TextView>(R.id.tv_name)
            tvDistibutor_name=itemView.findViewById(R.id.tvDistibutor_name) as TextView
            tvDate=itemView.findViewById(R.id.tvDate) as TextView
            yvTime=itemView.findViewById(R.id.yvTime) as TextView
            llRow=itemView.findViewById(R.id.llRow) as LinearLayout

            tv_name.setText(orderModel.getCustName())
            tvDistibutor_name.setText(orderModel.getUserName())
            tvDate.setText(orderModel.getOrderDate())

            llRow.setOnClickListener {
                ConstantVariable.animationEffect(llRow, context as Activity)
                onItemClickValue!!.getPositionOfList(orderModel.getOrderId()!!)
            }
        }


    }


}