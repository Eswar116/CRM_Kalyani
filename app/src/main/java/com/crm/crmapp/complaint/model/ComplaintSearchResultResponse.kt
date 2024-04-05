package com.crm.crmapp.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ComplaintSearchResultResponse {
    @SerializedName("complaint_id")
    @Expose
    private var complaintId: Int? = null
    @SerializedName("type_name")
    @Expose
    private var typeName: String? = null
    @SerializedName("complaint_date")
    @Expose
    private var complaintDate: String? = null
    @SerializedName("cust_name")
    @Expose
    private var custName: String? = null
    @SerializedName("priority_text")
    @Expose
    private var priorityText: String? = null

    fun getComplaintId(): Int? {
        return complaintId
    }

    fun setComplaintId(complaintId: Int?) {
        this.complaintId = complaintId
    }

    fun getTypeName(): String? {
        return typeName
    }

    fun setTypeName(typeName: String) {
        this.typeName = typeName
    }

    fun getComplaintDate(): String? {
        return complaintDate
    }

    fun setComplaintDate(complaintDate: String) {
        this.complaintDate = complaintDate
    }

    fun getCustName(): String? {
        return custName
    }

    fun setCustName(custName: String) {
        this.custName = custName
    }

    fun getPriorityText(): String? {
        return priorityText
    }

    fun setPriorityText(priorityText: String) {
        this.priorityText = priorityText
    }

}