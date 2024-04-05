package com.crm.crmapp.customer.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.model.DocDetailModel
import java.util.ArrayList

class DistributionAdapter( private  val context: Context?, docDetailList: ArrayList<DocDetailModel>?) :
    RecyclerView.Adapter<DistributionAdapter.MyViewHolder>(){
private var mdocDetailList : ArrayList<DocDetailModel> = ArrayList<DocDetailModel>()

init {
    mdocDetailList = docDetailList!!
}


override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DistributionAdapter.MyViewHolder {

    val v = LayoutInflater.from(p0.context).inflate(R.layout.row_new_order, p0, false)
    return DistributionAdapter.MyViewHolder(v)
}

override fun getItemCount(): Int {
    return 1
}

override fun onBindViewHolder(p0: DistributionAdapter.MyViewHolder, p1: Int) {
    p0.itemBind()
    p0.ivDelete.setOnClickListener {

    }

}

class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
    lateinit var ivDelete : ImageView
    lateinit var ivDocIcon : ImageView
    lateinit var tvDocSize : TextView
    lateinit var tvDocName : TextView

    fun itemBind(
        ){
        ivDelete=itemView.findViewById<ImageView>(R.id.ivDelete)
        ivDocIcon=itemView.findViewById<ImageView>(R.id.ivDocIcon)
        tvDocSize=itemView.findViewById<TextView>(R.id.tvDocSize)
        tvDocName=itemView.findViewById<TextView>(R.id.tvDocName)

        ivDelete.visibility= View.VISIBLE
        ivDelete?.setImageResource(android.R.drawable.ic_menu_view)

    }

}
}