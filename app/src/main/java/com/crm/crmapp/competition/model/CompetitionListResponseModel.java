
package com.crm.crmapp.competition.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionListResponseModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Result")
    @Expose
    private List<CompetitionListResult> result = null;

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

    public List<CompetitionListResult> getResult() {
        return result;
    }

    public void setResult(List<CompetitionListResult> result) {
        this.result = result;
    }

}
