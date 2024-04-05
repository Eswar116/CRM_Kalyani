
package com.crm.crmapp.customer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerResult implements Serializable {

    @SerializedName("grp_cust_name")
    @Expose
    private String grpCustName;
    @SerializedName("cust_type")
    @Expose
    private String custType;
    @SerializedName("cust_name")
    @Expose
    private String custName;
    @SerializedName("emailid")
    @Expose
    private String emailid;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("cust_id")
    @Expose
    private Integer custId;
    @SerializedName("is_new_entry")
    @Expose
    private boolean is_new_entry;

    public boolean isIs_new_entry() {
        return is_new_entry;
    }

    public CustomerResult setIs_new_entry(boolean is_new_entry) {
        this.is_new_entry = is_new_entry;
        return this;
    }

    public String getGrpCustName() {
        return grpCustName;
    }

    public void setGrpCustName(String grpCustName) {
        this.grpCustName = grpCustName;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

}
