package com.crm.crmapp.cRMDashboardModel.adapter

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crm.crmapp.CRMDashboardModel.Result1
import com.crm.crmapp.R
import java.util.ArrayList

class CRMDashboardResult1GridAdapter(var c: Context?, var result1List: ArrayList<Result1>) :
    RecyclerView.Adapter<CRMDashboardResult1GridAdapter.ViewHolder>() {
    private lateinit var v: View
    private var alColors: Array<String> = c!!.resources.getStringArray(R.array.colors)

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_layout_mainactivity, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.dataBind(c!!, alColors, position, result1List)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var cardview: CardView
        lateinit var tvordercount: TextView
        lateinit var tvordertotal: TextView
        lateinit var tvordercount_label: TextView
        lateinit var tvordertotal_label: TextView
        private var count: Int? = null

        fun dataBind(
            context: Context,
            alColors: Array<String>,
            position: Int,
            result1List: ArrayList<Result1>
        ) {
            cardview = itemView.findViewById<CardView>(R.id.cardview)
            tvordercount = itemView.findViewById<TextView>(R.id.tvordercount)
            tvordertotal = itemView.findViewById<TextView>(R.id.tvordertotal)
            tvordercount_label = itemView.findViewById<TextView>(R.id.tvordercount_label)
            tvordertotal_label = itemView.findViewById<TextView>(R.id.tvordertotal_label)
            count = position % alColors.size
            when (count) {
                0 -> {
                    if (result1List.size > 0) {
                        tvordercount_label.setText("Total Count")
                        tvordertotal_label.setText("Total Amount")

                        tvordercount.setText(result1List.get(0).cntTot.toString())
                        tvordertotal.setText(result1List.get(0).amtTot.toString())
                    };
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bgsuccess))
                }
                1 -> {
                    if (result1List.size > 0) {
                        tvordercount_label.setText("Pending Count")
                        tvordertotal_label.setText("Pending Sum")

                        tvordercount.setText(result1List.get(0).cntPending.toString())
                        tvordertotal.setText(result1List.get(0).sumPending.toString())
                    }
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bginfo))
                }
                2 -> {
                    if (result1List.size > 0) {
                        tvordercount_label.setText("Approved Count")
                        tvordertotal_label.setText("Approved Sum")

                        tvordercount.setText(result1List.get(0).cntApproved.toString())
                        tvordertotal.setText(result1List.get(0).sumApproved.toString())
                    }
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bgwarning))
                }
                3 -> {
                    tvordercount_label.setText("Rejected Count")
                    tvordertotal_label.setText("Rejected Sum")

                    if (result1List.size > 0) {
                        tvordercount.setText(result1List.get(0).cntRejected.toString())
                        tvordertotal.setText(result1List.get(0).sumRejected.toString())
                    }
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bgdanger))
                }
            }

        }

    }

}