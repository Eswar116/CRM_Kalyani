package com.crm.crmapp.attendence.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Result {

    @SerializedName("user_id")
    @Expose
    private var userId: Int? = null
    @SerializedName("atten_date")
    @Expose
    private var attenDate: String? = null
    @SerializedName("punch_in_time")
    @Expose
    private var punchInTime: String? = null
    @SerializedName("punch_out_time")
    @Expose
    private var punchOutTime: String? = null
    @SerializedName("state")
    @Expose
    private var state: String? = null
    @SerializedName("remarks")
    @Expose
    private var remarks: String? = null
    @SerializedName("geo_location_in_lat")
    @Expose
    private var geoLocationInLat: String? = null
    @SerializedName("geo_location_in_lon")
    @Expose
    private var geoLocationInLon: String? = null
    @SerializedName("geo_location_out_lat")
    @Expose
    private var geoLocationOutLat: String? = null
    @SerializedName("geo_location_out_lon")
    @Expose
    private var geoLocationOutLon: String? = null

    @SerializedName("atten_image")
    @Expose
    private var atten_image: String? = null

    @SerializedName("atten_image_out")
    @Expose
    private var atten_image_out: String? = null

    @SerializedName("attachment")
    @Expose
    private var attachment: String? = null
    @SerializedName("attachment_out")
    @Expose
    private var attachment_out: String? = null

    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }

    fun getAttenDate(): String? {
        return attenDate
    }

    fun setAttenDate(attenDate: String) {
        this.attenDate = attenDate
    }

    fun getImageOut(): String? {
        return atten_image_out
    }

    fun setImageOut(atten_image_out: String) {
        this.atten_image_out = atten_image_out
    }

    fun getImage(): String? {
        return atten_image
    }

    fun setImage(atten_image: String) {
        this.atten_image = atten_image
    }

    fun getImageServer(): String? {
        return attachment
    }

    fun setImageServer(attachment: String) {
        this.attachment = attachment
    }

    fun getImageServerOut(): String? {
        return attachment_out
    }

    fun setImageServerOut(attachment_out: String) {
        this.attachment_out = attachment_out
    }

    fun getPunchInTime(): String? {
        return punchInTime
    }

    fun setPunchInTime(punchInTime: String) {
        this.punchInTime = punchInTime
    }

    fun getPunchOutTime(): String? {
        return punchOutTime
    }

    fun setPunchOutTime(punchOutTime: String) {
        this.punchOutTime = punchOutTime
    }

    fun getState(): String? {
        return state
    }

    fun setState(state: String) {
        this.state = state
    }

    fun getRemarks(): String? {
        return remarks
    }

    fun setRemarks(remarks: String) {
        this.remarks = remarks
    }

    fun getGeoLocationInLat(): String? {
        return geoLocationInLat
    }

    fun setGeoLocationInLat(geoLocationInLat: String) {
        this.geoLocationInLat = geoLocationInLat
    }

    fun getGeoLocationInLon(): String? {
        return geoLocationInLon
    }

    fun setGeoLocationInLon(geoLocationInLon: String) {
        this.geoLocationInLon = geoLocationInLon
    }

    fun getGeoLocationOutLat(): String? {
        return geoLocationOutLat
    }

    fun setGeoLocationOutLat(geoLocationOutLat: String) {
        this.geoLocationOutLat = geoLocationOutLat
    }

    fun getGeoLocationOutLon(): String? {
        return geoLocationOutLon
    }

    fun setGeoLocationOutLon(geoLocationOutLon: String) {
        this.geoLocationOutLon = geoLocationOutLon
    }
}