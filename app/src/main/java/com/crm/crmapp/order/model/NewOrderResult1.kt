package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NewOrderResult1 {


    @SerializedName("_user_id")
    @Expose
    private var userId: Int? = null
    @SerializedName("_mkt_pln_id")
    @Expose
    private var _mkt_pln_id: Int? = null
    @SerializedName("_cust_id")
    @Expose
    private var custId: Int? = null
    @SerializedName("_order_id")
    @Expose
    private var _order_id: Int? = null

    @SerializedName("_order_date")
    @Expose
    private var orderDate: String? = null
    @SerializedName("_is_no_order")
    @Expose
    private var isNoOrder: Int? = null
    @SerializedName("_no_of_boxes")
    @Expose
    private var noOfBoxes: String? = null
    @SerializedName("_remarks")
    @Expose
    private var remarks: String? = null
    @SerializedName("_is_entered_erp")
    @Expose
    private var isEnteredErp: Int? = null

    @SerializedName("_isdeleted")
    @Expose
    private var _isdeleted: Int? = null

    @SerializedName("_status_text")
    @Expose
    private var statusText: String? = null

    @SerializedName("_lat")
    @Expose
    private var _lat: String? = null
    @SerializedName("_lon")
    @Expose
    private var _lon: String? = null

    fun getLatitude(): String? {
        return _lat
    }

    fun setLatitude(_lat: String) {
        this._lat = _lat
    }

    fun getLongitude(): String? {
        return _lon
    }

    fun setLongitude(_lon: String) {
        this._lon = _lon
    }

    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }

    fun getOrderId(): Int? {
        return _order_id
    }

    fun setOrderId(_order_id: Int?) {
        this._order_id = _order_id
    }


    fun getMarketPlanId(): Int? {
        return _mkt_pln_id
    }

    fun setMarketPlanId(_mkt_pln_id: Int?) {
        this._mkt_pln_id = _mkt_pln_id
    }

    fun getCustId(): Int? {
        return custId
    }

    fun setCustId(custId: Int?) {
        this.custId = custId
    }


    fun getIsDeleted(): Int? {
        return _isdeleted
    }

    fun setIsDeleted(_isdeleted: Int?) {
        this._isdeleted = _isdeleted
    }

    fun getOrderDate(): String? {
        return orderDate
    }

    fun setOrderDate(orderDate: String) {
        this.orderDate = orderDate
    }

    fun getIsNoOrder(): Int? {
        return isNoOrder
    }

    fun setIsNoOrder(isNoOrder: Int?) {
        this.isNoOrder = isNoOrder
    }

    fun getNoOfBoxes(): String? {
        return noOfBoxes
    }

    fun setNoOfBoxes(noOfBoxes: String) {
        this.noOfBoxes = noOfBoxes
    }

    fun getRemarks(): String? {
        return remarks
    }

    fun setRemarks(remarks: String) {
        this.remarks = remarks
    }

    fun getIsEnteredErp(): Int? {
        return isEnteredErp
    }

    fun setIsEnteredErp(isEnteredErp: Int?) {
        this.isEnteredErp = isEnteredErp
    }

    fun getStatusText(): String? {
        return statusText
    }

    fun setStatusText(statusText: String) {
        this.statusText = statusText
    }
}