package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderDetailRequest {

    @SerializedName("orderId")
    @Expose
    private var orderId: Int? = null


    fun getOrderId(): Int? {
        return orderId
    }

    fun setOrderId(orderId: Int?) {
        this.orderId = orderId
    }
}