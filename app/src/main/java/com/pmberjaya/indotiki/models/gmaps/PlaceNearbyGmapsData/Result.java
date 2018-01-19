package com.pmberjaya.indotiki.models.gmaps.PlaceNearbyGmapsData;

/**
 * Created by edwin on 5/30/2016.
 */
public class Result {
    Geometry geometry;
    String name;
    String vicinity;
    String place_id;



    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }


}
