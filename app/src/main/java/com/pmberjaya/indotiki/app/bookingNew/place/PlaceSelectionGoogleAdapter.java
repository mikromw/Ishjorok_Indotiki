package com.pmberjaya.indotiki.app.bookingNew.place;

import java.util.ArrayList;
import java.util.HashMap;


import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.models.bookingNew.PlaceSearchData;
import com.pmberjaya.indotiki.dao.SessionManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlaceSelectionGoogleAdapter extends BaseAdapter {
	ArrayList<PlaceSearchData> dataLocation;
	DBController controller;
	Context context;
	private static LayoutInflater inflater = null;

	SessionManager session;
	String userId;

	private ArrayList<Integer> itemType = new ArrayList<Integer>();

	private String id_favorite;

    public PlaceSelectionGoogleAdapter(Context context) {
        controller = DBController.getInstance(context);
        //------------------------------------Session--------------------------------------------------------
        session = new SessionManager(context);
        HashMap<String, String> map = session.getUserDetails();
        userId = map.get(session.KEY_ID);
        //---------------------------------------------------------------------------------------------------
        dataLocation = new ArrayList<>();
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public ArrayList<PlaceSearchData> getItemdata() {
		return dataLocation;
	}
	static class ViewHolder {
		TextView jalan;
		TextView jalanDetil;
		LinearLayout jalanDetilLayout;
		TextView header_type;
		TextView distance;
		ImageView favorite;
        public TextView no_result_txt;
    }
	public static final int TYPE_ITEM = 0;
    public static final int TYPE_HISTORY_ITEM = 1;
    public static final int TYPE_SEPARATOR = 2;
    public static final int TYPE_NO_RESULT = 3;

	@Override
	public int getItemViewType(int position) {
		return itemType.get(position);
	}

	@Override
	public int getViewTypeCount() {
		return 4;
	}

	public void addItem(ArrayList<PlaceSearchData> locationlist) {
        for(int i = 0; i<locationlist.size(); i++) {
            dataLocation.add(locationlist.get(i));
            itemType.add(TYPE_ITEM);
        }
	}
    public void clearItem() {
        if(dataLocation!=null){
            for(int i = 0; i<dataLocation.size();) {
                int rowType = getItemViewType(i);
                if(rowType==TYPE_ITEM){
                    dataLocation.remove(i);
                    itemType.remove(i);
                }else{
                    i++;
                }
            }
        }
	}
    public void clearHistoryItem() {
        if(dataLocation!=null){
            for(int i = 0; i<dataLocation.size();) {
                int rowType = getItemViewType(i);
                if(rowType==TYPE_HISTORY_ITEM){
                    dataLocation.remove(i);
                    itemType.remove(i);
                }else{
                    i++;
                }
            }
        }
    }
    public void setNoResultView() {
        PlaceSearchData placeSearchData = new PlaceSearchData();
        placeSearchData.setPlace("No Result");
        dataLocation.add(placeSearchData);
        itemType.add(TYPE_NO_RESULT);
    }
    public void removeNoResultView() {
        int noResultPosition = itemType.indexOf(TYPE_NO_RESULT);
        if(noResultPosition!=-1) {
            itemType.remove(noResultPosition);
            dataLocation.remove(noResultPosition);
        }

    }
    public void addHistoryItem(ArrayList<PlaceSearchData> locationlist) {
        for(int i = 0; i<locationlist.size(); i++) {
            dataLocation.add(locationlist.get(i));
            itemType.add(TYPE_HISTORY_ITEM);
        }
    }
	public void addSectionHeaderItem(final String title) {
		PlaceSearchData placeSearchData = new PlaceSearchData();
        placeSearchData.setPlace(title);
        dataLocation.add(placeSearchData);
        itemType.add(TYPE_SEPARATOR);
	}
	public void clearSectionResultHeaderItem(){
        for (int i=0; i<dataLocation.size();i++) {
            PlaceSearchData placeSearchData = dataLocation.get(i);
            if(placeSearchData.getPlace()!=null&&placeSearchData.getPlace().equals("Result")){
                dataLocation.remove(i);
                itemType.remove(i);
            }
        }
    }
    public void clearSectionHistoryHeaderItem(){
        for (int i=0; i<dataLocation.size();i++) {
            PlaceSearchData placeSearchData = dataLocation.get(i);
            if(placeSearchData.getPlace()!=null&&placeSearchData.getPlace().equals("History")){
                dataLocation.remove(i);
                itemType.remove(i);
            }
        }
    }

	@Override
	public int getCount() {
		return dataLocation.size();
	}

	@Override
	public PlaceSearchData getItem(int i) {
		return dataLocation.get(i);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		int rowType = getItemViewType(position);
		if (convertView == null){
			holder = new ViewHolder();
			switch (rowType) {
				case TYPE_ITEM:
					convertView = inflater.inflate(R.layout.place_selection_google_item_adapter, null);
					holder.jalan = (TextView) convertView.findViewById(R.id.jalan);
					holder.jalanDetil = (TextView) convertView.findViewById(R.id.jalanDetail);
					holder.distance = (TextView) convertView.findViewById(R.id.distance);
					holder.favorite = (ImageView) convertView.findViewById(R.id.favorite);
					break;
                case TYPE_HISTORY_ITEM:
                    convertView = inflater.inflate(R.layout.place_selection_google_item_adapter, null);
                    holder.jalan = (TextView) convertView.findViewById(R.id.jalan);
                    holder.jalanDetil = (TextView) convertView.findViewById(R.id.jalanDetail);
                    holder.jalanDetilLayout = (LinearLayout) convertView.findViewById(R.id.jalanDetailLayout);
                    holder.distance = (TextView) convertView.findViewById(R.id.distance);
                    holder.favorite = (ImageView) convertView.findViewById(R.id.favorite);
                    break;
				case TYPE_SEPARATOR:
					convertView = inflater.inflate(R.layout.place_selection_google_header_adapter, null);
					holder.header_type = (TextView) convertView.findViewById(R.id.header_type);
					break;
                case TYPE_NO_RESULT:
                    convertView = inflater.inflate(R.layout.place_selection_google_no_result_adapter, null);
                    holder.no_result_txt = (TextView) convertView.findViewById(R.id.no_result_txt);
                    break;
			}

			convertView.setTag(holder);
		}
		else {
	       holder = (ViewHolder) convertView.getTag();
		}

        switch (rowType) {
            case TYPE_ITEM:
                if(dataLocation.get(position).getFavorite().equals("false")){
                    holder.favorite.setImageResource(R.mipmap.ic_base_favorite_off);
                }
                else{
                    holder.favorite.setImageResource(R.mipmap.ic_base_favorite_on);
                }
                holder.jalan.setText(dataLocation.get(position).getPlace());
                holder.jalanDetil.setText(dataLocation.get(position).getPlace_details());

//                holder.distance.setText(dataLocation.get(position).getDistance());
                holder.distance.setVisibility(View.GONE);

                holder.favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dataLocation.get(position).getFavorite().equals("false")){
                            controller.openDataBase();
                            long id_favorite = controller.insertFavoriteTransport(dataLocation.get(position).getPlace(),
                                    dataLocation.get(position).getPlace_details(), dataLocation.get(position).getPlace_id(), "google",
                                    dataLocation.get(position).getLat(), dataLocation.get(position).getLng());
                            controller.close();
                            dataLocation.get(position).setFavorite("true");
                            notifyDataSetChanged();
                        }
                        else{
                            controller.openDataBase();
                            controller.deleteFavoriteTransport(dataLocation.get(position).getPlace_id());
                            Log.d("ID_FAVORITEEE",dataLocation.get(position).getPlace_id());
                            controller.close();
                            dataLocation.get(position).setFavorite("false");
                            notifyDataSetChanged();
                        }
                    }
                });
                break;
            case TYPE_SEPARATOR:
                holder.header_type.setText(dataLocation.get(position).getPlace());
                break;
            case TYPE_HISTORY_ITEM:
                holder.favorite.setVisibility(View.GONE);
                holder.jalan.setText(dataLocation.get(position).getPlace());
                holder.distance.setVisibility(View.GONE);
                if(dataLocation.get(position).getPlace()==null){
                    holder.jalan.setText(dataLocation.get(position).getPlace_details());
                    holder.jalanDetilLayout.setVisibility(View.GONE);
                }else{
                    holder.jalan.setText(dataLocation.get(position).getPlace());
                    holder.jalanDetil.setText(dataLocation.get(position).getPlace_details());
                    holder.jalanDetilLayout.setVisibility(View.VISIBLE);
                }
//                holder.distance.setText(data.get(position).get("distance"));
                holder.jalanDetil.setText(dataLocation.get(position).getPlace_details());
                break;
            case TYPE_NO_RESULT:
                holder.no_result_txt.setText(context.getResources().getString(R.string.no_result));
                break;
        }
		return convertView; 
	}
}
