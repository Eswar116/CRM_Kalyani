
package com.crm.crmapp.customer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetailByIDResult {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("is_new_entry")
    @Expose
    private Boolean isNewEntry;
    @SerializedName("grpcustname")
    @Expose
    private String grpcustname;
    @SerializedName("emailid")
    @Expose
    private String emailid;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("addr_state")
    @Expose
    private String addrState;
    @SerializedName("cust_type")
    @Expose
    private String custType;
    @SerializedName("cust_name")
    @Expose
    private String custName;
    @SerializedName("cust_id")
    @Expose
    private Integer custId;
    @SerializedName("addr_city")
    @Expose
    private String addrCity;
    @SerializedName("addr_country")
    @Expose
    private String addrCountry;
    @SerializedName("addr_pin")
    @Expose
    private String addrPin;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("group_cust_id")
    @Expose
    private Integer group_cust_id;


    public Integer getFav() {
        return fav;
    }

    public void setFav(Integer fav) {
        this.fav = fav;
    }

    @SerializedName("fav")
    @Expose
    private Integer fav;

    public Integer getGroup_cust_id() {
        return group_cust_id;
    }

    public void setGroup_cust_id(Integer group_cust_id) {
        this.group_cust_id = group_cust_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsNewEntry() {
        return isNewEntry;
    }

    public void setIsNewEntry(Boolean isNewEntry) {
        this.isNewEntry = isNewEntry;
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

    public String getAddrState() {
        return addrState;
    }

    public void setAddrState(String addrState) {
        this.addrState = addrState;
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

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public void setAddrCity(String addrCity) {
        this.addrCity = addrCity;
    }

    public String getAddrCountry() {
        return addrCountry;
    }

    public void setAddrCountry(String addrCountry) {
        this.addrCountry = addrCountry;
    }

    public String getAddrPin() {
        return addrPin;
    }

    public void setAddrPin(String addrPin) {
        this.addrPin = addrPin;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
