package com.pmberjaya.indotiki.app.bookingData.bookingProgress;

import java.util.List;


import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData.BookingInProgressMemberData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookingInProgressAdapter extends ArrayAdapter<BookingInProgressMemberData> {
	List<BookingInProgressMemberData> data;
	Context mContext;
	private static LayoutInflater inflater = null;
	private ViewHolder holder;
	public BookingInProgressAdapter(Context context, int resource,
                                    List<BookingInProgressMemberData> bookingList) {
		super(context, resource, bookingList);
		data = bookingList;
		mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public List<BookingInProgressMemberData> getItemdata() {
		return data;
	}
	static class ViewHolder {
		TextView tanggal;
		TextView jalan_awal;
		TextView lokasi_awal;
		TextView jalan_akhir;
		TextView lokasi_akhir;
		ImageView transport_picture;
		LinearLayout status;
		TextView tv_status;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder = new ViewHolder();
		if (convertView == null){
			convertView = inflater.inflate(R.layout.booking_in_progress_list_adapter, null);
			holder.tanggal = (TextView) convertView.findViewById(R.id.tanggal);
			holder.lokasi_awal= (TextView) convertView.findViewById(R.id.lokasi_awal);
			holder.jalan_awal= (TextView) convertView.findViewById(R.id.jalan_awal);
			holder.lokasi_akhir = (TextView) convertView.findViewById(R.id.lokasi_akhir);
			holder.jalan_akhir= (TextView) convertView.findViewById(R.id.jalan_akhir);
			holder.transport_picture = (ImageView) convertView.findViewById(R.id.transport_picture);
			holder.status = (LinearLayout) convertView.findViewById(R.id.status);
			holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			convertView.setTag(holder);
		}
		else {
	       holder = (ViewHolder) convertView.getTag();
		}
		holder.tanggal.setText(data.get(position).getRequestTime());
		
		holder.jalan_awal.setText(data.get(position).getFrom());
		holder.jalan_akhir.setText(data.get(position).getTo());
		if(data.get(position).getFromPlace() == null){
			holder.lokasi_awal.setVisibility(View.GONE);
		}
		else{
			holder.lokasi_awal.setText(data.get(position).getFromPlace());
		}
		if(data.get(position).getStatus()!=null&&data.get(position).getStatus().equals("waiting")){
			holder.status.setVisibility(View.VISIBLE);
			holder.tv_status.setText(mContext.getResources().getString(R.string.searching));
		}else if(data.get(position).getStatus()!=null&&data.get(position).getStatus().equals("accept")){
			holder.status.setVisibility(View.VISIBLE);
			holder.tv_status.setText(mContext.getResources().getString(R.string.accept));
		}else if(data.get(position).getStatus()!=null&&data.get(position).getStatus().equals("pick_up")){
			holder.status.setVisibility(View.VISIBLE);
			holder.tv_status.setText(mContext.getResources().getString(R.string.pick_up));
		}else{
			holder.status.setVisibility(View.GONE);
		}
		if(data.get(position).getToPlace()== null){
			holder.lokasi_akhir.setVisibility(View.GONE);
		}
		else{
			holder.lokasi_akhir.setText(data.get(position).getToPlace());
		}
		if(data.get(position).getSourceTable().equals("transport")){
			if(data.get(position).getTransportation().equals("motorcycle_taxi")) {
				holder.transport_picture.setImageResource(R.mipmap.ic_logo_motorcycle_grey);
			}else if(data.get(position).getTransportation().equals("pedicab")) {
				holder.transport_picture.setImageResource(R.mipmap.ic_logo_pedicab_grey);
			}
		}
		else if(data.get(position).getSourceTable().equals("food")){
			holder.transport_picture.setImageResource(R.mipmap.ic_logo_food_grey);
		}
		else if(data.get(position).getSourceTable().equals("courier")){
			holder.transport_picture.setImageResource(R.mipmap.ic_logo_courier_grey);
		}
		else if (data.get(position).getSourceTable().equals("car")) {
			holder.transport_picture.setImageResource(R.mipmap.ic_logo_motorcycle_grey);
		}else if (data.get(position).getSourceTable().equals("mart")) {
			holder.transport_picture.setImageResource(R.mipmap.ic_logo_mart);
		}
		return convertView; 
	}
	
}
