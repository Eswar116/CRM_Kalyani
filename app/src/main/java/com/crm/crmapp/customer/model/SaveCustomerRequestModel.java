
package com.crm.crmapp.customer.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveCustomerRequestModel {

    @SerializedName("Result_1")
    @Expose
    private List<SaveCustomerResult1> result1 = null;
    @SerializedName("Result_2")
    @Expose
    private List<SaveCustomerResult2> result2 = null;

    public List<SaveCustomerResult1> getResult1() {
        return result1;
    }

    public void setResult1(List<SaveCustomerResult1> result1) {
        this.result1 = result1;
    }

    public List<SaveCustomerResult2> getResult2() {
        return result2;
    }

    public void setResult2(List<SaveCustomerResult2> result2) {
        this.result2 = result2;
    }

}
