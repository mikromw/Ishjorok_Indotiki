package com.pmberjaya.indotiki.app.main

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.pmberjaya.indotiki.R
import com.pmberjaya.indotiki.app.account.login.LoginActivity
import com.pmberjaya.indotiki.app.bookingData.bookingComplete.BookingCompleteDetail
import com.pmberjaya.indotiki.app.bookingNew.SearchDriverNew
import com.pmberjaya.indotiki.base.BaseActivity
import com.pmberjaya.indotiki.base.BaseGenericCallback
import com.pmberjaya.indotiki.base.BaseGenericInterface
import com.pmberjaya.indotiki.callbacks.APIErrorCallback
import com.pmberjaya.indotiki.config.Constants
import com.pmberjaya.indotiki.controllers.BookingController
import com.pmberjaya.indotiki.dao.DBController
import com.pmberjaya.indotiki.dao.SessionManager
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberData.BookingCompleteData
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberData.BookingCompleteModel
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData.BookingInProgressModel
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable
import com.pmberjaya.indotiki.services.TimeService
import com.pmberjaya.indotiki.services.fcm.FCMAuthLoginHandler
import com.pmberjaya.indotiki.utilities.PicassoLoader
import com.pmberjaya.indotiki.utilities.Utility
import java.util.*

/**
 * Created by Exel staderlin on 7/17/2017.
 */

class MainActivityTab : BaseActivity() {
    private var session: SessionManager? = null
    private var bookingDataParcelable: BookingDataParcelable? = null
    private var bottomNavigation: AHBottomNavigation? = null
    private val runSearchOtherDriver = 0

    private var dbController: DBController? = null
    private val bottomNavigationItems = ArrayList<AHBottomNavigationItem>()
    private var viewPager: ViewPager? = null
    private var bookingController: BookingController? = null
    private var currentProgress = 0
    private var onBackPressedListener: OnBackPressedListener? = null
    private var promoPopUpCode: String? = null

    internal lateinit var intent: Intent

