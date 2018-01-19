package com.pmberjaya.indotiki.app.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.pmberjaya.indotiki.R
import com.pmberjaya.indotiki.app.account.profile.EditProfileActivity
import com.pmberjaya.indotiki.app.deposit.DepositTab
import com.pmberjaya.indotiki.app.others.TermOfService
import com.pmberjaya.indotiki.app.promo.MyPromoActivity
import com.pmberjaya.indotiki.dao.SessionKey
import com.pmberjaya.indotiki.dao.SessionManager
import com.pmberjaya.indotiki.services.fcm.FCMRealtimeDatabaseHandler
import com.pmberjaya.indotiki.utilities.PicassoLoader
import com.pmberjaya.indotiki.utilities.Utility

/**
 * Created by edwin on 07/11/2017.
 */

class OtherMenu : android.support.v4.app.Fragment() {

    private var layoutEditProfile: LinearLayout? = null
    private var layoutDeposit: LinearLayout? = null
    private var layoutMyPromo: LinearLayout? = null
    private var layoutInviteFriend: LinearLayout? = null
    private var layoutContactUs: LinearLayout? = null
    private var layoutRateApplication: LinearLayout? = null
    private var layoutTermOfService: LinearLayout? = null
    private var layoutLogIn: LinearLayout? = null
    private var layoutLogOut: LinearLayout? = null
    private var layoutMenuLoggedIn: LinearLayout? = null
    private var tvRate: TextView? = null
    private var tvUserName: TextView? = null
    private var tvEmail: TextView? = null
    private var tvPhoneNumber: TextView? = null
    private var ivPhotoProfile: ImageView? = null

    private var nama: String? = null
    private var nohp: String? = null
    private var email: String? = null
    private var avatar: String? = null
    private var session: SessionManager? = null
    private val sessionKey: SessionKey? = null

    private val login = View.OnClickListener { (activity as MainActivityTab).intentToLogin() }

    private val editprofile = View.OnClickListener {
        val i = Intent(activity, EditProfileActivity::class.java)
        startActivity(i)
    }

    private val intenttodeposit = View.OnClickListener {
        val i = Intent(activity, DepositTab::class.java)
        startActivity(i)
    }
    private val intenttoMyPromo = View.OnClickListener {
        val i = Intent(activity, MyPromoActivity::class.java)
        startActivity(i)
    }

