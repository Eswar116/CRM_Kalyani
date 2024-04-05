
package com.crm.crmapp.order.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchCustomerResultModel implements Serializable
{

    @SerializedName("grp_cust_name")
    @Expose
    private String grpCustName;
    @SerializedName("is_new_entry")
    @Expose
    private Boolean isNewEntry;
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
    private final static long serialVersionUID = 9095644962799554748L;

    public String getGrpCustName() {
        return grpCustName;
    }

    public void setGrpCustName(String grpCustName) {
        this.grpCustName = grpCustName;
    }

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
