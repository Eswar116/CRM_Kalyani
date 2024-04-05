package com.crm.crmapp.marketPlan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UpdateMarketPlanStateRequestResponseModel {


    @SerializedName("Result")
    @Expose
    public var result: ArrayList<Result>? = null
    @SerializedName("message")
    @Expose
    public var message: String? = null
    @SerializedName("status")
    @Expose
    public var status: String? = null
    @SerializedName("mktpln")
    @Expose
    public var mktpln: String? = null

    class Result : Serializable {
        @SerializedName("_mkp_id")
        @Expose
        public var _mkp_id: Int? = null
        @SerializedName("_stage")
        @Expose
        public var stage: String? = null
        @SerializedName("_approved_by")
        @Expose
        public var approvedBy: Int? = null
        @SerializedName("_is_approved")
        @Expose
        public var isApproved: String? = null
        @SerializedName("frm_user_id")
        @Expose
        public var frm_user_id: String? = null
        @SerializedName("_mkt_pln_id")
        @Expose
        public var mkt_pln_id: Int? = null
        @SerializedName("_user_id")
        @Expose
        public var user_id: Int? = null
        @SerializedName("_to_date")
        @Expose
        public var to_date: String? = null
        @SerializedName("_from_date")
        @Expose
        public var from_date: String? = null
        @SerializedName("_reject_reason")
        @Expose
        public var _reject_reason: String? = null

    }

}