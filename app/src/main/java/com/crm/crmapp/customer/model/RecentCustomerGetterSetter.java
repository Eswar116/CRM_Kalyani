
package com.crm.crmapp.customer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecentCustomerGetterSetter {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Result")
    @Expose
    private List<CustomerResult> result = null;

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

    public List<CustomerResult> getResult() {
        return result;
    }

    public void setResult(List<CustomerResult> result) {
        this.result = result;
    }

}
