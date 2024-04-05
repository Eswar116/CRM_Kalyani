
package com.crm.crmapp.customer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveCustomerResult1 {

    @SerializedName("_user_id")
    @Expose
    private Integer userId;
    @SerializedName("_cust_id")
    @Expose
    private Integer custId;
    @SerializedName("_cust_name")
    @Expose
    private String custName;
    @SerializedName("_cust_type")
    @Expose
    private String custType;
    @SerializedName("_address")
    @Expose
    private String address;
    @SerializedName("_mobileno")
    @Expose
    private String mobileno;
    @SerializedName("_emailid")
    @Expose
    private String emailid;
    @SerializedName("_remarks")
    @Expose
    private String remarks;
    @SerializedName("_isactive")
    @Expose
    private Integer isactive;

    public Integer getGrp_cust_id() {
        return grp_cust_id;
    }

    public void setGrp_cust_id(Integer grp_cust_id) {
        this.grp_cust_id = grp_cust_id;
    }

    @SerializedName("_grp_cust_id")
    @Expose
    private Integer grp_cust_id;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

}
