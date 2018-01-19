package com.pmberjaya.indotiki.models.help;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edwin on 15/11/2017.
 */

public class HelpCategoryData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subcategory")
    @Expose
    private String subcategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}
