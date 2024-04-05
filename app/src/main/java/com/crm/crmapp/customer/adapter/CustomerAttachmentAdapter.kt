package com.crm.crmapp.customer.adapter

import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.crm.crmapp.R
import android.view.*
import com.crm.crmapp.order.util.ConstantVariable
import uk.co.senab.photoview.PhotoViewAttacher


class CustomerAttachmentAdapter(
    private val context: Context?,
    docDetailList: ArrayList<String>?,
    private val isDeleteVisible: Boolean?
) : RecyclerView.Adapter<CustomerAttachmentAdapter.MyViewHolder>() {


    private var mdocDetailList: ArrayList<String> = ArrayList<String>()

    init {
        mdocDetailList = docDetailList!!
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.row_new_order, p0, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mdocDetailList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.itemBind(mdocDetailList[p1], context, isDeleteVisible)
        p0.ivDelete.setOnClickListener {
            if (mdocDetailList.size > 0) {
                this.mdocDetailList.removeAt(p1)
                notifyItemRemoved(p1);
                notifyItemRangeChanged(p1, getItemCount() - p1);
            }
        }

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var ivDelete: ImageView
        lateinit var ivDocIcon: ImageView
        lateinit var tvDocSize: TextView
        lateinit var tvDocName: TextView
        var dialog: Dialog? = null
        fun itemBind(
            docDetailList: String, context: Context?, isDeleteVisible: Boolean?
        ) {


            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
            dialog!!.setContentView(R.layout.dialog_image)
            dialog!!.setCancelable(true)
            ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)
            ivDocIcon = itemView.findViewById<ImageView>(R.id.ivDocIcon)
            tvDocSize = itemView.findViewById<TextView>(R.id.tvDocSize)
            tvDocName = itemView.findViewById<TextView>(R.id.tvDocName)

            if (isDeleteVisible!!) {
                ivDelete.visibility = View.VISIBLE
            } else {
                ivDelete.visibility = View.GONE
            }

            val strs = docDetailList.split("EssCRM")

            if (strs.size > 1) {
                if (strs.get(0) != null && strs.get(1) != null && strs.get(2) != null) {
                    ConstantVariable.setImage(context!!, ivDocIcon, strs.get(0)!!)
                    tvDocSize.setText(strs.get(2) + "kb")
                    tvDocName.setText(strs.get(1))
                }
            } else {
                ConstantVariable.setImage(context!!, ivDocIcon, docDetailList!!)
            }

            ivDocIcon.setOnClickListener {

                if (strs.size > 1) {
                    if (strs.get(0) != null && strs.get(1) != null && strs.get(2) != null) {

                        showCustomDialog(context, strs.get(0)!!)
                    }
                } else {
                    showCustomDialog(context, docDetailList)
                }
            }
        }

        private fun showCustomDialog(context: Context?, docDetailList: String) {
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog!!.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            (dialog!!.findViewById(R.id.bt_close) as ImageView).setOnClickListener {
                dialog!!.dismiss()
            }

            val pAttacher: PhotoViewAttacher
            var imageView = (dialog!!.findViewById(R.id.ivZoomImage) as ImageView)

            ConstantVariable.setImage(context!!, imageView, docDetailList!!)

            pAttacher = PhotoViewAttacher(imageView)
            pAttacher.update()

            dialog!!.show()
            dialog!!.window!!.attributes = lp
        }

    }

}