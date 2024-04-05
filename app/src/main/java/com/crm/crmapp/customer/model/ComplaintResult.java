
package com.crm.crmapp.customer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplaintResult {

    @SerializedName("complaint_id")
    @Expose
    private Integer complaintId;
    @SerializedName("type_name")
    @Expose
    private String typeName;
    @SerializedName("complaint_date")
    @Expose
    private String complaintDate;
    @SerializedName("cust_name")
    @Expose
    private String custName;
    @SerializedName("priority_text")
    @Expose
    private String priorityText;

    public Integer getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Integer complaintId) {
        this.complaintId = complaintId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getPriorityText() {
        return priorityText;
    }

    public void setPriorityText(String priorityText) {
        this.priorityText = priorityText;
    }

}
