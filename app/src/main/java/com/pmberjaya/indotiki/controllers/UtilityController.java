package com.pmberjaya.indotiki.controllers;

import android.app.ProgressDialog;
import android.content.Context;

import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.callbacks.gmaps.DirectionRouteGMapsCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.DistanceTimeGMapsCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.GeocoderLocationGMapsCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.PlaceDetailGmapsCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.PlaceNearbyGmapsCallback;
import com.pmberjaya.indotiki.callbacks.main.VersionAndMaintenanceCallback;
import com.pmberjaya.indotiki.interfaces.gmaps.DirectionRouteInterface;
import com.pmberjaya.indotiki.interfaces.gmaps.DistanceTimeInterface;
import com.pmberjaya.indotiki.interfaces.gmaps.GeocoderLocationInterface;
import com.pmberjaya.indotiki.interfaces.gmaps.PlaceDetailInterface;
import com.pmberjaya.indotiki.interfaces.gmaps.PlaceNearbyInterface;
import com.pmberjaya.indotiki.interfaces.main.VersionAndMaintenanceInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.io.ApiInterface;
import com.pmberjaya.indotiki.io.ErrorUtils;
import com.pmberjaya.indotiki.io.RestClientNonApi;
import com.pmberjaya.indotiki.io.RestPhp;
import com.pmberjaya.indotiki.dao.SessionManager;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by edwin on 4/23/2016.
 */
public class UtilityController extends BaseController{
    private static UtilityController _instance;
    private static String base_Url = Config.APP_API_URL;
    private Context context;
    private String message="ERROR";
    private SessionManager session;
    private String sukses;
    private ProgressDialog dialog;
    protected UtilityController(Context paramContext)
    {
        super(paramContext);
        this.context = paramContext;
    }

