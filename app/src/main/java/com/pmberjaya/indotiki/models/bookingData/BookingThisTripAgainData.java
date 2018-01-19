package com.pmberjaya.indotiki.models.bookingData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edwin on 5/3/2016.
 */
public class BookingThisTripAgainData {

    @SerializedName("transportation")
    @Expose
    private String transportation;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("from_place")
    @Expose
    private String fromPlace;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("to_place")
    @Expose
    private String toPlace;
    @SerializedName("location_detail")
    @Expose
    private String locationDetail;
    @SerializedName("location_detail_sender")
    @Expose
    private String locationDetailSender;
    @SerializedName("location_detail_receiver")
    @Expose
    private String locationDetailReceiver;
    @SerializedName("waiting")
    @Expose
    private String waiting;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("lat_from")
    @Expose
    private String latFrom;
    @SerializedName("lng_from")
    @Expose
    private String lngFrom;
    @SerializedName("lat_to")
    @Expose
    private String latTo;
    @SerializedName("lng_to")
    @Expose
    private String lngTo;
    @SerializedName("from_place_id")
    @Expose
    private String fromPlaceId;
    @SerializedName("to_place_id")
    @Expose
    private String toPlaceId;

    @SerializedName("name_sender")
    @Expose
    private String nameSender;

    @SerializedName("phone_sender")
    @Expose
    private String phoneSender;
    @SerializedName("name_receiver")
    @Expose
    private String nameReceiver;
    @SerializedName("phone_receiver")
    @Expose
    private String phoneReceiver;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("item_photo")
    @Expose
    private String item_photo;
    @SerializedName("item_photo_small")
    @Expose
    private String item_photo_small;
    @SerializedName("interval")
    @Expose
    private String duration;

    private String note;
    private String store_id;
    private String quantity;
    private String price_per_item;
    private String total_item;
    private String total_item_price;
    private String item_id;
    private String open_time_now;
    private String close_time_now;
    private String status_store;

    private String payment;
    private String promo;
    private String district;
    private String original_price;
    private String promo_price;
    private String category_voucher;
    private String tip;
    private String cash_paid;
    private String deposit_paid;
    private String mart_id;
    /**
     *
     * @return
     * The userId
     */

    public String getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     * The user_id
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getTransportation() {
        return transportation;
    }

    /**
     *
     * @param transportation
     * The transportation
     */
    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    /**
     *
     * @return
     * The from
     */
    public String getFrom() {
        return from;
    }

    /**
     *
     * @param from
     * The from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     *
     * @return
     * The fromPlace
     */
    public String getFromPlace() {
        return fromPlace;
    }

    /**
     *
     * @param fromPlace
     * The from_place
     */
    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    /**
     *
     * @return
     * The to
     */
    public String getTo() {
        return to;
    }

    /**
     *
     * @param to
     * The to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     *
     * @return
     * The toPlace
     */
    public String getToPlace() {
        return toPlace;
    }

    /**
     *
     * @param toPlace
     * The to_place
     */
    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    /**
     *
     * @return
     * The locationDetail
     */
    public String getLocationDetail() {
        return locationDetail;
    }

    /**
     *
     * @param locationDetail
     * The location_detail
     */
    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    /**
     *
     * @return
     * The waiting
     */
    public String getWaiting() {
        return waiting;
    }

