package com.pmberjaya.indotiki.app.bookingData.bookingComplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberData.BookingCompleteData;
import com.pmberjaya.indotiki.models.main.MainMenuItemData;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BookingCompleteAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private final LayoutInflater mLayoutInflater;
	ArrayList<List<BookingCompleteData>> data;
	ArrayList<String> headerData;
	Context context;
	DBController controller;
	String userId;
	BookingCompleteFragment bookingCompleteFragment;
	SessionManager session;

	public BookingCompleteAdapter(Context context, ArrayList<List<BookingCompleteData>> childGroup, ArrayList<String> headerGroup, BookingCompleteFragment bookingCompleteFragment) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		data = childGroup;
		headerData =headerGroup;
		this.bookingCompleteFragment =bookingCompleteFragment;
		controller = DBController.getInstance(context);
		session = new SessionManager(context);
		HashMap<String,String> userData = session.getUserDetails();
		userId = userData.get(session.KEY_ID);
	}


	@Override
	public int getGroupCount() {
		return headerData.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return headerData.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.booking_complete_group_adapter, parent, false);
		}
		final TextView text = (TextView) convertView.findViewById(R.id.sample_activity_list_group_item_text);
		text.setText(headerData.get(groupPosition));

		final ImageView expandedImage = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_expanded_image);
		final int resId = isExpanded ? R.mipmap.ic_minus : R.mipmap.ic_plus;
		expandedImage.setImageResource(resId);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return data.get(groupPosition).size();
	}

	@Override
	public BookingCompleteData getChild(int groupPosition, int childPosition) {
		return data.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	public List<BookingCompleteData> getItemdata() {
		return data.get(0);
	}
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.booking_complete_list_adapter, parent, false);
		}

		TextView tvCompleteTime = (TextView) convertView.findViewById(R.id.tvCompleteTime);
		TextView tvFrom= (TextView) convertView.findViewById(R.id.tvFrom);
		TextView tvTo= (TextView) convertView.findViewById(R.id.tvTo);
		LinearLayout statusLayout = (LinearLayout) convertView.findViewById(R.id.statusLayout);
		LinearLayout btBookAgain = (LinearLayout) convertView.findViewById(R.id.btBookAgain);
		LinearLayout btnReceipt = (LinearLayout) convertView.findViewById(R.id.btnReceipt);
		ImageView ivRequestType = (ImageView) convertView.findViewById(R.id.ivRequestType);
		TextView tvRequestType = (TextView) convertView.findViewById(R.id.tvRequestType);
		tvCompleteTime.setText(data.get(groupPosition).get(childPosition).getTime());
		btBookAgain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean ifThisMenuIsAvailableInThisArea = checkMenuInThisArea(groupPosition, childPosition);
				if(ifThisMenuIsAvailableInThisArea) {
					bookingCompleteFragment.bookingAgain(data.get(groupPosition).get(childPosition));
				}else {
					Toast.makeText(mContext, mContext.getResources().getString(R.string.menu_not_available), Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnReceipt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				bookingCompleteFragment.sendReceipt(data.get(groupPosition).get(childPosition).getId(),data.get(groupPosition).get(childPosition).getSource_table());
			}
		});

		if(data.get(groupPosition).get(childPosition).getFrom_place()==null){
			tvFrom.setText(data.get(groupPosition).get(childPosition).getFrom());
		}
		else{
			tvFrom.setText(data.get(groupPosition).get(childPosition).getFrom_place());
		}
		if(data.get(groupPosition).get(childPosition).getTo_place()==null){
			tvTo.setText(data.get(groupPosition).get(childPosition).getTo());
		}
		else{
			tvTo.setText(data.get(groupPosition).get(childPosition).getTo_place());
		}
		if(data.get(groupPosition).get(childPosition).getSource_table().equals("transport")){
			if(data.get(groupPosition).get(childPosition).getTransportation().equals("pedicab")){
				ivRequestType.setImageResource(R.mipmap.ic_logo_pedicab_white);
				tvRequestType.setText(mContext.getResources().getString(R.string.pedicab));
			}
			else {
				ivRequestType.setImageResource(R.mipmap.ic_logo_motorcycle_white);
				tvRequestType.setText(mContext.getResources().getString(R.string.indo_ojek));
			}
		}
		/*else if(data.get(groupPosition).get(childPosition).getSource_table().equals("taxi")){
			ivRequestType.setImageResource(R.mipmap.ic_logo_);
			tvRequestType.setText(mContext.getResources().getString(R.string.taxi));
		}*/
		else if(data.get(groupPosition).get(childPosition).getSource_table().equals("food")){
			ivRequestType.setImageResource(R.mipmap.ic_logo_food_white);
			tvRequestType.setText(mContext.getResources().getString(R.string.indo_food));
		}
		else if(data.get(groupPosition).get(childPosition).getSource_table().equals("courier")){
			ivRequestType.setImageResource(R.mipmap.ic_logo_courier_white);
			tvRequestType.setText(mContext.getResources().getString(R.string.indo_courier));
		}/*else if(data.get(groupPosition).get(childPosition).getSource_table().equals("taxi")){
			ivRequestType.setImageResource(R.mipmap.ic_circular_taxi_white);
			tvRequestType.setText(mContext.getResources().getString(R.string.taxi));
		}else if (data.get(groupPosition).get(childPosition).getSource_table().equals("car")){
			ivRequestType.setImageResource(R.mipmap.ic_circular_car_6_passenger_white);
			tvRequestType.setText(mContext.getResources().getString(R.string.car));
		}*/else if (data.get(groupPosition).get(childPosition).getSource_table().equals("mart")){
			ivRequestType.setImageResource(R.mipmap.ic_logo_mart_white);
			tvRequestType.setText(mContext.getResources().getString(R.string.market));
		}
		if(data.get(groupPosition).get(childPosition).getStatus().equals("passenger_cancel")||data.get(groupPosition).get(childPosition).getStatus().equals("driver_cancel")){
			statusLayout.setBackgroundColor(Utility.getColor(mContext.getResources(), R.color.red_900, null));
			btnReceipt.setVisibility(View.GONE);
		}
		else{
			statusLayout.setBackgroundColor(Utility.getColor(mContext.getResources(), R.color.green_900, null));
			btnReceipt.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private boolean checkMenuInThisArea(int groupPosition, int childPosition) {
		DBController dbController = DBController.getInstance(mContext);
		ArrayList<MainMenuItemData> mainMenuItemDatas = dbController.getMainMenuItemData();
		String requestType = data.get(groupPosition).get(childPosition).getSource_table();
		String service ="";
		if (requestType.equals("transport")) {
			service = "INDO_OJEK";
		}else if (requestType.equals("courier")) {
			service = "INDO_COURIER";
		}else if (requestType.equals("food")) {
			service = "INDO_FOOD";
		}else if (requestType.equals("car")) {
			service = "INDO_CAR";
		}else if (requestType.equals("taxi")) {
			service = "INDO_TAXI";
		}else if (requestType.equals("mart")) {
			service = "INDO_MART";
		}
		for(int i=0; i<mainMenuItemDatas.size(); i++){
			if(mainMenuItemDatas.get(i).getDisplay_menu().equals(service)&&(mainMenuItemDatas.get(i).getStatus() != null && mainMenuItemDatas.get(i).getStatus().equals("2"))){
				return true;
			}
		}
		return false;
	}


	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
