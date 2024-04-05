
package com.crm.crmapp.CRMDashboardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CRMDashboardResponseModel {

    @SerializedName("Result_6")
    @Expose
    private List<Result4> result4 = null;
    @SerializedName("Result_7")
    @Expose
    private List<Result7> result7 = null;
    @SerializedName("Result_4")
    @Expose
    private List<Result5> result5 = null;
    @SerializedName("Result_2")
    @Expose
    private List<Result2> result2 = null;
    @SerializedName("Result_3")
    @Expose
    private List<Result3> result3 = null;
    @SerializedName("Result_1")
    @Expose
    private List<Result1> result1 = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Result4> getResult4() {
        return result4;
    }

    public void setResult4(List<Result4> result4) {
        this.result4 = result4;
    }

    public List<Result5> getResult5() {
        return result5;
    }

    public void setResult5(List<Result5> result5) {
        this.result5 = result5;
    }

    public List<Result2> getResult2() {
        return result2;
    }

    public void setResult2(List<com.crm.crmapp.CRMDashboardModel.Result2> result2) {
        this.result2 = result2;
    }

    public List<Result3> getResult3() {
        return result3;
    }

    public void setResult3(List<Result3> result3) {
        this.result3 = result3;
    }

    public List<Result1> getResult1() {
        return result1;
    }

    public void setResult1(List<Result1> result1) {
        this.result1 = result1;
    }

    public List<Result7> getResult7() {
        return result7;
    }

    public void setResult7(List<Result7> result7) {
        this.result7 = result7;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
