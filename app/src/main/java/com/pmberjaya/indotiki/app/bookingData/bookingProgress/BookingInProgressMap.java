package com.pmberjaya.indotiki.app.bookingData.bookingProgress;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.DirectionRouteGMapsCallback;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.controllers.UtilityController;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.interfaces.gmaps.DirectionRouteInterface;
import com.pmberjaya.indotiki.models.gmaps.DirectionRouteGmapsData.Legs;
import com.pmberjaya.indotiki.models.gmaps.DirectionRouteGmapsData.PolylineData;
import com.pmberjaya.indotiki.models.gmaps.DirectionRouteGmapsData.Routes;
import com.pmberjaya.indotiki.models.gmaps.DirectionRouteGmapsData.Steps;
import com.pmberjaya.indotiki.models.bookingData.DriverPositionMapData;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingInProgressMap extends BaseActivity implements OnMapReadyCallback {
    protected Toolbar toolbar;
    public Runnable runnable;
    public Handler handler;
    String driver_id;
    String driver_number_plate;
    String driver_name;
    String latitude_from;
    String longitude_from;
    String latitude_to;
    String longitude_to;
    String requestType;
    String driver_phone;
    String driver_avatar;
    private Marker markerDriver;
    String transportation;
    private float zoomPosition = 13.0f;
    private Marker marker;
    private String markerDriverId;
    private String markerId;
    private double latitude_driver;
    private double longitude_driver;
    Marker lastOpenned = null;
    private GoogleMap googleMap;
    private boolean getPosition = true;
    private Firebase firebase;
    SessionManager session;
    private String requestId;
    private ImageButton bt_traffic;
    private String toMarkerId;
    private String fromMarkerId;
    private String from_detail;
    private String from_place;
    private String to_place;
    private String to_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_in_progress_map);
        initToolbar();
        renderView();
        getIntentExtra();
        initializeMap();
        setFirebase();
        getDirectionRoute(latitude_from,longitude_from,latitude_to,longitude_to);
    }

    private void renderView() {
        bt_traffic = (ImageButton) findViewById(R.id.bt_traffic);
    }

    public void setFirebase(){
        session = new SessionManager(this);
        Firebase.setAndroidContext(this);
        if(session!=null) {
            String id = session.getRegistrationId();
            //Creating a firebase object
            firebase = new Firebase(Config.FIREBASE_APP + id);
            if (firebase != null) {
                firebase.addValueEventListener(firebaseListener);
            }
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.booking_map));
        }
    }

    private void getIntentExtra() {
        Intent i = getIntent();
        driver_id = i.getStringExtra("driver_id");
        driver_number_plate = i.getStringExtra("driver_plate_number");
        driver_name = i.getStringExtra("driver_name");
        driver_phone = i.getStringExtra("driver_phone");
        driver_avatar = i.getStringExtra("driver_avatar");
        latitude_from = i.getStringExtra("lat_from");
        longitude_from = i.getStringExtra("lng_from");
        latitude_to = i.getStringExtra("lat_to");
        longitude_to = i.getStringExtra("lng_to");
        requestId = i.getStringExtra("request_id");
        requestType = i.getStringExtra("request_type");
        transportation = i.getStringExtra("transportation");
        from_place = i.getStringExtra("from_place");
        from_detail= i.getStringExtra("from_detail");
        to_place = i.getStringExtra("to_place");
        to_detail= i.getStringExtra("to_detail");
    }

    private void getDriverPosition(String driver_id) {
        getPosition = true;
        String api = Utility.getInstance().getTokenApi(BookingInProgressMap.this);
        BookingController.getInstance(BookingInProgressMap.this).getDriverPositionMap(driver_id, api, driverPositionInterface);
        return;
    }

    BaseGenericInterface driverPositionInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> driverPositionMapCallback) {
            int sukses = driverPositionMapCallback.getSukses();
            if (sukses == 2) {
                DriverPositionMapData driverPositionMapData = (DriverPositionMapData) driverPositionMapCallback.getData();

                if (driverPositionMapData != null) {
                    String lat = driverPositionMapData.getLat();
                    String lng = driverPositionMapData.getLng();

                    final double latitude = Double.parseDouble(lat);
                    final double longitude = Double.parseDouble(lng);

                    latitude_driver = latitude;
                    longitude_driver = longitude;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (markerDriver != null) {
                                markerDriver.remove();
                            }
                            MarkerOptions markerDriverOption = null;
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoomPosition));
                            if (requestType.equals("transport")) {
                                if (transportation.equals("motorcycle_taxi")) {
                                    markerDriverOption = new MarkerOptions().position(
                                            new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_motorcycle_top)).anchor(0.5f, 0.5f);
                                } else if (transportation.equals("pedicab")) {
                                    markerDriverOption = new MarkerOptions().position(
                                            new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_pedicab_top));
                                }
                            } else if (requestType.equals("courier")) {
                                if (transportation.equals("motorcycle_taxi")) {
                                    markerDriverOption = new MarkerOptions().position(
                                            new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_motorcycle_top)).anchor(0.5f, 0.5f);
                                } else if (transportation.equals("pedicab")) {
                                    markerDriverOption = new MarkerOptions().position(
                                            new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_pedicab_top));
                                }
                            } else if (requestType.equals("food")) {
                                if (transportation.equals("motorcycle_taxi")) {
                                    markerDriverOption = new MarkerOptions().position(
                                            new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_motorcycle_top)).anchor(0.5f, 0.5f);
                                } else if (transportation.equals("pedicab")) {
                                    markerDriverOption = new MarkerOptions().position(
                                            new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_pedicab_top));
                                }
                            }
                            markerDriver = googleMap.addMarker(markerDriverOption);
                            markerDriverId = markerDriver.getId();
                        }
                    });

                }
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(BookingInProgressMap.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    getDriverPosition(driver_id);
                }else{
                    getDriverPosition(driver_id);
                }
            }
        }
    };


    public void initializeMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Enable / Disable zooming functionality

    }

    public void getDirectionRoute(String latitude_from, String longitude_from, String latitude_to, String longitude_to) {
            //String api = Utility.getInstance().getTokenApi(BookingInProgressDetail.this);
            //String output = "json";
            String origin = latitude_from + "," + longitude_from;
            String destinations = latitude_to + "," + longitude_to;
            UtilityController.getInstance(BookingInProgressMap.this).getDirectionRoute(directionParameters(origin, destinations), directionRouteInterface);
            return;
    }

    public Map<String, String> directionParameters(String origins, String destinations) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("origin", origins);
        params.put("destination", destinations);
        params.put("avoid", "tolls");
        params.put("sensor", "false");
        params.put("key", Config.SERVER_API_KEY);
        return params;
    }
    DirectionRouteInterface directionRouteInterface = new DirectionRouteInterface() {
        @Override
        public void onSuccessGetDirectionRoute(DirectionRouteGMapsCallback directionRouteGMapsCallback) {
            String status = directionRouteGMapsCallback.getStatus();
            if (status.equals("OK")) {
                List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
                List<Routes> routesArray = directionRouteGMapsCallback.getRoutes();
                ArrayList<LatLng> list = new ArrayList<LatLng>();
                PolylineOptions lineOptions = null;
                for (int i = 0; i < routesArray.size(); i++) {
                    List<Legs> legsArray = routesArray.get(i).getLegs();
                    List path = new ArrayList<HashMap<String, String>>();

                    /** Traversing all legs */
                    for (int j = 0; j < legsArray.size(); j++) {
                        List<Steps> stepsArray = legsArray.get(j).getSteps();
                        /** Traversing all steps */
                        for (int k = 0; k < stepsArray.size(); k++) {
                            lineOptions = new PolylineOptions();
                            PolylineData polyline = stepsArray.get(k).getPolyline();
                            String points = polyline.getPoints();
                            list.addAll(decodePoly(points));
                            /** Traversing all points */

                        }
                        lineOptions.addAll(list);
                        lineOptions.width(3);
                        lineOptions.color(Color.RED);
                    }
                }
                googleMap.addPolyline(lineOptions);

            } else {
                getDirectionRoute(latitude_from,longitude_from,latitude_to,longitude_to);
            }
        }

        @Override
        public void onErrorGetDirectionRoute(APIErrorCallback data) {
            getDirectionRoute(latitude_from,longitude_from,latitude_to,longitude_to);
        }
    };

    private ArrayList<LatLng> decodePoly(String encoded) {

        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.booking_in_progress_detail_menu, menu);
        return true;
    }


    //	public static int getZoomLevel(double lat1, double lon1, double lat2, double lon2) {
