package com.crm.crmapp.settings

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SettingsSyncRequestResponseModel {

    @SerializedName("cldId")
    @Expose
    public var cldId: String? = null

    @SerializedName("horgId")
    @Expose
    public var horgId: String? = null

    @SerializedName("slocId")
    @Expose
    public var slocId: String? = null

    @SerializedName("message")
    @Expose
    public var message: String? = null

    @SerializedName("status")
    @Expose
    public var status: Boolean? = null

}

