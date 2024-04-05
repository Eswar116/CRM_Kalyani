package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class OrderListRequest() {

    @SerializedName("userId")
    @Expose
    private var userId: Int? = null
    @SerializedName("columnName")
    @Expose
    private var columnName: String? = null
    @SerializedName("orderByType")
    @Expose
    private var orderByType: String? = null
    @SerializedName("filterText")
    @Expose
    private var filterText: String? = null
    @SerializedName("offSet")
    @Expose
    private var offSet: Int? = null
    @SerializedName("NoRows")
    @Expose
    private var noRows: Int? = null


    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }

    fun getColumnName(): String? {
        return columnName
    }

    fun setColumnName(columnName: String) {
        this.columnName = columnName
    }

    fun getOrderByType(): String? {
        return orderByType
    }

    fun setOrderByType(orderByType: String) {
        this.orderByType = orderByType
    }

    fun getFilterText(): String? {
        return filterText
    }

    fun setFilterText(filterText: String) {
        this.filterText = filterText
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
}