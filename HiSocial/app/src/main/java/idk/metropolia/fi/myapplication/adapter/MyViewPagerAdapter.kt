package idk.metropolia.fi.myapplication.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import java.util.ArrayList

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 23:09 2018/11/10
 * @ Description：Build for Metropolia project
 */
class MyViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    public val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun getTitle(position: Int): String {
        return mFragmentTitleList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }

    // 解决viewpager滑动卡顿
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // super.destroyItem(container, position, `object`)
    }
}
