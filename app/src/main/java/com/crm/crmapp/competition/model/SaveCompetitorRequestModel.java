
package com.crm.crmapp.competition.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveCompetitorRequestModel {

    @SerializedName("Result_1")
    @Expose
    private List<SaveCompetitorResult1> result1 = null;
    @SerializedName("Result_2")
    @Expose
    private List<SaveCompetitorResult2> result2 = null;

    public List<SaveCompetitorResult1> getResult1() {
        return result1;
    }

    public void setResult1(List<SaveCompetitorResult1> result1) {
        this.result1 = result1;
    }

    public List<SaveCompetitorResult2> getResult2() {
        return result2;
    }

    public void setResult2(List<SaveCompetitorResult2> result2) {
        this.result2 = result2;
    }

}
