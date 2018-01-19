 package com.pmberjaya.indotiki.app.bookingNew.place;
 
import java.util.ArrayList;
import java.util.List;


import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.interfaces.bookingNew.SearchPlaceInterface;
import com.pmberjaya.indotiki.utilities.Utility;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class PlaceSelectionTab extends BaseActivity implements ViewPager.OnPageChangeListener{
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private String state;
	private String activity;
	private String transportationData;
	private String latitude;
	private String longitude;
	private static PlaceSelectionFavoriteFragment favorite;
	private static PlaceSelectionGoogleFragment target;
	private int[] tabIcons = {
			R.mipmap.ic_base_location_white,
			R.drawable.xml_ic_favorite_white,
			R.mipmap.ic_base_map_white
	};
	public EditText atv_places;
	public SearchPlaceInterface searchPlaceInterface;
	private ImageView ic_action;
	private ImageView ic_clear;
	private EditText tv_map_place;
	private String request_type;
	private String autoload;
	private SearchPlaceInterface searchFavoriteInterface;
	private String blockCharacterSet = "[@#~-/<>,.?~:;#^|$%&*!'`()_+=\"]";
	private InputFilter filter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

			if (source != null && blockCharacterSet.contains(("" + source))) {
				return "";
			}
			return null;
		}
	};

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_selection_tab);
		getIntentExtra();
		setupViewPager();
		renderViews();
		setupTabIcons();
    }
    public void renderViews(){
		ic_action = (ImageView) findViewById(R.id.ic_action);
		ic_clear = (ImageView) findViewById(R.id.ic_clear);
		ic_clear.setOnClickListener(clear);
		atv_places = (EditText) findViewById(R.id.atv_places);
		atv_places.setFilters(new InputFilter[] { filter });
		tv_map_place = (EditText) findViewById(R.id.tv_map_place);

		tv_map_place.setFilters(new InputFilter[] { filter });
		tv_map_place.setEnabled(false);
		atv_places.addTextChangedListener(atvPlacesTextWatcher);
	}
    public View.OnClickListener clear = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			atv_places.setText("");
			ic_clear.setVisibility(View.GONE);
		}
	};
    public TextWatcher atvPlacesTextWatcher = new TextWatcher(){

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
			searchPlaceInterface.beforeTextChanged(s, start, count, after);
			searchFavoriteInterface.beforeTextChanged(s, start, count, after);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			if(count>0){
				ic_clear.setVisibility(View.VISIBLE);
			}else {
				ic_clear.setVisibility(View.GONE);
			}
			searchPlaceInterface.onTextChanged(s, start, before, count);
			searchFavoriteInterface.onTextChanged(s, start, before, count);
		}
		@Override
		public void afterTextChanged(final Editable thelocation) {
			searchPlaceInterface.afterTextChanged(thelocation);
			searchFavoriteInterface.afterTextChanged(thelocation);

		}
	};

    public void setAtvPlaces(String text){
		tv_map_place.setText(text);
	}
    public void setSearchPlaceInterface(SearchPlaceInterface searchPlaceInterface){
		this.searchPlaceInterface = searchPlaceInterface;
	}
	public void setSearchFavoriteInterface(SearchPlaceInterface searchFavoriteInterface){
		this.searchFavoriteInterface = searchFavoriteInterface;
	}

	private void setupTabIcons() {
		tabLayout.getTabAt(0).setIcon(tabIcons[0]);
		tabLayout.getTabAt(1).setIcon(tabIcons[1]);
		tabLayout.getTabAt(2).setIcon(tabIcons[2]);
	}
	public void doLoadFrequent(){
		favorite.initDB(atv_places.getText().toString());
		favorite.init();
	}

	public void doLoadNewGoogle(){
		target.PlaceNearby();
	}

	private void getIntentExtra(){
		Intent i = getIntent();
		state = i.getStringExtra(Constants.STATE);
		activity = i.getStringExtra(Constants.PLACE_TYPE);
		request_type = i.getStringExtra(Constants.REQUEST_TYPE);
		autoload = i.getStringExtra(Constants.AUTOLOAD);
		latitude = String.valueOf(i.getDoubleExtra(Constants.LATITUDE, 0));
		longitude = String.valueOf(i.getDoubleExtra(Constants.LONGITUDE, 0));
		initToolbar();

	}
	private void initToolbar(){
		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		if(toolbar!=null) {
			setSupportActionBar(toolbar);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		ActionBar actionBar = getSupportActionBar();
		if(actionBar!=null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			if(activity.equals("from")){
				getSupportActionBar().setTitle(getResources().getString(R.string.choose_origin));
			}
			else if(activity.equals("to")){
				getSupportActionBar().setTitle(getResources().getString(R.string.choose_destination));
			}
		}
	}
	private void setupViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		Bundle bundle = new Bundle();
		bundle.putString(Constants.LATITUDE, latitude);
		bundle.putString(Constants.LONGITUDE, longitude);
		bundle.putString(Constants.PLACE_TYPE, activity);
		bundle.putString(Constants.STATE, state);
		bundle.putString(Constants.REQUEST_TYPE, request_type);
		bundle.putString(Constants.AUTOLOAD, autoload);
        target = new PlaceSelectionGoogleFragment();
		target.setArguments(bundle);
		favorite = new PlaceSelectionFavoriteFragment();
		favorite.setArguments(bundle);
		Fragment map = new PlaceSelectionMapFragment();
		map.setArguments(bundle);
		adapter.addFragment(target, getResources().getString(R.string.target));
		adapter.addFragment(favorite, getResources().getString(R.string.favorite));
		adapter.addFragment(map, getResources().getString(R.string.map));
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(3);
		tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if(position==0){
					ic_action.setImageResource(R.mipmap.ic_base_search_grey);
					atv_places.setVisibility(View.VISIBLE);
					tv_map_place.setVisibility(View.GONE);
				}
				else if(position==1){
					doLoadFrequent();
					ic_action.setImageResource(R.mipmap.ic_base_search_grey);
					atv_places.setVisibility(View.VISIBLE);
					tv_map_place.setVisibility(View.GONE);
				}
				else if(position == 2){
					ic_action.setImageResource(R.mipmap.ic_from_place_gray);
					ic_clear.setVisibility(View.GONE);
					atv_places.setVisibility(View.GONE);
					tv_map_place.setVisibility(View.VISIBLE);
					Utility.getInstance().hideKeyboard(PlaceSelectionTab.this,atv_places);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}
	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int position) {
	}

	class ViewPagerAdapter extends FragmentPagerAdapter {
		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();

		public ViewPagerAdapter(FragmentManager manager) {
			super(manager);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}

		public void addFragment(Fragment fragment, String title) {
			mFragmentList.add(fragment);
			mFragmentTitleList.add(title);
		}

		@Override
		public CharSequence getPageTitle(int position) {
//			return mFragmentTitleList.get(position);
			return null;
		}
	}
	@Override
    public void onBackPressed() 
    {
		this.finish();
        super.onBackPressed();
    }
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	 @Override
		protected void onDestroy()
	{
		super.onDestroy();
	}
	 @Override
	  public void onPause() {
	    super.onPause();
	  }
}