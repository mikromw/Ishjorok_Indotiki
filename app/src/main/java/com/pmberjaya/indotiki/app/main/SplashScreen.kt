package com.pmberjaya.indotiki.app.main

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.text.Html
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView
import com.crashlytics.android.Crashlytics
import com.google.android.gms.location.LocationSettingsStates
import com.pmberjaya.indotiki.R
import com.pmberjaya.indotiki.base.BaseGenericCallback
import com.pmberjaya.indotiki.base.BaseGenericInterface
import com.pmberjaya.indotiki.callbacks.APIErrorCallback
import com.pmberjaya.indotiki.callbacks.gmaps.GeocoderLocationGMapsCallback
import com.pmberjaya.indotiki.callbacks.main.KeyCallback
import com.pmberjaya.indotiki.callbacks.main.VersionAndMaintenanceCallback
import com.pmberjaya.indotiki.config.Config
import com.pmberjaya.indotiki.config.Constants
import com.pmberjaya.indotiki.controllers.UserController
import com.pmberjaya.indotiki.controllers.UtilityController
import com.pmberjaya.indotiki.dao.DBController
import com.pmberjaya.indotiki.dao.LocationSessionManager
import com.pmberjaya.indotiki.dao.SessionKey
import com.pmberjaya.indotiki.interfaces.account.APIKeyInterface
import com.pmberjaya.indotiki.interfaces.gmaps.GeocoderLocationInterface
import com.pmberjaya.indotiki.interfaces.main.VersionAndMaintenanceInterface
import com.pmberjaya.indotiki.interfaces.misc.GPSTrackerInterface
import com.pmberjaya.indotiki.interfaces.misc.OnCreateDatabaseInterface
import com.pmberjaya.indotiki.models.main.AppVersionData
import com.pmberjaya.indotiki.models.main.BannerData
import com.pmberjaya.indotiki.models.main.DistrictCentralData
import com.pmberjaya.indotiki.utilities.GPSTracker
import com.pmberjaya.indotiki.utilities.Utility
import com.pmberjaya.indotiki.views.EventPromoCustomDialog
import io.fabric.sdk.android.Fabric
import java.io.IOException
import java.util.*

class SplashScreen : AppCompatActivity(), EventPromoCustomDialog.onSubmitListener, OnCreateDatabaseInterface, GPSTrackerInterface {
    private var locationSession: LocationSessionManager? = null
    private var sessionKey: SessionKey? = null
    private var controller: DBController? = null
    private var bt_try_again: Button? = null
    private var loadinglayout: LottieAnimationView? = null
    private var type = ""
    private var api: String? = null
    private var aDialog: AlertDialog? = null
    private var error_layout: CardView? = null
    private var city: String? = null
    private var state: String? = null
    private var district_id: String? = null
    private var state_id :String?=null

    private var district_id_central = ""
    private var mLastLocation: Location? = null

    var try_again_listener: View.OnClickListener = View.OnClickListener {
        if (type == "MaintenanceAndAppVersion") {
            getVersionAndMaintenance()
        } else if (type == "putKey") {
            doMakeKey()
        } else if (type == "getGeocoderLocation") {
            checkCity(mLastLocation)
        } else if (type == "getPopUpBanner") {
            getPromoEventPopUp(district_id_central)
        } else if (type == "getDistrictCentral") {
            getDistrictCentral(district_id)
        }
    }

