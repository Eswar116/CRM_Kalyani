
package com.crm.crmapp.customer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderResult {

    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("main_cust_name")
    @Expose
    private String mainCustName;
    @SerializedName("cust_name")
    @Expose
    private String custName;
    @SerializedName("status_text")
    @Expose
    private String statusText;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getMainCustName() {
        return mainCustName;
    }

    public void setMainCustName(String mainCustName) {
        this.mainCustName = mainCustName;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

}
