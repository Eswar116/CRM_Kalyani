
package com.crm.crmapp.competition.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveCompetitorResult1 {

    @SerializedName("_comp_id")
    @Expose
    private Integer compId;
    @SerializedName("_user_id")
    @Expose
    private Integer userId;
    @SerializedName("_cust_id")
    @Expose
    private Integer custId;
    @SerializedName("_competitor_name")
    @Expose
    private String competitorName;
    @SerializedName("_c_date")
    @Expose
    private String cDate;
    @SerializedName("_remarks")
    @Expose
    private String remarks;
    @SerializedName("_is_active")
    @Expose
    private Integer isActive;

    public Integer getCompId() {
        return compId;
    }

    public void setCompId(Integer compId) {
        this.compId = compId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getCompetitorName() {
        return competitorName;
    }

    public void setCompetitorName(String competitorName) {
        this.competitorName = competitorName;
    }

    public String getCDate() {
        return cDate;
    }

    public void setCDate(String cDate) {
        this.cDate = cDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

}
