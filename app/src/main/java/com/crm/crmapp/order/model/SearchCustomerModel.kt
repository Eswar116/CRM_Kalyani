package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class SearchCustomerModel(){

    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

    @SerializedName("Result")
    @Expose
    private var result: List<SearchCustomerResultModel>? = null

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

    fun getResult(): List<SearchCustomerResultModel>? {
        return result
    }

    fun setResult(result: List<SearchCustomerResultModel>) {
        this.result = result
    }
}