package com.crm.crmapp.registration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class LoginRequest() {



    @SerializedName("_user_name")
    @Expose
    private var userName: String? = null
    @SerializedName("_user_password")
    @Expose
    private var userPassword: String? = null
    @SerializedName("_device_desc")
    @Expose
    private var deviceDesc: String? = null

    @SerializedName("_device_key")
    @Expose
     var _device_key: String? = null

    @SerializedName("_is_remember")
    @Expose
    private var isRemember: Int? = null

    fun getUserName(): String? {
        return userName
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

  fun getDeviceKey(): String? {
        return _device_key
    }

    fun setDeviceKey(_device_key: String) {
        this._device_key = _device_key
    }

    fun getUserPassword(): String? {
        return userPassword
    }

    fun setUserPassword(userPassword: String) {
        this.userPassword = userPassword
    }

    fun getDeviceDesc(): String? {
        return deviceDesc
    }

    fun setDeviceDesc(deviceDesc: String) {
        this.deviceDesc = deviceDesc
    }

    fun getIsRemember(): Int? {
        return isRemember
    }

    fun setIsRemember(isRemember: Int?) {
        this.isRemember = isRemember
    }

}
