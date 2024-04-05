
package com.crm.crmapp.expense.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateExpenseStateRequestResult {

    @SerializedName("exp_id")
    @Expose
    private Integer expId;
    @SerializedName("frm_user_id")
    @Expose
    private Integer frmUserId;
    @SerializedName("manager_user_id")
    @Expose
    private Integer managerUserId;
    @SerializedName("approval_stage")
    @Expose
    private String approvalStage;
    @SerializedName("commentsbyApprover")
    @Expose
    private String commentsbyApprover;

    public Integer getExpId() {
        return expId;
    }

    public void setExpId(Integer expId) {
        this.expId = expId;
    }

    public Integer getFrmUserId() {
        return frmUserId;
    }

    public void setFrmUserId(Integer frmUserId) {
        this.frmUserId = frmUserId;
    }

    public Integer getManagerUserId() {
        return managerUserId;
    }

    public void setManagerUserId(Integer managerUserId) {
        this.managerUserId = managerUserId;
    }

    public String getApprovalStage() {
        return approvalStage;
    }

    public void setApprovalStage(String approvalStage) {
        this.approvalStage = approvalStage;
    }

    public String getCommentsbyApprover() {
        return commentsbyApprover;
    }

    public void setCommentsbyApprover(String commentsbyApprover) {
        this.commentsbyApprover = commentsbyApprover;
    }

}
