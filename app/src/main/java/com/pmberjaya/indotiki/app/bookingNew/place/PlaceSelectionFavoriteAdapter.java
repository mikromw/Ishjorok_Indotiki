package com.pmberjaya.indotiki.app.bookingNew.place;

import java.util.ArrayList;
import java.util.HashMap;


import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.dao.SessionManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceSelectionFavoriteAdapter extends ArrayAdapter<HashMap<String,String>> {
	ArrayList<HashMap<String,String>> data;
	DBController controller;
	Context activity;
	int positionactive;
	private static LayoutInflater inflater = null;
	private ViewHolder holder;
	private ProgressDialog pDialog;
	SessionManager session;
	String userId;
	private String id_favorite;
	private static final String TAG_SUKSES = "sukses";
	private static final String TAG_PESAN = "pesan";
	private static final String TAG = "DaftarListingnonActive";
	public PlaceSelectionFavoriteAdapter(Context context, int resource, ArrayList<HashMap<String, String>> productList) {
		super(context, resource, productList);
		  //------------------------------------Session--------------------------------------------------------
		controller = DBController.getInstance(context);
		session = new SessionManager(context);
		HashMap<String,String> map = session.getUserDetails();
		userId = map.get(session.KEY_ID);
		//---------------------------------------------------------------------------------------------------
		data = productList;
		activity = context;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public ArrayList<HashMap<String,String>> getItemdata() {
		return data;
	}
	static class ViewHolder {
		TextView jalan;
		TextView jalanDetil;
		TextView distance;
		ImageView favorite;
	}		
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder = new ViewHolder();
		if (convertView == null){
			convertView = inflater.inflate(R.layout.place_selection_favorite_adapter, null);
			holder.jalan = (TextView) convertView.findViewById(R.id.jalan);
			holder.jalanDetil = (TextView) convertView.findViewById(R.id.jalanDetail);
			holder.favorite = (ImageView) convertView.findViewById(R.id.favorite);
			holder.distance = (TextView) convertView.findViewById(R.id.distance);
			convertView.setTag(holder);
		}
		else {
	       holder = (ViewHolder) convertView.getTag();
		}
		if(data.get(position).get("favorite").equals("false")){
			holder.favorite.setImageResource(R.mipmap.ic_base_favorite_off);
		}
		else{
			holder.favorite.setImageResource(R.mipmap.ic_base_favorite_on);
		}
		holder.jalan.setText(data.get(position).get("place"));
		holder.distance.setText(data.get(position).get("distance"));
		holder.jalanDetil.setText(data.get(position).get("place_details"));
		holder.favorite.setOnClickListener(new View.OnClickListener() {
			  @Override
		        public void onClick(View v) {
				  if(data.get(position).get("favorite").equals("false")){
						controller.openDataBase();
						long id_favorite = controller.insertFavoriteTransport(data.get(position).get("name"),
								data.get(position).get("description"), data.get(position).get("id"), "google", 
								data.get(position).get("lat"), data.get(position).get("lng"));
						controller.close();
						data.get(position).put("id_favorite", String.valueOf(id_favorite));
						data.get(position).put("favorite", "true");
//					  	((PlaceSelectionTab)activity).favorite();
				        notifyDataSetChanged();
					}
					else{
						controller.openDataBase();
						controller.deleteFavoriteTransport(data.get(position).get("place_id"));
					  	Log.d("DELETES",">"+data.get(position).get("place_id"));
						controller.close();
						data.get(position).put("favorite", "false");
					  	((PlaceSelectionTab)activity).doLoadFrequent();
					  	((PlaceSelectionTab)activity).doLoadNewGoogle();

			            notifyDataSetChanged();
					}
		        }
		});
		return convertView; 
	}

 
}
