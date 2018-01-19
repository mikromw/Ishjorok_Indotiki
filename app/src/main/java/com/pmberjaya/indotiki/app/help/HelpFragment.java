package com.pmberjaya.indotiki.app.help;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.models.help.HelpCategoryData;
import com.pmberjaya.indotiki.models.help.HelpFaqData;
import com.pmberjaya.indotiki.models.help.HelpServiceData;
import com.pmberjaya.indotiki.models.help.HelpSubCategoryData;
import com.pmberjaya.indotiki.services.fcm.FCMAuthLoginHandler;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.List;


/**
 * Created by Rony on 03/11/2017.
 */

public class HelpFragment extends Fragment implements GestureDetector.OnGestureListener, MainActivityTab.OnBackPressedListener {
    private View rootView;
    private Toolbar toolbar;
    private EditText ed_search;
    private LinearLayout ic_search;
    ViewFlipper viewFlipper;
    private LinearLayout servicecategoryhelp_layout;
    private LinearLayout helpFAQlayout;
    private LinearLayout helpServiceErrorLayout;
    private RecyclerView categoryservices_list;
    private HelpServiceAdapter helpServiceAdapter;
    List<HelpSubCategoryData>helpserviceSubCategoryData;
    List<HelpCategoryData>serviceCategoryHelpData;
    List<HelpFaqData>helpFAQData;
    List<HelpSubCategoryData>searchHelpData;
    private ListView submenuhelp_list;
    private ListView searched_list;
    private RecyclerView helpFAQlist;
    private HelpSubCategoryAdapter helpSubCategoryAdapter;
    private HelpFaqAdapter helpFAQAdapter;
    private HelpSearchQuestionAdapter searchedQuestionAdapter;
    private WebView webView;
    private ProgressBar progressBar;
    boolean selectedinprogress = false;
    private String movefrom = null;
    private TextView tv_noresult;
    private TextView tv_question;
    private ImageView btn_helpservice_tryagain;
    private boolean beginSearch = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.help_fragment, container, false);
        ((MainActivityTab) getActivity()).setOnBackPressedListener(this);
        renderView();
        initToolbar();
        getHelpService();
        return rootView;
    }

    public void renderView() {
        toolbar = (Toolbar) rootView.findViewById(R.id.tool_bar);
        ed_search = (EditText) rootView.findViewById(R.id.ed_search);
        ic_search = (LinearLayout) rootView.findViewById(R.id.icon_search);
        viewFlipper = (ViewFlipper)rootView.findViewById(R.id.viewflipper);
        servicecategoryhelp_layout = (LinearLayout) rootView.findViewById(R.id.servicecategoryhelp_layout);
        helpFAQlayout = (LinearLayout) rootView.findViewById(R.id.frequentlyasked_layout);
        categoryservices_list = (RecyclerView)rootView.findViewById(R.id.categories_help_list);
        submenuhelp_list = (ListView)rootView.findViewById(R.id.submenuhelp_list);
        searched_list = (ListView)rootView.findViewById(R.id.searched_help_list);
        helpFAQlist = (RecyclerView)rootView.findViewById(R.id.frequentquestions_list);
        tv_noresult = (TextView)rootView.findViewById(R.id.tv_search_noresult);
        tv_question = (TextView)rootView.findViewById(R.id.tv_question);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        helpServiceErrorLayout = (LinearLayout) rootView.findViewById(R.id.helpServiceErrorLayout);
        btn_helpservice_tryagain = (ImageView) rootView.findViewById(R.id.bt_helpservice_tryagain);
        webView = (WebView)rootView.findViewById(R.id.webview);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed_search.getText().length()>0){
                    progressBar.setVisibility(View.VISIBLE);
                    viewFlipper.setDisplayedChild(3);
                    String q = ed_search.getText().toString();
                    beginSearch = true;
                    getSearchHelp(q);
                }else {
                    if(beginSearch) {
                        doBack();
                        beginSearch = false;
                    }
                }
            }
        });

        btn_helpservice_tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHelpService();
            }
        });
    }

    private void getSearchHelp(String subject) {
        progressBar.setVisibility(View.VISIBLE);
        String api = Utility.getInstance().getTokenApi(getActivity());
        UserController.getInstance(getActivity()).getSearchHelp(subject,api,getSearchHelpInterface);
        return;
    }

    BaseGenericInterface getSearchHelpInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            progressBar.setVisibility(View.GONE);
            if (baseGenericCallback.getSukses() == 2) {
                searchHelpData = (List<HelpSubCategoryData>) baseGenericCallback.getData();
                setSearchedQuestionList(searchHelpData);

            } else {

            }

        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(getActivity(),getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void initToolbar() {
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.nav_item_support));
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    private void getHelpService() {
        progressBar.setVisibility(View.VISIBLE);
        String api = Utility.getInstance().getTokenApi(getActivity());
        UserController.getInstance(getActivity()).getHelpService(api,getHelpServiceInterface);
        return;
    }

    BaseGenericInterface getHelpServiceInterface = new BaseGenericInterface() {
        @Override
        public  void onSuccess(BaseGenericCallback baseGenericCallback) {
            progressBar.setVisibility(View.GONE);
            if (baseGenericCallback.getSukses() == 2) {
                HelpServiceData helpServiceData = (HelpServiceData) baseGenericCallback.getData();
                serviceCategoryHelpData = helpServiceData.getCategory();
                helpFAQData = helpServiceData.getTopFive();
                setHelpServiceAdapter(serviceCategoryHelpData, helpFAQData);

            } else {
                helpServiceErrorLayout.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if(apiErrorCallback.getError()!=null) {
                progressBar.setVisibility(View.GONE);
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    helpServiceErrorLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    helpServiceErrorLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void categoryhelpnext(String id){
        if(selectedinprogress== false){
            getHelpServiceSubcategoryList(id);
        }
    }

    public void getHelpAnswer(String answer,String intentfrom,String question){
        webView.loadData(answer, "text/html", null);
        movefrom = intentfrom;

        viewFlipper.setDisplayedChild(2);
        tv_question.setText(question);
    }

//    private void getFAQList() {
//        progressBar.setVisibility(View.VISIBLE);
//        String api = Utility.getInstance().getTokenApi(getActivity());
//        UserController.getInstance(getActivity()).getHelpFAQ(api,getHelpFAQListInterface);
//        return;
//    }
//
//    BaseGenericInterface getHelpFAQListInterface = new BaseGenericInterface() {
//        @Override
//        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
//            if(baseGenericCallback.getCallback().equals("getHelpFAQList")) {
//                progressBar.setVisibility(View.GONE);
//                if (baseGenericCallback.getSukses() == 2) {
//                    helpFAQData = (List<HelpFAQData>) baseGenericCallback.getData();
//                    setFAQList(helpFAQData);
//
//                } else {
//                    errorFAQlayout.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//
//        @Override
//        public void onError(APIErrorCallback apiErrorCallback) {
//            if(apiErrorCallback.getCallback().equals("getHelpFAQList")) {
//                if(apiErrorCallback.getError()!=null) {
//                    progressBar.setVisibility(View.GONE);
//                    if (apiErrorCallback.getError().equals("Invalid API key ")) {
//                        Utility.getInstance().showAlert(getActivity(), getResources().getString(R.string.relogin));
//                    }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
//                        errorFAQlayout.setVisibility(View.VISIBLE);
//                        Toast.makeText(getActivity(),getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
//                    }else{
//                        errorFAQlayout.setVisibility(View.VISIBLE);
//                        Toast.makeText(getActivity(),getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }
//    };

//    public void setFAQList(List<HelpFAQData> faqListarray) {
//        helpFAQlayout.setVisibility(View.VISIBLE);
//        helpFAQAdapter = new HelpFAQAdapter(getActivity(),faqListarray,HelpFragment.this);
//        final FragmentActivity c = getActivity();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
//        helpFAQlist.setLayoutManager(layoutManager);
//        helpFAQlist.setHasFixedSize(true);
//        helpFAQlist.setAdapter(helpFAQAdapter);
//    }

    public void setSearchedQuestionList(List<HelpSubCategoryData> searchHelpData) {
        if(searchHelpData.size()!=0){
            tv_noresult.setVisibility(View.GONE);
            searched_list.setVisibility(View.VISIBLE);
        }else {
            tv_noresult.setVisibility(View.VISIBLE);
            searched_list.setVisibility(View.GONE);
        }
        searchedQuestionAdapter = new HelpSearchQuestionAdapter(getActivity(),0,searchHelpData,HelpFragment.this);
        searched_list.setAdapter(searchedQuestionAdapter);
    }


    public void setHelpServiceAdapter(List<HelpCategoryData> helpservicedata,List <HelpFaqData> helpFAQData) {
        helpServiceAdapter= new HelpServiceAdapter(getActivity(),helpservicedata,HelpFragment.this);
        servicecategoryhelp_layout.setVisibility(View.VISIBLE);
        GridLayoutManager gLayoutManager = new GridLayoutManager(getActivity(), 2);
        categoryservices_list.setHasFixedSize(true);
        categoryservices_list.setLayoutManager(gLayoutManager);
        categoryservices_list.setAdapter(helpServiceAdapter);
//        ================
        helpFAQAdapter = new HelpFaqAdapter(getActivity(),helpFAQData,HelpFragment.this);
        helpFAQlayout.setVisibility(View.VISIBLE);
        final FragmentActivity c = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        helpFAQlist.setLayoutManager(layoutManager);
        helpFAQlist.setHasFixedSize(true);
        helpFAQlist.setAdapter(helpFAQAdapter);
    }

    private void getHelpServiceSubcategoryList(String id) {
        progressBar.setVisibility(View.VISIBLE);
        selectedinprogress = true;
        String api = Utility.getInstance().getTokenApi(getActivity());
        UserController.getInstance(getActivity()).getHelpServiceSelected(id,api,getHelpServiceSubcategoryInterface);
        return;
    }

    BaseGenericInterface getHelpServiceSubcategoryInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            progressBar.setVisibility(View.GONE);

            if (baseGenericCallback.getSukses() == 2) {
                helpserviceSubCategoryData = (List<HelpSubCategoryData>) baseGenericCallback.getData();
                SetListSubmenuAdapter(helpserviceSubCategoryData);
//                    viewFlipper.setDisplayedChild(1);
                viewFlipper.showNext();
                selectedinprogress = false;
//                    setHelpServiceAdapter(serviceCategoryData);

            } else {
                selectedinprogress = false;
                Toast.makeText(getActivity(),baseGenericCallback.getPesan(),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(getActivity(),getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void SetListSubmenuAdapter(List<HelpSubCategoryData> helparray) {
        submenuhelp_list.setVisibility(View.VISIBLE);
        helpSubCategoryAdapter = new HelpSubCategoryAdapter(getActivity(), 0,helparray,HelpFragment.this);
        submenuhelp_list.setAdapter(helpSubCategoryAdapter);
    }

    @Override
    public void doBack() {
        if (viewFlipper.getDisplayedChild() == 1 || viewFlipper.getDisplayedChild()==2 && movefrom ==null) {
            viewFlipper.showPrevious();
        }else if(viewFlipper.getDisplayedChild()==3) {
            if(ed_search.getText().length()>0){
                ed_search.setText("");
            }
            viewFlipper.setDisplayedChild(0);
            progressBar.setVisibility(View.GONE);

        }else if(viewFlipper.getDisplayedChild()==2 ){
            if(movefrom !=null &&movefrom.equals("faq") || movefrom.equals("search")) {
                if(ed_search.getText().length()>0){
                    ed_search.setText("");
                }
                viewFlipper.setDisplayedChild(0);
                movefrom = null;
            }else {
                viewFlipper.showPrevious();
            }/*else if(movefrom.equals("subcategory")){
                viewFlipper.setDisplayedChild(0);
                movefrom = null;
                webView.clearView();*/
//            }
            webView.clearView();
        }else {
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(null);
            builder.setMessage(R.string.exit_confirmation);
            builder.setIcon(R.mipmap.ic_launcher);

            // Set the action buttons
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    getActivity().stopService(new Intent(getActivity(), FCMAuthLoginHandler.class));
                    getActivity().finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }
    }
}