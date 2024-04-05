
package com.crm.crmapp.CRMDashboardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result2 {

    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("amt")
    @Expose
    private Integer amt;
    @SerializedName("exp_date")
    @Expose
    private String expDate;

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Integer getAmt() {
        return amt;
    }

    public void setAmt(Integer amt) {
        this.amt = amt;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

}
