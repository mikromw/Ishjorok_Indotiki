package com.pmberjaya.indotiki.app.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.bookingNew.OrderTransportNew;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.models.main.MainMenuData2;
import com.pmberjaya.indotiki.models.main.SubMenuData;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;

/**
 * Created by edwin on 06/01/2017.
 */

public class MenuGridAdapter extends RecyclerView.Adapter<MenuGridAdapter.ItemRowHolder> {
    private final LocationSessionManager locationSessionManager;
    private ArrayList<MainMenuData2> dataMenu;
    private ArrayList<SubMenuData> subMenu;
    private Context mContext;
    private AlertDialog levelDialog;

    public MenuGridAdapter(Context context, ArrayList<MainMenuData2> dataMenu) {
        this.dataMenu = dataMenu;
        this.mContext = context;
        locationSessionManager = new LocationSessionManager(mContext);
    }

    public void changeSetDataAdapter(ArrayList<MainMenuData2> dataMenu) {
        this.dataMenu = dataMenu;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_menu_grid_adapter, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        int widthIcon = getDisplayMeasurement();
        LinearLayout.LayoutParams lParams  = new LinearLayout.LayoutParams(widthIcon,widthIcon);
        mh.icon.setLayoutParams(lParams);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {
        if (dataMenu.get(i).getMenuImage() != 0) {
            itemRowHolder.icon.setImageResource(dataMenu.get(i).getMenuImage());
        } else {
            itemRowHolder.icon.setImageResource(0);
        }

        if (dataMenu.get(i).getMenuName() != null) {
            itemRowHolder.text.setText(dataMenu.get(i).getMenuName());
        } else {
            itemRowHolder.text.setText(mContext.getResources().getString(R.string.empty_string));
        }

        Typeface custom_font = Typeface.createFromAsset(mContext.getResources().getAssets(), "fonts/BenchNine-Bold.ttf");
        itemRowHolder.text.setTypeface(custom_font);
//        TypedValue outValue = new TypedValue();
//        mContext.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
//        itemRowHolder.menu_item_layout.setBackgroundResource(outValue.resourceId);
        itemRowHolder.menu_item_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dataMenu.get(i).getStatus() != null && dataMenu.get(i).getStatus().equals("2")) {
                    if (dataMenu.get(i).getMenuName() != null) {
                        if (dataMenu.get(i).getMenuName().equals(mContext.getResources().getString(R.string.indo_ojek))) {
                            if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(locationSessionManager.getUserDistrictIdCentral())) {
                                Intent intent = new Intent(mContext, OrderTransportNew.class);
                                intent.putExtra(Constants.TRANSPORTATION_TYPE, "motorcycle_taxi");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mContext.startActivity(intent);
                            } else {
                                showSnackbar(itemRowHolder);
                            }
                        } else if (dataMenu.get(i).getMenuName().equals(mContext.getResources().getString(R.string.indo_becak))) {
                            if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(locationSessionManager.getUserDistrictIdCentral())) {
                                Intent intent = new Intent(mContext, OrderTransportNew.class);
                                intent.putExtra(Constants.TRANSPORTATION_TYPE, "pedicab");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mContext.startActivity(intent);
                            } else {
                                showSnackbar(itemRowHolder);
                            }
                        } else if (dataMenu.get(i).getMenuName().equals(mContext.getString(R.string.indo_courier))) {
//                            if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(locationSessionManager.getUserDistrictIdCentral())) {
//                                Intent intent = new Intent(mContext, OrderCourierNew.class);
//                                intent.putExtra(Constants.TRANSPORTATION_TYPE, "motorcycle_taxi");
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                mContext.startActivity(intent);
//                            } else {
//                                showSnackbar(itemRowHolder);
//                            }
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();

                        } else if (dataMenu.get(i).getMenuName().equals(mContext.getString(R.string.indo_food))) {
//                            Intent intent = new Intent(mContext, FoodMainActivity.class);
//                            intent.putExtra(Constants.TRANSPORTATION_TYPE, "motorcycle_taxi");
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            mContext.startActivity(intent);
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();

                        } else if (dataMenu.get(i).getMenuName().equals(mContext.getString(R.string.indo_car))) {
                            if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(locationSessionManager.getUserDistrictIdCentral())) {

                            } else {
                                showSnackbar(itemRowHolder);
                            }
                        } else if (dataMenu.get(i).getMenuName().equals(mContext.getResources().getString(R.string.bill))) {
                            if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(locationSessionManager.getUserDistrictIdCentral())) {
                                subMenu = dataMenu.get(i).getSubmenu();
                                Intent intent = new Intent(mContext, SubMainMenu.class);
                                intent.putParcelableArrayListExtra("subMenu", subMenu);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mContext.startActivity(intent);
                            } else {
                                showSnackbar(itemRowHolder);
                            }
                        } else if (dataMenu.get(i).getMenuName().equals(mContext.getString(R.string.pulsa))) {
                            if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(locationSessionManager.getUserDistrictIdCentral())) {
//                                Intent i = new Intent(mContext, PulsaMain.class);
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                mContext.startActivity(i);
                            } else {
                                showSnackbar(itemRowHolder);
                            }
                        }  else if (dataMenu.get(i).getMenuName().equals(mContext.getString(R.string.mart))) {
                            if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(locationSessionManager.getUserDistrictIdCentral())) {
//                                Intent intent = new Intent(mContext, MartMain.class);
//                                intent.putExtra("transportationData", "market");
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                mContext.startActivity(intent);
                            } else {
                                showSnackbar(itemRowHolder);
                            }
                        }
                    }
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.menu_is_under_maintenance), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSnackbar(ItemRowHolder itemRowHolder) {
        final Snackbar snackbar = Snackbar
                .make(itemRowHolder.menu_item_layout, mContext.getResources().getString(R.string.service_unavailable), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(mContext.getResources().getString(R.string.close), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private int getDisplayMeasurement(){
        int screenWidth = Utility.getInstance().getDisplayWidth(mContext);
        int iconDimension = screenWidth/8;
        return iconDimension;
    }
    @Override
    public int getItemCount() {
        return (null != dataMenu ? dataMenu.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected LinearLayout menu_item_layout;
        protected TextView text;
        protected ImageView icon;

        public ItemRowHolder(View view) {
            super(view);
            menu_item_layout = (LinearLayout) view.findViewById(R.id.menu_item_layout);
            text = (TextView) view.findViewById(R.id.tv_text);
            icon = (ImageView) view.findViewById(R.id.iv_gambar);
        }
    }
}
