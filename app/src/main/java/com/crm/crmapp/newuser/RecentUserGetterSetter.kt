package com.crm.crmapp.newuser

import com.crm.crmapp.customer.model.CustomerResult
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecentUserGetterSetter {

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("status")
    @Expose
    private var status: String? = null

    @SerializedName("Result")
    @Expose
    private var result: List<UserResult?>? = null

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getResult(): List<UserResult?>? {
        return result
    }

    fun setResult(result: List<UserResult?>?) {
        this.result = result
    }

}