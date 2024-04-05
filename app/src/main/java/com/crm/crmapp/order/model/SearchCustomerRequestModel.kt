package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SearchCustomerRequestModel {
    @SerializedName("searchKey")
    @Expose
    private var searchKey: String? = null
    @SerializedName("offSet")
    @Expose
    private var offSet: Int? = null
    @SerializedName("NoRows")
    @Expose
    private var noRows: Int? = null
    @SerializedName("UserID")
    @Expose
    private var UserID: Int? = null
    @SerializedName("MPId")
    @Expose
    private var MPId: Int? = null
    @SerializedName("StateCode")
    @Expose
    private var stateCode: String? = null

    fun getSearchKey(): String? {
        return searchKey
    }

    fun setSearchKey(searchKey: String) {
        this.searchKey = searchKey
    }

    fun getOffSet(): Int? {
        return offSet
    }

    fun setOffSet(offSet: Int) {
        this.offSet = offSet
    }

    fun getNoRows(): Int? {
        return noRows
    }

    fun setNoRows(noRows: Int) {
        this.noRows = noRows
    }

    fun getUserId(): Int? {
        return UserID
    }

    fun setUserId(UserId: Int) {
        this.UserID = UserId
    }

    fun getMPId(): Int? {
        return MPId
    }

    fun setMPId(MPId: Int) {
        this.MPId = MPId
    }

    fun getStateCode(): String? {
        return stateCode
    }

    fun setStateCode(stateCode: String) {
        this.stateCode = stateCode
    }


}