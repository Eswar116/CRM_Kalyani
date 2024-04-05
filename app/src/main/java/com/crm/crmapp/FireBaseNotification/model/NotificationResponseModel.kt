package com.crm.crmapp.fireBaseNotification.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class NotificationResponseModel {
    @SerializedName("msg")
    @Expose
    private var msg: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("Result")
    @Expose
    private var result: List<Result>? = null

    fun getMsg(): String? {
        return msg
    }

    fun setMsg(msg: String) {
        this.msg = msg
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


    class Result{
        @SerializedName("is_read")
        @Expose
        private var isRead: Boolean? = null
        @SerializedName("n_date")
        @Expose
        private var nDate: String? = null
        @SerializedName("bean_type")
        @Expose
        private var beanType: String? = null
        @SerializedName("n_title")
        @Expose
        private var nTitle: String? = null
        @SerializedName("id")
        @Expose
        private var id: Int? = null
        @SerializedName("bean_id")
        @Expose
        private var beanId: Int? = null
        @SerializedName("n_body")
        @Expose
        private var nBody: String? = null

        fun getIsRead(): Boolean? {
            return isRead
        }

        fun setIsRead(isRead: Boolean?) {
            this.isRead = isRead
        }

        fun getNDate(): String? {
            return nDate
        }

        fun setNDate(nDate: String) {
            this.nDate = nDate
        }

        fun getBeanType(): String? {
            return beanType
        }

        fun setBeanType(beanType: String) {
            this.beanType = beanType
        }

        fun getNTitle(): String? {
            return nTitle
        }

        fun setNTitle(nTitle: String) {
            this.nTitle = nTitle
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

        fun getNBody(): String? {
            return nBody
        }

        fun setNBody(nBody: String) {
            this.nBody = nBody
        }
    }
}