
package com.crm.crmapp.competition.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveCompetitorResult2 {

    @SerializedName("_comp_attachment")
    @Expose
    private String compAttachment;
    @SerializedName("_type")
    @Expose
    private String type;
    @SerializedName("_extension")
    @Expose
    private String extension;

    public String getCompAttachment() {
        return compAttachment;
    }

    public void setCompAttachment(String compAttachment) {
        this.compAttachment = compAttachment;
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
