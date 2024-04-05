package com.crm.crmapp.expense.adapter

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
import com.crm.crmapp.expense.adapter.ExpenseAdapter.MyViewHolder
import com.crm.crmapp.expense.model.SearchExpenseResult

class ExpenseAdapter(
    val context: Context?,
    var alResult_global: ArrayList<SearchExpenseResult>,
    var onItemClick: onItemClick
) : RecyclerView.Adapter<MyViewHolder>() {

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.fragment_row_order, p0, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return alResult_global!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (context != null) {
            onItemClickValue = onItemClick
            holder.dataBind(context, position, alResult_global)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tv_name: TextView
        lateinit var tvDistibutor_name: TextView
        lateinit var tvDate: TextView
        lateinit var yvTime: TextView
        lateinit var ll_expense: LinearLayout

        fun dataBind(
            context: Context,
            int: Int,
            alResult_global: ArrayList<SearchExpenseResult>
        ) {
            tv_name = itemView.findViewById<TextView>(R.id.tv_name)
            tvDistibutor_name = itemView.findViewById(R.id.tvDistibutor_name) as TextView
            tvDate = itemView.findViewById(R.id.tvDate) as TextView
            yvTime = itemView.findViewById(R.id.yvTime) as TextView
            ll_expense = itemView.findViewById(R.id.ll_expense) as LinearLayout

            ll_expense.setOnClickListener {
                ConstantVariable.animationEffect(ll_expense, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                if (onItemClickValue != null) {
                    onItemClickValue?.getPositionOfList(int)
                }
            }
            tv_name.setText(alResult_global!!.get(int).expName)
            tvDistibutor_name.setText("Amount : " + alResult_global!!.get(int).expAmount)
            //tvDate.setText(ConstantVariable.parseDateToddMMyyyy(alResult_global!!.get(int).expDate))
            tvDate.setText(ConstantVariable.parseDateTo_ddMMMyyyy_From_yyyyMMDD_WithoutTime(alResult_global!!.get(int).expDate))
            yvTime.setText(alResult_global!!.get(int).approvalStage)

            if (alResult_global!!.get(int).approvalStage.equals("Pending", ignoreCase = true)) {
                yvTime.setBackgroundResource(R.drawable.bg_pill_info)
            } else if (alResult_global!!.get(int).approvalStage.equals("Approved", ignoreCase = true)) {
                yvTime.setBackgroundResource(R.drawable.bg_pill_success)
            } else if (alResult_global!!.get(int).approvalStage.equals("Rejected", ignoreCase = true)) {
                yvTime.setBackgroundResource(R.drawable.bg_pill_danger)
            }


        }

    }

}