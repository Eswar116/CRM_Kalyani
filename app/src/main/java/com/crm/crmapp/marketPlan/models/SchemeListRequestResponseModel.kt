package com.crm.crmapp.marketPlan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SchemeListRequestResponseModel {

    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("Result")
    @Expose
    private var result: List<SchemeListRequestResponseModel.Result>? = null

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

    fun getResult(): List<SchemeListRequestResponseModel.Result>? {
        return result
    }

    fun setResult(result: List<SchemeListRequestResponseModel.Result>) {
        this.result = result
    }

    class Result : Serializable {
        @SerializedName("scheme_name")
        @Expose
        public var scheme_name: String? = null
        @SerializedName("scheme_code")
        @Expose
        public var scheme_code: String? = null
        @SerializedName("start_date")
        @Expose
        public var start_date: String? = null
        @SerializedName("end_date")
        @Expose
        public var end_date: String? = null
    }
}