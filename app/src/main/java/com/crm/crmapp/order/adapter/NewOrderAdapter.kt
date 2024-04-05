package com.crm.crmapp.order.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.model.DocDetailModel
import android.view.*
import com.crm.crmapp.order.util.ConstantVariable
import uk.co.senab.photoview.PhotoViewAttacher




class NewOrderAdapter(
     var context: Context?,
    docDetailList: ArrayList<DocDetailModel>?
) : RecyclerView.Adapter<NewOrderAdapter.MyViewHolder>() {



    private var mdocDetailList : ArrayList<DocDetailModel> = ArrayList<DocDetailModel>()
    init {
        mdocDetailList = docDetailList!!
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewOrderAdapter.MyViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.row_new_order, p0, false)
        return NewOrderAdapter.MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mdocDetailList!!.size
    }

    override fun onBindViewHolder(p0: NewOrderAdapter.MyViewHolder, p1: Int) {
        p0.itemBind(mdocDetailList!![p1],context)
        p0.ivDelete.setOnClickListener {

            if (mdocDetailList.size>0) {
                this.mdocDetailList.removeAt(p1)

                notifyItemRemoved(p1);
                notifyItemRangeChanged(p1, getItemCount() - p1);
            }
        }

    }


    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        lateinit var ivDelete :ImageView
        lateinit var ivDocIcon :ImageView
        lateinit var tvDocSize :TextView
        lateinit var tvDocName :TextView
         var dialog : Dialog?=null

        fun itemBind(
            docDetailList: DocDetailModel,
            context: Context?
        ){

            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
            dialog!!.setContentView(R.layout.dialog_image)
            dialog!!.setCancelable(true)
            ivDelete=itemView.findViewById<ImageView>(R.id.ivDelete)
            ivDocIcon=itemView.findViewById<ImageView>(R.id.ivDocIcon)
            tvDocSize=itemView.findViewById<TextView>(R.id.tvDocSize)
            tvDocName=itemView.findViewById<TextView>(R.id.tvDocName)
            tvDocName?.setText(docDetailList.docName)
            tvDocSize?.setText(docDetailList.docSize.toString()+" "+"kb")
            ivDocIcon?.setImageBitmap(ConstantVariable.StringToBitMap(docDetailList.uri))
            ivDelete.visibility=View.VISIBLE

            ivDocIcon.setOnClickListener {
                showCustomDialog(ConstantVariable.StringToBitMap(docDetailList.uri))
            }


        }

        private fun showCustomDialog(stringToBitMap: Bitmap?) {
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog!!.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT



            (dialog!!.findViewById(R.id.bt_close) as ImageView).setOnClickListener {
                dialog!!.dismiss() }

            val pAttacher: PhotoViewAttacher
            var imageView=  (dialog!!.findViewById(R.id.ivZoomImage) as ImageView)
            imageView.scaleType =ImageView.ScaleType.FIT_XY
            imageView.setImageBitmap(stringToBitMap)
            pAttacher = PhotoViewAttacher(imageView)
            pAttacher.update()

            dialog!!.show()
            dialog!!.window!!.attributes = lp
        }

    }
}