package com.crm.crmapp.cRMDashboardModel.adapter

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crm.crmapp.CRMDashboardModel.Result3
import com.crm.crmapp.R
import java.util.ArrayList

class CRMDashboardResult3GridAdapter(var c: Context?, var result3List: ArrayList<Result3>) :
    RecyclerView.Adapter<CRMDashboardResult3GridAdapter.ViewHolder>() {
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
        viewHolder.dataBind(c!!, alColors, position, result3List)
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
            result3List: ArrayList<Result3>
        ) {
            cardview = itemView.findViewById<CardView>(R.id.cardview)
            tvordercount = itemView.findViewById<TextView>(R.id.tvordercount)
            tvordertotal = itemView.findViewById<TextView>(R.id.tvordertotal)
            tvordercount_label = itemView.findViewById<TextView>(R.id.tvordercount_label)
            tvordertotal_label = itemView.findViewById<TextView>(R.id.tvordertotal_label)
            count = position % alColors.size
            when (count) {
                0 -> {
                    if (result3List.size > 0) {
                        tvordercount_label.setText("Total Count")
                        tvordertotal_label.setText("Total Box")

                        tvordercount.setText(result3List.get(0).totCnt.toString())
                        tvordertotal.setText(result3List.get(0).totBox.toString())
                    };
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bgsuccess))
                }
                1 -> {
                    if (result3List.size > 0) {
                        tvordercount_label.setText("New Count")
                        tvordertotal_label.setText("New Box")

                        tvordercount.setText(result3List.get(0).newCnt.toString())
                        tvordertotal.setText(result3List.get(0).newBox.toString())
                    }
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bginfo))
                }
                2 -> {
                    if (result3List.size > 0) {
                        tvordercount_label.setText("Processed Count")
                        tvordertotal_label.setText("Processed Box")

                        tvordercount.setText(result3List.get(0).processedCnt.toString())
                        tvordertotal.setText(result3List.get(0).processedBox.toString())
                    }
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bgwarning))
                }
                3 -> {
                    if (result3List.size > 0) {
                        tvordercount_label.setText("Rejected Count")
                        tvordertotal_label.setText("Rejected Box")

                        tvordercount.setText(result3List.get(0).rejectedCnt.toString())
                        tvordertotal.setText(result3List.get(0).rejectedBox.toString())
                    }
                    cardview.setCardBackgroundColor(context.getResources().getColor(R.color.bgdanger))
                }
            }

        }

    }

}