package com.crm.crmapp.marketPlan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ResponsiblePersoneResponse :Serializable{
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("Result")
    @Expose
    private var result: List<Result>? = null

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun getResult(): List<Result>? {
        return result
    }

    fun setResult(result: List<Result>) {
        this.result = result
    }


    class Result :Serializable {
        @SerializedName("email_id")
        @Expose
        private var emailId: String? = null
        @SerializedName("reports_to_fullname")
        @Expose
        private var reportsToFullname: String? = null
        @SerializedName("user_name")
        @Expose
        private var userName: String? = null
        @SerializedName("l_name")
        @Expose
        private var lName: String? = null
        @SerializedName("mobile_no")
        @Expose
        private var mobileNo: String? = null
        @SerializedName("reports_to_id")
        @Expose
        private var reportsToId: Int? = null
        @SerializedName("user_type")
        @Expose
        private var userType: String? = null
        @SerializedName("user_id")
        @Expose
        private var userId: Int? = null
        @SerializedName("f_name")
        @Expose
        private var fName: String? = null
        @SerializedName("reports_to_username")
        @Expose
        private var reportsToUsername: String? = null
        @SerializedName("category")
        @Expose
        private var category: String? = null
        @SerializedName("department")
        @Expose
        private var department: String? = null
        @SerializedName("profile_pic_url")
        @Expose
        private var profilePicUrl: String? = null

        fun getEmailId(): String? {
            return emailId
        }

        fun setEmailId(emailId: String) {
            this.emailId = emailId
        }

        fun getReportsToFullname(): String? {
            return reportsToFullname
        }

        fun setReportsToFullname(reportsToFullname: String) {
            this.reportsToFullname = reportsToFullname
        }

        fun getUserName(): String? {
            return userName
        }

        fun setUserName(userName: String) {
            this.userName = userName
        }

        fun getLName(): String? {
            return lName
        }

        fun setLName(lName: String) {
            this.lName = lName
        }

        fun getMobileNo(): String? {
            return mobileNo
        }

        fun setMobileNo(mobileNo: String) {
            this.mobileNo = mobileNo
        }

        fun getReportsToId(): Int? {
            return reportsToId
        }

        fun setReportsToId(reportsToId: Int?) {
            this.reportsToId = reportsToId
        }

        fun getUserType(): String? {
            return userType
        }

        fun setUserType(userType: String) {
            this.userType = userType
        }

        fun getUserId(): Int? {
            return userId
        }

        fun setUserId(userId: Int?) {
            this.userId = userId
        }

        fun getFName(): String? {
            return fName
        }

        fun setFName(fName: String) {
            this.fName = fName
        }

        fun getReportsToUsername(): String? {
            return reportsToUsername
        }

        fun setReportsToUsername(reportsToUsername: String) {
            this.reportsToUsername = reportsToUsername
        }

        fun getCategory(): String? {
            return category
        }

        fun setCategory(category: String) {
            this.category = category
        }

        fun getDepartment(): String? {
            return department
        }

        fun setDepartment(department: String) {
            this.department = department
        }

        fun getProfilePicUrl(): String? {
            return profilePicUrl
        }

        fun setProfilePicUrl(profilePicUrl: String) {
            this.profilePicUrl = profilePicUrl
        }
    }
}