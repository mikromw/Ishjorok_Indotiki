package com.pmberjaya.indotiki.app.main

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.pmberjaya.indotiki.R
import com.pmberjaya.indotiki.base.BaseGenericCallback
import com.pmberjaya.indotiki.base.BaseGenericInterface
import com.pmberjaya.indotiki.callbacks.APIErrorCallback
import com.pmberjaya.indotiki.controllers.UserController
import com.pmberjaya.indotiki.dao.LocationSessionManager
import com.pmberjaya.indotiki.dao.SessionManager
import com.pmberjaya.indotiki.models.event.EventPromoModel
import com.pmberjaya.indotiki.models.main.MainMenuData2
import com.pmberjaya.indotiki.models.main.MainMenuItemData
import com.pmberjaya.indotiki.utilities.Utility
import me.relex.circleindicator.CircleIndicator
import java.util.*

/**
 * Created by edwin on 18/10/2016.
 */

class MenuGridFragment : Fragment() {

    private var viewPager: ViewPager? = null
    private var listMenu: RecyclerView? = null
    private var layoutBanner: RelativeLayout? = null
    private var session: SessionManager? = null
    private var mainBannerAdapter: MainBannerAdapter? = null
    var adapter: MenuGridAdapter? = null
        private set

    private var currentItem: Int = 0
    private var totalItems: Int = 0
    private var districtId: String? = null
    private var runGetImageEvent = true

    private var Update: Runnable? = null
    private var handler: Handler? = null
    private var swipeTimer: Timer? = null
    private var pagerIndicator: CircleIndicator? = null
    private var mainMenuItemDatas: ArrayList<MainMenuItemData>? = null
    private var mainMenuArray: ArrayList<MainMenuData2>? = null

