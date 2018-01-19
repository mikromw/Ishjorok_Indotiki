package com.pmberjaya.indotiki.app.main

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.pmberjaya.indotiki.R
import com.pmberjaya.indotiki.app.bookingData.bookingComplete.BookingCompleteFragment
import com.pmberjaya.indotiki.app.bookingData.bookingProgress.BookingInProgressFragment
import com.pmberjaya.indotiki.dao.SessionManager
import java.util.*

/**
 * Created by edwin on 06/11/2017.
 */

class BookingTabBottomNavigation : Fragment(), ViewPager.OnPageChangeListener {
    private var appBarLayout: AppBarLayout? = null
    private var toolbar: Toolbar? = null
    private var rootView: View? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var loading: LinearLayout? = null
    internal lateinit var session: SessionManager


    //1. Inisialisasi View
    //2. SetTablayout dengan ViewPager
    //3. Tambah Fragment ke Viewpagernya

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.booking_data_tab, container, false)
        initSession() //inisialisasi session
        renderView(rootView)  //inisialisasi view
        checkIfLogin() //Check login atau enggak
        return rootView
    }

    private fun initSession() {
        session = SessionManager(activity)
    }

    private fun renderView(rootView: View?) {
        loading = rootView!!.findViewById<View>(R.id.layout_loading) as LinearLayout
        viewPager = rootView.findViewById<View>(R.id.viewpager) as ViewPager
        tabLayout = rootView.findViewById<View>(R.id.tabs) as TabLayout
        appBarLayout = rootView.findViewById<View>(R.id.app_bar) as AppBarLayout
        toolbar = rootView.findViewById<View>(R.id.tool_bar) as Toolbar
    }

    private fun checkIfLogin() {
        if (!session.isLogin) {
            loading!!.visibility = View.VISIBLE
            appBarLayout!!.visibility = View.GONE
            Toast.makeText(activity, resources.getString(R.string.please_login), Toast.LENGTH_SHORT).show()
        } else {
            loading!!.visibility = View.GONE
            appBarLayout!!.visibility = View.VISIBLE
            initToolbar()
            setupViewPager(viewPager)
            tabLayout!!.setupWithViewPager(viewPager)
        }
    }

    private fun initToolbar() {
        if (toolbar != null) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }
        val actionBar = (activity as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            (activity as AppCompatActivity).supportActionBar!!.title = resources.getString(R.string.booking_data)
        }
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(BookingInProgressFragment(), resources.getString(R.string.booking_progress))
        adapter.addFragment(BookingCompleteFragment(), resources.getString(R.string.booking_complete))
        viewPager!!.adapter = adapter
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {

    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): android.support.v4.app.Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: android.support.v4.app.Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}