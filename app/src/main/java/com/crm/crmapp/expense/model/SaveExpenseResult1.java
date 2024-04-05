
package com.crm.crmapp.expense.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveExpenseResult1 {

    @SerializedName("_exp_id")
    @Expose
    private Integer expId;
    @SerializedName("_user_id")
    @Expose
    private Integer userId;
    @SerializedName("_exp_type")
    @Expose
    private Integer expType;
    @SerializedName("_exp_date")
    @Expose
    private String expDate;
    @SerializedName("_exp_amount")
    @Expose
    private Integer expAmount;
    @SerializedName("_remarks")
    @Expose
    private String remarks;
    @SerializedName("_isactive")
    @Expose
    private Integer isactive;

    public Integer getExpId() {
        return expId;
    }

    public void setExpId(Integer expId) {
        this.expId = expId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getExpType() {
        return expType;
    }

    public void setExpType(Integer expType) {
        this.expType = expType;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public Integer getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(Integer expAmount) {
        this.expAmount = expAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

}
