package com.crm.crmapp.customer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class MobileValidationResponse {

    @SerializedName("msg")
    @Expose
    private var msg: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

    fun getMsg(): String? {
        return msg
    }

    fun setMsg(msg: String) {
        this.msg = msg
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

}