//		double distance = Util.getDistance(lat1, lon1, lat2, lon2, 'K');
//		driverDistance = (double) Utils.getDistanceInMeter(lat1, lon1, lat2, lon2);
//		if (distance > 15.0d) {
//			return 10;
//		}
//		if (distance > 10.0d) {
//			return 11;
//		}
//		if (distance > 5.0d) {
//			return 12;
//		}
//		if (distance > 2.7d) {
//			return 13;
//		}
//		if (distance > 1.0d) {
//			return 14;
//		}
//		return 15;
//	}
    public ValueEventListener firebaseListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            Log.d("SERVICE DATA CHANGE","RUNNING");
            if (snapshot.getValue() == null)
                return;
            if(snapshot.child(requestId).getValue() != null) {
                try {
                    String location = snapshot.child(requestId).child("l").getValue().toString();
                    location = location.replace("[", "");
                    location = location.replace("]", "");
                    location = location.replace(",", "");
                    String[] part = location.split(" ");
                    String destlatitude = part[0];
                    String destlongitude = part[1];
                    MarkerOptions markerDriverOption = null;
                    if (requestType.equals("transport")) {
                        if (transportation.equals("motorcycle_taxi")) {
                            markerDriverOption = new MarkerOptions().position(
                                    new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_motorcycle_top)).anchor(0.5f, 0.5f);
                        } else if (transportation.equals("pedicab")) {
                            markerDriverOption = new MarkerOptions().position(
                                    new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_pedicab_top));
                        }
                    } else if (requestType.equals("courier")) {
                        if (transportation.equals("motorcycle_taxi")) {
                            markerDriverOption = new MarkerOptions().position(
                                    new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_motorcycle_top)).anchor(0.5f, 0.5f);
                        } else if (transportation.equals("pedicab")) {
                            markerDriverOption = new MarkerOptions().position(
                                    new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_pedicab_top));
                        }
                    }  else if (requestType.equals("food")) {
                        if (transportation.equals("motorcycle_taxi")) {
                            markerDriverOption = new MarkerOptions().position(
                                    new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_motorcycle_top)).anchor(0.5f, 0.5f);
                        } else if (transportation.equals("pedicab")) {
                            markerDriverOption = new MarkerOptions().position(
                                    new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_pedicab_top));
                        } else if (transportation.equals("car")) {
                            markerDriverOption = new MarkerOptions().position(
                                    new LatLng(latitude_driver, longitude_driver)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_pedicab_top)).anchor(0.5f,0.5f);
                        }
                    }
