package com.crm.crmapp.order.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ImageRequest {

    @SerializedName("imgPath")
    @Expose
    private var imgPath: String? = null

    fun getImgPath(): String? {
        return imgPath
    }

    fun setImgPath(imgPath: String) {
        this.imgPath = imgPath
    }

}