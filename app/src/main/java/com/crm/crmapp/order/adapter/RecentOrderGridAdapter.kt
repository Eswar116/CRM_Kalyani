package com.crm.crmapp.order.adapter

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.crm.crmapp.R
import com.crm.crmapp.order.model.OrderDashboardResult
import java.util.*

class RecentOrderGridAdapter(var c: Context?, var orderDashboardList: ArrayList<OrderDashboardResult>) :
    RecyclerView.Adapter<RecentOrderGridAdapter.ViewHolder>() {
    private lateinit var v: View
    private var alColors: Array<String> = c!!.resources.getStringArray(R.array.colors)

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        v = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_recent_order_grid2, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.dataBind(c!!, alColors, position, orderDashboardList)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var img_user: ImageView
        lateinit var cardview: CardView
        lateinit var tvordercount: TextView
        lateinit var tvordertotal: TextView
        private var count: Int? = null

        fun dataBind(
            context: Context,
            alColors: Array<String>,
            position: Int,
            orderDashboardList: ArrayList<OrderDashboardResult>
        ) {
            img_user = itemView.findViewById<ImageView>(R.id.img_user)
            cardview = itemView.findViewById<CardView>(R.id.cardview)
            tvordercount = itemView.findViewById<TextView>(R.id.tvordercount)
            tvordertotal = itemView.findViewById<TextView>(R.id.tvordertotal)
            count = position % alColors.size
            when (count) {
                0 -> {
                    if (orderDashboardList.size > 0) {
                        tvordercount.setText("Total Orders : " + orderDashboardList.get(0).totCnt.toString())
                        tvordertotal.setText("Total Box : " + orderDashboardList.get(0).totBox.toString())
                    }
                    img_user.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorGrey),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    );
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bgsuccess))
                }
                1 -> {
                    if (orderDashboardList.size > 0) {
                        tvordercount.setText("New Orders : " + orderDashboardList.get(0).newCnt.toString())
                        tvordertotal.setText("New Box : " + orderDashboardList.get(0).newBox.toString())
                    }
                    img_user.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorGrey),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    );
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bginfo))
                }
                2 -> {
                    if (orderDashboardList.size > 0) {
                        tvordercount.setText("Processed Orders : " + orderDashboardList.get(0).processedCnt.toString())
                        tvordertotal.setText("Processed Box : " + orderDashboardList.get(0).processedBox.toString())
                    }
                    img_user.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorGrey),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    );
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bgwarning))
                }
                3 -> {
                    if (orderDashboardList.size > 0) {
                        tvordercount.setText("Rejected Orders : " + orderDashboardList.get(0).rejectedCnt.toString())
                        tvordertotal.setText("Rejected Box : " + orderDashboardList.get(0).rejectedBox.toString())
                    }
                    img_user.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorGrey),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    );
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bgdanger))
                }
            }


        }

    }

}