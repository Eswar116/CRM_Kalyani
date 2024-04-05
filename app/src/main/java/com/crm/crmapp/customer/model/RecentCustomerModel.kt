package com.crm.crmapp.customer.model

class RecentCustomerModel() {

    private var name = ""

    fun getName(): String {
        return name
    }
    fun setName(name: String) {
        this.name = name
    }

    private var userId: Int = 0

    fun getUserId(): Int {
        return userId
    }
    fun setUserId(userId: Int) {
        this.userId = userId
    }

    fun getCustId(): Int {
        return custId
    }

    fun setCustId(custId: Int) {
        this.custId = custId
    }

    private var custId: Int = 0
}