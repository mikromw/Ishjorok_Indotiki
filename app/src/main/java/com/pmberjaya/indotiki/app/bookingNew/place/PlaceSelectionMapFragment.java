package com.pmberjaya.indotiki.app.bookingNew.place;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.GeocoderLocationGMapsCallback;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.controllers.UtilityController;
import com.pmberjaya.indotiki.interfaces.gmaps.GeocoderLocationInterface;
import com.pmberjaya.indotiki.models.gmaps.GeocoderLocationGmapsData.AddressComponent;
import com.pmberjaya.indotiki.models.gmaps.GeocoderLocationGmapsData.Result;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class PlaceSelectionMapFragment extends Fragment  implements OnMapReadyCallback {
    SupportMapFragment fm;
    double latitude = 3.58941805;
    double longitude = 98.67576956;
    GoogleMap googleMap;
    Button btn_location;
    boolean firstVisible = false;
    private Timer timer=new Timer();
    private final long DELAY = 1000; // milliseconds
    private String lat;
    private String lng;
    private ImageView transparentImageView;
    private boolean mMapIsTouched;
    private SupportMapFragment mapFragment;
    private String address;
    PlaceSelectionTab placeSelectionTab;
    private int btn_location_height=0;
    private UtilityController utilityController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.place_selection_map_fragment, container, false);
        placeSelectionTab = (PlaceSelectionTab) getActivity();
        utilityController =  UtilityController.getInstance(getActivity());
        renderView(rootView);
        getBundle();
        initializeMap();
        return rootView;

    }
    Geocoder geocoder;
    List<Address> addresses;
    private void initializeMap(){
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        mapFragment.getMapAsync(this);
        // Enable / Disable zooming controls
    }
    private void renderView(View rootView){
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        btn_location = (Button) rootView.findViewById(R.id.buttonLocation);
        btn_location.setOnClickListener(setPlace);
        ViewTreeObserver vto = btn_location.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                btn_location.getViewTreeObserver().removeOnPreDrawListener(this);
                int valueInPixels = (int) getResources().getDimension(R.dimen.common_padding_margin);

                int dp = (int) (getResources().getDimension(R.dimen.common_padding_margin) / getResources().getDisplayMetrics().density);
                btn_location_height = btn_location.getMeasuredHeight()+ (2*dp);
                Log.d("", "Height: " + btn_location.getMeasuredHeight()+ " Width: " + btn_location.getMeasuredWidth());
                return true;
            }
        });
        transparentImageView = (ImageView) rootView.findViewById(R.id.transparent_image);
    }
    private void getBundle(){
        Bundle bundle = this.getArguments();
        lat =bundle.getString("latitude");
        lng =bundle.getString("longitude");
    }
    private OnClickListener setPlace = new OnClickListener(){

        @Override
        public void onClick(View v) {
            if(address!=null) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constants.LATITUDE, String.valueOf(latitude));
                returnIntent.putExtra(Constants.LONGITUDE, String.valueOf(longitude));
                returnIntent.putExtra(Constants.PLACE_DETAILS, address);
                getActivity().setResult(getActivity().RESULT_OK, returnIntent);
                getActivity().finish();
            }
        }
    };
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(firstVisible!=true){
                firstVisible = isVisibleToUser;
                EditText blabla = (EditText) getActivity().findViewById(R.id.atv_places);
            }
        }else{
            // fragment is no longer visible
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Tag", "FragmentA.onDestroyView() has been called.");
        timer.cancel();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setPadding(0,0,0,btn_location_height);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setMyLocationEnabled(true);


        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        // Disable touch on transparent view
                        mMapIsTouched = true;
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mMapIsTouched = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mMapIsTouched = false;
                        return false;

                    default:
                        return true;
                }
            }
        });
        // Getting a reference to the AutoCompleteTextView
        if(lat!=null&&!lat.equals("")&&lng!=null&&!lng.equals("")){
            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat),Double.parseDouble(lng)) , 13.0f) );
        }
        else{
            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude) , 13.0f) );
        }
        googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange (CameraPosition position) {
                address=null;
                btn_location.setBackgroundResource(R.drawable.xml_bg_rounded_solid_grey_6dp);
                placeSelectionTab.setAtvPlaces(getResources().getString(R.string.loading_address));
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Get the center of the Map.

                                try {
                                    LatLng centerOfMap = googleMap.getCameraPosition().target;
                                    latitude = centerOfMap.latitude;
                                    longitude = centerOfMap.longitude;
                                    getStateInBackground(String.valueOf(latitude), String.valueOf(longitude));
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }, DELAY);
            }
        });
    }
    public void getStateInBackground(String latitude_from, String longitude_from)
    {
            //String api = Utility.getInstance().getTokenApi(BookingInProgressDetail.this);
            //String output = "json";
            String origin = latitude_from+","+longitude_from;
            utilityController.getGeocoderLocationMap(utilityController.geocoderParams(origin), geocoderLocationInterface);
            return;
    }
    GeocoderLocationInterface geocoderLocationInterface = new GeocoderLocationInterface() {
        @Override
        public void onSuccessGetGeocoderLocation(GeocoderLocationGMapsCallback geocoderLocationGMapsCallback) {
            String status= geocoderLocationGMapsCallback.getStatus();
            if (status.equals("OK")) {
                List<Result> result =  geocoderLocationGMapsCallback.getResults();
                List<AddressComponent> addresses = result.get(0).getAddressComponents();
                String country="";
                for(int i =0; i<addresses.size(); i++){
                    List<String> types = addresses.get(i).getTypes();
                    String type = types.get(0);
                    if(type.equals("country")){
//                        country = addresses.get(i).getLongName();
                    }else if (type.equals("administrative_area_level_2")){
//                        city = addresses.get(i).getLongName();
                    }else if (type.equals("administrative_area_level_1")){
//                        state = addresses.get(i).getLongName();
                    }else if(type.equals("route")){
                        address = addresses.get(i).getLongName();
                    }
                }
                placeSelectionTab.setAtvPlaces(address);
                btn_location.setBackgroundResource(R.drawable.xml_btn_primary);
            } else {
            }
        }

        @Override
        public void onErrorGetGeocoderLocation(APIErrorCallback data) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

}


