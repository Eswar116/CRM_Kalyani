package com.crm.crmapp.marketPlan.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.marketPlan.models.CategoryListRequestResponseModel

class CategoryListAdapter(
    val context: Context?,
    var categoryList: ArrayList<CategoryListRequestResponseModel.Result>,
    val btnlistener: BtnClickListener
) : RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>() {

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.bindItems(position, context!!, categoryList)
        mClickListener = btnlistener
    }

    companion object {
        var mClickListener: BtnClickListener? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.row_dialog_expense_traveling, p0, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categoryList!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            position: Int,
            context: Context,
            mtravelList: ArrayList<CategoryListRequestResponseModel.Result>
        ) {
            var llRow = itemView.findViewById(R.id.llRow) as LinearLayout
            var tvExpenseType = itemView.findViewById<TextView>(R.id.tvExpenseType)

            if (mtravelList!!.get(position).cat_name != null) {
                tvExpenseType.setText(mtravelList!!.get(position).cat_name)
            } else if (mtravelList!!.get(position).category != null) {
                tvExpenseType.setText(mtravelList!!.get(position).category)
            }

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