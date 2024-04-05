
package com.crm.crmapp.expense.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveExpenseResult2 {

    @SerializedName("_exp_attachment")
    @Expose
    private String expAttachment;
    @SerializedName("_type")
    @Expose
    private String type;
    @SerializedName("_extension")
    @Expose
    private String extension;

    public String getExpAttachment() {
        return expAttachment;
    }

    public void setExpAttachment(String expAttachment) {
        this.expAttachment = expAttachment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
