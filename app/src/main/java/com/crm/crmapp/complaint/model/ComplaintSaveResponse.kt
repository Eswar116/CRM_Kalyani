package com.crm.crmapp.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ComplaintSaveResponse {

    @SerializedName("msg")
    @Expose
    private var msg: String? = null
    @SerializedName("complaintid")
    @Expose
    private var complaintid: Int? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

    fun getMsg(): String? {
        return msg
    }

    fun setMsg(msg: String) {
        this.msg = msg
    }

    fun getComplaintid(): Int? {
        return complaintid
    }

    fun setComplaintid(complaintid: Int?) {
        this.complaintid = complaintid
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }
}