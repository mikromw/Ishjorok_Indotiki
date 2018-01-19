package com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingInProgressMemberData {


@SerializedName("source_table")
@Expose
private String sourceTable;
@SerializedName("id")
@Expose
private String id;
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
@SerializedName("request_time")
@Expose
private String requestTime;
@SerializedName("status")
@Expose
private String status;
    private String lat_from;
    private String lng_from;
    private String lat_to;
    private String lng_to;
    private String transportation;
/**
* 
* @return
* The sourceTable
*/
public String getSourceTable() {
return sourceTable;
}

/**
* 
* @param sourceTable
* The source_table
*/
public void setSourceTable(String sourceTable) {
this.sourceTable = sourceTable;
}

/**
* 
* @return
* The id
*/
public String getId() {
return id;
}

/**
* 
* @param id
* The id
*/
public void setId(String id) {
this.id = id;
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
* The requestTime
*/
public String getRequestTime() {
return requestTime;
}

/**
* 
* @param requestTime
* The requestTime
*/
public void setRequestTime(String requestTime) {
this.requestTime = requestTime;
}

public String getStatus() {
return status;
}
public void setStatus(String status) {
this.status = status;
}


    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getLat_from() {
        return lat_from;
    }

    public void setLat_from(String lat_from) {
        this.lat_from = lat_from;
    }

    public String getLng_from() {
        return lng_from;
    }

    public void setLng_from(String lng_from) {
        this.lng_from = lng_from;
    }

    public String getLat_to() {
        return lat_to;
    }

    public void setLat_to(String lat_to) {
        this.lat_to = lat_to;
    }

    public String getLng_to() {
        return lng_to;
    }

    public void setLng_to(String lng_to) {
        this.lng_to = lng_to;
    }
}