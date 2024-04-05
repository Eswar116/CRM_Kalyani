
package com.crm.crmapp.CRMDashboardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result4 {

    @SerializedName("atten_date")
    @Expose
    private String attenDate;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("punch_out_time")
    @Expose
    private String punchOutTime;
    @SerializedName("punch_in_time")
    @Expose
    private String punchInTime;

    public String getAttenDate() {
        return attenDate;
    }

    public void setAttenDate(String attenDate) {
        this.attenDate = attenDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPunchOutTime() {
        return punchOutTime;
    }

    public void setPunchOutTime(String punchOutTime) {
        this.punchOutTime = punchOutTime;
    }

    public String getPunchInTime() {
        return punchInTime;
    }

    public void setPunchInTime(String punchInTime) {
        this.punchInTime = punchInTime;
    }

}
