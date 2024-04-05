
package com.crm.crmapp.expense.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateExpenseStateResponseModel {

    @SerializedName("expenseid")
    @Expose
    private Integer expenseid;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getExpenseid() {
        return expenseid;
    }

    public void setExpenseid(Integer expenseid) {
        this.expenseid = expenseid;
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
