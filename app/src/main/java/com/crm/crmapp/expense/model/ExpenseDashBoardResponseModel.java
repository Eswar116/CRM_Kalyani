
package com.crm.crmapp.expense.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseDashBoardResponseModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Result_1")
    @Expose
    private List<ExpenseDashBoardResult> result = null;


    public List<Result2> getResult2() {
        return result2;
    }

    public void setResult2(List<Result2> result2) {
        this.result2 = result2;
    }

    @SerializedName("Result_2")
    @Expose
    private List<Result2> result2 = null;

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

    public List<ExpenseDashBoardResult> getResult() {
        return result;
    }

    public void setResult(List<ExpenseDashBoardResult> result) {
        this.result = result;
    }

}
