package com.crm.crmapp.newuser

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserResult :Serializable {
    @SerializedName("user_id")
    @Expose
    private var userId: Int? = null

    @SerializedName("user_full_name")
    @Expose
    private var userFullName: String? = null

    @SerializedName("isactive")
    @Expose
    private var isactive: Boolean? = null

    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }

    fun getUserFullName(): String? {
        return userFullName
    }

    fun setUserFullName(userFullName: String?) {
        this.userFullName = userFullName
    }

    fun getIsactive(): Boolean? {
        return isactive
    }

    fun setIsactive(isactive: Boolean?) {
        this.isactive = isactive
    }

}