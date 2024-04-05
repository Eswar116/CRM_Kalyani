package com.crm.crmapp.marketPlan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MarketPlanInfoRequestResponse {

    @SerializedName("message")
    @Expose
    public var message: String? = null
    @SerializedName("status")
    @Expose
    public var status: String? = null
    @SerializedName("Result_1")
    @Expose
    public var result_1: List<Result_1>? = null
    @SerializedName("Result_2")
    @Expose
    public var result_2: List<Result_2>? = null
    @SerializedName("Result_3")
    @Expose
    public var result_3: List<Result_3>? = null
    @SerializedName("Result_4")
    @Expose
    public var result_4: List<Result_4>? = null
    @SerializedName("Result_5")
    @Expose
    public var result_5: List<Result_5>? = null
    @SerializedName("Result_6")
    @Expose
    public var result_6: List<Result_6>? = null

    class Result_1 : Serializable {
        @SerializedName("owner_user_name")
        @Expose
        public var owner_user_name: String? = null
        @SerializedName("cat_name")
        @Expose
        public var cat_name: String? = null
        @SerializedName("city_code")
        @Expose
        public var city_code: String? = null
        @SerializedName("startdate")
        @Expose
        public var startdate: String? = null
        @SerializedName("plan_owner")
        @Expose
        public var plan_owner: Int? = null
        @SerializedName("owner_user_cat")
        @Expose
        public var owner_user_cat: String? = null
        @SerializedName("owner_full_name")
        @Expose
        public var owner_full_name: String? = null
        @SerializedName("scheme_name")
        @Expose
        public var scheme_name: String? = null
        @SerializedName("city_name")
        @Expose
        public var city_name: String? = null
        @SerializedName("approved_by")
        @Expose
        public var approved_by: Int? = null
        @SerializedName("market_plan_name")
        @Expose
        public var market_plan_name: String? = null
        @SerializedName("owner_email_id")
        @Expose
        public var owner_email_id: String? = null
        @SerializedName("enddate")
        @Expose
        public var enddate: String? = null
        @SerializedName("stage")
        @Expose
        public var stage: String? = null
        @SerializedName("state_name")
        @Expose
        public var state_name: String? = null
        @SerializedName("distributor_id")
        @Expose
        public var distributor_id: Int? = null
        @SerializedName("is_approved")
        @Expose
        public var is_approved: Int? = null
        @SerializedName("distributor_name")
        @Expose
        public var distributor_name: String? = null
        @SerializedName("id")
        @Expose
        public var id: Int? = null
        @SerializedName("state_code")
        @Expose
        public var state_code: String? = null
        @SerializedName("remarks")
        @Expose
        public var remarks: String? = null
        @SerializedName("owner_mobile_no")
        @Expose
        public var owner_mobile_no: String? = null
    }

    class Result_2 : Serializable {
        @SerializedName("extension")
        @Expose
        public var extension: String? = null
        @SerializedName("id")
        @Expose
        public var id: Int? = null
        @SerializedName("type")
        @Expose
        public var type: String? = null
        @SerializedName("url")
        @Expose
        public var url: String? = null
        @SerializedName("remarks")
        @Expose
        public var remarks: String? = null
        @SerializedName("market_plan_id")
        @Expose
        public var market_plan_id: Int? = null
    }

    class Result_3 : Serializable {
        @SerializedName("email_id")
        @Expose
        public var email_id: String? = null
        @SerializedName("user_id")
        @Expose
        public var user_id: Int? = null
        @SerializedName("user_full_name")
        @Expose
        public var user_full_name: String? = null
        @SerializedName("mobile_no")
        @Expose
        public var mobile_no: String? = null
    }

    class Result_4 : Serializable {
        @SerializedName("is_new_entry")
        @Expose
        public var is_new_entry: Boolean? = null
        @SerializedName("address")
        @Expose
        public var address: String? = null
        @SerializedName("cust_type")
        @Expose
        public var cust_type: String? = null
        @SerializedName("cust_name")
        @Expose
        public var cust_name: String? = null
        @SerializedName("grpcustname")
        @Expose
        public var grpcustname: String? = null
        @SerializedName("emailid")
        @Expose
        public var emailid: String? = null
        @SerializedName("mobileno")
        @Expose
        public var mobileno: String? = null
        @SerializedName("cust_id")
        @Expose
        public var id:Int? = null
    }

    class Result_5 : Serializable {
        @SerializedName("order_date")
        @Expose
        public var order_date: String? = null
        @SerializedName("main_cust_name")
        @Expose
        public var main_cust_name: String? = null
        @SerializedName("cust_name")
        @Expose
        public var cust_name: String? = null
        @SerializedName("status_text")
        @Expose
        public var status_text: String? = null
        @SerializedName("order_id")
        @Expose
        public var order_id: Int? = null
        @SerializedName("erp_ref_id")
        @Expose
        public var erp_ref_id: String? = null
    }

    class Result_6 : Serializable {
        @SerializedName("edit")
        @Expose
        public var edit: String? = null
        @SerializedName("approve")
        @Expose
        public var approve: String? = null
        @SerializedName("status")
        @Expose
        public var status: String? = null
        @SerializedName("edit_fromdate")
        @Expose
        public var edit_fromdate: String? = null
    }

}