package com.crm.crmapp.order.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.customer.activity.NewCustomerActivity
import com.crm.crmapp.order.model.SearchCustomerResultModel
import kotlinx.android.synthetic.main.row_recent_customer_adapter.view.*

class SearchCustomerAdapter(
    var context: Context,
    var alResult: List<SearchCustomerResultModel>,
    var onItemClick: onItemClick
) : RecyclerView.Adapter<SearchCustomerAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.row_search_customer, p0, false)
        return SearchCustomerAdapter.MyViewHolder(v)
    }

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun getItemCount(): Int {
        return alResult.size
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.bindItems(alResult, position, context)
        onItemClickValue = onItemClick
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(alData: List<SearchCustomerResultModel>?, position: Int, context: Context) {
            var tvName = itemView.findViewById(R.id.tvName) as TextView
            var tvDesc = itemView.findViewById(R.id.tvDesc) as TextView
            var tvdistributor = itemView.findViewById(R.id.tvdistributor) as TextView
            var llRow = itemView.findViewById(R.id.llRow) as LinearLayout
            var ll_edit = itemView.findViewById(R.id.ll_edit) as LinearLayout

            tvName.text = alData?.get(position)?.getCustName()
          //  tvDesc.text = "DISTRIBUTER : " + alData?.get(position)?.getGrpCustName()

            /*if (item.isIs_new_entry() == null || item.isIs_new_entry() == true) {
                itemView.ll_edit.visibility = View.VISIBLE
            } else {
                itemView.ll_edit.visibility = View.GONE
            }*/

            if (alData?.get(position)?.getCustType() == null || alData?.get(position)?.getCustType().equals("", true)) {
                tvdistributor.setText("")
            } else {
                tvdistributor.setText("(" + alData?.get(position)?.getCustType() + ")")
            }


            if (alData?.get(position)?.getGrpCustName() == null || alData?.get(position)?.getGrpCustName().equals("", true)) {
                tvDesc.text=""
            } else {
                tvDesc.text = "DISTRIBUTER : " + alData?.get(position)?.getGrpCustName()

            }


            llRow.setOnClickListener {

                if (onItemClickValue != null) {
                    ConstantVariable.animationEffect(llRow, context as Activity)
                    onItemClickValue?.getPositionOfList(position)
                }
            }

            if (alData?.get(position)?.isNewEntry == true) {
                itemView.ll_edit.visibility = View.VISIBLE
            } else {
                itemView.ll_edit.visibility = View.GONE
            }

            itemView.ivCall.setOnClickListener {
                ConstantVariable.sendPhoneCall(context, alData?.get(position)?.getMobileno().toString())
                ConstantVariable.animationEffect(itemView.ivCall, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            itemView.ivEmail.setOnClickListener {
                ConstantVariable.sendEmail(context, alData?.get(position)?.getEmailid().toString(), "")
                ConstantVariable.animationEffect(itemView.ivEmail, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

            ll_edit.setOnClickListener {
                var intent = Intent(context, NewCustomerActivity::class.java)
                intent.putExtra(ConstantVariable.TAG_FROM, "search")
                intent.putExtra(ConstantVariable.TAG_OBJECT, alData?.get(position))
                context.startActivity(intent)
                ConstantVariable.animationEffect(itemView.ll_edit, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

        }

    }
}