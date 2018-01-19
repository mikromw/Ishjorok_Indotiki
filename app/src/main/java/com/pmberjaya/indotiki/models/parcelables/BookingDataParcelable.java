package com.pmberjaya.indotiki.models.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.models.bookingData.BookingThisTripAgainData;
import com.pmberjaya.indotiki.models.bookingData.DriverData;

/**
 * Created by edwin on 02/06/2017.
 */

public class BookingDataParcelable implements Parcelable{
    public String id;
    public String requestType;
    public String transportation;
    public String requestTime;
    public String acceptTime;
    public String endTime;
    public String distance;
    public double distanceValue = 0.0;
    public String price;
    public String payment = "1";
    public String channel;
    public String promoCode;
    public String promoPrice;
    public String originalPrice;
    public String tip;
    public String tipBefore;
    public String status;
    public String categoryVoucher;
    public String state;
    public String district;
    public String totalPrice;
    public String depositPaid;
    public String cashPaid;
    public String fromPlace;
    public String toPlace;
    public String from;
    public String to;
    public double latFrom;
    public double lngFrom;
    public double latTo;
    public double lngTo;
    public double latUser;
    public double lngUser;
    public DriverData driverData;
    public BookingTransportDatas bookingTransportDatas;
    public BookingCourierDatas bookingCourierDatas;
    public BookingFoodDatas bookingFoodDatas;
    public BookingMartDatas bookingMartDatas;

    public BookingDataParcelable(){
        bookingTransportDatas = new BookingTransportDatas();
        bookingCourierDatas = new BookingCourierDatas();
        bookingFoodDatas = new BookingFoodDatas();
        bookingMartDatas = new BookingMartDatas();
    }
    protected BookingDataParcelable(Parcel in) {
        id = in.readString();
        requestType = in.readString();
        driverData = in.readParcelable(DriverData.class.getClassLoader());
        transportation = in.readString();
        requestTime = in.readString();
        acceptTime = in.readString();
        endTime = in.readString();
        distance = in.readString();
        distanceValue = in.readDouble();
        price = in.readString();
        payment = in.readString();
        channel = in.readString();
        promoCode = in.readString();
        promoPrice = in.readString();
        originalPrice = in.readString();
        tip = in.readString();
        tipBefore = in.readString();
        status = in.readString();
        categoryVoucher = in.readString();
        state = in.readString();
        district = in.readString();
        totalPrice = in.readString();
        depositPaid = in.readString();
        cashPaid = in.readString();
        latFrom = in.readDouble();
        lngFrom = in.readDouble();
        latTo = in.readDouble();
        lngTo = in.readDouble();
        latUser = in.readDouble();
        lngUser = in.readDouble();
        from = in.readString();
        fromPlace = in.readString();
        to = in.readString();
        toPlace = in.readString();
        bookingTransportDatas = in.readParcelable(BookingTransportDatas.class.getClassLoader());
        bookingCourierDatas = in.readParcelable(BookingCourierDatas.class.getClassLoader());
        bookingFoodDatas = in.readParcelable(BookingFoodDatas.class.getClassLoader());
        bookingMartDatas = in.readParcelable(BookingMartDatas.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(requestType);
        dest.writeParcelable(driverData, flags);
        dest.writeString(transportation);
        dest.writeString(requestTime);
        dest.writeString(acceptTime);
        dest.writeString(endTime);
        dest.writeString(distance);
        dest.writeDouble(distanceValue);
        dest.writeString(price);
        dest.writeString(payment);
        dest.writeString(channel);
        dest.writeString(promoCode);
        dest.writeString(promoPrice);
        dest.writeString(originalPrice);
        dest.writeString(tip);
        dest.writeString(tipBefore);
        dest.writeString(status);
        dest.writeString(categoryVoucher);
        dest.writeString(state);
        dest.writeString(district);
        dest.writeString(totalPrice);
        dest.writeString(depositPaid);
        dest.writeString(cashPaid);
        dest.writeDouble(latFrom);
        dest.writeDouble(lngFrom);
        dest.writeDouble(latTo);
        dest.writeDouble(lngTo);
        dest.writeDouble(latUser);
        dest.writeDouble(lngUser);
        dest.writeString(from);
        dest.writeString(fromPlace);
        dest.writeString(to);
        dest.writeString(toPlace);
        dest.writeParcelable(bookingTransportDatas, flags);
        dest.writeParcelable(bookingCourierDatas, flags);
        dest.writeParcelable(bookingFoodDatas, flags);
        dest.writeParcelable(bookingMartDatas, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookingDataParcelable> CREATOR = new Creator<BookingDataParcelable>() {
        @Override
        public BookingDataParcelable createFromParcel(Parcel in) {
            return new BookingDataParcelable(in);
        }

        @Override
        public BookingDataParcelable[] newArray(int size) {
            return new BookingDataParcelable[size];
        }
    };

    public BookingDataParcelable buildBookingDataParcelable(BaseGenericCallback<BookingThisTripAgainData> baseGenericCallback){
        BookingThisTripAgainData bookingThisTripAgainData = baseGenericCallback.getData();
        distance = bookingThisTripAgainData.getDistance();
        originalPrice = bookingThisTripAgainData.getOriginal_price();
        payment = bookingThisTripAgainData.getPayment();
        price = bookingThisTripAgainData.getPrice();
        promoCode = bookingThisTripAgainData.getPromo();
        transportation = bookingThisTripAgainData.getTransportation();
        cashPaid = bookingThisTripAgainData.getCash_paid();
        depositPaid = bookingThisTripAgainData.getDeposit_paid();
        if(requestType.equals(Constants.TRANSPORT)||requestType.equals(Constants.CAR)){
            bookingTransportDatas = buildBookingTransportDatas(bookingThisTripAgainData);
        }else if(requestType.equals(Constants.COURIER)){
            bookingCourierDatas = buildBookingCourierDatas(bookingThisTripAgainData);
        }else if(requestType.equals(Constants.FOOD)){
//            bookingFoodDatas = buildBookingFoodDatas(bookingThisTripAgainData);
        }else if (requestType.equals(Constants.MART)){
//            bookingMartDatas = buildBookingMartDatas(bookingThisTripAgainData);
        }
        return this;
    }

    public BookingTransportDatas buildBookingTransportDatas(BookingThisTripAgainData bookingThisTripAgainData){
        BookingTransportDatas bookingTransportDatas = new BookingTransportDatas();
        bookingTransportDatas.locationDetail = bookingThisTripAgainData.getLocationDetail();
        return bookingTransportDatas;
    }

    public BookingCourierDatas buildBookingCourierDatas(BookingThisTripAgainData bookingThisTripAgainData){
        BookingCourierDatas bookingCourierDatas = new BookingCourierDatas();
        bookingCourierDatas.item = bookingThisTripAgainData.getItem();
        bookingCourierDatas.item_photo = bookingThisTripAgainData.getItem_photo();
        bookingCourierDatas.name_sender= bookingThisTripAgainData.getNameSender();
        bookingCourierDatas.phone_sender= bookingThisTripAgainData.getPhoneSender();
        bookingCourierDatas.location_detail_sender= bookingThisTripAgainData.getLocationDetailSender();
        bookingCourierDatas.name_receiver= bookingThisTripAgainData.getNameReceiver();
        bookingCourierDatas.phone_receiver= bookingThisTripAgainData.getPhoneReceiver();
        bookingCourierDatas.location_detail_receiver= bookingThisTripAgainData.getLocationDetailReceiver();
        return bookingCourierDatas;
    }

}