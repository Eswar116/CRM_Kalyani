
package com.crm.crmapp.competition.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CompetitionListResult implements Serializable {

    @SerializedName("competitor_name")
    @Expose
    private String competitorName;
    @SerializedName("c_date")
    @Expose
    private String cDate;
    @SerializedName("cust_name")
    @Expose
    private String custName;
    @SerializedName("id")
    @Expose
    private Integer id;

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

}
