package com.crm.crmapp.newuser

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

class ReportsToUserAdapter(
    var context: Context,
    var data: ArrayList<UserResult>,
    var onItemClick: BtnClickListener
) : RecyclerView.Adapter<ReportsToUserAdapter.MyViewHolder>() {

    companion object {
        var onItemClickValue: BtnClickListener? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.row_dialog_expense_traveling, p0, false)
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
        private var llDetail: LinearLayout? = null
        fun dataBind(
            item: UserResult,
            context: Context,
            int: Int
        ) {
            tvRecentCust = itemView.findViewById(R.id.tvExpenseType)
            llDetail = itemView.findViewById(R.id.llRow)
            tvRecentCust!!.setText(item.getUserFullName())
            llDetail!!.setOnClickListener {
                ConstantVariable.animationEffect(llDetail!!, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                if (onItemClickValue != null) {
                    onItemClickValue?.onBtnClick(int, llDetail!!, item.getUserFullName()!!)
                }
            }
        }
    }

    open interface BtnClickListener {
        fun onBtnClick(position: Int, llRow: LinearLayout, type: String)
    }
}