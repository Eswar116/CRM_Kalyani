package com.crm.crmapp.marketPlan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MarketPlanListRequestResponse {

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
        @SerializedName("owner_full_name")
        @Expose
        public var owner_full_name: String? = null
        @SerializedName("market_plan_name")
        @Expose
        public var market_plan_name: String? = null
        @SerializedName("enddate")
        @Expose
        public var enddate: String? = null
        @SerializedName("stage")
        @Expose
        public var stage: String? = null
        @SerializedName("state_name")
        @Expose
        public var state_name: String? = null
        @SerializedName("distributor_name")
        @Expose
        public var distributor_name: String? = null
        @SerializedName("id")
        @Expose
        public var id: String? = null
        @SerializedName("startdate")
        @Expose
        public var startdate: String? = null
    }
}