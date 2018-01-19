package com.pmberjaya.indotiki.models.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by willy on 5/13/2016.
 */
public class FoodItemTempData implements Parcelable{
    public String id;
    public String partner_id;
    public String media;
    public String label;
    public String price;
    public String normal_price;
    public String discount_percent;
    public String description;
    public String category;
    public int quantity=0;
    public String description_note;
    public String category_id;
    public boolean isnote;

    public FoodItemTempData(){

    }
    protected FoodItemTempData(Parcel in) {
        this.id = in.readString();
        this.partner_id = in.readString();
        this.media = in.readString();
        this.label = in.readString();
        this.price = in.readString();
        this.description = in.readString();
        this.category = in.readString();
        this.quantity = in.readInt();
        this.description_note = in.readString();
        this.category_id = in.readString();
        this.normal_price = in.readString();
        this.discount_percent = in.readString();
    }

    public static final Parcelable.Creator<FoodItemTempData> CREATOR = new Parcelable.Creator<FoodItemTempData>() {
        @Override
        public FoodItemTempData createFromParcel(Parcel in) {
            return new FoodItemTempData(in);
        }

        @Override
        public FoodItemTempData[] newArray(int size) {
            return new FoodItemTempData[size];
        }
    };

    public void updateObject(FoodItemTempData data){
        this.id = data.getId();
        this.partner_id = data.getPartner_id();
        this.media = data.getMedia();
        this.label = data.getLabel();
        this.price = data.getPrice();
        this.description = data.getDescription();
        this.category = data.getCategory();
        this.quantity = data.getQuantity();
        this.description_note = data.getDescription_note();
        this.category_id = data.getCategory_id();
        this.normal_price = data.getDiscount_price();
        this.discount_percent = data.getDiscount_percent();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(partner_id);
        dest.writeString(media);
        dest.writeString(label);
        dest.writeString(price);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeInt(quantity);
        dest.writeString(description_note);
        dest.writeString(category_id);
        dest.writeString(normal_price);
        dest.writeString(discount_percent);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean getIsnote() {
        return isnote;
    }

    public void setIsnote(boolean isnote) {
        this.isnote = isnote;
    }

    public String getDescription_note() {
        return description_note;
    }

    public void setDescription_note(String desciption_note) {
        this.description_note = desciption_note;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDiscount_price() {
        return normal_price;
    }

    public void setDiscount_price(String normal_price) {
        this.normal_price = normal_price;
    }

    public String getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(String discount_percent) {
        this.discount_percent = discount_percent;
    }
}


