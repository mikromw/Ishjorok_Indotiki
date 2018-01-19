package com.pmberjaya.indotiki.app.deposit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.deposit.adapter.DepositLogAdapter;
import com.pmberjaya.indotiki.callbacks.deposit.DepositCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.interfaces.deposit.DepositLogInterface;
import com.pmberjaya.indotiki.models.deposit.DepositData.DepositData;
import com.pmberjaya.indotiki.models.deposit.DepositData.DepositLogModel;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.views.EndlessListView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepositLogFragment extends Fragment implements DatePickerDialog.OnDateSetListener, EndlessListView.EndlessListener {
    private EndlessListView list;
    private SessionManager session;
    private DepositLogAdapter adapter;
    private String driverId;
    private LinearLayout loadinglayout;
    private LinearLayout nobookinglayout;
    private LinearLayout listlayout;
    private TextView dateTextView;
    private TextView dateEndTextView;
    private String date_start = "";
    private String month_start = "";
    private String year_start = "";
    private String from_date = "";
    private String to_date = "";
    private String date_end = "";
    private String month_end = "";
    private String year_end = "";
    private int type_date_picker;
    private LinearLayout depositlayout;
    private LinearLayout date_start_button;
    private LinearLayout date_end_button;
    private int banyakdata;
    private int page = 1;
    private TextView tv_error_timeout;
    private LinearLayout bt_try_again;
    private RelativeLayout error_layout;
    private SwipeRefreshLayout swiperefresh;
    private boolean runGetDepositDriver = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.deposit_log_list_activity, container, false);
        //------------------------------------Session--------------------------------------------------------
        session = new SessionManager(getActivity());
        HashMap<String, String> mapData = session.getUserDetails();
        driverId = mapData.get(session.KEY_ID);
        swiperefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        loadinglayout = (LinearLayout) rootView.findViewById(R.id.layout_loading);
        depositlayout = (LinearLayout) rootView.findViewById(R.id.deposit_layout);
        nobookinglayout = (LinearLayout) rootView.findViewById(R.id.nobookinglayout);
        listlayout = (LinearLayout) rootView.findViewById(R.id.deposit_list_layout);
        list = (EndlessListView) rootView.findViewById(R.id.deposit_list);
        list.setListener(this);
        tv_error_timeout = (TextView) rootView.findViewById(R.id.tvErrorMessage);
        error_layout = (RelativeLayout) rootView.findViewById(R.id.layoutError);
        bt_try_again = (LinearLayout) rootView.findViewById(R.id.btnError);
        bt_try_again.setOnClickListener(try_again_listener);
        date_start_button = (LinearLayout) rootView.findViewById(R.id.date_start_button);
        dateTextView = (TextView) rootView.findViewById(R.id.date_textview);
        date_end_button = (LinearLayout) rootView.findViewById(R.id.date_end_button);
        dateEndTextView = (TextView) rootView.findViewById(R.id.date_end_textview);
        LinearLayout dateButton = (LinearLayout) rootView.findViewById(R.id.date_calculate_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!date_start.equals("") && !month_start.equals("") && !year_start.equals("") && !date_end.equals("") && !month_end.equals("") &&
                        !year_end.equals("")) {
                    page = 1;
                    loadinglayout.setVisibility(View.VISIBLE);
                    adapter = null;
                    getDepositDriver(String.valueOf(page));
                }
            }
        });
        //------------------------------------TextView-------------------------------------------------------
        // Show a datepicker when the dateButton is clicked
        date_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type_date_picker = 0;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        DepositLogFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        date_end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type_date_picker = 1;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        DepositLogFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadinglayout.setVisibility(View.VISIBLE);
                nobookinglayout.setVisibility(View.GONE);
                listlayout.setVisibility(View.GONE);
                adapter = null;
                getDepositDriver(String.valueOf(page));
                ((DepositTab)getActivity()).checkDeposit();
            }
        });
        return rootView;
    }

    public View.OnClickListener try_again_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            renderloadingView();
            getDepositDriver(String.valueOf(page));
        }
    };

    public void SetListViewAdapter(List<DepositData> depositArray) {
        if (adapter == null) {
            nobookinglayout.setVisibility(View.GONE);
            loadinglayout.setVisibility(View.GONE);
            listlayout.setVisibility(View.VISIBLE);
            depositlayout.setVisibility(View.VISIBLE);
            adapter = new DepositLogAdapter(getActivity(), R.layout.deposit_log_list_activity, depositArray);
            list.setLoadingView(R.layout.view_loading_list_layout);
            list.setDepositAdapter(adapter);
            String deposit_hasil = String.valueOf(adapter.getItem(0).getBalance());
            String depositHasilText = "Rp. " + Utility.getInstance().convertPrice(deposit_hasil);
        } else {
            list.addNewDepositData(depositArray);
            list.removeLoadingView();
        }
    }

    private void getDepositDriver(String page) {
        runGetDepositDriver = true;
        Log.d("Inki-Pay", "JALAN");
        String api = Utility.getInstance().getTokenApi(getActivity());
        UserController.getInstance(getActivity()).getDepositLogData(depositParameters(page), api, depositLogInterface);
        return;
    }

    public Map<String, String> depositParameters(String page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("offset", page);
        params.put("from_date", from_date);
        params.put("to_date", to_date);
        return params;
    }
    DepositLogInterface depositLogInterface = new DepositLogInterface() {
        @Override
        public void onSuccessGetDepositLog(DepositCallback depositCallback) {
            swiperefresh.setRefreshing(false);
            Log.d("DepositCallback", "Jalan");
            int sukses = depositCallback.getSukses();
            Log.d("SUKSES", ">" + sukses);
            if (sukses == 2) {
                page++;
                DepositLogModel data = depositCallback.getData();
                List<DepositData> DriverDepositData = data.getResultArray();
                String deposit = depositCallback.getDeposit();
                String depositText = null;
                if (deposit != null) {
                    depositText = "Rp. " + Utility.getInstance().convertPrice(deposit);
                } else {
                    depositText = "Rp. 0";
                }
                banyakdata = Integer.parseInt(data.getTotal());
                Log.d("banyak data", "" + banyakdata);
                if (banyakdata == 0) {
                    loadinglayout.setVisibility(View.GONE);
                    listlayout.setVisibility(View.GONE);
                    nobookinglayout.setVisibility(View.VISIBLE);
                } else {
                    SetListViewAdapter(DriverDepositData);
                }
            } else {
                loadinglayout.setVisibility(View.GONE);
                nobookinglayout.setVisibility(View.VISIBLE);
            }
            runGetDepositDriver = false;
        }

        @Override
        public void onErrorGetDepositLog(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
                } else {
                    if (page >= 2) {
                        renderErrorListView();
                    } else {
                        renderErrorView();
                    }
//                    Toast.makeText(this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void renderloadingView() {
        loadinglayout.setVisibility(View.VISIBLE);
        depositlayout.setVisibility(View.GONE);
        error_layout.setVisibility(View.GONE);
    }

    public void renderErrorView() {
        loadinglayout.setVisibility(View.GONE);
        depositlayout.setVisibility(View.GONE);
        error_layout.setVisibility(View.VISIBLE);
        tv_error_timeout.setText(R.string.currently_unavailable);
    }

    public void renderErrorListView() {
        list.removeLoadingView();
        list.setErrorListView(R.layout.view_error_endless_listview);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (type_date_picker == 0) {
            String date_text = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            date_start = String.valueOf(dayOfMonth);
            month_start = String.valueOf(monthOfYear);
            if (month_start.length() == 1) {
                month_start = "0" + month_start;
            }
            year_start = String.valueOf(year);
            from_date = year_start + "-" + month_start + "-" + date_start;
            dateTextView.setText(date_text);
        } else {
            String date_end_text = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            date_end = String.valueOf(dayOfMonth);
            month_end = String.valueOf(monthOfYear);
            year_end = String.valueOf(year);
            if (month_end.length() == 1) {
                month_end = "0" + month_end;
            }
            to_date = year_end + "-" + month_end + "-" + date_end;
            dateEndTextView.setText(date_end_text);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (runGetDepositDriver == true) {
            getDepositDriver(String.valueOf(page));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void loadData() {
        int total = list.getAdapter().getCount();
        if (banyakdata > total) {
            getDepositDriver(String.valueOf(page));
        } else {
            list.removeLoadingView();
        }
    }

    @Override
    public void onTryAgainAPICallback() {
        list.removeLoadingView();
        list.setLoadingView(R.layout.view_loading_list_layout);
        getDepositDriver(String.valueOf(page));
    }
}