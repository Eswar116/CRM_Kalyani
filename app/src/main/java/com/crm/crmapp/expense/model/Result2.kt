package com.crm.crmapp.expense.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Result2 {

    @SerializedName("cnt")
    @Expose
    private var cnt: Int? = null
    @SerializedName("amt")
    @Expose
    private var amt: Double? = null
    @SerializedName("exp_date")
    @Expose
    private var expDate: String? = null

    fun getCnt(): Int? {
        return cnt
    }

    fun setCnt(cnt: Int?) {
        this.cnt = cnt
    }

    fun getAmt(): Double? {
        return amt
    }

    fun setAmt(amt: Double?) {
        this.amt = amt
    }

    fun getExpDate(): String? {
        return expDate
    }

    fun setExpDate(expDate: String) {
        this.expDate = expDate
    }
}