    internal var getBookingCompleteMemberInterface: BaseGenericInterface = object : BaseGenericInterface {
        override fun <T> onSuccess(baseGenericCallback: BaseGenericCallback<T>) {
            if (baseContext != null) {
                val sukses = baseGenericCallback.sukses
                val data = baseGenericCallback.data as BookingCompleteModel
                if (sukses == 2) {
                    val bookingCompleteMemberData = data.resultArray
                    val banyakdata = bookingCompleteMemberData.size
                    dbController!!.deleteBookingHistory()
                    if (banyakdata != 0) {
                        forceRateBookingData = ArrayList()
                        for (i in 0 until banyakdata) {
                            dbController!!.insertHistoryBooking(bookingCompleteMemberData[i])
                            if (bookingCompleteMemberData[i].rate == null && bookingCompleteMemberData[i].status == "complete" && Utility.getInstance().isValidUntilNow(bookingCompleteMemberData[i].timeData)) {
                                forceRateBookingData.add(bookingCompleteMemberData[i])
                            }
                        }
                        if (forceRateBookingData.size != 0) {
                            //                            goRateDriver();
                        }
                    } else {
                    }
                } else {
                }
            }
        }

        override fun onError(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                if (apiErrorCallback.error == "Invalid API key ") {
                    if (apiErrorCallback.error == "Invalid API key ") {
                        Utility.getInstance().showInvalidApiKeyAlert(this@MainActivityTab, resources.getString(R.string.relogin))
                    } else {
                        getBookingCompleteMember()
                    }
                }
            }
        }
    }

    internal var getBookingInProgressMemberInterface: BaseGenericInterface = object : BaseGenericInterface {
        override fun <T> onSuccess(baseGenericCallback: BaseGenericCallback<T>) {
            val data = baseGenericCallback.data as BookingInProgressModel
            val bookingListDriverData = data.resultArray
            currentProgress = bookingListDriverData.size

            dbController!!.deleteBookingProgress(null)

            if (currentProgress != 0) {
                for (i in 0 until currentProgress) {
                    dbController!!.insertProgressBooking(bookingListDriverData[i])
                }
                bottomNavigation!!.setNotification(currentProgress.toString(), 1)
            }
        }

        override fun onError(apiErrorCallback: APIErrorCallback) {
            if (apiErrorCallback.error != null) {
                if (apiErrorCallback.error == "Invalid API key ") {
                    Utility.getInstance().showInvalidApiKeyAlert(this@MainActivityTab, resources.getString(R.string.relogin))
                } else {
                    getBookingInProgressMember()
                }
            }
        }
    }

    internal lateinit var itemfilterdata: Array<CharSequence>
    private var filterArray: Array<String>? = null
    internal lateinit var forceRateBookingData: ArrayList<BookingCompleteData>
    internal var positionRateDriver = 0


    private val negativeSearchDriverAgain = DialogInterface.OnClickListener { dialogInterface, i ->
        val intentToBooking = Intent(this@MainActivityTab, BookingCompleteDetail::class.java)
        intentToBooking.putExtra("requestType", bookingDataParcelable!!.requestType)
        intentToBooking.putExtra("requestId", bookingDataParcelable!!.id)
        intentToBooking.putExtra("activity", "broadcast")
        val dbController = DBController.getInstance(this@MainActivityTab)
        dbController.deleteChatHistory(bookingDataParcelable!!.id, bookingDataParcelable!!.requestType)
        val idArray = ArrayList<String>()
        idArray.add(bookingDataParcelable!!.id)
        dbController.deleteBookingProgress(idArray)
        PicassoLoader.deleteImageFromDir(this@MainActivityTab, bookingDataParcelable!!.requestType, bookingDataParcelable!!.id)
        Toast.makeText(this@MainActivityTab, resources.getString(R.string.booking_completed), Toast.LENGTH_SHORT).show()
        intentToBooking.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentToBooking)
        finish()
        dialogInterface.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_tab)
        initSession()
        dbController = DBController(this@MainActivityTab)
        bookingController = BookingController.getInstance(this)
        renderView()
        getIntentExtra()
        initBottomNavigation()
    }

    private fun initSession() {
        session = SessionManager(this)
        if (session!!.isLogin) {
            //            getBookingInProgressMember();
            //            getBookingCompleteMember(); // ini buat ngasih bintang ke rider.
        }
    }


    private fun renderView() {
        viewPager = findViewById(R.id.viewPager)
        bottomNavigation = findViewById(R.id.navigation)
    }

    private fun initBottomNavigation() {
        val item1 = AHBottomNavigationItem(R.string.title_home, R.drawable.xml_ic_home_black, R.color.colorPrimary)
        val item2 = AHBottomNavigationItem(R.string.booking, R.mipmap.ic_clock_circular, R.color.colorPrimary)
        val item3 = AHBottomNavigationItem(R.string.news, R.mipmap.ic_event, R.color.colorPrimary)
        //        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.help , R.mipmap.ic_base_help, R.color.colorPrimary);
        val item4 = AHBottomNavigationItem(R.string.other, R.mipmap.ic_other_setting, R.color.colorPrimary)
        bottomNavigationItems.add(item1)
        bottomNavigationItems.add(item2)
        bottomNavigationItems.add(item3)
        bottomNavigationItems.add(item4)
        //        bottomNavigationItems.add(item5);
        bottomNavigation!!.addItems(bottomNavigationItems)
        bottomNavigation!!.isBehaviorTranslationEnabled = false
        bottomNavigation!!.accentColor = Utility.getColor(resources, R.color.colorPrimaryDark, null)
        bottomNavigation!!.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW


        setUpViewPager(viewPager) //setViewpagernya
        viewPager!!.offscreenPageLimit = 4 //setViewPAger Agar tidak createView lagi

        if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(intent.action) && intent.action == Constants.SEARCH_DRIVER) {
            viewPager!!.setCurrentItem(1, false)
            setSelectedBottomNavigation(1)
        }
        bottomNavigation!!.setOnTabSelectedListener { position, wasSelected ->
            when (position) {
                0 -> {
                    viewPager!!.setCurrentItem(0, false)
                    setSelectedBottomNavigation(position)
                }
                1 -> if (session!!.isLogin) {
                    viewPager!!.setCurrentItem(1, false)
                    setSelectedBottomNavigation(position)
                } else {
                    val i = Intent(this@MainActivityTab, LoginActivity::class.java)
                    startActivityForResult(i, Constants.STATE_LOGIN_CODE)
                }
                2 -> {
                    viewPager!!.setCurrentItem(2, false)
                    setSelectedBottomNavigation(position)
                }
            //                    case 3:
            //                        viewPager.setCurrentItem(3, false);
            //                        setSelectedBottomNavigation(position);
            //                        break;
                else -> {
                    viewPager!!.setCurrentItem(3, false)
                    setSelectedBottomNavigation(position)
                }
            }
            false
        }


        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun setUpViewPager(viewPager: ViewPager?) {
        val adapter = MainMenuPagerAdapter(supportFragmentManager)

        val mainMenu = MainMenu()
        val bookingTabBottomNavigation = BookingTabBottomNavigation()
        val eventTab = EventTab()
        //        HelpFragment helpFragment = new HelpFragment();
        val otherMenu = OtherMenu()

        if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(promoPopUpCode)) {
            val bundle = Bundle()
            bundle.putString(Constants.PROMO_POPUP_CODE, promoPopUpCode)
            mainMenu.arguments = bundle
        }
        adapter.addFragment(mainMenu)
        adapter.addFragment(bookingTabBottomNavigation)
        adapter.addFragment(eventTab)
        //        adapter.addFragment(helpFragment);
        adapter.addFragment(otherMenu)
        viewPager!!.adapter = adapter
        viewPager.offscreenPageLimit = adapter.count
    }

    fun getIntentExtra() {
        intent = getIntent()
        if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(intent.action) && session!!.isLogin) {
            if (intent.action == "searchOtherDriver") {
                bookingDataParcelable = BookingDataParcelable()
                bookingDataParcelable!!.id = intent.getStringExtra("request_id")
                bookingDataParcelable!!.requestType = intent.getStringExtra("request_type")
                bookingDataParcelable!!.transportation = intent.getStringExtra("transportation")
                //                showDialogSearchOtherDriver();
            }
        }
        promoPopUpCode = intent.getStringExtra(Constants.PROMO_POPUP_CODE)

    }

    override fun sendNotificationToDriver() {
        session!!.setTimerRepeatData(bookingDataParcelable!!.id, bookingDataParcelable!!.requestType, System.currentTimeMillis())
        val service = Intent(this, TimeService::class.java)
        service.putExtra("request_id", bookingDataParcelable!!.id)
        service.putExtra("request_type", bookingDataParcelable!!.requestType)
        startService(service)
    }

    override fun intentToSearchDriverActivity() {
        val intent = Intent(this, SearchDriverNew::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("request_id", bookingDataParcelable!!.id)
        intent.putExtra("request_type", bookingDataParcelable!!.requestType)
        intent.putExtra("transportation", bookingDataParcelable!!.transportation)
        startActivity(intent)
    }

    protected fun getBookingInProgressMember() {
        val api = Utility.getInstance().getTokenApi(this@MainActivityTab)
        itemfilterdata = resources.getStringArray(R.array.filter_item_data) as Array<CharSequence>
        filterArray = arrayOfNulls<String>(6) as Array<String>
        for (i in itemfilterdata.indices) {
            filterArray!![i] = itemfilterdata[i].toString()
        }
        BookingController.getInstance(this@MainActivityTab).getBookingInProgressMember(filterArray, api, getBookingInProgressMemberInterface)
        return
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.STATE_LOGIN_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this@MainActivityTab, MainActivityTab::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
                viewPager!!.setCurrentItem(1, false)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                viewPager!!.setCurrentItem(0, false)
            }
        }
    }

    protected fun getBookingCompleteMember() {
        val api = Utility.getInstance().getTokenApi(this@MainActivityTab)
        itemfilterdata = resources.getStringArray(R.array.filter_item_data) as Array<CharSequence>
        filterArray = arrayOfNulls<String>(6) as Array<String>
        for (i in itemfilterdata.indices) {
            filterArray!![i] = itemfilterdata[i].toString()
        }
        BookingController.getInstance(this@MainActivityTab).getBookingCompleteMember(filterArray, api, getBookingCompleteMemberInterface)
        return
    }

    fun goRateDriver() {
        val intentToBooking = Intent(this@MainActivityTab, BookingCompleteDetail::class.java)
        intentToBooking.putExtra("requestType", forceRateBookingData[positionRateDriver].source_table)
        intentToBooking.putExtra("requestId", forceRateBookingData[positionRateDriver].id)
        intentToBooking.putExtra("activity", "rate")
        intentToBooking.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivityForResult(intentToBooking, RATE_DRIVER)
    }

    fun setSelectedBottomNavigation(position: Int) {
        bottomNavigation!!.setCurrentItem(position, false)
    }

    fun intentToLogin() {
        val i = Intent(this@MainActivityTab, LoginActivity::class.java)
        startActivityForResult(i, Constants.STATE_LOGIN_CODE)
    }

    override fun onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener!!.doBack()
        } else {
            val dialog: AlertDialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle(null)
            builder.setMessage(R.string.exit_confirmation)
            builder.setIcon(R.mipmap.ic_launcher)

            // Set the action buttons
            builder.setPositiveButton(R.string.ok) { dialog, id ->
                stopService(Intent(this@MainActivityTab, FCMAuthLoginHandler::class.java))
                this@MainActivityTab.finish()
            }
            builder.setNegativeButton(R.string.cancel) { dialog, id -> dialog.dismiss() }
            dialog = builder.create()
            dialog.show()
            dialog.setCanceledOnTouchOutside(false)
        }
    }

    interface OnBackPressedListener {
        fun doBack()
    }

    fun setOnBackPressedListener(onBackPressedListener: OnBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener
    }

    override fun onDestroy() {
        onBackPressedListener = null
        super.onDestroy()
    }

    companion object {

        var RATE_DRIVER = 1
    }


}
