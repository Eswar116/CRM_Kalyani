package com.crm.crmapp.customer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class FavrtSaveResponseModel {

    @SerializedName("favouriteidid")
    @Expose
    private var favouriteidid: Int? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

    fun getFavouriteidid(): Int? {
        return favouriteidid
    }

    fun setFavouriteidid(favouriteidid: Int?) {
        this.favouriteidid = favouriteidid
    }

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
}