//					MarkerOptions markerOptions = new MarkerOptions().position();
                    LatLng destlatlng = new LatLng(Double.parseDouble(destlatitude), Double.parseDouble(destlongitude));
                    if (markerDriver != null) {
                        animateMarker(destlatlng, false);
                        LatLng rscLatlng = markerDriver.getPosition();
                        double bearing = pivotMarker(rscLatlng, destlatlng);
                        rotateMarker((float) bearing, markerDriver.getRotation());
                    } else {
                        markerDriver = googleMap.addMarker(markerDriverOption);
                    }
                } catch (Exception e) {
                }
            }
        }
        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("The read failed: ", firebaseError.getMessage());
        }
    };

    private double pivotMarker(LatLng myLatLng, LatLng toLatLng) {
        double PI = 3.14159;
        double lat1 = myLatLng.latitude * PI / 180;
        double long1 = myLatLng.longitude * PI / 180;
        double lat2 = toLatLng.latitude * PI / 180;
        double long2 = toLatLng.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    public void rotateMarker(final float toRotation, final float st) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = st;
        final long duration = 1000;
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;

                markerDriver.setRotation(-rot > 180 ? rot / 2 : rot);
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    public void animateMarker(final LatLng toPosition, final boolean hideMarke) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(BookingInProgressMap.this.markerDriver.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 5000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                markerDriver.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarke) {
                        markerDriver.setVisible(false);
                    } else {
                        markerDriver.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        registerReceiver(locationReceiver, new IntentFilter(Config.BROADCAST_REALTIME_LOCATION));
        if (getPosition == true) {
            getDriverPosition(driver_id);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(firebase!=null) {
            firebase.removeEventListener(firebaseListener);
        }
//        unregisterReceiver(locationReceiver);
    }
    Marker fromMarker;
    Marker toMarker;
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        bt_traffic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(googleMap.isTrafficEnabled()){
                    googleMap.setTrafficEnabled(false);
                }else{
                    googleMap.setTrafficEnabled(true);
                }
            }
        });
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitude_to), Double.parseDouble(longitude_to)), zoomPosition));
        googleMap.setMyLocationEnabled(true);
