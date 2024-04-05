package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class OrderDetailResult1{
    @SerializedName("order_date")
    @Expose
    private var orderDate: String? = null
    @SerializedName("user_id")
    @Expose
    private var userId: Int? = null
    @SerializedName("user_name")
    @Expose
    private var userName: String? = null
    @SerializedName("is_no_order")
    @Expose
    private var isNoOrder: Boolean? = null
    @SerializedName("is_entered_erp")
    @Expose
    private var isEnteredErp: Boolean? = null
    @SerializedName("cust_name")
    @Expose
    private var custName: String? = null
    @SerializedName("mkt_pln_name")
    @Expose
    private var mkt_pln_name: String? = null
    @SerializedName("no_of_boxes")
    @Expose
    private var noOfBoxes: Int? = null
    @SerializedName("status_text")
    @Expose
    private var statusText: String? = null
    @SerializedName("order_id")
    @Expose
    private var orderId: Int? = null
    @SerializedName("cust_id")
    @Expose
    private var custId: Int? = null
    @SerializedName("mkt_pln_id")
    @Expose
    private var mkt_pln_id: Int? = null
    @SerializedName("erp_ref_id")
    @Expose
    private var erpRefId: String? = null
    @SerializedName("remarks")
    @Expose
    private var remarks: String? = null

    fun getOrderDate(): String? {
        return orderDate
    }

    fun setOrderDate(orderDate: String) {
        this.orderDate = orderDate
    }

    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }

    fun getUserName(): String? {
        return userName
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun getIsNoOrder(): Boolean? {
        return isNoOrder
    }

    fun setIsNoOrder(isNoOrder: Boolean?) {
        this.isNoOrder = isNoOrder
    }

    fun getIsEnteredErp(): Boolean? {
        return isEnteredErp
    }

    fun setIsEnteredErp(isEnteredErp: Boolean?) {
        this.isEnteredErp = isEnteredErp
    }

    fun getCustName(): String? {
        return custName
    }

    fun setCustName(custName: String) {
        this.custName = custName
    }


    fun getNoOfBoxes(): Int? {
        return noOfBoxes
    }

    fun setNoOfBoxes(noOfBoxes: Int?) {
        this.noOfBoxes = noOfBoxes
    }

    fun getStatusText(): String? {
        return statusText
    }

    fun setStatusText(statusText: String) {
        this.statusText = statusText
    }

    fun getOrderId(): Int? {
        return orderId
    }

    fun setOrderId(orderId: Int?) {
        this.orderId = orderId
    }

    fun getCustId(): Int? {
        return custId
    }

    fun setCustId(custId: Int?) {
        this.custId = custId
    }

    fun getMarketPlanId(): Int? {
        return mkt_pln_id
    }

    fun setMarketPlanId(mkt_pln_id: Int?) {
        this.mkt_pln_id = mkt_pln_id
    }

    fun getMarketPlanName(): String? {
        return mkt_pln_name
    }

    fun setMarketPlanName(mkt_pln_name: String?) {
        this.mkt_pln_name = mkt_pln_name
    }

    fun getErpRefId(): String? {
        return erpRefId
    }

    fun setErpRefId(erpRefId: String) {
        this.erpRefId = erpRefId
    }

    fun getRemarks(): String? {
        return remarks
    }

    fun setRemarks(remarks: String) {
        this.remarks = remarks
    }

}