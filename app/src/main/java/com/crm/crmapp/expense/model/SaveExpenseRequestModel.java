
package com.crm.crmapp.expense.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveExpenseRequestModel {

    @SerializedName("Result_1")
    @Expose
    private List<SaveExpenseResult1> result1 = null;
    @SerializedName("Result_2")
    @Expose
    private List<SaveExpenseResult2> result2 = null;

    public List<SaveExpenseResult1> getResult1() {
        return result1;
    }

    public void setResult1(List<SaveExpenseResult1> result1) {
        this.result1 = result1;
    }

    public List<SaveExpenseResult2> getResult2() {
        return result2;
    }

    public void setResult2(List<SaveExpenseResult2> result2) {
        this.result2 = result2;
    }

}
