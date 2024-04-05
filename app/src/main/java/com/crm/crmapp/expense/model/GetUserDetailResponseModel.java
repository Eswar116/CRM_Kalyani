
package com.crm.crmapp.expense.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserDetailResponseModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Result")
    @Expose
    private List<GetUserDetailResponseResult> result = null;

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

    public List<GetUserDetailResponseResult> getResult() {
        return result;
    }

    public void setResult(List<GetUserDetailResponseResult> result) {
        this.result = result;
    }

}
