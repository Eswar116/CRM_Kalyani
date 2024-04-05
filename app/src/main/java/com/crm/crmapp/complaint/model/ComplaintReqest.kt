package com.crm.crmapp.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ComplaintReqest {
    @SerializedName("userId")
    @Expose
    private var userId: Int? = null

    //todo change user id key for search main fragment api
    @SerializedName("user_id")
    @Expose
    private var user_id: Int? = null
    @SerializedName("offSet")
    @Expose
    private var offSet: Int? = null
    @SerializedName("NoRows")
    @Expose
    private var noRows: Int? = null
    @SerializedName("filterText")
    @Expose
    private var filterText: String? = null


    //todo change user id key for search main fragment api
    @SerializedName("searchKey")
    @Expose
    private var searchKey: String? = null

    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }

    fun getUser_Id(): Int? {
        return user_id
    }

    fun setUser_Id(user_id: Int?) {
        this.user_id = user_id
    }

    fun getOffSet(): Int? {
        return offSet
    }

    fun setOffSet(offSet: Int?) {
        this.offSet = offSet
    }

    fun getNoRows(): Int? {
        return noRows
    }

    fun setNoRows(noRows: Int?) {
        this.noRows = noRows
    }

    fun getFilterText(): String? {
        return filterText
    }

    fun setFilterText(filterText: String) {
        this.filterText = filterText
    }

    fun getSearchKey(): String? {
        return searchKey
    }

    fun setSearchKey(searchKey: String) {
        this.searchKey = searchKey
    }
}