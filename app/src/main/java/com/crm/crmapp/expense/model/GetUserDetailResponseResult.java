
package com.crm.crmapp.expense.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetUserDetailResponseResult {

    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("reports_to_fullname")
    @Expose
    private String reportsToFullname;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("l_name")
    @Expose
    private String lName;
    @SerializedName("usr_fullname")
    @Expose
    private String usr_fullname;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("reports_to_id")
    @Expose
    private Integer reportsToId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("f_name")
    @Expose
    private String fName;
    @SerializedName("reports_to_username")
    @Expose
    private String reportsToUsername;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("profile_pic_url")
    @Expose
    private String profilePicUrl;
    @SerializedName("user_password")
    @Expose
    private String user_password;
    @SerializedName("isactive")
    @Expose
    private String isactive;
    @SerializedName("alloted_states")
    @Expose
    private List<Result> allotedStates;
    @SerializedName("reports_to")
    @Expose
    private Integer reportsTo;

    public Integer getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(Integer reportsTo) {
        this.reportsTo = reportsTo;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public List<Result> getAllotedStates() {
        return allotedStates;
    }

    public void setAllotedStates(List<Result> allotedStates) {
        this.allotedStates = allotedStates;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getReportsToFullname() {
        return reportsToFullname;
    }

    public void setReportsToFullname(String reportsToFullname) {
        this.reportsToFullname = reportsToFullname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Integer getReportsToId() {
        return reportsToId;
    }

    public void setReportsToId(Integer reportsToId) {
        this.reportsToId = reportsToId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getReportsToUsername() {
        return reportsToUsername;
    }

    public void setReportsToUsername(String reportsToUsername) {
        this.reportsToUsername = reportsToUsername;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getUsr_fullname() {
        return usr_fullname;
    }

    public void setUsr_fullname(String usr_fullname) {
        this.usr_fullname = usr_fullname;
    }

    public static class Result implements Serializable {
        @SerializedName("state_code")
        @Expose
        public String state_code;
    }
}
