package idk.metropolia.fi.myapplication.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import idk.metropolia.fi.myapplication.fragment.SettingsFragment

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 20:18 2018/10/31
 * @ Description：Build for Metropolia project
 */
class MyTabAdapter(fm: FragmentManager, private val mFragments: List<SettingsFragment>, private val mTitles: Array<String>)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }
}
