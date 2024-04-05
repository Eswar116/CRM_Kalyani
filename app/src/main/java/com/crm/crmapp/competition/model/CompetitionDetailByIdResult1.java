
package com.crm.crmapp.competition.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionDetailByIdResult1 {

    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("comp_date")
    @Expose
    private String compDate;
    @SerializedName("cust_name")
    @Expose
    private String custName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_entered")
    @Expose
    private Boolean isEntered;
    @SerializedName("cust_id")
    @Expose
    private Integer custId;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("competitor_name")
    @Expose
    private String competitorName;

    public String getCompetitorName() {
        return competitorName;
    }

    public void setCompetitorName(String competitorName) {
        this.competitorName = competitorName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompDate() {
        return compDate;
    }

    public void setCompDate(String compDate) {
        this.compDate = compDate;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsEntered() {
        return isEntered;
    }

    public void setIsEntered(Boolean isEntered) {
        this.isEntered = isEntered;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
