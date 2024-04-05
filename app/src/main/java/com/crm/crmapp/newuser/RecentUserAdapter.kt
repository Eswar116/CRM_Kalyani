package com.crm.crmapp.newuser

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import kotlinx.android.synthetic.main.row_recent_customer_adapter.view.llDetail
import kotlinx.android.synthetic.main.row_recent_customer_adapter.view.ll_edit
import kotlinx.android.synthetic.main.row_recent_customer_adapter.view.tvRecentCust
import kotlinx.android.synthetic.main.row_recent_user_adapter.view.*

class RecentUserAdapter(
    var context: Context,
    var data: ArrayList<UserResult>,
    var onItemClick: onItemClick,
    var onActivateClick: OnActivateClick
) :
    RecyclerView.Adapter<RecentUserAdapter.MyViewHolder>() {
    companion object {
        var onItemClickValue: onItemClick? = null
        var onActivate: OnActivateClick? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.row_recent_user_adapter, p0, false)
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
        private var tvRecentCust: TextView? = null
        private var tvdistributor: TextView? = null
        private var tvactive: TextView? = null
        private var btnActivate: Button? = null
        private var llDetail: LinearLayout? = null

        fun dataBind(
            item: UserResult,
            context: Context,
            int: Int
        ) {
            tvRecentCust = itemView.findViewById<TextView>(R.id.tvRecentCust)
            tvdistributor = itemView.findViewById<TextView>(R.id.tvdistributor)
            tvactive = itemView.findViewById<TextView>(R.id.tvactive)
            btnActivate = itemView.findViewById<Button>(R.id.btnActivate)
            llDetail = itemView.findViewById<LinearLayout>(R.id.llDetail)

            itemView.tvRecentCust.setText(item.getUserFullName())

            /*
            itemView.ivEmail.setOnClickListener {
                ConstantVariable.sendEmail(context, item.getEmailid(), "")
                ConstantVariable.animationEffect(itemView.ivEmail, context as Activity)
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }*/
            if (item.getIsactive() != null && item.getIsactive() == true) {
                itemView.tvactive.setText("Active : Yes")
                btnActivate!!.visibility = View.GONE
                itemView.llDetail.setBackgroundColor(context.resources.getColor(R.color.grey_5))
                itemView.ll_edit.setOnClickListener {
                    ConstantVariable.animationEffect(it, context as Activity)
                    context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    var intent = Intent(context, NewUserCreateActivity::class.java)
                    intent.putExtra(ConstantVariable.TAG_FROM, "edit")
                    intent.putExtra(ConstantVariable.TAG_OBJECT, item)
                    context.startActivity(intent)
                }

                itemView.llDetail.setOnClickListener {
                    ConstantVariable.animationEffect(itemView.llDetail, context as Activity)
                    context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    if (onItemClickValue != null) {
                        onItemClickValue?.getPositionOfList(int)
                    }
                }
            } else {
                itemView.tvactive.setText("Active : No")
                btnActivate!!.visibility = View.VISIBLE
                itemView.llDetail.setBackgroundColor(context.resources.getColor(R.color.deep_orange_300))
                btnActivate!!.setOnClickListener {
                    ConstantVariable.animationEffect(btnActivate!!, context as Activity)
                    if (onActivate != null) {
                        onActivate?.getPositionOfList(int)
                    }
                }
            }
        }
    }

    interface OnActivateClick {
        fun getPositionOfList(position :Int)
    }

}