package com.pmberjaya.indotiki.models.gmaps.GeocoderLocationGmapsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 16/07/2016.
 */
public class Result {
    @SerializedName("address_components")
    @Expose
    private List<AddressComponent> addressComponents = new ArrayList<AddressComponent>();
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("place_id")
    @Expose
    private String placeId;


    /**
     *
     * @return
     * The addressComponents
     */
    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    /**
     *
     * @param addressComponents
     * The address_components
     */
    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }

    /**
     *
     * @return
     * The formattedAddress
     */
    public String getFormattedAddress() {
        return formattedAddress;
    }

    /**
     *
     * @param formattedAddress
     * The formatted_address
     */
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    /**
     *
     * @return
     * The placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     *
     * @param placeId
     * The place_id
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }


}
