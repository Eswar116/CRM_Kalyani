
package com.crm.crmapp.order.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDashboardResult {

    @SerializedName("processed_cnt")
    @Expose
    private Integer processedCnt;
    @SerializedName("processed_box")
    @Expose
    private Integer processedBox;
    @SerializedName("rejected_cnt")
    @Expose
    private Integer rejectedCnt;
    @SerializedName("rejected_box")
    @Expose
    private Integer rejectedBox;
    @SerializedName("tot_box")
    @Expose
    private Integer totBox;
    @SerializedName("new_cnt")
    @Expose
    private Integer newCnt;
    @SerializedName("new_box")
    @Expose
    private Integer newBox;
    @SerializedName("tot_cnt")
    @Expose
    private Integer totCnt;

    public Integer getProcessedCnt() {
        return processedCnt;
    }

    public void setProcessedCnt(Integer processedCnt) {
        this.processedCnt = processedCnt;
    }

    public Integer getProcessedBox() {
        return processedBox;
    }

    public void setProcessedBox(Integer processedBox) {
        this.processedBox = processedBox;
    }

    public Integer getRejectedCnt() {
        return rejectedCnt;
    }

    public void setRejectedCnt(Integer rejectedCnt) {
        this.rejectedCnt = rejectedCnt;
    }

    public Integer getRejectedBox() {
        return rejectedBox;
    }

    public void setRejectedBox(Integer rejectedBox) {
        this.rejectedBox = rejectedBox;
    }

    public Integer getTotBox() {
        return totBox;
    }

    public void setTotBox(Integer totBox) {
        this.totBox = totBox;
    }

    public Integer getNewCnt() {
        return newCnt;
    }

    public void setNewCnt(Integer newCnt) {
        this.newCnt = newCnt;
    }

    public Integer getNewBox() {
        return newBox;
    }

    public void setNewBox(Integer newBox) {
        this.newBox = newBox;
    }

    public Integer getTotCnt() {
        return totCnt;
    }

    public void setTotCnt(Integer totCnt) {
        this.totCnt = totCnt;
    }

}
