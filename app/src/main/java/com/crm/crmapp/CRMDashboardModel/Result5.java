
package com.crm.crmapp.CRMDashboardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result5 {

    @SerializedName("is_new_entry")
    @Expose
    private Boolean isNewEntry;
    @SerializedName("cust_type")
    @Expose
    private String custType;
    @SerializedName("cust_name")
    @Expose
    private String custName;
    @SerializedName("grp_cust_name")
    @Expose
    private String grpcustname;
    @SerializedName("emailid")
    @Expose
    private String emailid;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("cust_id")
    @Expose
    private Integer custId;

    public Boolean getIsNewEntry() {
        return isNewEntry;
    }

    public void setIsNewEntry(Boolean isNewEntry) {
        this.isNewEntry = isNewEntry;
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

    public String getGrpcustname() {
        return grpcustname;
    }

    public void setGrpcustname(String grpcustname) {
        this.grpcustname = grpcustname;
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
