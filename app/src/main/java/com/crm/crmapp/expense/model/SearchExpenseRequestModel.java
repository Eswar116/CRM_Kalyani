
package com.crm.crmapp.expense.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchExpenseRequestModel {

    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("offSet")
    @Expose
    private Integer offSet;
    @SerializedName("NoRows")
    @Expose
    private Integer noRows;
    @SerializedName("filterText")
    @Expose
    private String filterText;
    @SerializedName("expid")
    @Expose
    private Integer expid;
    @SerializedName("Days")
    @Expose
    private Integer Days;

    public Integer getDays() {
        return Days;
    }

    public void setDays(Integer days) {
        Days = days;
    }

    public Integer getExpid() {
        return expid;
    }

    public void setExpid(Integer expid) {
        this.expid = expid;
    }

    public Integer getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(Integer user_Id) {
        this.user_Id = user_Id;
    }

    @SerializedName("user_Id")
    @Expose
    private Integer user_Id;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOffSet() {
        return offSet;
    }

    public void setOffSet(Integer offSet) {
        this.offSet = offSet;
    }

    public Integer getNoRows() {
        return noRows;
    }

    public void setNoRows(Integer noRows) {
        this.noRows = noRows;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

}
