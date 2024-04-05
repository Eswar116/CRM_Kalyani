package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class OrderDetailResult2 {

    @SerializedName("order_detail_id")
    @Expose
    private var orderDetailId: Int? = null
    @SerializedName("order_mst_id")
    @Expose
    private var orderMstId: Int? = null
    @SerializedName("order_attachment_url")
    @Expose
    private var orderAttachmentUrl: String? = null
    @SerializedName("remarks")
    @Expose
    private var remarks: String? = null

    fun getOrderDetailId(): Int? {
        return orderDetailId
    }

    fun setOrderDetailId(orderDetailId: Int?) {
        this.orderDetailId = orderDetailId
    }

    fun getOrderMstId(): Int? {
        return orderMstId
    }

    fun setOrderMstId(orderMstId: Int?) {
        this.orderMstId = orderMstId
    }

    fun getOrderAttachmentUrl(): String? {
        return orderAttachmentUrl
    }

    fun setOrderAttachmentUrl(orderAttachmentUrl: String) {
        this.orderAttachmentUrl = orderAttachmentUrl
    }

    fun getRemarks(): String? {
        return remarks
    }

    fun setRemarks(remarks: String) {
        this.remarks = remarks
    }
}