    private val intenttoInvite = View.OnClickListener { v ->
        //            Intent i = new Intent(getActivity(), InviteFriends.class);
        //            startActivity(i);
        Snackbar.make(v, "Invite teman teman mu pakai indotiki dan dapatkan deposit gratis dari hendry yanto", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    private val intenttoTermService = View.OnClickListener {
        val i =  Intent(activity, TermOfService::class.java)
        startActivity(i)
//        val builder1 = AlertDialog.Builder(context)
//        builder1.setMessage("KALIAN TIDAK BOLEH MENIPU KAYAK SI RISYAD")
//        builder1.setCancelable(true)
//        val alert11 = builder1.create()
//        alert11.show()
    }

    private val intenttocontact = View.OnClickListener {
        //            Intent i = new Intent(getActivity(), ContactUsActivity.class);
        //            startActivity(i);
        val builder1 = AlertDialog.Builder(context)
        builder1.setMessage("Exel : 082391118015, Hendry Yanto: 082156956656, Danny Tandean: 082377123345")
        builder1.setCancelable(true)
        val alert11 = builder1.create()
        alert11.show()
    }

    private val intenttorate = View.OnClickListener {
        val uri = Uri.parse("market://details?id=" + activity.packageName)
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
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.packageName)))
        }
    }


    private val logout = View.OnClickListener {
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(null)
        builder.setMessage(resources.getString(R.string.alert_logout))
        builder.setIcon(R.mipmap.ic_launcher)
        // Set the action buttons
        builder.setPositiveButton(R.string.yes) { dialog, id ->
            //stopService(myIntent);
            val session = SessionManager(activity)
            session.logoutUser()
            val intent = Intent(activity, MainActivityTab::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            Toast.makeText(activity, resources.getString(R.string.success_logout), Toast.LENGTH_SHORT)
            startActivity(intent)
            activity.stopService(Intent(activity, FCMRealtimeDatabaseHandler::class.java))
            activity.finish()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, id -> dialog.dismiss() }
        dialog = builder.create()
        dialog.show()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.other_menu, container, false)
        initSession() //inisialisasi Session
        renderView(rootView) //inisialisasi view
        initHandler() //inisialisasi event click
        checkSession() //Check data yang tersimpan yang sudah tersimpan saat login
        return rootView
    }

    private fun initSession() {
        session = SessionManager(activity)
    }

    private fun renderView(rootView: View) {
        layoutEditProfile = rootView.findViewById(R.id.layoutEditProfile)
        layoutDeposit = rootView.findViewById(R.id.layoutDeposit)
        layoutInviteFriend = rootView.findViewById(R.id.layoutInviteFriend)
        layoutContactUs = rootView.findViewById(R.id.layoutContactUs)
        layoutRateApplication = rootView.findViewById(R.id.layoutRateApplication)
        layoutTermOfService = rootView.findViewById(R.id.layoutTermOfService)
        layoutLogIn = rootView.findViewById(R.id.layoutLogIn)
        layoutLogOut = rootView.findViewById(R.id.layoutLogOut)
        layoutMyPromo = rootView.findViewById(R.id.layoutMyPromo)
        layoutMenuLoggedIn = rootView.findViewById(R.id.layoutMenuLoggedIn)
        ivPhotoProfile = rootView.findViewById(R.id.ivPhotoProfile)
        tvRate = rootView.findViewById(R.id.tvRate)
        tvUserName = rootView.findViewById(R.id.tvUserName)
        tvEmail = rootView.findViewById(R.id.tvEmail)
        tvPhoneNumber = rootView.findViewById(R.id.tvPhoneNumber)

        tvRate!!.text = resources.getString(R.string.title_rate_app) + " (v." + Utility.getAppVersionName(activity) + ")"
    }

    private fun initHandler() {
        layoutLogIn!!.setOnClickListener(login)
        layoutEditProfile!!.setOnClickListener(editprofile)
        layoutDeposit!!.setOnClickListener(intenttodeposit)
        layoutInviteFriend!!.setOnClickListener(intenttoInvite)
        layoutContactUs!!.setOnClickListener(intenttocontact)
        layoutRateApplication!!.setOnClickListener(intenttorate)
        layoutTermOfService!!.setOnClickListener(intenttoTermService)
        layoutMyPromo!!.setOnClickListener(intenttoMyPromo)
        layoutLogOut!!.setOnClickListener(logout)
    }

    private fun checkSession() {
        if (session!!.isLogin) {
            layoutEditProfile!!.visibility = View.VISIBLE
            layoutLogIn!!.visibility = View.GONE
            val mapData = session!!.userDetails
            avatar = mapData[SessionManager.KEY_AVATAR]
            nama = mapData[SessionManager.KEY_NAMA]
            email = mapData[SessionManager.KEY_EMAIL]
            nohp = mapData[SessionManager.KEY_PHONE]

            if (avatar != null && avatar != "") {
                PicassoLoader.loadProfile(activity, avatar, ivPhotoProfile, R.mipmap.img_no_avatar)
            } else {
                PicassoLoader.loadProfileFail(activity, ivPhotoProfile, R.mipmap.img_no_avatar)
            }

            tvUserName!!.text = nama
            tvEmail!!.text = email
            tvPhoneNumber!!.text = nohp
            layoutMenuLoggedIn!!.visibility = View.VISIBLE
            layoutLogOut!!.visibility = View.VISIBLE

        } else {
            layoutEditProfile!!.visibility = View.GONE
            layoutLogIn!!.visibility = View.VISIBLE
            layoutMenuLoggedIn!!.visibility = View.GONE
            layoutLogOut!!.visibility = View.GONE
        }
    }


    override fun onResume() {
        super.onResume()
        checkSession()
    }

    override fun onPause() {
        super.onPause()
    }
}
