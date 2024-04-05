package com.crm.crmapp.search

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

class SearchMainAdapter(
    var context: Context,
    var complaintResultList: ArrayList<SearchMainResponse.Result>?,
    var onItemClick: onItemClick
): RecyclerView.Adapter<SearchMainAdapter.MyViewHolder>() {

    companion object {
        var onItemClickValue : onItemClick?=null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.row_search_main, p0, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  complaintResultList!!.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {

        p0.dataBind( complaintResultList!![p1],p1,context)
        onItemClickValue =onItemClick
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        lateinit var tvStatus: TextView
        lateinit var tvDescription: TextView
        lateinit var tvName: TextView
        lateinit var llRow: LinearLayout
        fun dataBind(searchResult: SearchMainResponse.Result, position: Int, context: Context){
            tvName=itemView.findViewById<TextView>(R.id.tvName)
            llRow=itemView.findViewById<LinearLayout>(R.id.llRow)
            tvDescription=itemView.findViewById(R.id.tvDescription) as TextView
            tvStatus=itemView.findViewById(R.id.tvStatus) as TextView
            tvName.setText(searchResult.getH1())
            tvDescription.setText(searchResult.getH2())
            tvStatus.setText(searchResult.getType())
            llRow.setOnClickListener {
                if (SearchMainAdapter.onItemClickValue != null) {
                    ConstantVariable.animationEffect(llRow, context as Activity)
                    SearchMainAdapter.onItemClickValue?.getPositionOfList(position!!)
                }
            }
        }
    }
}