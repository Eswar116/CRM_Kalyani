package com.crm.crmapp.salesscheme

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.marketPlan.models.SchemeListRequestResponseModel

class SalesSchemeAdapter(
    var context: Context,
    var data: ArrayList<SchemeListRequestResponseModel.Result>,
    var onItemClick: onItemClick,
    var onActivateClick: OnActivateClick
) :
    RecyclerView.Adapter<SalesSchemeAdapter.MyViewHolder>() {
    companion object {
        var onItemClickValue: onItemClick? = null
        var onActivate: OnActivateClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.row_sales_scheme_adapter, p0, false)
        return MyViewHolder(view, p1)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dataBind(data[position], context, position)
        onItemClickValue = onItemClick
        onActivate = onActivateClick
    }

    class MyViewHolder(itemView: View, p1: Int) : RecyclerView.ViewHolder(itemView) {
        private var tvSchemeName: TextView? = null
        private var tvSchemeCode: TextView? = null
        private var llDetail: LinearLayout? = null
        private var ll_edit: LinearLayout? = null

        fun dataBind(
            item: SchemeListRequestResponseModel.Result,
            context: Context,
            int: Int
        ) {
            tvSchemeName = itemView.findViewById<TextView>(R.id.tvSchemeName)
            tvSchemeCode = itemView.findViewById<TextView>(R.id.tvSchemeCode)
            llDetail = itemView.findViewById<LinearLayout>(R.id.llDetail)
            ll_edit = itemView.findViewById<LinearLayout>(R.id.ll_edit)

            tvSchemeName!!.setText(item.scheme_name)
            tvSchemeCode!!.setText(item.scheme_code)

            llDetail!!.setOnClickListener {
                ConstantVariable.animationEffect(it, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                if (onItemClickValue != null) {
                    onItemClickValue?.getPositionOfList(int)
                }
            }

            ll_edit!!.setOnClickListener {
                ConstantVariable.animationEffect(it, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                if (onActivate != null) {
                    onActivate?.getEditPositionOfList(int)
                }
            }
        }
    }

    interface OnActivateClick {
        fun getActivatePositionOfList(position: Int)
        fun getEditPositionOfList(position: Int)
    }

}