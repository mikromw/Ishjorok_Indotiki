package com.pmberjaya.indotiki.models.parcelables;

import android.os.Parcel;
import android.os.Parcelable;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 5/20/2016.
 */
public class BookingFoodDatas implements Parcelable{
    public String locationDetail;
    public int foodCost = 0;
    public int foodQuantityTotal = 0;
    public List<FoodItemTempData> foodItemTempDatas;
    public List<FoodItemTempData> foodItemTempManuallyDatas;
    public String total_item_price_before;
    public String food_receipt;
    public String restaurantCategoryId;
    public String restaurantCategoryName;
    public String restaurantSubCategoryId;
    public String restaurantSubCategoryName;

    protected BookingFoodDatas(Parcel in) {
        locationDetail = in.readString();
        foodCost = in.readInt();
        foodQuantityTotal = in.readInt();
        foodItemTempDatas = new ArrayList<FoodItemTempData>();
        foodItemTempDatas = in.createTypedArrayList(FoodItemTempData.CREATOR);
        foodItemTempManuallyDatas = new ArrayList<FoodItemTempData>();
        foodItemTempManuallyDatas = in.createTypedArrayList(FoodItemTempData.CREATOR);
        total_item_price_before = in.readString();
        food_receipt = in.readString();
        restaurantCategoryId = in.readString();
        restaurantCategoryName = in.readString();
        restaurantSubCategoryId = in.readString();
        restaurantSubCategoryName = in.readString();
    }
    public BookingFoodDatas() {
        this.foodItemTempDatas = new ArrayList<FoodItemTempData>();
        this.foodItemTempManuallyDatas = new ArrayList<FoodItemTempData>();
    }
    public static final Creator<BookingFoodDatas> CREATOR = new Creator<BookingFoodDatas>() {
        @Override
        public BookingFoodDatas createFromParcel(Parcel in) {
            return new BookingFoodDatas(in);
        }

        @Override
        public BookingFoodDatas[] newArray(int size) {
            return new BookingFoodDatas[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationDetail);
        dest.writeInt(foodCost);
        dest.writeInt(foodQuantityTotal);
        dest.writeTypedList(foodItemTempDatas);
        dest.writeTypedList(foodItemTempManuallyDatas);
        dest.writeString(total_item_price_before);
        dest.writeString(food_receipt);
        dest.writeString(restaurantCategoryId);
        dest.writeString(restaurantCategoryName);
        dest.writeString(restaurantSubCategoryId);
        dest.writeString(restaurantSubCategoryName);
    }


}
