package com.crm.crmapp.customer.adapter

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
import com.crm.crmapp.customer.model.SaveCustomerResult1
import kotlinx.android.synthetic.main.row_recent_customer_adapter.view.*

class OfflineAdapterCustomer(
    val context: Context?,
    var orderAllModelList: ArrayList<SaveCustomerResult1>,
    var onItemClick: onItemClick
): RecyclerView.Adapter<OfflineAdapterCustomer.MyViewHolder>() {
    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {

        p0.dataBind(orderAllModelList!![p1],context,p1)
        onItemClickValue=onItemClick
    }

    companion object {
        var onItemClickValue: onItemClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OfflineAdapterCustomer.MyViewHolder{
        val v = LayoutInflater.from(p0.context).inflate(R.layout.row_recent_customer_adapter, p0, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  orderAllModelList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var tvRecentCust: TextView? = null
        private var tvdistributor: TextView? = null
        private var tvCustomerGroupName: TextView? = null
        private var ivCall: ImageView? = null
        private var llDetail: LinearLayout? = null

        fun dataBind(
            item: SaveCustomerResult1,
            context: Context?,
            position: Int
        ){
            tvRecentCust = itemView.findViewById<TextView>(R.id.tvRecentCust)
            tvdistributor = itemView.findViewById<TextView>(R.id.tvdistributor)
            tvCustomerGroupName = itemView.findViewById<TextView>(R.id.tvCustomerGroupName)
            ivCall = itemView.findViewById<ImageView>(R.id.ivCall)
            llDetail = itemView.findViewById<LinearLayout>(R.id.llDetail)
            llDetail!!.setBackgroundColor(context!!.resources.getColor(R.color.white))
            itemView.tvRecentCust.setText(item.getCustName())
            itemView.tvdistributor.setText("(" + item.getCustType() + ")")

            itemView.tvCustomerGroupName.setText("Distributer : " + item.getCustType())

            itemView.ivCall.setOnClickListener {
                ConstantVariable.sendPhoneCall(context!!, item.getMobileno())
                ConstantVariable.animationEffect(itemView.ivCall, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
            itemView.ivEmail.setOnClickListener {
                ConstantVariable.sendEmail(context!!, "Check", item.getEmailid())
                ConstantVariable.animationEffect(itemView.ivEmail, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

            itemView.ll_edit.setOnClickListener {
              /*  var intent = Intent(context, NewCustomerActivity::class.java)
                intent.putExtra(ConstantVariable.TAG_FROM, "edit")
                intent.putExtra(ConstantVariable.TAG_OBJECT, item)
                context!!.startActivity(intent)
                ConstantVariable.animationEffect(itemView.ll_edit, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);*/
            }

            itemView.llDetail.setOnClickListener {
                ConstantVariable.animationEffect(itemView.llDetail, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                if (OfflineAdapterCustomer.onItemClickValue != null) {
                    OfflineAdapterCustomer.onItemClickValue?.getPositionOfList(position)
                }
            }

        }


    }


}

