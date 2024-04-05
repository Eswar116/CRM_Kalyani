package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewOrderResponse {
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("Result")
    @Expose
    private var result: List<Result>? = null
    @SerializedName("msg")
    @Expose
    private var msg: String? = null

    @SerializedName("custid")
    @Expose
    private var custid: Int? = null

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

    fun getCustId(): Int? {
        return custid
    }

    fun setCustId(custid: Int?) {
        this.custid = custid
    }

    fun getMsg(): String? {
        return msg
    }

    fun setMsg(msg: String) {
        this.msg = msg
    }


    class Result {
        @SerializedName("cust_name")
        @Expose
        private var custName: String? = null
        @SerializedName("emailid")
        @Expose
        private var emailid: String? = null
        @SerializedName("mobileno")
        @Expose
        private var mobileno: String? = null
        @SerializedName("order_id")
        @Expose
        private var orderId: Int? = null
        @SerializedName("cust_id")
        @Expose
        private var custId: Int? = null
        @SerializedName("no_of_boxes")
        @Expose
        private var no_of_boxes: String? = null

        fun getNoOfBoxes(): String? {
            return no_of_boxes
        }

        fun setNoOfBoxes(no_of_boxes: String?) {
            this.no_of_boxes = no_of_boxes
        }

        fun getCustName(): String? {
            return custName
        }

        fun setCustName(custName: String) {
            this.custName = custName
        }

        fun getEmailid(): String? {
            return emailid
        }

        fun setEmailid(emailid: String) {
            this.emailid = emailid
        }

        fun getMobileno(): String? {
            return mobileno
        }

        fun setMobileno(mobileno: String) {
            this.mobileno = mobileno
        }

        fun getOrderId(): Int? {
            return orderId
        }

        fun setOrderId(orderId: Int?) {
            this.orderId = orderId
        }

        fun getCustId(): Int? {
            return custId
        }

        fun setCustId(custId: Int?) {
            this.custId = custId
        }
    }
}