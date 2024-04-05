package com.crm.crmapp.marketPlan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CityListRequestResponseModel {

    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("Result")
    @Expose
    private var result: List<CityListRequestResponseModel.Result>? = null

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

    fun getResult(): List<CityListRequestResponseModel.Result>? {
        return result
    }

    fun setResult(result: List<CityListRequestResponseModel.Result>) {
        this.result = result
    }

    class Result : Serializable {
        @SerializedName("city_name")
        @Expose
        public var city_name: String? = null
        @SerializedName("state_code")
        @Expose
        public var state_code: String? = null
        @SerializedName("city_code")
        @Expose
        public var city_code: String? = null
    }


}