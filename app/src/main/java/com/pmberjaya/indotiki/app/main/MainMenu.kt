package com.pmberjaya.indotiki.app.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.DeviceShareButton
import com.pmberjaya.indotiki.R
import com.pmberjaya.indotiki.app.promo.MyPromoActivity
import com.pmberjaya.indotiki.base.BaseGenericCallback
import com.pmberjaya.indotiki.base.BaseGenericInterface
import com.pmberjaya.indotiki.callbacks.APIErrorCallback
import com.pmberjaya.indotiki.config.Constants
import com.pmberjaya.indotiki.controllers.UserController
import com.pmberjaya.indotiki.dao.DBController
import com.pmberjaya.indotiki.dao.LocationSessionManager
import com.pmberjaya.indotiki.dao.SessionManager
import com.pmberjaya.indotiki.models.account.UserModel
import com.pmberjaya.indotiki.models.deposit.DepositData.CheckDepositData
import com.pmberjaya.indotiki.models.main.MainMenuItemData
import com.pmberjaya.indotiki.utilities.Utility
import com.pmberjaya.indotiki.views.EventPromoCustomDialog
import java.util.*

/**
 * Created by edwin on 04/11/2017.
 */

class MainMenu : Fragment(), EventPromoCustomDialog.onSubmitListener {

    private var bottomLayoutSheet: BottomSheetBehavior<*>? = null
    private var fragmentScroll: NestedScrollView? = null
    private var scrollContentSlidingPanel: ScrollView? = null
    private var layoutContentBottomSheet: LinearLayout? = null
    private var layoutBottomSheet: RelativeLayout? = null
    private var layoutMain: RelativeLayout? = null
    private var ivArrow: ImageView? = null
    private var tvInkiPayBalance: TextView? = null
    private var layoutNotLoginInfo: LinearLayout? = null
    private var layoutInkiPayBalance: LinearLayout? = null
    private var btnInviteFriend: LinearLayout? = null
    private var btnShareFb: LinearLayout? = null
    private var fbShareButton: DeviceShareButton? = null
    private var edVoucherCode: EditText? = null
    private var btnUseVoucher: Button? = null
    private var btnLogin: LinearLayout? = null
    private var pDialog: ProgressDialog? = null

    var banyakdata: Int = 0
    private var nama: String? = null
    private var nohp: String? = null
    private val pesan: String? = null
    private var avatar: String? = null
    private val userId: String? = null
    private var deposit: String? = null
    private var district_id: String? = null
    private var promoPopUpCode: String? = null

    private var mActivity: Activity? = null
    private var fm: FragmentManager? = null
    private var ft: FragmentTransaction? = null
    private var session: SessionManager? = null
    private var dbController: DBController? = null
    private var userController: UserController? = null
    private var menuGridFragment: MenuGridFragment? = null
    private var locationSessionManager: LocationSessionManager? = null
    private var mainMenuItemDatas: ArrayList<MainMenuItemData>? = null
    private val menu = arrayOf("INDO_OJEK", "INDO_COURIER", "INDO_FOOD", "INDO_PULSA", "INDO_TAXI", "INDO_CAR", "INDO_PEDICAB", "INDO_TAGIHAN")

