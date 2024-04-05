
package com.crm.crmapp.expense.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseDashBoardResult {

    @SerializedName("cnt_pending")
    @Expose
    private Integer cntPending;
    @SerializedName("sum_pending")
    @Expose
    private Integer sumPending;
    @SerializedName("cnt_approved")
    @Expose
    private Integer cntApproved;
    @SerializedName("sum_approved")
    @Expose
    private Integer sumApproved;
    @SerializedName("sum_rejected")
    @Expose
    private Integer sumRejected;
    @SerializedName("cnt_rejected")
    @Expose
    private Integer cntRejected;
    @SerializedName("cnt_tot")
    @Expose
    private Integer cntTot;
    @SerializedName("amt_tot")
    @Expose
    private Integer amtTot;

    public Integer getCntPending() {
        return cntPending;
    }

    public void setCntPending(Integer cntPending) {
        this.cntPending = cntPending;
    }

    public Integer getSumPending() {
        return sumPending;
    }

    public void setSumPending(Integer sumPending) {
        this.sumPending = sumPending;
    }

    public Integer getCntApproved() {
        return cntApproved;
    }

    public void setCntApproved(Integer cntApproved) {
        this.cntApproved = cntApproved;
    }

    public Integer getSumApproved() {
        return sumApproved;
    }

    public void setSumApproved(Integer sumApproved) {
        this.sumApproved = sumApproved;
    }

    public Integer getSumRejected() {
        return sumRejected;
    }

    public void setSumRejected(Integer sumRejected) {
        this.sumRejected = sumRejected;
    }

    public Integer getCntRejected() {
        return cntRejected;
    }

    public void setCntRejected(Integer cntRejected) {
        this.cntRejected = cntRejected;
    }

    public Integer getCntTot() {
        return cntTot;
    }

    public void setCntTot(Integer cntTot) {
        this.cntTot = cntTot;
    }

    public Integer getAmtTot() {
        return amtTot;
    }

    public void setAmtTot(Integer amtTot) {
        this.amtTot = amtTot;
    }

}
