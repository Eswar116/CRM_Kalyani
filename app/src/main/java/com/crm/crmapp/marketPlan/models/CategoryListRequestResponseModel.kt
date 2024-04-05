package com.crm.crmapp.marketPlan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CategoryListRequestResponseModel {

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

    fun getResult(): List<CategoryListRequestResponseModel.Result>? {
        return result
    }

    fun setResult(result: List<CategoryListRequestResponseModel.Result>) {
        this.result = result
    }

    class Result : Serializable {
        @SerializedName("cat_code")
        @Expose
        public var cat_code: String? = null

        @SerializedName("cat_name")
        @Expose
        public var cat_name: String? = null
        @SerializedName("category")
        @Expose
        public var category: String? = null
    }

}