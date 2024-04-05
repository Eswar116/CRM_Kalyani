
package com.crm.crmapp.expense.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseDetailByIDModel {

    @SerializedName("Result_2")
    @Expose
    private List<ExpenseDetailByIDResult2> result2 = null;
    @SerializedName("Result_1")
    @Expose
    private List<ExpenseDetailByIDResult1> result1 = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<ExpenseDetailByIDResult2> getResult2() {
        return result2;
    }

    public void setResult2(List<ExpenseDetailByIDResult2> result2) {
        this.result2 = result2;
    }

    public List<ExpenseDetailByIDResult1> getResult1() {
        return result1;
    }

    public void setResult1(List<ExpenseDetailByIDResult1> result1) {
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