    /**
     *
     * @param waiting
     * The waiting
     */
    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }

    /**
     *
     * @return
     * The distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     * The distance
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     * The price
     */
    public String getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The latFrom
     */
    public String getLatFrom() {
        return latFrom;
    }

    /**
     *
     * @param latFrom
     * The lat_from
     */
    public void setLatFrom(String latFrom) {
        this.latFrom = latFrom;
    }

    /**
     *
     * @return
     * The lngFrom
     */
    public String getLngFrom() {
        return lngFrom;
    }

    /**
     *
     * @param lngFrom
     * The lng_from
     */
    public void setLngFrom(String lngFrom) {
        this.lngFrom = lngFrom;
    }

    /**
     *
     * @return
     * The latTo
     */
    public String getLatTo() {
        return latTo;
    }

    /**
     *
     * @param latTo
     * The lat_to
     */
    public void setLatTo(String latTo) {
        this.latTo = latTo;
    }

    /**
     *
     * @return
     * The lngTo
     */
    public String getLngTo() {
        return lngTo;
    }

    /**
     *
     * @param lngTo
     * The lng_to
     */
    public void setLngTo(String lngTo) {
        this.lngTo = lngTo;
    }

    /**
     *
     * @return
     * The fromPlaceId
     */
    public String getFromPlaceId() {
        return fromPlaceId;
    }

    /**
     *
     * @param fromPlaceId
     * The from_place_id
     */
    public void setFromPlaceId(String fromPlaceId) {
        this.fromPlaceId = fromPlaceId;
    }

    /**
     *
     * @return
     * The toPlaceId
     */
    public String getToPlaceId() {
        return toPlaceId;
    }

    /**
     *
     * @param toPlaceId
     * The to_place_id
     */
    public void setToPlaceId(String toPlaceId) {
        this.toPlaceId = toPlaceId;
    }
    public String getPhoneSender() {
        return phoneSender;
    }

    public void setPhoneSender(String phoneSender) {
        this.phoneSender = phoneSender;
    }

    public String getLocationDetailSender() {
        return locationDetailSender;
    }

    public void setLocationDetailSender(String locationDetailSender) {
        this.locationDetailSender = locationDetailSender;
    }

    public String getLocationDetailReceiver() {
        return locationDetailReceiver;
    }

    public void setLocationDetailReceiver(String locationDetailReceiver) {
        this.locationDetailReceiver = locationDetailReceiver;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getNameReceiver() {
        return nameReceiver;
    }

    public void setNameReceiver(String nameReceiver) {
        this.nameReceiver = nameReceiver;
    }

    public String getPhoneReceiver() {
        return phoneReceiver;
    }

    public void setPhoneReceiver(String phoneReceiver) {
        this.phoneReceiver = phoneReceiver;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem_photo() {
        return item_photo;
    }

    public void setItem_photo(String item_photo) {
        this.item_photo = item_photo;
    }

    public String getItem_photo_small() {
        return item_photo_small;
    }

    public void setItem_photo_small(String item_photo_small) {
        this.item_photo_small = item_photo_small;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice_per_item() {
        return price_per_item;
    }

    public void setPrice_per_item(String price_per_item) {
        this.price_per_item = price_per_item;
    }

    public String getTotal_item() {
        return total_item;
    }

    public void setTotal_item(String total_item) {
        this.total_item = total_item;
    }

    public String getItemId() {
        return item_id;
    }

    public void setItemId(String itemId) {
        this.item_id = itemId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getOpen_time_now() {
        return open_time_now;
    }

    public void setOpen_time_now(String open_time_now) {
        this.open_time_now = open_time_now;
    }

    public String getClose_time_now() {
        return close_time_now;
    }

    public void setClose_time_now(String close_time_now) {
        this.close_time_now = close_time_now;
    }

    public String getStatus_store() {
        return status_store;
    }

    public void setStatus_store(String status_store) {
        this.status_store = status_store;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getPromo_price() {
        return promo_price;
    }

    public void setPromo_price(String promo_price) {
        this.promo_price = promo_price;
    }

    public String getCategory_voucher() {
        return category_voucher;
    }

    public void setCategory_voucher(String category_voucher) {
        this.category_voucher = category_voucher;
    }

    public String getTotal_item_price() {
        return total_item_price;
    }

    public void setTotal_item_price(String total_item_price) {
        this.total_item_price = total_item_price;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getCash_paid() {
        return cash_paid;
    }

    public void setCash_paid(String cash_paid) {
        this.cash_paid = cash_paid;
    }

    public String getDeposit_paid() {
        return deposit_paid;
    }

    public void setDeposit_paid(String deposit_paid) {
        this.deposit_paid = deposit_paid;
    }

    public String getMart_id() {
        return mart_id;
    }

    public void setMart_id(String mart_id) {
        this.mart_id = mart_id;
    }
}
