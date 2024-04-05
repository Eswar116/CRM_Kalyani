package com.crm.crmapp.marketPlan.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R

class StringListAdapter(
    val context: Context?,
    var list: ArrayList<String>,
    val btnlistener: BtnClickListener
) : RecyclerView.Adapter<StringListAdapter.MyViewHolder>() {

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.bindItems(position, context!!, list)
        mClickListener = btnlistener
    }

    companion object {
        var mClickListener: BtnClickListener? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.row_dialog_expense_traveling, p0, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(position: Int, context: Context, mtravelList: ArrayList<String>) {
            var llRow = itemView.findViewById(R.id.llRow) as LinearLayout
            var tvExpenseType = itemView.findViewById<TextView>(R.id.tvExpenseType)
            tvExpenseType.setText(mtravelList!!.get(position))

            llRow.setOnClickListener {
                if (mClickListener != null)
                    mClickListener?.onBtnClick(
                        position, llRow, tvExpenseType.text.toString()
                    )
            }

        }

    }

    open interface BtnClickListener {
        fun onBtnClick(position: Int, llRow: LinearLayout, type: String)
    }

}