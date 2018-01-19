package com.pmberjaya.indotiki.app.bookingNew.place;
 
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.bookingNew.place.PlaceSelectionFavoriteAdapter;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.interfaces.bookingNew.SearchPlaceInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class PlaceSelectionFavoriteFragment extends Fragment implements SearchPlaceInterface {
    /************************************************-------Database&Session-----------**********************************************************************/
    DBController controller;
/************************************************************************************************************************************************/
/************************************************-------URL & JSON-----------**********************************************************************/
    /************************************************************************************************************************************************/
    ArrayList<HashMap<String, String>> FavoritePlace;
    LinearLayout loadinglayout;
    LinearLayout no_favorite;
    private ListView list;
    String userId;
    String latitude_gps;
    String longitude_gps;
    String transportationData;
    int banyakdata;
    PlaceSelectionFavoriteAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.place_selection_favorite_fragment, container, false);
        renderView(rootView);
        PlaceSelectionTab placeSelectionTab= (PlaceSelectionTab) getActivity();
        placeSelectionTab.setSearchFavoriteInterface(this);


//        atv_places = (EditText) rootView.findViewById(R.id.atv_places);
//        atv_places.addTextChangedListener(new TextWatcher(){
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                // TODO Auto-generated method stub

//
//            }
//            private Timer timer=new Timer();
//            private final long DELAY = 500; // milliseconds
//            @Override
//            public void afterTextChanged(final Editable thelocation) {
//					/*timer.cancel();
//
//                    timer = new Timer();
//                    timer.schedule(new TimerTask() {
//
//                        public void run() {
//                        	getActivity().runOnUiThread(new Runnable() {
//    	                        @Override
//    	                        public void run() {
//									location=thelocation.toString();
//									controller.openDataBase();
//					        		FavoritePlace = controller.getFavoriteTransportOrder(location);
//					        		controller.close();
//					        		 banyakdata = FavoritePlace.size();
//					     	        FavoritePlaceData = new ArrayList<HashMap<String,String>>();
//					     	        for(int i=0; i<FavoritePlace.size(); i++){
//					         			String lat = FavoritePlace.get(i).get("lat");
//					         			String lng = FavoritePlace.get(i).get("lng");
//
//					         			placesDownloadTask = new DownloadTask(FavoritePlace.get(i));
//					               		String url = getPlaceDistanceUrl(latitude_gps, longitude_gps, lat, lng);
//					               		 Log.d("URLNYAAA",url+"");
//					                     placesDownloadTask.execute(url);
//					     	        }
//    	                        }
//                        	});
//                        }
//                    }, DELAY);*/
//            }
//
//        });
        getBundle();
//        init();
        return rootView;

    }

    private void getBundle(){
        Bundle bundle = this.getArguments();
        transportationData=bundle.getString("transportation");
        latitude_gps =bundle.getString("latitude");
        longitude_gps =bundle.getString("longitude");
    }
    public void init(){
        loadinglayout.setVisibility(View.GONE);
        if(FavoritePlace.size()!=0) {
            for (int i = 0; i < FavoritePlace.size(); i++) {
                double lat = Double.parseDouble(FavoritePlace.get(i).get("lat"));
                double lng = Double.parseDouble(FavoritePlace.get(i).get("lng"));

                String distanceText = distance(Double.parseDouble(latitude_gps), Double.parseDouble(longitude_gps), lat, lng).substring(0, 5) + " km";
                FavoritePlace.get(i).put("distance", distanceText);
                FavoritePlace.get(i).put("favorite", "true");

            }
            SetListViewAdapter(FavoritePlace);
            list.setOnItemClickListener(itemListViewClickListener);
        }
        else{
            no_favorite.setVisibility(View.VISIBLE);
        }
    }
    public void initDB(String strQuery){
        loadinglayout.setVisibility(View.VISIBLE);
        no_favorite.setVisibility(View.GONE);
        controller = DBController.getInstance(getActivity());
        FavoritePlace = controller.getFavoriteTransportOrder(strQuery);
        banyakdata = FavoritePlace.size();
        controller.close();
    }
    private OnItemClickListener itemListViewClickListener = new OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {

            HashMap<String,String> map = adapter.getItemdata().get(position);
            String place = map.get("place");
            String place_detail = map.get("place_details");
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.LATITUDE,map.get("lat"));
            returnIntent.putExtra(Constants.LONGITUDE,map.get("lng"));
            returnIntent.putExtra(Constants.PLACE,place);
            returnIntent.putExtra(Constants.PLACE_DETAILS,place_detail);
            getActivity().setResult(getActivity().RESULT_OK,returnIntent);
            getActivity().finish();

        }

    };
    private void renderView(View rootView){
        list = (ListView) rootView.findViewById(R.id.locationfrequentlist);
        no_favorite= (LinearLayout) rootView.findViewById(R.id.no_favorite);
        loadinglayout= (LinearLayout) rootView.findViewById(R.id.loadinglayout);
    }

    public void SetListViewAdapter(ArrayList<HashMap<String, String>> jalanDetilArray) {
        list.setVisibility(View.VISIBLE);
        adapter = new PlaceSelectionFavoriteAdapter(getActivity(), 0, jalanDetilArray);
        list.setAdapter(adapter);
    }
    private String distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return String.valueOf(dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    private Timer timer=new Timer();
    private final long DELAY = 1000; // milliseconds

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(final Editable thelocation) {
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if(getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (thelocation.length() > 1) {
                                initDB(thelocation.toString());
                                init();
                            } else if (thelocation.length() == 0) {
                                initDB(null);
                                init();
                            }
                        }
                    });
                }
            }
        }, DELAY);
    }
}


