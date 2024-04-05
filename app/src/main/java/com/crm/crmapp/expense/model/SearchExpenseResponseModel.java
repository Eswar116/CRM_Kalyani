
package com.crm.crmapp.expense.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchExpenseResponseModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Result")
    @Expose
    private List<SearchExpenseResult> result1 = null;
    @SerializedName("Result2")
    @Expose
    private List<Result2> Result2 = null;

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

    public List<SearchExpenseResult> getResult() {
        return result1;
    }

    public void setResult(List<SearchExpenseResult> result1) {
        this.result1 = result1;
    }


    public List<Result2> getResult2() {
        return Result2;
    }

    public void setResult2(List<Result2> Result2) {
        this.Result2 = Result2;
    }

}