    public static UtilityController getInstance(Context paramContext)
    {
        if (_instance == null) {
            _instance = new UtilityController(paramContext);
        }
        return _instance;
    }
    public void getGeocoderLocation(Map<String, String>  parameters, final GeocoderLocationInterface geocoderLocationInterface){
        ApiInterface service = RestPhp.getClient();
        Call<GeocoderLocationGMapsCallback> call = service.getGeocodeLocation(parameters);
        call.enqueue(new Callback<GeocoderLocationGMapsCallback>() {
            @Override
            public void onResponse(Call<GeocoderLocationGMapsCallback> call, Response<GeocoderLocationGMapsCallback> response) {
                if (response.isSuccessful()) {
                    GeocoderLocationGMapsCallback Data = response.body();
                    geocoderLocationInterface.onSuccessGetGeocoderLocation(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    geocoderLocationInterface.onErrorGetGeocoderLocation(error);
                }
            }

            @Override
            public void onFailure(Call<GeocoderLocationGMapsCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                geocoderLocationInterface.onErrorGetGeocoderLocation(error);
            }
        });
    }

    public void getGeocoderLocationMap(Map<String, String>  parameters, final GeocoderLocationInterface geocoderLocationInterface){
        ApiInterface service = RestPhp.getClient();
        Call<GeocoderLocationGMapsCallback> call = service.getGeocodeLocation(parameters);
        call.enqueue(new Callback<GeocoderLocationGMapsCallback>() {
            @Override
            public void onResponse(Call<GeocoderLocationGMapsCallback> call, Response<GeocoderLocationGMapsCallback> response) {
                if (response.isSuccessful()) {
                    GeocoderLocationGMapsCallback Data = response.body();
                    geocoderLocationInterface.onSuccessGetGeocoderLocation(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    geocoderLocationInterface.onErrorGetGeocoderLocation(error);
                }
            }

            @Override
            public void onFailure(Call<GeocoderLocationGMapsCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                geocoderLocationInterface.onErrorGetGeocoderLocation(error);
            }
        });
    }
    public Map<String, String> geocoderParams(String origins) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("latlng", origins);
        params.put("sensor", "true");
        params.put("key", Config.SERVER_API_KEY);
        return  params;
    }

    public void getcurrentaddress(Map<String, String>  parameters, final GeocoderLocationInterface currentAddressInterface){
        ApiInterface service = RestPhp.getClient();
        Call<GeocoderLocationGMapsCallback> call = service.getGeocodeLocation(parameters);
        call.enqueue(new Callback<GeocoderLocationGMapsCallback>() {
            @Override
            public void onResponse(Call<GeocoderLocationGMapsCallback> call, Response<GeocoderLocationGMapsCallback> response) {
                if (response.isSuccessful()) {
                    GeocoderLocationGMapsCallback Data = response.body();
                    currentAddressInterface.onSuccessGetGeocoderLocation(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    currentAddressInterface.onErrorGetGeocoderLocation(error);
                }
            }

            @Override
            public void onFailure(Call<GeocoderLocationGMapsCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                currentAddressInterface.onErrorGetGeocoderLocation(error);
            }
        });
    }

    public void getDistanceTimePlace(Map<String, String>  parameters, final DistanceTimeInterface distanceTimeInterface){
        ApiInterface service = RestPhp.getClient();
        Call<DistanceTimeGMapsCallback> call = service.getDistanceTimePlace(parameters);
        call.enqueue(new Callback<DistanceTimeGMapsCallback>() {
            @Override
            public void onResponse(Call<DistanceTimeGMapsCallback> call, Response<DistanceTimeGMapsCallback> response) {
                if (response.isSuccessful()) {
                    DistanceTimeGMapsCallback Data = response.body();
                    distanceTimeInterface.onSuccessGetDistanceTime(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    distanceTimeInterface.onErrorGetDistanceTime(error);
                }
            }

            @Override
            public void onFailure(Call<DistanceTimeGMapsCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                distanceTimeInterface.onErrorGetDistanceTime(error);
            }
        });
    }

    public void getDirectionRoute(Map<String, String>  parameters, final DirectionRouteInterface directionRouteInterface){
        ApiInterface service = RestPhp.getClient();
        Call<DirectionRouteGMapsCallback> call = service.getDirectionRoute(parameters);
        call.enqueue(new Callback<DirectionRouteGMapsCallback>() {
            @Override
            public void onResponse(Call<DirectionRouteGMapsCallback> call, Response<DirectionRouteGMapsCallback> response) {
                if (response.isSuccessful()) {
                    DirectionRouteGMapsCallback Data = response.body();
                    directionRouteInterface.onSuccessGetDirectionRoute(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    directionRouteInterface.onErrorGetDirectionRoute(error);
                }
            }

            @Override
            public void onFailure(Call<DirectionRouteGMapsCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                directionRouteInterface.onErrorGetDirectionRoute(error);
            }
        });
    }

    public void getPlaceNearby(Map<String, String>  parameters, final PlaceNearbyInterface placeNearbyInterface){
        ApiInterface service = RestPhp.getClient();
        Call<PlaceNearbyGmapsCallback> call = service.getPlaceNearby(parameters);
        call.enqueue(new Callback<PlaceNearbyGmapsCallback>() {
            @Override
            public void onResponse(Call<PlaceNearbyGmapsCallback> call, Response<PlaceNearbyGmapsCallback> response) {
                if (response.isSuccessful()) {
                    PlaceNearbyGmapsCallback Data = response.body();
                    placeNearbyInterface.onSuccessGetPlaceNearby(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    placeNearbyInterface.onErrorGetPlaceNearby(error);
                }
            }

            @Override
            public void onFailure(Call<PlaceNearbyGmapsCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                placeNearbyInterface.onErrorGetPlaceNearby(error);
            }
        });
    }

    public void getPlaceDetail(Map<String, String>  parameters, final PlaceDetailInterface placeDetailInterface){
        ApiInterface service = RestPhp.getClient();
        Call<PlaceDetailGmapsCallback> call = service.getPlaceDetail(parameters);
        call.enqueue(new Callback<PlaceDetailGmapsCallback>() {
            @Override
            public void onResponse(Call<PlaceDetailGmapsCallback> call, Response<PlaceDetailGmapsCallback> response) {
                if (response.isSuccessful()) {
                    PlaceDetailGmapsCallback Data = response.body();
                    placeDetailInterface.onSuccessGetPlaceDetail(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    placeDetailInterface.onErrorGetPlaceDetail(error);
                }
            }

            @Override
            public void onFailure(Call<PlaceDetailGmapsCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                placeDetailInterface.onErrorGetPlaceDetail(error);
            }
        });
    }

    public void getVersionAndMaintenance(Map<String, String>  parameters, final VersionAndMaintenanceInterface versionAndMaintenanceInterface){
        ApiInterface service = RestClientNonApi.getClient();
        Call<VersionAndMaintenanceCallback> call = service.getVersionAndMaintenance(parameters);
        call.enqueue(new Callback<VersionAndMaintenanceCallback>() {
            @Override
            public void onResponse(Call<VersionAndMaintenanceCallback> call, Response<VersionAndMaintenanceCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    VersionAndMaintenanceCallback versionAndMaintenanceCallback = response.body();
                    versionAndMaintenanceInterface.onSuccessVersionAndMaintenance(versionAndMaintenanceCallback);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    versionAndMaintenanceInterface.onErrorVersionAndMaintenance(error);
                }
            }
            @Override
            public void onFailure(Call<VersionAndMaintenanceCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                versionAndMaintenanceInterface.onErrorVersionAndMaintenance(error);
            }
        });
    }
}

