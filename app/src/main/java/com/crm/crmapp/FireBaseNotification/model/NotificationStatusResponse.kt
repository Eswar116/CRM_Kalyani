package com.crm.crmapp.fireBaseNotification.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class NotificationStatusResponse {

    @SerializedName("msg")
    @Expose
    private var msg: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("Result")
    @Expose
    private var result: String? = null

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

    fun getResult(): String? {
        return result
    }

    fun setResult(result: String) {
        this.result = result
    }
}