//        getDirectionRoute(latitude_from, longitude_from, latitude_to, longitude_to);
//		getDriverPosition(driver_id);

        MarkerOptions marker_place = new MarkerOptions().position(
                new LatLng(Double.parseDouble(latitude_from), Double.parseDouble(longitude_from))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_from_place));
        fromMarker = googleMap.addMarker(marker_place);
        fromMarkerId = fromMarker.getId();
        MarkerOptions marker_place2 = new MarkerOptions().position(
                new LatLng(Double.parseDouble(latitude_to), Double.parseDouble(longitude_to))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_to_place));
        toMarker = googleMap.addMarker(marker_place2);
        toMarkerId = toMarker.getId();

        googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (cameraPosition.zoom != zoomPosition) {
                    zoomPosition = cameraPosition.zoom;

                }
            }
        });
        googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(final Marker marker) {
                markerId = marker.getId();
                Log.d("markerId", "" + markerId);
                Log.d("markerDriverId", "" + markerDriverId);

                if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(markerDriverId)&&markerDriverId.equals(markerId)) {
                    BookingInProgressMap.this.marker = marker;
                    // Getting view from the layout file info_window_layout;
                    View v = getLayoutInflater().inflate(R.layout.booking_in_progress_marker_dialog, null);

                    TextView platNumber = (TextView) v.findViewById(R.id.platNumber);
                    if (driver_number_plate != null) {
                        platNumber.setText(driver_number_plate);
                    }
                    ImageView fotoprofil = (ImageView) v.findViewById(R.id.fotoprofil);
                    String link_image = driver_avatar;
                    PicassoLoader.loadProfile(BookingInProgressMap.this, link_image, fotoprofil, R.mipmap.img_no_avatar_driver);
                    TextView txtNama = (TextView) v.findViewById(R.id.txtNama);
                    txtNama.setText(driver_name);
                    TextView txtNohp = (TextView) v.findViewById(R.id.txtNohp);
                    txtNohp.setText(driver_phone);
                    return v;
                } else if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(fromMarkerId)&&fromMarkerId.equals(markerId)){
                    BookingInProgressMap.this.marker = marker;
                    // Getting view from the layout file info_window_layout;
                    View v = getLayoutInflater().inflate(R.layout.booking_in_progress_marker_dialog, null);
                    TextView txtNama = (TextView) v.findViewById(R.id.txtNama);
                    txtNama.setVisibility(View.GONE);
                    ImageView fotoprofil = (ImageView) v.findViewById(R.id.fotoprofil);
                    fotoprofil.setVisibility(View.GONE);
                    ImageView garis = (ImageView) v.findViewById(R.id.garis);
                    TextView fromPlace = (TextView) v.findViewById(R.id.platNumber);
                    TextView fromDetail = (TextView) v.findViewById(R.id.txtNohp);
                    if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(from_place)){
                        fromPlace.setText(from_place);
                        fromDetail.setText(from_detail);
                    }else{
                        fromPlace.setText(from_detail);
                        fromDetail.setVisibility(View.GONE);
                        garis.setVisibility(View.GONE);
                    }
                    return v;
                } else if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(toMarkerId)&&toMarkerId.equals(markerId)){
                    BookingInProgressMap.this.marker = marker;
                    // Getting view from the layout file info_window_layout;
                    View v = getLayoutInflater().inflate(R.layout.booking_in_progress_marker_dialog, null);
                    TextView txtNama = (TextView) v.findViewById(R.id.txtNama);
                    txtNama.setVisibility(View.GONE);
                    ImageView fotoprofil = (ImageView) v.findViewById(R.id.fotoprofil);
                    fotoprofil.setVisibility(View.GONE);
                    ImageView garis = (ImageView) v.findViewById(R.id.garis);
                    TextView toPlace = (TextView) v.findViewById(R.id.platNumber);
                    TextView toDetail = (TextView) v.findViewById(R.id.txtNohp);
                    if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(to_place)){
                        toPlace.setText(to_place);
                        toDetail.setText(to_detail);
                    }else{
                        toPlace.setText(to_detail);
                        toDetail.setVisibility(View.GONE);
                        garis.setVisibility(View.GONE);
                    }

                    return v;
                }else{
                    return null;
                }
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {
                if (BookingInProgressMap.this.marker != null
                        && BookingInProgressMap.this.marker.isInfoWindowShown()) {
                    BookingInProgressMap.this.marker.hideInfoWindow();
                    BookingInProgressMap.this.marker.showInfoWindow();
                }
                return null;
            }
        });

        googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                // Check if there is an open info window
                if (lastOpenned != null) {
                    // Close the info window
                    lastOpenned.hideInfoWindow();

                    // Is the marker the same marker that was already open
                    if (lastOpenned.equals(marker)) {
                        // Nullify the lastOpenned object
                        lastOpenned = null;
                        // Return so that the info window isn't openned again
                        return true;
                    }
                }

                // Open the info window for the marker
                if (latitude_driver != 0.0 && longitude_driver != 0.0) {
                    marker.showInfoWindow();
                }
                // Re-assign the last openned such that we can close it later
                lastOpenned = marker;

                // Event was handled by our code do not launch default behaviour.
                return true;
            }
        });
    }
}