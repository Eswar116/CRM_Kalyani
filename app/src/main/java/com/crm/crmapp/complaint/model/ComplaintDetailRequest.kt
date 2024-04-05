package com.crm.crmapp.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ComplaintDetailRequest {
    @SerializedName("compid")
    @Expose
    private var compid: Int? = null

    fun getCompid(): Int? {
        return compid
    }

    fun setCompid(compid: Int?) {
        this.compid = compid
    }
}