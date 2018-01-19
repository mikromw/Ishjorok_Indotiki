package com.pmberjaya.indotiki.models.gmaps.PlaceDetailGmapsData;


/**
 * Created by edwin on 5/30/2016.
 */
public class ResultDetail {
    GeometryDetail geometry;
    String name;
    String formatted_address;
    String place_id;

    public GeometryDetail getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryDetail geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
