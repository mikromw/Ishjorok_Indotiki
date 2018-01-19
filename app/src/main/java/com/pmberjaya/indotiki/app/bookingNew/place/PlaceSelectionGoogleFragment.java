package com.pmberjaya.indotiki.app.bookingNew.place;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.config.Config;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.callbacks.gmaps.PlaceDetailGmapsCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.PlaceNearbyGmapsCallback;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.controllers.UtilityController;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.interfaces.bookingNew.SearchPlaceInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.interfaces.gmaps.PlaceDetailInterface;
import com.pmberjaya.indotiki.interfaces.gmaps.PlaceNearbyInterface;
import com.pmberjaya.indotiki.models.gmaps.PlaceDetailGmapsData.GeometryDetail;
import com.pmberjaya.indotiki.models.gmaps.PlaceDetailGmapsData.LocationDetail;
import com.pmberjaya.indotiki.models.gmaps.PlaceDetailGmapsData.ResultDetail;
import com.pmberjaya.indotiki.models.gmaps.PlaceNearbyGmapsData.Geometry;
import com.pmberjaya.indotiki.models.gmaps.PlaceNearbyGmapsData.Location;
import com.pmberjaya.indotiki.models.gmaps.PlaceNearbyGmapsData.Result;
import com.pmberjaya.indotiki.models.bookingNew.PlaceSearchData;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PlaceSelectionGoogleFragment extends Fragment implements SearchPlaceInterface {
	/************************************************-------Database&Session-----------**********************************************************************/
	SessionManager session;
	DBController controller;
/************************************************************************************************************************************************/
/************************************************-------URL & JSON-----------**********************************************************************/
	/************************************************************************************************************************************************/
	ArrayList<HashMap<String,String>> FavoritePlace;
	private String latitude_gps;
	private String longitude_gps;
	LinearLayout loadinglayout;
	boolean firstVisible = false;
	private ListView list;
	String location="";
	String userId;
	String activity;
	String state;
	private Timer timer=new Timer();
	private double lat_constant =3.58941805;
	private double lng_constant =98.67576956;
	private ArrayList<PlaceSearchData> DataGPSList;
	PlaceSelectionGoogleAdapter adapter;
	private String request_type;
	private String autoload;
	private TextView instructionText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.place_selection_google_fragment, container, false);
		Bundle bundle = this.getArguments();
		activity =bundle.getString(Constants.PLACE_TYPE);
		latitude_gps =bundle.getString(Constants.LATITUDE);
		longitude_gps =bundle.getString(Constants.LONGITUDE);
		state =bundle.getString(Constants.STATE);
		request_type = bundle.getString(Constants.REQUEST_TYPE);
		autoload = bundle.getString(Constants.AUTOLOAD);
		PlaceSelectionTab placeSelectionTab = (PlaceSelectionTab) getActivity();
		placeSelectionTab.setSearchPlaceInterface(this);

		controller = DBController.getInstance(getActivity());
		controller.openDataBase();

		FavoritePlace = controller.getFavoritePlaceId("google");
		controller.close();

		session = new SessionManager(getActivity());
		final HashMap<String,String> map= session.getUserDetails();
		userId = map.get(session.KEY_ID);
		list = (ListView) rootView.findViewById(R.id.locationgooglelist);
		instructionText = (TextView) rootView.findViewById(R.id.instructionText);
		list.setOnItemClickListener(listItemClickListener);
		loadinglayout= (LinearLayout) rootView.findViewById(R.id.loadinglayout);

		if(autoload!=null&&autoload.equals("true")){
			if(isLatLngBeUsed())
			{
				loadinglayout.setVisibility(View.VISIBLE);
			}
			else
			{
				loadinglayout.setVisibility(View.GONE);
			}
		}
		else{
			loadinglayout.setVisibility(View.GONE);
			instructionText.setVisibility(View.VISIBLE);
		}
        PlaceNearby();
		return rootView;

	}


	public ArrayList<PlaceSearchData> getHistoryPlaceDB(String originOrDestination){
		return controller.getPlaceHistory(originOrDestination, request_type);
	}
	public void PlaceNearby(){
		if(autoload!=null&&autoload.equals("true")){
			if(isLatLngBeUsed()) {
				getPlaceNearby(latitude_gps, longitude_gps);
			}
		}else {
            SetListViewAdapterFromDB();
        }
	}

	private OnItemClickListener listItemClickListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
			PlaceSearchData placeSearchData = adapter.getItemdata().get(position);
			if(adapter.getItemViewType(position)!=adapter.TYPE_SEPARATOR) {
				String place = placeSearchData.getPlace();
				String place_detail = placeSearchData.getPlace_details();
				String lat = placeSearchData.getLat();
				String lng = placeSearchData.getLng();
				Intent returnIntent = new Intent();
				returnIntent.putExtra(Constants.LATITUDE, lat);
				returnIntent.putExtra(Constants.LONGITUDE, lng);
				returnIntent.putExtra(Constants.PLACE, place);
				returnIntent.putExtra(Constants.PLACE_DETAILS, place_detail);
				getActivity().setResult(getActivity().RESULT_OK, returnIntent);
				getActivity().finish();
			}
		}
	};
	/*@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if(firstVisible!=true){

			}

		}else{
			// fragment is no longer visible
		}
	}*/

	public void SetListViewAdapterFromDB() {
		loadinglayout.setVisibility(View.GONE);
		if(adapter==null) {
			adapter = new PlaceSelectionGoogleAdapter(getActivity());
		}
		ArrayList<PlaceSearchData> bookingHistoryDBArray = getHistoryPlaceDB(activity);
		if(bookingHistoryDBArray!=null&&bookingHistoryDBArray.size()!=0 && location.equals("")) {
			adapter.clearSectionHistoryHeaderItem();
			adapter.clearHistoryItem();
			adapter.addSectionHeaderItem("History");
			adapter.addHistoryItem(getHistoryPlaceDB(activity));
		}
		list.setAdapter(adapter);
	}

	public void SetListViewAdapter(ArrayList<PlaceSearchData> jalanDetilArray) {
		loadinglayout.setVisibility(View.GONE);
		if(adapter==null) {
			adapter = new PlaceSelectionGoogleAdapter(getActivity());
		}
		adapter.removeNoResultView();
		adapter.clearSectionResultHeaderItem();
		adapter.clearItem();
		adapter.addSectionHeaderItem("Result");
		adapter.addItem(jalanDetilArray);
		list.setAdapter(adapter);
	}

	public void getPlaceNearby(String latitude, String longitude)
	{
		//String api = Utility.getInstance().getTokenApi(BookingInProgressDetail.this);
		//String output = "json";
		instructionText.setVisibility(View.GONE);
		String location = latitude+","+longitude;
		String radius = "1000";
		UtilityController.getInstance(getActivity()).getPlaceNearby(nearbyParameters(location, radius), placeNearbyInterface);
		return;
	}
	PlaceNearbyInterface placeNearbyInterface = new PlaceNearbyInterface() {
		@Override
		public void onSuccessGetPlaceNearby(PlaceNearbyGmapsCallback placeNearbyGmapsCallback) {
			String status= placeNearbyGmapsCallback.getStatus();
			if (status.equals("OK")) {
				List<Result> result = placeNearbyGmapsCallback.getResults();
				controller.openDataBase();
				FavoritePlace = controller.getFavoritePlaceId("google");
				controller.close();
				Log.d("sizeeee",">"+String.valueOf(FavoritePlace.size()));
				if(result!=null&&result.size()!=0){
					DataGPSList = new ArrayList<PlaceSearchData>();
					double distance = 0;
					for(int i =0; i<result.size(); i++){
						Geometry geometry = result.get(i).getGeometry();
						Location location = geometry.getLocation();
						String latitude_place =location.getLat();
						String longitude_place =location.getLng();
						if(isLatLngBeUsed()) {
							distance = Utility.getInstance().distance(Double.parseDouble(latitude_gps), Double.parseDouble(longitude_gps), Double.parseDouble(latitude_place), Double.parseDouble(longitude_place));
						}
						else{
							distance = Utility.getInstance().distance(lat_constant, lng_constant, Double.parseDouble(latitude_place), Double.parseDouble(longitude_place));
						}
						String distanceText = String.valueOf(distance).substring(0, 5)+" km";
						if(result.get(i).getVicinity()!=null&&!result.get(i).getVicinity().equals(result.get(i).getName())){
							PlaceSearchData placeSearchData = new PlaceSearchData();
							placeSearchData.setFavorite("false");
							placeSearchData.setId_favorite("");
							for (int j = 0; j < FavoritePlace.size(); j++) {
								String place_id = FavoritePlace.get(j).get("place_id");
								if (result.get(i).getPlace_id().equals(place_id)) {
									placeSearchData.setFavorite("true");
									placeSearchData.setId_favorite(FavoritePlace.get(j).get("favorite_id"));
									break;
								}
							}
							placeSearchData.setPlace(result.get(i).getName());
							placeSearchData.setPlace_details(result.get(i).getVicinity());
							placeSearchData.setLat(latitude_place);
							placeSearchData.setLng(longitude_place);
							placeSearchData.setPlace_id(result.get(i).getPlace_id());
							placeSearchData.setDistance(distanceText);
							DataGPSList.add(placeSearchData);
						}
					}
					SetListViewAdapterFromDB();
					SetListViewAdapter(DataGPSList);

				}
				else{
					loadinglayout.setVisibility(View.GONE);
					adapter.removeNoResultView();
					adapter.setNoResultView();
					adapter.clearItem();
					adapter.addSectionHeaderItem("Result");
				}

			} else {
				loadinglayout.setVisibility(View.GONE);
				adapter.removeNoResultView();
				adapter.setNoResultView();
				adapter.clearItem();
				adapter.addSectionHeaderItem("Result");
			}
		}

		@Override
		public void onErrorGetPlaceNearby(APIErrorCallback apiErrorCallback) {
			if(apiErrorCallback.getError()!=null) {
				Toast.makeText(getActivity(),getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
				loadinglayout.setVisibility(View.GONE);
			}
		}
	};

	public Map<String, String> nearbyParameters(String location, String radius) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("key", Config.SERVER_API_KEY);
		params.put("location", location);
		params.put("radius", radius);
		params.put("sensor", "false");
		return  params;
	}

	public void getPlaceDetail(String place, String state)
	{
		//String api = Utility.getInstance().getTokenApi(BookingInProgressDetail.this);
		//String output = "json";
		String query = place+" in "+state;
		String language ="indonesia";
		String radius = "100";
		UtilityController.getInstance(getActivity()).getPlaceDetail(detailParameter(query, language, radius), placeDetailInterface);
		return;
	}

	public Map<String, String> detailParameter(String query, String language, String radius) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("key", Config.SERVER_API_KEY);
		params.put("query", query);
		params.put("language", language);
		params.put("radius", radius);
		return  params;
	}

	PlaceDetailInterface placeDetailInterface = new PlaceDetailInterface() {
		@Override
		public void onSuccessGetPlaceDetail(PlaceDetailGmapsCallback placeDetailGmapsCallback) {
			String status= placeDetailGmapsCallback.getStatus();
			if (status.equals("OK")) {
				List<ResultDetail> result = placeDetailGmapsCallback.getResults();
				if(result!=null&&result.size()!=0){
					DataGPSList = new ArrayList<PlaceSearchData>();
					double distance = 0;
					for(int i =0; i<result.size(); i++){
						GeometryDetail geometry = result.get(i).getGeometry();
						LocationDetail location = geometry.getLocation();
						String latitude_place =location.getLat();
						String longitude_place =location.getLng();
						if(isLatLngBeUsed()) {
							distance = Utility.getInstance().distance(Double.parseDouble(latitude_gps), Double.parseDouble(longitude_gps), Double.parseDouble(latitude_place), Double.parseDouble(longitude_place));
						}
						else{
							distance = Utility.getInstance().distance(lat_constant, lng_constant, Double.parseDouble(latitude_place), Double.parseDouble(longitude_place));
						}
						String distanceText = String.valueOf(distance).substring(0, 5)+" km";
						if(result.get(i).getFormatted_address()!=null){
							PlaceSearchData placeSearchData = new PlaceSearchData();
							placeSearchData.setFavorite("false");
							placeSearchData.setId_favorite("");
							for (int j = 0; j < FavoritePlace.size(); j++) {
								String place_id = FavoritePlace.get(j).get("place_id");
								if (result.get(i).getPlace_id().equals(place_id)) {
									placeSearchData.setFavorite("true");
									placeSearchData.setId_favorite(FavoritePlace.get(j).get("favorite_id"));
									break;
								}
							}
							placeSearchData.setPlace(result.get(i).getName());
							placeSearchData.setPlace_details(result.get(i).getFormatted_address());
							placeSearchData.setLat(latitude_place);
							placeSearchData.setLng(longitude_place);
							placeSearchData.setPlace_id(result.get(i).getPlace_id());
							placeSearchData.setDistance(distanceText);
							DataGPSList.add(placeSearchData);

						}
					}
					SetListViewAdapter(DataGPSList);
				}
				else{
					loadinglayout.setVisibility(View.GONE);
					adapter.removeNoResultView();
					adapter.setNoResultView();
					adapter.clearItem();
					adapter.addSectionHeaderItem("Result");
				}
			} else {
				loadinglayout.setVisibility(View.GONE);
//                adapter.addSectionHeaderItem("Result");
//				adapter.clearSectionHistoryHeaderItem();
				adapter.clearSectionResultHeaderItem();
				adapter.removeNoResultView();
				adapter.setNoResultView();
				adapter.clearItem();

			}
			adapter.clearHistoryItem();
			adapter.clearSectionHistoryHeaderItem();
		}

		@Override
		public void onErrorGetPlaceDetail(APIErrorCallback apiErrorCallback) {
			if(apiErrorCallback.getError()!=null) {
				Toast.makeText(getActivity(),getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
				loadinglayout.setVisibility(View.GONE);
			}
		}
	};


	public boolean isLatLngBeUsed(){
		if(latitude_gps.equals("0.0")&&longitude_gps.equals("0.0")){
			return false;
		}
		else {
			return true;
		}
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onPause() {
		super.onPause();
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d("Tag", "FragmentA.onDestroyView() has been called.");
		timer.cancel();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}
	private final long DELAY = 1000; // milliseconds
	@Override
	public void afterTextChanged(final Editable thelocation) {
		timer.cancel();
		timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(thelocation.length()>1){
							loadinglayout.setVisibility(View.VISIBLE);
							instructionText.setVisibility(View.GONE);
							String lastCharacterLocation =thelocation.toString().substring(thelocation.length()-1,thelocation.length());
							if(lastCharacterLocation.equals(" ")){
								location = thelocation.toString().substring(0, thelocation.length()-1);
							}
							else {
								location = thelocation.toString();
							}
							controller.openDataBase();
							FavoritePlace = controller.getFavoritePlaceId("google");
							controller.close();
							adapter.clearSectionHistoryHeaderItem();
							adapter.clearSectionResultHeaderItem();
							adapter.clearItem();
							adapter.clearHistoryItem();
							getPlaceDetail(location.toString(),state);
						}
						else if(thelocation.length()==0){
							loadinglayout.setVisibility(View.VISIBLE);
							location="";
							adapter.clearSectionHistoryHeaderItem();
							adapter.clearSectionResultHeaderItem();
							adapter.clearItem();
							adapter.clearHistoryItem();

							if(autoload!=null&&autoload.equals("true")){
								if(isLatLngBeUsed()) {
									getPlaceNearby(latitude_gps, longitude_gps);
								}
								else{
									loadinglayout.setVisibility(View.GONE);
								}
							}
							else{
								loadinglayout.setVisibility(View.GONE);
								instructionText.setVisibility(View.VISIBLE);
								SetListViewAdapterFromDB();
//								SetListViewAdapter(DataGPSList);
//								if(activity.equals("to")){
//									adapter.clearSectionResultHeaderItem();
//									adapter.clearItem();
//								}
								adapter.removeNoResultView();

							}
						}
					}
				});
			}
		}, DELAY);
	}
}
