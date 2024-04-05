package com.crm.crmapp.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ComplaintDetailResponse {

    @SerializedName("Result_2")
    @Expose
    private var result2: List<Result2>? = null
    @SerializedName("Result_1")
    @Expose
    private var result1: List<Result1>? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

    fun getResult2(): List<Result2>? {
        return result2
    }

    fun setResult2(result2: List<Result2>) {
        this.result2 = result2
    }

    fun getResult1(): List<Result1>? {
        return result1
    }

    fun setResult1(result1: List<Result1>) {
        this.result1 = result1
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }


    class Result1 {
        @SerializedName("type_name")
        @Expose
        private var typeName: String? = null
        @SerializedName("source_text")
        @Expose
        private var sourceText: String? = null
        @SerializedName("is_open")
        @Expose
        private var isOpen: Boolean? = null
        @SerializedName("type_id")
        @Expose
        private var typeId: Int? = null
        @SerializedName("priority_text")
        @Expose
        private var priorityText: String? = null
        @SerializedName("sol_by_id")
        @Expose
        private var solById: String? = null
        @SerializedName("complaint_id")
        @Expose
        private var complaintId: Int? = null
        @SerializedName("user_id")
        @Expose
        private var userId: Int? = null
        @SerializedName("solution_text")
        @Expose
        private var solutionText: String? = null
        @SerializedName("complaint_date")
        @Expose
        private var complaintDate: String? = null
        @SerializedName("cust_name")
        @Expose
        private var custName: String? = null
        @SerializedName("cust_id")
        @Expose
        private var custId: Int? = null
        @SerializedName("remarks")
        @Expose
        private var remarks: String? = null
        @SerializedName("status")
        @Expose
        public var status: String? = null
        @SerializedName("startdate")
        @Expose
        public var startdate: String? = null

        fun getTypeName(): String? {
            return typeName
        }

        fun setTypeName(typeName: String) {
            this.typeName = typeName
        }

        fun getSourceText(): String? {
            return sourceText
        }

        fun setSourceText(sourceText: String) {
            this.sourceText = sourceText
        }

        fun getIsOpen(): Boolean? {
            return isOpen
        }

        fun setIsOpen(isOpen: Boolean?) {
            this.isOpen = isOpen
        }

        fun getTypeId(): Int? {
            return typeId
        }

        fun setTypeId(typeId: Int?) {
            this.typeId = typeId
        }

        fun getPriorityText(): String? {
            return priorityText
        }

        fun setPriorityText(priorityText: String) {
            this.priorityText = priorityText
        }

        fun getSolById(): String? {
            return solById
        }

        fun setSolById(solById: String) {
            this.solById = solById
        }

        fun getComplaintId(): Int? {
            return complaintId
        }

        fun setComplaintId(complaintId: Int?) {
            this.complaintId = complaintId
        }

        fun getUserId(): Int? {
            return userId
        }

        fun setUserId(userId: Int?) {
            this.userId = userId
        }

        fun getSolutionText(): String? {
            return solutionText
        }

        fun setSolutionText(solutionText: String) {
            this.solutionText = solutionText
        }

        fun getComplaintDate(): String? {
            return complaintDate
        }

        fun setComplaintDate(complaintDate: String) {
            this.complaintDate = complaintDate
        }

        fun getCustName(): String? {
            return custName
        }

        fun setCustName(custName: String) {
            this.custName = custName
        }

        fun getCustId(): Int? {
            return custId
        }

        fun setCustId(custId: Int?) {
            this.custId = custId
        }

        fun getRemarks(): String? {
            return remarks
        }

        fun setRemarks(remarks: String) {
            this.remarks = remarks
        }
    }

    class Result2 {
        @SerializedName("url")
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