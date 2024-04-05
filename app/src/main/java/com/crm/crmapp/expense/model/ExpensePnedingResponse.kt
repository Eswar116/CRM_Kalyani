package com.crm.crmapp.expense.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ExpensePendingResponse {

    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("Result")
    @Expose
    private var result: List<Result>? = null
    @SerializedName("Result_1")
    @Expose
    private var result1: List<Result>? = null
    @SerializedName("Result_2")
    @Expose
    private var result2: List<Result>? = null


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

    fun getResult1(): List<Result>? {
        return result1
    }

    fun setResult1(result: List<Result>) {
        this.result1 = result
    }

    fun getResult2(): List<Result>? {
        return result2
    }

    fun setResult2(result: List<Result>) {
        this.result2 = result
    }

    class Result {

        @SerializedName("exp_amount")
        @Expose
        private var expAmount: Double? = null
        @SerializedName("exp_name")
        @Expose
        private var expName: String? = null
        @SerializedName("full_name")
        @Expose
        private var fullName: String? = null
        @SerializedName("user_name")
        @Expose
        private var userName: String? = null
        @SerializedName("exp_date")
        @Expose
        private var expDate: String? = null
        @SerializedName("id")
        @Expose
        private var id: Int? = null

        fun getExpAmount(): Double? {
            return expAmount
        }

        fun setExpAmount(expAmount: Double?) {
            this.expAmount = expAmount
        }

        fun getExpName(): String? {
            return expName
        }

        fun setExpName(expName: String) {
            this.expName = expName
        }

        fun getFullName(): String? {
            return fullName
        }

        fun setFullName(fullName: String) {
            this.fullName = fullName
        }

        fun getUserName(): String? {
            return userName
        }

        fun setUserName(userName: String) {
            this.userName = userName
        }

        fun getExpDate(): String? {
            return expDate
        }

        fun setExpDate(expDate: String) {
            this.expDate = expDate
        }

        fun getId(): Int? {
            return id
        }

        fun setId(id: Int?) {
            this.id = id
        }

    }
}