package com.pmberjaya.indotiki.utilities;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.pmberjaya.indotiki.interfaces.misc.GPSTrackerInterface;

/**
 * Created by edwin on 09/10/2017.
 */

public class GPSTracker {
    private Activity mActivity;
    private static GPSTracker instance = null;

    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    LocationRequest mLocationRequestHighAccuracy;
    LocationRequest mLocationRequestBalancedPowerAccuracy;
    public static final int REQUEST_CHECK_SETTINGS = 1401;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private GPSTrackerInterface gpsTrackerInterface;

    //a private constructor so no instances can be made outside this class
    private GPSTracker() {}

    //Everytime you need an instance, call this
    //synchronized to make the call thread-safe
    public static synchronized GPSTracker getInstance() {
        if(instance == null)
            instance = new GPSTracker();

        return instance;
    }

    //Initialize this or any other variables in probably the Application class
    public void init( Activity mActivity, GPSTrackerInterface gpsTrackerInterface) {
        this.mActivity = mActivity;
        this.gpsTrackerInterface = gpsTrackerInterface;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.mActivity);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this.mActivity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        mLastLocation = location;
                        if (mLastLocation  != null) {
                            GPSTracker.this.gpsTrackerInterface.onLocationReceived(mLastLocation);
                        }else{
                            checkLocationSetting();
                        }
                    }
                });
    }
    public void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
    public void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy,
                mLocationCallback,
                null /* Looper */);
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                mLastLocation = location;
                GPSTracker.this.gpsTrackerInterface.onLocationReceived(mLastLocation);
            }

        }
        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            Toast.makeText(mActivity, "Sorry we cant get your location, Please try again "+locationAvailability.toString() , Toast.LENGTH_SHORT).show();
        }


    };

    private void createLocationRequest() {
        mLocationRequestHighAccuracy = new LocationRequest();
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequestHighAccuracy.setInterval(FATEST_INTERVAL);
        mLocationRequestHighAccuracy.setFastestInterval(UPDATE_INTERVAL);
        mLocationRequestHighAccuracy.setSmallestDisplacement(DISPLACEMENT);
//        mLocationRequestBalancedPowerAccuracy = new LocationRequest();
//        mLocationRequestBalancedPowerAccuracy.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        mLocationRequestBalancedPowerAccuracy.setInterval(FATEST_INTERVAL);
//        mLocationRequestBalancedPowerAccuracy.setFastestInterval(UPDATE_INTERVAL);
//        mLocationRequestBalancedPowerAccuracy.setSmallestDisplacement(DISPLACEMENT);
    }
    private LocationSettingsRequest.Builder builder;
    private void checkLocationSetting(){
        createLocationRequest();
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequestHighAccuracy)/*
                .addLocationRequest(mLocationRequestBalancedPowerAccuracy)*/;
        SettingsClient client = LocationServices.getSettingsClient(this.mActivity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this.mActivity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(this.mActivity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(GPSTracker.this.mActivity,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

}
