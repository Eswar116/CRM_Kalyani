package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class NewOrderResult2 {

    @SerializedName("_order_attachment_url")
    @Expose
    private var orderAttachmentUrl: String? = null

    fun getOrderAttachmentUrl(): String? {
        return orderAttachmentUrl
    }

    fun setOrderAttachmentUrl(orderAttachmentUrl: String) {
        this.orderAttachmentUrl = orderAttachmentUrl
    }
}