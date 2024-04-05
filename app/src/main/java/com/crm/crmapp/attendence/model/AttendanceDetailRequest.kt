package com.crm.crmapp.attendence.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class AttendanceDetailRequest {

    @SerializedName("_user_id")
    @Expose
    private var userId: Int? = null
    @SerializedName("_atten_date")
    @Expose
    private var attenDate: String? = null

    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }

    fun getAttenDate(): String? {
        return attenDate
    }

    fun setAttenDate(attenDate: String) {
        this.attenDate = attenDate
    }

}