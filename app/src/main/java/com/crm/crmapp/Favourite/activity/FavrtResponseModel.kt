package com.crm.crmapp.favourite.activity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class FavrtResponseModel {

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


    class Result {

        @SerializedName("bean_type")
        @Expose
        private var beanType: String? = null
        @SerializedName("isactive")
        @Expose
        private var isactive: Boolean? = null
        @SerializedName("id")
        @Expose
        private var id: Int? = null
        @SerializedName("bean_id")
        @Expose
        private var beanId: Int? = null
        @SerializedName("line2")
        @Expose
        private var line2: String? = null
        @SerializedName("remarks")
        @Expose
        private var remarks: String? = null
        @SerializedName("line1")
        @Expose
        private var line1: String? = null
        @SerializedName("favourite_date")
        @Expose
        private var favouriteDate: String? = null

        fun getBeanType(): String? {
            return beanType
        }

        fun setBeanType(beanType: String) {
            this.beanType = beanType
        }

        fun getIsactive(): Boolean? {
            return isactive
        }

        fun setIsactive(isactive: Boolean?) {
            this.isactive = isactive
        }

        fun getId(): Int? {
            return id
        }

        fun setId(id: Int?) {
            this.id = id
        }

        fun getBeanId(): Int? {
            return beanId
        }

        fun setBeanId(beanId: Int?) {
            this.beanId = beanId
        }

        fun getLine2(): String? {
            return line2
        }

        fun setLine2(line2: String) {
            this.line2 = line2
        }

        fun getRemarks(): String? {
            return remarks
        }

        fun setRemarks(remarks: String) {
            this.remarks = remarks
        }

        fun getLine1(): String? {
            return line1
        }

        fun setLine1(line1: String) {
            this.line1 = line1
        }

        fun getFavouriteDate(): String? {
            return favouriteDate
        }

        fun setFavouriteDate(favouriteDate: String) {
            this.favouriteDate = favouriteDate
        }

    }
}