
package com.crm.crmapp.competition.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionDetailByIdResponseModel {

    @SerializedName("Result_2")
    @Expose
    private List<CompetitionDetailByIdResult2> result2 = null;
    @SerializedName("Result_1")
    @Expose
    private List<CompetitionDetailByIdResult1> result1 = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<CompetitionDetailByIdResult2> getResult2() {
        return result2;
    }

    public void setResult2(List<CompetitionDetailByIdResult2> result2) {
        this.result2 = result2;
    }

    public List<CompetitionDetailByIdResult1> getResult1() {
        return result1;
    }

    public void setResult1(List<CompetitionDetailByIdResult1> result1) {
        this.result1 = result1;
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
