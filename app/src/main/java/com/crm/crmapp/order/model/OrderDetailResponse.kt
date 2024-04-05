package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class OrderDetailResponse {

    @SerializedName("Result_2")
    @Expose
    private var result2: List<OrderDetailResult2>? = null
    @SerializedName("Result_1")
    @Expose
    private var result1: List<OrderDetailResult1>? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

    fun getResult2(): List<OrderDetailResult2>? {
        return result2
    }

    fun setResult2(result2: List<OrderDetailResult2>) {
        this.result2 = result2
    }

    fun getResult1(): List<OrderDetailResult1>? {
        return result1
    }

    fun setResult1(result1: List<OrderDetailResult1>) {
        this.result1 = result1
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }
}