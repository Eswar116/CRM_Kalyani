
package com.crm.crmapp.order.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDashboardResponseModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Result")
    @Expose
    private List<OrderDashboardResult> result = null;

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

    public List<OrderDashboardResult> getResult() {
        return result;
    }

    public void setResult(List<OrderDashboardResult> result) {
        this.result = result;
    }

}
