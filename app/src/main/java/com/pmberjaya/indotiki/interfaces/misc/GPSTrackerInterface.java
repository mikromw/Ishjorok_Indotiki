package com.pmberjaya.indotiki.interfaces.misc;

        import android.location.Location;

/**
 * Created by edwin on 09/10/2017.
 */

public interface GPSTrackerInterface {
    void onLocationError(String str);
    void onLocationReceived(Location location);
}
