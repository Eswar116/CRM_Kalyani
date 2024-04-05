package com.crm.crmapp.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ComplaintSaveRequest {


    @SerializedName("Result_1")
    @Expose
    private var result1: List<ComplaintSaveRequest.Result1>? = null
    @SerializedName("Result_2")
    @Expose
    private var result2: List<ComplaintSaveRequest.Result2>? = null

    fun getResult1(): List<ComplaintSaveRequest.Result1>? {
        return result1
    }

    fun setResult1(result1: List<ComplaintSaveRequest.Result1>) {
        this.result1 = result1
    }

    fun getResult2(): List<ComplaintSaveRequest.Result2>? {
        return result2
    }

    fun setResult2(result2: List<ComplaintSaveRequest.Result2>) {
        this.result2 = result2
    }

    class Result1 {

        @SerializedName("_user_id")
        @Expose
        private var userId: Int? = null
        @SerializedName("_cust_id")
        @Expose
        private var custId: Int? = null
        @SerializedName("_comp_date")
        @Expose
        private var compDate: String? = null
        @SerializedName("_type_id")
        @Expose
        private var typeId: Int? = null
        @SerializedName("_comp_id")
        @Expose
        private var compId: Int? = null

        @SerializedName("_source_text")
        @Expose
        private var sourceText: String? = null
        @SerializedName("_priority_text")
        @Expose
        private var priorityText: String? = null
        @SerializedName("_remarks")
        @Expose
        private var remarks: String? = null
        @SerializedName("_status")
        @Expose
        public var status: String? = null
        @SerializedName("_start_date")
        @Expose
        public var start_date: String? = null

        fun getUserId(): Int? {
            return userId
        }

        fun setUserId(userId: Int?) {
            this.userId = userId
        }

        fun getCustId(): Int? {
            return custId
        }

        fun setCustId(custId: Int?) {
            this.custId = custId
        }

        fun getCompDate(): String? {
            return compDate
        }

        fun setCompDate(compDate: String) {
            this.compDate = compDate
        }

        fun getTypeId(): Int? {
            return typeId
        }

        fun setTypeId(typeId: Int?) {
            this.typeId = typeId
        }


        fun getCompId(): Int? {
            return compId
        }

        fun setCompId(compId: Int?) {
            this.compId = compId
        }

        fun getSourceText(): String? {
            return sourceText
        }

        fun setSourceText(sourceText: String) {
            this.sourceText = sourceText
        }

        fun getPriorityText(): String? {
            return priorityText
        }

        fun setPriorityText(priorityText: String) {
            this.priorityText = priorityText
        }

        fun getRemarks(): String? {
            return remarks
        }

        fun setRemarks(remarks: String) {
            this.remarks = remarks
        }

    }

    class Result2 {

        @SerializedName("_comp_attachment")
        @Expose
        private var compAttachment: String? = null
        @SerializedName("_type")
        @Expose
        private var type: String? = null
        @SerializedName("_extension")
        @Expose
        private var extension: String? = null

        fun getCompAttachment(): String? {
            return compAttachment
        }

        fun setCompAttachment(compAttachment: String) {
            this.compAttachment = compAttachment
        }

        fun getType(): String? {
            return type
        }

        fun setType(type: String) {
            this.type = type
        }

        fun getExtension(): String? {
            return extension
        }

        fun setExtension(extension: String) {
            this.extension = extension
        }
    }
}