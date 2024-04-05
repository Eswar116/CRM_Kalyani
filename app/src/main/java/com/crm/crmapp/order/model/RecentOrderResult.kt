package com.crm.crmapp.order.model

import com.crm.crmapp.order.util.ConstantVariable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class RecentOrderResult {
    @SerializedName("order_date")
    @Expose
    private var orderDate: String? = null
    @SerializedName("main_cust_name")
    @Expose
    private var mainCustName: String? = null
    @SerializedName("cust_name")
    @Expose
    private var custName: String? = null
    @SerializedName("status_text")
    @Expose
    private var statusText: String? = null
    @SerializedName("order_id")
    @Expose
    private var orderId: Int? = null
    @SerializedName("erp_ref_id")
    @Expose
    private var erpRefId: String? = null
    @SerializedName("mkt_pln_id")
    @Expose
    private var mkt_pln_id: Int? = null
    @SerializedName("mkt_pln_name")
    @Expose
    private var mkt_pln_name: String? = null
    @SerializedName("no_of_boxes")
    @Expose
    private var no_of_boxes: String? = null

    fun setNoOfBoxes(no_of_boxes: String) {
        this.no_of_boxes = no_of_boxes
    }
    fun getNoOfBoxes(): String? {
        return no_of_boxes
    }

    fun setMarketPlanId(mkt_pln_id: Int) {
        this.mkt_pln_id = mkt_pln_id
    }
    fun getmarketPlanId(): Int? {
        return mkt_pln_id
    }

    fun setMarketPlanName(mkt_pln_name: String) {
        this.mkt_pln_name = mkt_pln_name
    }

    fun getMarketPlanName(): String? {
        return mkt_pln_name
    }

    fun setErpRefId(erpRefId: String) {
        this.erpRefId = erpRefId
    }

    fun getErpRefId(): String? {
        return erpRefId
    }


    fun getOrderDate(): String? {
        return orderDate
    }

    fun setOrderDate(orderDate: String) {
        this.orderDate = ConstantVariable.parseDateToddMMyyyy(orderDate)
    }

    fun getMainCustName(): String? {
        return mainCustName
    }

    fun setMainCustName(mainCustName: String) {
        this.mainCustName = mainCustName
    }

    fun getCustName(): String? {
        return custName
    }

    fun setCustName(custName: String) {
        this.custName = custName
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
}