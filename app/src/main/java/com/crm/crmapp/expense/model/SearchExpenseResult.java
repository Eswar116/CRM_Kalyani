
package com.crm.crmapp.expense.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchExpenseResult implements Serializable {

    @SerializedName("exp_type_id")
    @Expose
    private Integer expTypeId;
    @SerializedName("exp_amount")
    @Expose
    private Integer expAmount;
    @SerializedName("exp_name")
    @Expose
    private String expName;
    @SerializedName("approved_by")
    @Expose
    private String approvedBy;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("is_approved")
    @Expose
    private Boolean isApproved;
    @SerializedName("exp_date")
    @Expose
    private String expDate;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("approval_stage")
    @Expose
    private String approvalStage;
    @SerializedName("commentsbyapprover")
    @Expose
    private String commentsbyapprover;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public Integer getExpTypeId() {
        return expTypeId;
    }

    public void setExpTypeId(Integer expTypeId) {
        this.expTypeId = expTypeId;
    }

    public Integer getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(Integer expAmount) {
        this.expAmount = expAmount;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApprovalStage() {
        return approvalStage;
    }

    public void setApprovalStage(String approvalStage) {
        this.approvalStage = approvalStage;
    }

    public String getCommentsbyapprover() {
        return commentsbyapprover;
    }

    public void setCommentsbyapprover(String commentsbyapprover) {
        this.commentsbyapprover = commentsbyapprover;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
