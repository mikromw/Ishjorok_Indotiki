package com.pmberjaya.indotiki.app.main

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.pmberjaya.indotiki.R
import com.pmberjaya.indotiki.app.event.EventListNewFragment
import com.pmberjaya.indotiki.app.event.EventListOtherFragment
import com.pmberjaya.indotiki.app.event.EventListPromoFragment
import com.pmberjaya.indotiki.dao.SessionManager
import java.util.*

/**
 * Created by edwin on 07/11/2017.
 */

class EventTab : Fragment(), ViewPager.OnPageChangeListener {
    private var rootView: View? = null
    private var toolbar: Toolbar? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private val item: MenuItem? = null
    private var loading: LinearLayout? = null
    private var session: SessionManager? = null
    private var appBarLayout: AppBarLayout? = null
    private var myContext: FragmentActivity? = null
    private var adapter: ViewPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.event_tab, container, false)
        initSession()
        renderView(rootView)
        setupViewPager()
        return rootView
    }

    private fun initSession() {
        session = SessionManager(activity)
    }

    private fun renderView(rootView: View?) {
        toolbar = rootView!!.findViewById<View>(R.id.tool_bar) as Toolbar
        loading = rootView.findViewById<View>(R.id.layout_loading) as LinearLayout
        appBarLayout = rootView.findViewById<View>(R.id.app_bar) as AppBarLayout
        appBarLayout!!.visibility = View.VISIBLE
        loading!!.visibility = View.GONE
        initToolbar()
    }

    fun setupViewPager() {
        viewPager = rootView!!.findViewById<View>(R.id.viewpager) as ViewPager
        adapter = ViewPagerAdapter(childFragmentManager)
        adapter!!.addFragment(EventListPromoFragment(), resources.getString(R.string.promo))
        adapter!!.addFragment(EventListNewFragment(), resources.getString(R.string.event_new))
        adapter!!.addFragment(EventListOtherFragment(), resources.getString(R.string.event_other))
        viewPager!!.adapter = adapter
        viewPager!!.offscreenPageLimit = adapter!!.count

        tabLayout = rootView!!.findViewById<View>(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(viewPager)
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

    private fun initToolbar() {
        if (toolbar != null) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }
        val actionBar = (activity as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            (activity as AppCompatActivity).supportActionBar!!.title = resources.getString(R.string.news)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (position == 0) {
            item!!.isVisible = false
        }
    }

    override fun onPageSelected(position: Int) {
        if (position == 0) {
            item!!.isVisible = false
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onAttach(activity: Activity?) {
        myContext = activity as FragmentActivity?
        super.onAttach(activity)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }
}