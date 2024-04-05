package com.crm.crmapp.marketPlan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewMarketPlanRequest {
    @SerializedName("Result_1")
    @Expose
    private List<Result1> result1 = null;
    @SerializedName("Result_2")
    @Expose
    private List<Result2> result2 = null;
    @SerializedName("Result_3")
    @Expose
    private List<Result3> result3 = null;

    public List<Result1> getResult1() {
        return result1;
    }

    public void setResult1(List<Result1> result1) {
        this.result1 = result1;
    }

    public List<Result2> getResult2() {
        return result2;
    }

    public void setResult2(List<Result2> result2) {
        this.result2 = result2;
    }

    public List<Result3> getResult3() {
        return result3;
    }

    public void setResult3(List<Result3> result3) {
        this.result3 = result3;
    }


    class Result1 {

        @SerializedName("_user_id")
        @Expose
        private Integer userId;
        @SerializedName("_mkypln_name")
        @Expose
        private String mkyplnName;
        @SerializedName("_frm_date")
        @Expose
        private String frmDate;
        @SerializedName("_to_date")
        @Expose
        private String toDate;
        @SerializedName("_state_code")
        @Expose
        private String stateCode;
        @SerializedName("_city_code")
        @Expose
        private String cityCode;
        @SerializedName("_category")
        @Expose
        private String category;
        @SerializedName("_scheme")
        @Expose
        private String scheme;
        @SerializedName("_cust_id")
        @Expose
        private Integer custId;
        @SerializedName("_remarks")
        @Expose
        private String remarks;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getMkyplnName() {
            return mkyplnName;
        }

        public void setMkyplnName(String mkyplnName) {
            this.mkyplnName = mkyplnName;
        }

        public String getFrmDate() {
            return frmDate;
        }

        public void setFrmDate(String frmDate) {
            this.frmDate = frmDate;
        }

        public String getToDate() {
            return toDate;
        }

        public void setToDate(String toDate) {
            this.toDate = toDate;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public Integer getCustId() {
            return custId;
        }

        public void setCustId(Integer custId) {
            this.custId = custId;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }


    class Result2 {

        @SerializedName("_mp_attachment")
        @Expose
        private String mpAttachment;

        public String getMpAttachment() {
            return mpAttachment;
        }

        public void setMpAttachment(String mpAttachment) {
            this.mpAttachment = mpAttachment;
        }

    }

    public class Result3 {

        @SerializedName("_mp_user_id")
        @Expose
        private Integer responsiblePersonId;

        public Integer getResponsiblePersonId() {
            return responsiblePersonId;
        }

        public void setResponsiblePersonId(Integer userId) {
            this.responsiblePersonId = userId;
        }
    }
}