    internal var getMainMenuInterface: BaseGenericInterface = object : BaseGenericInterface {
        override fun <T> onSuccess(getMainMenuItemCallback: BaseGenericCallback<T>) {
            if (getMainMenuItemCallback.sukses == 2) {
                val dataCustom = ArrayList<MainMenuItemData>() // Data custom untuk category menu home
                val data = getMainMenuItemCallback.data as ArrayList<MainMenuItemData>
                for (i in 0..2) {
                    dataCustom.add(data[i])
                }
                if (mainMenuItemDatas!!.size != 0) {
                    dbController!!.deleteMainMenu()
                    menuGridFragment!!.adapter!!.changeSetDataAdapter(menuGridFragment!!.getMainMenuArray(dataCustom))
                    menuGridFragment!!.adapter!!.notifyDataSetChanged()
                } else {
                    fm = activity.supportFragmentManager
                    ft = fm!!.beginTransaction()
                    val menuGridFragment = MenuGridFragment()
                    val bundle = Bundle()
                    bundle.putParcelableArrayList("menu_data", dataCustom)
                    menuGridFragment.arguments = bundle
                    ft!!.add(R.id.fragment_container, menuGridFragment)
                    ft!!.commitAllowingStateLoss()

                }
                dbController!!.insertMainMenu(data)
            } else {
                Toast.makeText(mActivity, pesan, Toast.LENGTH_SHORT).show()
            }
        }

        override fun onError(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                if (apiErrorCallback.error == "Invalid API key ") {
                    Utility.getInstance().showInvalidApiKeyAlert(mActivity, resources.getString(R.string.relogin))
                } else {
                    /*  if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                        Toast.makeText(mActivity,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mActivity,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }*/
                }
            }
        }
    }
    internal var getUserInterface: BaseGenericInterface = object : BaseGenericInterface {
        override fun <T> onSuccess(getUserDataCallback: BaseGenericCallback<T>) {
            val sukses = getUserDataCallback.sukses
            val pesan = getUserDataCallback.pesan
            if (sukses == 2) {
                val userdata = getUserDataCallback.data as UserModel
                if (userdata != null) {
                    avatar = userdata.getAvatar()
                    nama = userdata.getFullname()
                    nohp = userdata.getPhone()
                    deposit = userdata.getDeposit()
                    val account_for = userdata.getAccount_for()
                    val email = userdata.getEmail()
                    val session = SessionManager(mActivity)
                    tvInkiPayBalance!!.text = "Rp." + " " + Utility.getInstance().convertPrice(userdata.getDeposit())
                    session.createLoginSession(userId, nama, nohp, avatar, email, deposit, null)
                } else {
                    Utility.getInstance().showInvalidApiKeyAlert(mActivity, resources.getString(R.string.relogin))
                }
            } else {
                Toast.makeText(mActivity, pesan, Toast.LENGTH_SHORT).show()
            }
        }

        override fun onError(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                if (apiErrorCallback.error == "Invalid API key ") {
                    Utility.getInstance().showInvalidApiKeyAlert(mActivity, resources.getString(R.string.relogin))
                } else {
                    //                    if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    //                        Toast.makeText(mActivity,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                    //                    }else{
                    //                        Toast.makeText(mActivity,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    //                    }
                }
            }
        }
    }

    internal var checkDepositInterface: BaseGenericInterface = object : BaseGenericInterface {
        override fun <T> onSuccess(checkDepositCallback: BaseGenericCallback<T>) {
            val sukses = checkDepositCallback.sukses
            val pesan = checkDepositCallback.pesan
            if (sukses == 2) {
                val data = checkDepositCallback.data as CheckDepositData
                val deposit = data.deposit
                tvInkiPayBalance!!.text = "Rp. " + Utility.getInstance().convertPrice(deposit)
                session!!.createLoginSession(null, null, null, null, null, deposit, null)
            } else {
                Toast.makeText(mActivity, pesan, Toast.LENGTH_LONG).show()
            }
        }

        override fun onError(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                if (apiErrorCallback.error == "Invalid API key ") {
                    Utility.getInstance().showInvalidApiKeyAlert(mActivity, resources.getString(R.string.relogin))
                } else {
                    //                    if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    //                        Toast.makeText(mActivity,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                    //                    }else{
                    //                        Toast.makeText(mActivity,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    //                    }
                }
            }
        }
    }
    internal var sendReferralInterface: BaseGenericInterface = object : BaseGenericInterface {
        override fun <T> onSuccess(referralCallback: BaseGenericCallback<T>) {
            val datareferal = referralCallback.data as String
            val content = ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(datareferal))
                    .build()
            fbShareButton!!.shareContent = content
            btnShareFb!!.setOnClickListener { fbShareButton!!.performClick() }
            btnInviteFriend!!.setOnClickListener {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Sudah coba Indotiki? Jasa transportasi ojek, kirim paket, antar makan. Daftar di: " + datareferal)
                sharingIntent.type = "text/plain"
                startActivity(sharingIntent)
            }

        }

        override fun onError(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                if (apiErrorCallback.error == "Invalid API key ") {
                    Utility.getInstance().showInvalidApiKeyAlert(mActivity, resources.getString(R.string.relogin))
                } else {
                    sendReferral()
                }
            }
        }
    }
    internal var sendVoucherCodeInterface: BaseGenericInterface = object : BaseGenericInterface {
        override fun <T> onSuccess(sendVoucherCode: BaseGenericCallback<T>) {
            val sukses = sendVoucherCode.sukses
            val pesan = sendVoucherCode.pesan
            pDialog!!.dismiss()
            if (sukses == 2) {
                val i = Intent(mActivity, MyPromoActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                Toast.makeText(mActivity, pesan, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(mActivity, pesan, Toast.LENGTH_LONG).show()
            }
        }

        override fun onError(apiErrorCallback: APIErrorCallback) {
            pDialog!!.dismiss()
            if (apiErrorCallback.error != null) {
                if (apiErrorCallback.error == "Invalid API key ") {
                    Utility.getInstance().showInvalidApiKeyAlert(mActivity, resources.getString(R.string.relogin))
                } else if (apiErrorCallback.error == "Error: Internal Server Error") {
                    Toast.makeText(mActivity, resources.getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(mActivity, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.main_menu_activity, container, false)
        initController()
        getIntentExtra()
        renderView(rootView)
        initDBSession()
        init()
        return rootView
    }

    private fun getIntentExtra() {
        val bundle = this.arguments
        if (bundle != null) promoPopUpCode = bundle.getString(Constants.PROMO_POPUP_CODE)
    }

    private fun initController() {
        userController = UserController.getInstance(mActivity)
    }

    private fun initDBSession() {
        dbController = DBController.getInstance(mActivity)
        locationSessionManager = LocationSessionManager(mActivity)
        session = SessionManager(mActivity)
        val dataCustom = ArrayList<MainMenuItemData>() // Data custom untuk category menu home
        mainMenuItemDatas = dbController!!.mainMenuItemData
        if (mainMenuItemDatas!!.size != 0) {
            fm = activity.supportFragmentManager
            ft = fm!!.beginTransaction()
            menuGridFragment = MenuGridFragment()
            val bundle = Bundle()
            for (i in 0..2) {
                dataCustom.add(mainMenuItemDatas!![i])
            }
            bundle.putParcelableArrayList("menu_data", dataCustom)
            menuGridFragment!!.arguments = bundle
            ft!!.add(R.id.fragment_container, menuGridFragment)
            ft!!.commitAllowingStateLoss()
        }
        district_id = locationSessionManager!!.userDistrictIdCentral
        if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(locationSessionManager!!.userDistrictIdCentral)) {
            getMainMenuItem()
        } else {
            setDefaultMenu()
        }
    }

    private fun renderView(rootView: View) {
        layoutBottomSheet = rootView.findViewById(R.id.bottomLayoutSheet)
        bottomLayoutSheet = BottomSheetBehavior.from(layoutBottomSheet!!)
        fragmentScroll = rootView.findViewById(R.id.fragmentScroll)
        fragmentScroll!!.isSmoothScrollingEnabled = true
        scrollContentSlidingPanel = rootView.findViewById(R.id.scrollContentSlidingPanel)
        layoutContentBottomSheet = rootView.findViewById(R.id.layoutContentBottomSheet)

        layoutMain = rootView.findViewById(R.id.layoutMain)
        ivArrow = rootView.findViewById(R.id.ivArrow)
        tvInkiPayBalance = rootView.findViewById(R.id.tvInkiPayBalance)
        layoutNotLoginInfo = rootView.findViewById(R.id.layoutNotLoginInfo)
        layoutInkiPayBalance = rootView.findViewById(R.id.layoutInkiPayBalance)
        btnInviteFriend = rootView.findViewById(R.id.btnInviteFriend)
        btnShareFb = rootView.findViewById(R.id.btnShareFb)
        fbShareButton = rootView.findViewById(R.id.fbShareButton)
        edVoucherCode = rootView.findViewById(R.id.edVoucherCode)
        btnUseVoucher = rootView.findViewById(R.id.btnUseVoucher)
        btnLogin = rootView.findViewById(R.id.btnLogin)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        setBottomLayoutSheet()
        if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(promoPopUpCode)) {
            val handler = Handler()
            handler.postDelayed({
                bottomLayoutSheet!!.state = BottomSheetBehavior.STATE_EXPANDED
                edVoucherCode!!.setText(promoPopUpCode)
                val animShake = AnimationUtils.loadAnimation(mActivity, R.anim.shake)
                btnUseVoucher!!.startAnimation(animShake)
                //Do something after 100ms
            }, 1000)
        }
        btnLogin!!.setOnClickListener { (mActivity as MainActivityTab).intentToLogin() }
        if (session!!.isLogin) {
            getUserdata()
            getCheckDeposit()
            sendReferral()
            layoutInkiPayBalance!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val peekHeight = layoutInkiPayBalance!!.measuredHeight
            bottomLayoutSheet!!.peekHeight = peekHeight
            val lp = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT)
            lp.setMargins(0, 0, 0, peekHeight)
            layoutMain!!.layoutParams = lp
            layoutBottomSheet!!.setBackgroundColor(Utility.getColor(resources, R.color.orange_300, null))
            layoutInkiPayBalance!!.visibility = View.VISIBLE
            layoutNotLoginInfo!!.visibility = View.GONE
            btnUseVoucher!!.setOnClickListener {
                if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(edVoucherCode!!.text.toString())) {
                    sendVoucherCode()
                } else {
                    Toast.makeText(mActivity, resources.getString(R.string.please_enter_voucher_code), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            layoutNotLoginInfo!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val peekHeight = layoutNotLoginInfo!!.measuredHeight
            bottomLayoutSheet!!.peekHeight = peekHeight
            val lp = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT)
            lp.setMargins(0, 0, 0, peekHeight)
            layoutMain!!.layoutParams = lp
            layoutBottomSheet!!.setBackgroundColor(Utility.getColor(resources, R.color.red_400, null))
            layoutInkiPayBalance!!.visibility = View.GONE
            layoutNotLoginInfo!!.visibility = View.VISIBLE
            btnShareFb!!.setOnClickListener {
                (mActivity as MainActivityTab).intentToLogin()
                Toast.makeText(mActivity, resources.getString(R.string.sorry_must_login), Toast.LENGTH_SHORT).show()
            }
            btnInviteFriend!!.setOnClickListener {
                (mActivity as MainActivityTab).intentToLogin()
                Toast.makeText(mActivity, resources.getString(R.string.sorry_must_login), Toast.LENGTH_SHORT).show()
            }
            btnUseVoucher!!.setOnClickListener {
                (mActivity as MainActivityTab).intentToLogin()
                Toast.makeText(mActivity, resources.getString(R.string.sorry_must_login), Toast.LENGTH_SHORT).show()
            }
        }
        scrollContentSlidingPanel!!.setOnTouchListener { view, motionEvent ->
            val action = motionEvent.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    // Disallow ScrollView to intercept touch events.
                    layoutContentBottomSheet!!.requestDisallowInterceptTouchEvent(true)
                    // Disable touch on transparent view
                    false
                }

                MotionEvent.ACTION_UP -> {
                    // Allow ScrollView to intercept touch events.
                    layoutContentBottomSheet!!.requestDisallowInterceptTouchEvent(false)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    layoutContentBottomSheet!!.requestDisallowInterceptTouchEvent(true)
                    false
                }

                else -> true
            }
        }

        layoutInkiPayBalance!!.setOnClickListener {
            if (bottomLayoutSheet!!.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomLayoutSheet!!.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomLayoutSheet!!.state = BottomSheetBehavior.STATE_COLLAPSED
                scrollContentSlidingPanel!!.scrollTo(0, scrollContentSlidingPanel!!.top)
            }
        }

        layoutBottomSheet!!.setOnClickListener(null)
    }

    private fun setBottomLayoutSheet() {
        bottomLayoutSheet!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    ivArrow!!.setImageDrawable(resources.getDrawable(R.drawable.xml_ic_arrow_down_white))
                } else {
                    ivArrow!!.setImageDrawable(resources.getDrawable(R.drawable.xml_ic_arrow_up_white))
                    Utility.getInstance().hideKeyboard(mActivity, edVoucherCode)
                }
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(view: View, v: Float) {

            }
        })
    }

    private fun getMainMenuItem() {
        val api = Utility.getInstance().getTokenApi(mActivity)
        userController!!.getMainMenuItem(district_id, api, getMainMenuInterface)
    }

    fun parameters(district_id: String): Map<String, String> {
        val params = HashMap<String, String>()
        params.put("district_id", district_id)
        return params
    }

    private fun getUserdata() {
        val api = Utility.getInstance().getTokenApi(mActivity)
        userController!!.getUserData(api, getUserInterface)
    }

    fun getCheckDeposit() {
        val api = Utility.getInstance().getTokenApi(mActivity)
        userController!!.getCheckDeposit(api, checkDepositInterface)
    }

    fun sendReferral() {
        val api = Utility.getInstance().getTokenApi(mActivity)
        userController!!.sendReferral(api, sendReferralInterface)
        return
    }

    private fun sendVoucherCode() {
        val api = Utility.getInstance().getTokenApi(mActivity)
        pDialog = ProgressDialog.show(mActivity, "", "loading...")
        val imei = Utility.getInstance().getDeviceData(mActivity).imei
        val voucher_code = edVoucherCode!!.text.toString()
        userController!!.sendVouchercode(imei, voucher_code, api, sendVoucherCodeInterface)
        return
    }

    private fun setDefaultMenu() {
        val data = ArrayList<MainMenuItemData>()
        for (i in menu.indices) {
            val mainMenuItemData = MainMenuItemData(menu[i], "2")
            data.add(mainMenuItemData)
        }
        fm = activity.supportFragmentManager
        ft = fm!!.beginTransaction()
        val menuGridFragment = MenuGridFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("menu_data", data)
        menuGridFragment.arguments = bundle
        ft!!.add(R.id.fragment_container, menuGridFragment)
        ft!!.commitAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()
        if (session!!.isLogin) {
            getCheckDeposit()
        }
    }

    override fun onPause() {
        super.onPause()

    }

    override fun setOnSubmitListener(arg: String) {

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Activity) {
            mActivity = context
        }
    }
}
