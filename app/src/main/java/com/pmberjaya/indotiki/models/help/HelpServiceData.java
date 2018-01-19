package com.pmberjaya.indotiki.models.help;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by edwin on 16/11/2017.
 */

public class HelpServiceData {
    @SerializedName("top_five")
    @Expose
    private List<HelpFaqData> topFive = null;
    @SerializedName("category")
    @Expose
    private List<HelpCategoryData> category = null;

    public List<HelpFaqData> getTopFive() {
        return topFive;
    }

    public void setTopFive(List<HelpFaqData> topFive) {
        this.topFive = topFive;
    }

    public List<HelpCategoryData> getCategory() {
        return category;
    }

    public void setCategory(List<HelpCategoryData> category) {
        this.category = category;
    }

}