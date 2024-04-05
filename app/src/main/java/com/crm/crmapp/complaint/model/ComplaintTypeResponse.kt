package com.crm.crmapp.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ComplaintTypeResponse {
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("Result")
    @Expose
    private var result: List<ComplaintTypeResponse.Result>? = null

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

    fun getResult(): List<ComplaintTypeResponse.Result>? {
        return result
    }

    fun setResult(result: List<ComplaintTypeResponse.Result>) {
        this.result = result
    }


    class Result {

        @SerializedName("type_name")
        @Expose
        private var typeName: String? = null
        @SerializedName("id")
        @Expose
        private var id: Int? = null

        fun getTypeName(): String? {
            return typeName
        }

        fun setTypeName(typeName: String) {
            this.typeName = typeName
        }

        fun getId(): Int? {
            return id
        }

        fun setId(id: Int?) {
            this.id = id
        }
    }

}