    internal var imageEventInterface: BaseGenericInterface = object : BaseGenericInterface {
        override fun <T> onSuccess(baseGenericCallback: BaseGenericCallback<T>) {
            runGetImageEvent = false
            val pesan = baseGenericCallback.pesan
            if (baseGenericCallback.sukses == 2) {
                val data = baseGenericCallback.data as EventPromoModel
                val listArray = data.resultArray
                if (listArray.size != 0) {
                    mainBannerAdapter = MainBannerAdapter(context, listArray)
                    viewPager!!.adapter = mainBannerAdapter
                    pagerIndicator!!.setViewPager(viewPager)
                    currentItem = viewPager!!.currentItem
                    totalItems = viewPager!!.adapter.count
                    handler = Handler()
                    Update = Runnable {
                        if (currentItem > totalItems) { // In my case the number of pages are 5
                            currentItem = 0
                            viewPager!!.setCurrentItem(currentItem++, true)
                        } else {
                            viewPager!!.currentItem = currentItem++
                        }
                    }
                    swipeTimer = Timer()
                    swipeTimer!!.schedule(object : TimerTask() {

                        override fun run() {
                            handler!!.post(Update)
                        }
                    }, 1000, 6000)
                    viewPager!!.visibility = View.VISIBLE
                    layoutBanner!!.visibility = View.VISIBLE
                    viewPager!!.clipToPadding = false
                } else {
                    //                        layoutBanner.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(activity, pesan, Toast.LENGTH_SHORT).show()
                //                    layoutBanner.setVisibility(View.GONE);
            }
        }

        override fun onError(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                if (apiErrorCallback.error == "Invalid API key ") {
                    Utility.getInstance().showInvalidApiKeyAlert(context, resources.getString(R.string.relogin))
                } else {
                    if (!session!!.isLogin) {
                        viewPager!!.visibility = View.GONE
                    }

                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        if (bundle != null) {
            mainMenuItemDatas = bundle.getParcelableArrayList("menu_data")
            mainMenuArray = getMainMenuArray(mainMenuItemDatas)
        }
        val rootView = inflater!!.inflate(R.layout.main_menu_grid_fragment, container, false)
        initLocationSession()
        renderView(rootView)
        return rootView
    }

    private fun initLocationSession() {
        session = SessionManager(context)
        val locationSession = LocationSessionManager(context)
        districtId = locationSession.userDistrictIdCentral
    }

    private fun renderView(rootView: View) {
        listMenu = rootView.findViewById(R.id.listMenu)
        pagerIndicator = rootView.findViewById(R.id.pagerIndicator)
        layoutBanner = rootView.findViewById(R.id.layoutBanner)
        viewPager = rootView.findViewById(R.id.viewPager)
        adapter = MenuGridAdapter(activity, mainMenuArray)
        setMenuLayoutManager()
    }


    private fun setMenuLayoutManager() {
        val lLayout = GridLayoutManager(activity, 3)
        listMenu!!.setHasFixedSize(true)
        listMenu!!.layoutManager = lLayout
        listMenu!!.adapter = adapter
    }

    fun getMainMenuArray(mainMenuItemDatas: ArrayList<MainMenuItemData>?): ArrayList<MainMenuData2> {
        val menuArray = ArrayList<MainMenuData2>()
        for (i in mainMenuItemDatas!!.indices) {
            val mainMenuDatas = MainMenuData2()
            if (mainMenuItemDatas[i].display_menu == "INDO_OJEK") {
                mainMenuDatas.menuName = activity.resources.getString(R.string.indo_ojek)
                mainMenuDatas.menuImage = R.mipmap.ic_logo_motorcycle
                mainMenuDatas.isComing_soon = false
                mainMenuDatas.status = mainMenuItemDatas[i].status
            } else if (mainMenuItemDatas[i].display_menu == "INDO_COURIER" + "") {
                mainMenuDatas.menuName = activity.resources.getString(R.string.indo_courier)
                mainMenuDatas.menuImage = R.mipmap.ic_logo_courier
                mainMenuDatas.isComing_soon = false
                mainMenuDatas.status = mainMenuItemDatas[i].status
            } else if (mainMenuItemDatas[i].display_menu == "INDO_FOOD") {
                mainMenuDatas.menuName = activity.resources.getString(R.string.indo_food)
                mainMenuDatas.menuImage = R.mipmap.ic_logo_food
                mainMenuDatas.isComing_soon = false
                mainMenuDatas.status = mainMenuItemDatas[i].status
            }
            //            else if (mainMenuItemDatas.get(i).getDisplay_menu().equals("INDO_PULSA")){
            //                mainMenuDatas.setMenuName(getResources().getString(R.string.pulsa));
            //                mainMenuDatas.setMenuImage(R.mipmap.ic_logo_pulsa);
            //                mainMenuDatas.setComing_soon(false);
            //                mainMenuDatas.setStatus(mainMenuItemDatas.get(i).getStatus());
            //            } else if (mainMenuItemDatas.get(i).getDisplay_menu().equals("INDO_PEDICAB")) {
            //                mainMenuDatas.setMenuName(getActivity().getResources().getString(R.string.indo_becak));
            //                mainMenuDatas.setMenuImage(R.mipmap.ic_logo_pedicab);
            //                mainMenuDatas.setComing_soon(false);
            //                mainMenuDatas.setStatus(mainMenuItemDatas.get(i).getStatus());
            //            } else if (mainMenuItemDatas.get(i).getDisplay_menu().equals("INDO_MART")) {
            //                mainMenuDatas.setMenuName(getActivity().getResources().getString(R.string.mart));
            //                mainMenuDatas.setMenuImage(R.mipmap.ic_logo_mart);
            //                mainMenuDatas.setComing_soon(false);
            //                mainMenuDatas.setStatus(mainMenuItemDatas.get(i).getStatus());
            //            } else if (mainMenuItemDatas.get(i).getDisplay_menu().equals("INDO_TAGIHAN")) {
            //                mainMenuDatas.setMenuName(getActivity().getResources().getString(R.string.bill));
            //                mainMenuDatas.setMenuImage(R.mipmap.ic_logo_pln);
            //                mainMenuDatas.setComing_soon(false);
            //                mainMenuDatas.setStatus(mainMenuItemDatas.get(i).getStatus());
            //                mainMenuDatas.setSubmenu(mainMenuItemDatas.get(i).getSubmenu());
            //            }else if (mainMenuItemDatas.get(i).getDisplay_menu().equals("INDO_CAR")) {
            //                mainMenuDatas.setMenuName(getActivity().getResources().getString(R.string.car));
            //                mainMenuDatas.setMenuImage(R.mipmap.ic_logo_pulsa);
            //                mainMenuDatas.setComing_soon(false);
            //                mainMenuDatas.setStatus(mainMenuItemDatas.get(i).getStatus());
            //                mainMenuDatas.setSubmenu(mainMenuItemDatas.get(i).getSubmenu());
            //            }
            menuArray.add(mainMenuDatas)
        }
        return menuArray
    }

    private fun getImageEvent() {
        runGetImageEvent = true
        //        viewPager.setVisibility(View.GONE);
        Log.d("ImageEvent", "JALAN")
        val api = Utility.getInstance().getTokenApi(context)
        UserController.getInstance(activity).getEventPromoList(api, bannerParams(), imageEventInterface)
        return
    }

    private fun bannerParams(): Map<String, String> {
        val params = HashMap<String, String>()
        params.put("district_id", districtId!!)
        params.put("app", "0")
        return params
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (runGetImageEvent == true) {
            getImageEvent()
        }
    }
}