    internal var versionAndMaintenanceInterface: VersionAndMaintenanceInterface = object : VersionAndMaintenanceInterface {
        override fun onSuccessVersionAndMaintenance(versionAndMaintenanceCallback: VersionAndMaintenanceCallback) {
            val sukses = versionAndMaintenanceCallback.sukses
            val data_maintenance = versionAndMaintenanceCallback.data_maintenance
            val data_version = versionAndMaintenanceCallback.data_version
            if (sukses == 2) {
                if (data_maintenance != null && (data_maintenance.type == "1" || data_maintenance.type == "2")) {
                    val details = data_maintenance.details
                    showAlertMaintenance(details)
                } else if (data_version.size != 0) {
                    showAlertAppVersion(data_version)
                } else {
                    sessionKey = SessionKey(applicationContext)
                    if (sessionKey!!.isHaveKey) {
                        if (district_id != null) {
                            getDistrictCentral(district_id)
                        } else {
                            intentToMainMenu()
                        }
                    } else {
                        doMakeKey()
                    }
                }
            } else {
                renderErrorView("MaintenanceAndAppVersion")
            }
        }

        override fun onErrorVersionAndMaintenance(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                renderErrorView("MaintenanceAndAppVersion")
            }
        }
    }

    internal var geocoderLocationInterface: GeocoderLocationInterface = object : GeocoderLocationInterface {
        override fun onSuccessGetGeocoderLocation(geocoderLocationGMapsCallback: GeocoderLocationGMapsCallback) {
            val status = geocoderLocationGMapsCallback.status
            if (status == "OK") {
                var route: String? = null
                val result = geocoderLocationGMapsCallback.results
                val addresses = result[0].addressComponents
                var country = ""
                for (i in addresses.indices) {
                    val types = addresses[i].types
                    val type = types[0]
                    if (type == "country") {
                        country = addresses[i].longName
                    } else if (type == "administrative_area_level_2") {
                        city = addresses[i].longName
                    } else if (type == "administrative_area_level_1") {
                        state = addresses[i].longName
                    } else if (type == "route") {
                        route = addresses[i].longName
                    } else if (type == "neighborhood") {
                        state = addresses[i].longName
                    }
                }
                if (city != null) {
                    val dbController = DBController(this@SplashScreen)
                    val locationData = dbController.getUserLocation(city)
                    if (locationData != null) {
                        district_id = locationData["district_id"]
                        state_id = locationData!!["state_id"]
                    }
                }
            } else {
                renderErrorView("getGeocoderLocation")
            }
            if (!Utility.getInstance().checkIfStringIsNotNullOrEmpty(district_id)) {
                district_id = locationSession!!.userDistrictIdCentral
            }
            getVersionAndMaintenance()
        }

        override fun onErrorGetGeocoderLocation(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                renderErrorView("getGeocoderLocation")
            }
        }
    }

    internal var getDistrictCentralInterface: BaseGenericInterface = object : BaseGenericInterface {
        override fun <T> onSuccess(districtCentralCallback: BaseGenericCallback<T>) {
            val sukses = districtCentralCallback.sukses
            if (sukses == 2) {
                val districtCentralData = districtCentralCallback.data as DistrictCentralData
                if (districtCentralData != null) {
                    district_id_central = districtCentralData.district_id_central
                }
                locationSession!!.setUserLocation(state_id, state, district_id, city, district_id_central)
                getPromoEventPopUp(district_id_central)
            }
        }

        override fun onError(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                if (apiErrorCallback.error == "Invalid API key ") {
                    Utility.getInstance().showInvalidApiKeyAlert(this@SplashScreen, resources.getString(R.string.relogin))
                } else {
                    renderErrorView("getDistrictCentral")
                }
            }
        }
    }

    internal var apiKeyInterface: APIKeyInterface = object : APIKeyInterface {
        override fun onSuccessGetAPIKey(response: KeyCallback) {
            val statusIsTrue = response.status!!
            if (statusIsTrue) {
                api = response.key
                sessionKey!!.createKey(api)
                if (district_id != null) {
                    getDistrictCentral(district_id)
                } else {
                    intentToMainMenu()
                }
            } else {
                renderErrorView("putKey")
            }
        }

        override fun onErrorGetAPIKey(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                renderErrorView("putKey")
            }
        }
    }

    internal var popUpInterface: BaseGenericInterface = object : BaseGenericInterface {
        override fun <T> onSuccess(bannerCallback: BaseGenericCallback<T>) {
            val sukses = bannerCallback.sukses
            if (sukses == 2) {
                val data = bannerCallback.data as List<BannerData>
                if (data.size != 0) {
                    val random = Random()
                    val max = data.size - 1
                    val min = 0
                    val position = random.nextInt(max - min + 1) + min
                    val eventPromoPhoto = data[position].cover
                    val title = data[position].title
                    val description = data[position].description
                    val promoPopUpCode = data[position].code_promo
                    val eventPromoCustomDialog = EventPromoCustomDialog()
                    eventPromoCustomDialog.mListener = this@SplashScreen
                    eventPromoCustomDialog.eventPromoPhoto = eventPromoPhoto
                    eventPromoCustomDialog.eventPromoTitle = title
                    eventPromoCustomDialog.eventPromoDescription = description
                    eventPromoCustomDialog.eventPromoCode = promoPopUpCode
                    eventPromoCustomDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme)
                    eventPromoCustomDialog.show(supportFragmentManager, "DialogFragment")
                    eventPromoCustomDialog.isCancelable = false
                } else {
                    intentToMainMenu()
                }
            } else {
                intentToMainMenu()
            }
        }

        override fun onError(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                if (apiErrorCallback.error == "Invalid API key ") {
                    Utility.getInstance().showInvalidApiKeyAlert(this@SplashScreen, resources.getString(R.string.relogin))
                } else {
                    renderErrorView("getPopUpBanner")
                }
            }
        }
    }
    //    1. AMBIL LOKASI
    //    2. AMBIL DATA MAINTENANCE
    //    3. AMBIL DATA API KEY
    //    4. AMBIL DATA PROMO

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Fabric.with(this, Crashlytics())
        bindViews()
        locationSession = LocationSessionManager(this)
        controller = DBController(this)
        try {
            controller!!.copyDatabase(this)
        } catch (ioe: IOException) {
            throw Error("Unable to create database")
        }
        //=>OnSuccessCreateDatabase()
    }

    override fun OnSuccessCreateDatabase() {
        GPSTracker.getInstance().init(this@SplashScreen, this)
    }

    override fun OnErrorCreateDatabase(errorMsg: String) {

    }

    override fun onLocationReceived(location: Location) {
        val latitude_gps = location.latitude
        val longitude_gps = location.longitude
        mLastLocation = location
        checkCity(mLastLocation)
        val locationSession = LocationSessionManager(this)
        locationSession.setCurrentLatLng(latitude_gps.toString(), longitude_gps.toString())
    }


    override fun onLocationError(str: String) {

    }

    private fun bindViews() {
        loadinglayout = findViewById<View>(R.id.loading) as LottieAnimationView
        error_layout = findViewById<View>(R.id.error_layout) as CardView
        bt_try_again = findViewById<View>(R.id.bt_try_again) as Button
        bt_try_again!!.setOnClickListener(try_again_listener)
    }

    fun getVersionAndMaintenance() {
        renderloadingView()
        val version_code = Utility.getInstance().getAppVersionCode(this)
        val type = "3"
        UtilityController.getInstance(this).getVersionAndMaintenance(parameters(version_code, type), versionAndMaintenanceInterface)
        return
    }

    fun parameters(version_code: Int, type: String): Map<String, String> {
        val params = HashMap<String, String>()
        params.put("version_code", version_code.toString())
        params.put("type", type)
        return params
    }

    fun intentToMainMenu() {
        val confirm = Intent(this@SplashScreen, MainActivityTab::class.java)
        confirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(confirm)
        finish()
    }

    fun makeChangeLog(appVersionDatas: List<AppVersionData>): String {
        val sb = StringBuilder()
        var prefix = ""
        for (i in appVersionDatas.indices) {
            val version = "Version: " + appVersionDatas[i].app_version_name
            val changelog = appVersionDatas[i].changelog
            sb.append(prefix)
            prefix = "<br>"
            sb.append("<font size='16'><b>$version</b></font>")
            sb.append(prefix)
            sb.append(changelog)
        }
        return sb.toString()
    }

    protected fun showAlertAppVersion(appVersionDatas: List<AppVersionData>) {
        val change = makeChangeLog(appVersionDatas)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(R.string.new_updates)
        alertDialog.setIcon(R.mipmap.ic_launcher)
        alertDialog.setMessage(Html.fromHtml(change))
        alertDialog.setPositiveButton(R.string.update) { dialog, which ->
            val uri = Uri.parse("market://details?id=" + packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)))
            }

            finish()
        }
        // on pressing cancel button
        aDialog = alertDialog.create()
        aDialog!!.setCanceledOnTouchOutside(false)
        aDialog!!.setCancelable(false)
        aDialog!!.show()
    }

    protected fun showAlertMaintenance(message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(resources.getString(R.string.maintenance))
        alertDialog.setMessage(message)
        alertDialog.setIcon(R.mipmap.ic_launcher)
        alertDialog.setNegativeButton(R.string.back) { dialog, id ->
            dialog.dismiss()
            finish()
        }
        // on pressing cancel button
        aDialog = alertDialog.create()
        aDialog!!.setCanceledOnTouchOutside(false)
        aDialog!!.show()
    }

    fun renderloadingView() {
        loadinglayout!!.visibility = View.VISIBLE
        error_layout!!.visibility = View.GONE
    }

    fun renderErrorView(type: String) {
        this.type = type
        loadinglayout!!.visibility = View.GONE
        error_layout!!.visibility = View.VISIBLE
    }

    fun checkCity(location: Location?) {
        renderloadingView()
        val origin = location!!.latitude.toString() + "," + location.longitude
        UtilityController.getInstance(this@SplashScreen).getGeocoderLocation(geocoderParams(origin), geocoderLocationInterface)
        return
    }

    fun geocoderParams(origins: String): Map<String, String> {
        val params = HashMap<String, String>()
        params.put("language", "id")
        params.put("latlng", origins)
        params.put("sensor", "true")
        params.put("key", Config.SERVER_API_KEY)
        return params
    }

    fun getDistrictCentral(district_id: String?) {
        renderloadingView()
        val api = Utility.getInstance().getTokenApi(this@SplashScreen)
        UserController.getInstance(this).getDistrictCentral(api, districtCentralParameters(district_id), getDistrictCentralInterface)
        return
    }

    fun districtCentralParameters(district_id: String?): Map<String, String> {
        val params = HashMap<String, String>()
        params.put("district_id", district_id!!)
        return params
    }

    fun doMakeKey() {
        renderloadingView()
        UserController.getInstance(this).putKey(apiKeyInterface)
    }

    fun getPromoEventPopUp(district_id: String) {
        renderloadingView()
        val page = "1"
        val type_banner = "POP_UP"
        val api = Utility.getInstance().getTokenApi(this@SplashScreen)
        UserController.getInstance(this).getBanner(popUpParameters(district_id, type_banner, page), api, popUpInterface)
        return
    }

    fun popUpParameters(district_id: String, type_banner: String, page: String): Map<String, String> {
        val params = HashMap<String, String>()
        params.put("district_id", district_id)
        params.put("type_banner", type_banner)
        params.put("page", page)
        params.put("app", Constants.APP_MEMBER)
        return params
    }

    override fun setOnSubmitListener(promoCodeIntentExtra: String) {
        val intent = Intent(this@SplashScreen, MainActivityTab::class.java)
        intent.putExtra(Constants.PROMO_POPUP_CODE, promoCodeIntentExtra)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val states = LocationSettingsStates.fromIntent(data)
        when (requestCode) {
            GPSTracker.REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d("YES", "OK")
                    // All required changes were successfully made
                    GPSTracker.getInstance().startLocationUpdates()
                }
                Activity.RESULT_CANCELED ->
                    // The user was asked to change settings, but chose not to
                    Log.d("CANCELLED", "NO")
                else -> {
                }
            }
        }
    }

    public override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (aDialog != null) {
            aDialog!!.dismiss()
        }
    }


    override fun onStart() {
        super.onStart()
    }


}
