package com.crm.crmapp.marketPlan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewMarketPlanResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("MPid")
    @Expose
    private Integer mPid;
    @SerializedName("status")
    @Expose
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getMPid() {
        return mPid;
    }

    public void setMPid(Integer mPid) {
        this.mPid = mPid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
