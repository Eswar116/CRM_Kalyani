
package com.crm.crmapp.expense.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateExpenseStateRequestModel {

    @SerializedName("Result")
    @Expose
    private List<UpdateExpenseStateRequestResult> result = null;

    public List<UpdateExpenseStateRequestResult> getResult() {
        return result;
    }

    public void setResult(List<UpdateExpenseStateRequestResult> result) {
        this.result = result;
    }

}
