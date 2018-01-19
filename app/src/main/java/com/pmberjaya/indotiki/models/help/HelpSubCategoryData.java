package com.pmberjaya.indotiki.models.help;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pmberjaya.indotiki.base.BaseCallback;

/**
 * Created by edwin on 15/11/2017.
 */

public class HelpSubCategoryData extends BaseCallback {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("subcategory")
    @Expose
    private String subcategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}
