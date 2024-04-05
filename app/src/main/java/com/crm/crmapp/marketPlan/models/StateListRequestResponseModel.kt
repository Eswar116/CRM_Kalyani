package com.crm.crmapp.marketPlan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StateListRequestResponseModel : Serializable {

    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("Result")
    @Expose
    private var result: List<Result>? = null

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

    fun getResult(): List<Result>? {
        return result
    }

    fun setResult(result: List<Result>) {
        this.result = result
    }


    class Result : Serializable {
        @SerializedName("state_code")
        @Expose
        public var state_code: String? = null
        @SerializedName("state_name")
        @Expose
        public var state_name: String? = null
    }
}