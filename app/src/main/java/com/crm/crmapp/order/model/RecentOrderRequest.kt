package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class RecentOrderRequest {

    @SerializedName("userId")
    @Expose
    private var userId: Int? = null


    //todo mobile validation request
    @SerializedName("mobNo")
    @Expose
    private var mobNo: String? = null

    @SerializedName("search")
    @Expose
    private var search: String? = null

    fun getSearch(): String? {
        return search
    }

    fun setSearch(search: String?) {
        this.search = search
    }

    fun getMobile(): String? {
        return mobNo
    }

    fun setMobile(mobNo: String?) {
        this.mobNo = mobNo
    }





    // todo used in notification status api as request
    @SerializedName("noti_id")
    @Expose
    private var noti_id: Int? = null

    fun getNoti(): Int? {
        return noti_id
    }

    fun setNoti(noti_id: Int?) {
        this.noti_id = noti_id
    }




    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }
}