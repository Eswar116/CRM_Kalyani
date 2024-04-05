package com.crm.crmapp.customer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class FavrtSaveRequest {
    @SerializedName("Result")
    @Expose
    private var result: List<Result>? = null

    fun getResult(): List<Result>? {
        return result
    }

    fun setResult(result: List<Result>) {
        this.result = result
    }

    class Result {
        @SerializedName("_user_id")
        @Expose
        private var userId: String? = null
        @SerializedName("_bean_type")
        @Expose
        private var beanType: String? = null
        @SerializedName("_bean_id")
        @Expose
        private var beanId: Int? = null
        @SerializedName("_remarks")
        @Expose
        private var remarks: String? = null
        @SerializedName("_isactive")
        @Expose
        private var isactive: Int? = null

        fun getUserId(): String? {
            return userId
        }

        fun setUserId(userId: String) {
            this.userId = userId
        }

        fun getBeanType(): String? {
            return beanType
        }

        fun setBeanType(beanType: String) {
            this.beanType = beanType
        }

        fun getBeanId(): Int? {
            return beanId
        }

        fun setBeanId(beanId: Int?) {
            this.beanId = beanId
        }

        fun getRemarks(): String? {
            return remarks
        }

        fun setRemarks(remarks: String) {
            this.remarks = remarks
        }

        fun getIsactive(): Int? {
            return isactive
        }

        fun setIsactive(isactive: Int?) {
            this.isactive = isactive
        }
    }
}