
package com.crm.crmapp.customer.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetailByIDModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Result_1")
    @Expose
    private List<CustomerDetailByIDResult> result = null;
    @SerializedName("Result_2")
    @Expose
    private List<CustomerDetailByIDResult2> result2 = null;

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

    public List<CustomerDetailByIDResult> getResult() {
        return result;
    }

    public void setResult(List<CustomerDetailByIDResult> result) {
        this.result = result;
    }

    public List<CustomerDetailByIDResult2> getResult2() {
        return result2;
    }

    public void setResult2(List<CustomerDetailByIDResult2> result) {
        this.result2 = result2;
    }

}
