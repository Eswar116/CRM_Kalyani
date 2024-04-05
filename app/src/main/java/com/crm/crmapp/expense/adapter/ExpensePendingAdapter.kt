package com.crm.crmapp.expense.adapter

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.expense.model.ExpensePendingResponse
import java.util.*

class ExpensePendingAdapter  (var context: Context, var data: ArrayList<ExpensePendingResponse.Result>?, var onItemClick: onItemClick) :

    RecyclerView.Adapter<ExpensePendingAdapter.MyViewHolder>() {
    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.row_expense_pending, p0, false)
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
            item: ExpensePendingResponse.Result,
            context: Context,
            int: Int
        ) {
            tvRecentCust = itemView.findViewById(R.id.tvOrderNo)
            tvdistributor = itemView.findViewById(R.id.tvdistributor)
            tvCustomerGroupName = itemView.findViewById(R.id.tvT)
            tvPlan = itemView.findViewById(R.id.tvPlan)
            tvDate = itemView.findViewById(R.id.tvDate)
            llRow = itemView.findViewById(R.id.llRow)

            tvRecentCust?.setText("Expense by "+item.getFullName())
            tvDate?.setText(item.getExpDate())
            tvPlan?.setText(item.getExpName())
            tvCustomerGroupName?.setText(item.getExpAmount().toString())

            llRow?.setOnClickListener {
                ConstantVariable.animationEffect(llRow!!, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

                if (onItemClickValue != null) {
                    onItemClickValue?.getPositionOfList(item.getId()!!)
                }
            }
        }
    }

    fun setItemList(imageList: ArrayList<ExpensePendingResponse.Result>) {
        if (this.data != null) {
            this.data!!.clear()
            this.data!!.addAll(imageList)
            notifyDataSetChanged()
        } else {
            this.data = imageList
            notifyDataSetChanged()
        }
    }
}