package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ImageRequestResponse {

    @SerializedName("msg")
    @Expose
    private var msg: String? = null
    @SerializedName("imgString")
    @Expose
    private var imgString: String? = null

    fun getMsg(): String? {
        return msg
    }

    fun setMsg(msg: String) {
        this.msg = msg
    }

    fun getImgString(): String? {
        return imgString
    }

    fun setImgString(imgString: String) {
        this.imgString = imgString
    }

}