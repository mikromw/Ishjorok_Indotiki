package com.pmberjaya.indotiki.views;

/*
 * Copyright (C) 2012 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.bookingData.bookingComplete.BookingCompleteAdapter;
import com.pmberjaya.indotiki.app.bookingData.bookingProgress.BookingInProgressAdapter;
import com.pmberjaya.indotiki.app.deposit.adapter.DepositConfirmationListAdapter;
import com.pmberjaya.indotiki.app.deposit.adapter.DepositLogAdapter;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData.BookingInProgressMemberData;
import com.pmberjaya.indotiki.models.deposit.DepositData.DepositData;

import java.util.List;

public class EndlessListView extends ListView implements OnScrollListener {

	private View footer;
	private boolean isLoading;
	private EndlessListener listener;
	private BookingInProgressAdapter bookinginprogressmemberadapter;
	private BookingCompleteAdapter bookingcompletememberadapter;
    int mPosition=0;
    int mOffset=0;
    private boolean status;
	private DepositLogAdapter depositAdapter;
	private DepositConfirmationListAdapter depositConfirmationAdapter;

	public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setOnScrollListener(this);
	}

	public EndlessListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnScrollListener(this);
	}

	public EndlessListView(Context context) {
		super(context);
		this.setOnScrollListener(this);
	}

	public void setListener(EndlessListener listener) {
		this.listener = listener;
	}

	public void statusadd(boolean status){
		this.status = status;
	}

	public boolean getstatusadd(){
		return status;
	}



	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (getAdapter() == null)
			return ;

		if (getAdapter().getCount() == 0)
			return ;

		int l = visibleItemCount + firstVisibleItem;
		if (l >= totalItemCount && !isLoading) {
			// It is time to add new data. We call the listener
			if(footer!=null) {
				this.addFooterView(footer);
			}
			isLoading = true;
			listener.loadData();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		 int position = this.getFirstVisiblePosition();
         View v = this.getChildAt(0);
          int offset = (v == null) ? 0 : v.getTop();

          if (mPosition < position || (mPosition == position && mOffset < offset)){
               // Scrolled up
        	 // this.getParent().requestDisallowInterceptTouchEvent(true);

          } else {
               // Scrolled down
          }

	}

	public void setLoadingView(int resId) {
		LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = (View) inflater.inflate(resId, null);
		this.addFooterView(footer);
	}
	public void setErrorListView(int resId) {
		LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = (View) inflater.inflate(resId, null);
		Button bt_try_again_list = (Button) footer.findViewById(R.id.bt_try_again_list);
		bt_try_again_list.setOnClickListener(try_again_list_listener);
		this.addFooterView(footer);
	}
	public View.OnClickListener try_again_list_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			listener.onTryAgainAPICallback();
		}
	};

	public void removeLoadingView() {
		this.removeFooterView(footer);
	}

	public void setBookingInProgressAdapter(BookingInProgressAdapter adapter) {
		super.setAdapter(adapter);
		this.bookinginprogressmemberadapter = adapter;
		this.removeFooterView(footer);
		isLoading = false;
	}
	public void addNewBookingInProgressMemberData(List<BookingInProgressMemberData> data) {
		this.removeFooterView(footer);
		bookinginprogressmemberadapter.addAll(data);
		bookinginprogressmemberadapter.notifyDataSetChanged();
		isLoading = false;
	}
	public void setDepositAdapter(DepositLogAdapter depositAdapter) {
		super.setAdapter(depositAdapter);
		this.depositAdapter = depositAdapter;
		this.removeFooterView(footer);
		isLoading = false;
	}
	public void addNewDepositData(List<DepositData> data) {
		this.removeFooterView(footer);
		depositAdapter.addAll(data);
		depositAdapter.notifyDataSetChanged();
		isLoading = false;
	}


	public  interface EndlessListener {
		public void loadData() ;
		public void onTryAgainAPICallback();
	}

}
