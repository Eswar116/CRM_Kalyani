package com.crm.crmapp.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class SearchMainResponse : Serializable {

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


    class Result :Serializable{
        @SerializedName("h1")
        @Expose
        private var h1: String? = null
        @SerializedName("h2")
        @Expose
        private var h2: String? = null
        @SerializedName("id")
        @Expose
        private var id: Int? = null
        @SerializedName("type")
        @Expose
        private var type: String? = null

        fun getH1(): String? {
            return h1
        }

        fun setH1(h1: String) {
            this.h1 = h1
        }

        fun getH2(): String? {
            return h2
        }

        fun setH2(h2: String) {
            this.h2 = h2
        }

        fun getId(): Int? {
            return id
        }

        fun setId(id: Int?) {
            this.id = id
        }

        fun getType(): String? {
            return type
        }

        fun setType(type: String) {
            this.type = type
        }
    }
}