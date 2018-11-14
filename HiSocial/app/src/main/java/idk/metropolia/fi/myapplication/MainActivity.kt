package idk.metropolia.fi.myapplication

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.ahao9.socialevent.utils.MyToast
import idk.metropolia.fi.myapplication.adapter.MyViewPagerAdapter
import idk.metropolia.fi.myapplication.utils.Tools
import idk.metropolia.fi.myapplication.utils.ViewAnimationUtils
import idk.metropolia.fi.myapplication.view.fragment.*

class MainActivity : AppCompatActivity(), HomeFragment.MyOnScrollChangeListener {
    private lateinit var view_pager: ViewPager
    private lateinit var viewPagerAdapter: MyViewPagerAdapter
    private lateinit var tab_layout: TabLayout
    private var mExitTime: Long = 0

    private lateinit var homeFragment: HomeFragment
    private lateinit var nearByFragment: NearByFragment
    private lateinit var newEventFragment: NewEventFragment
    private lateinit var profileFragment: ProfileFragment

    private lateinit var fab: FloatingActionButton
    private var hide = false
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initComponents()
        initListeners()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_logo_sized)
        // toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        // supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)
    }

    private fun initComponents() {
        homeFragment = HomeFragment()
        nearByFragment = NearByFragment()
        newEventFragment = NewEventFragment()
        profileFragment = ProfileFragment()
        mapFragment = MapFragment()

        view_pager = findViewById(R.id.view_pager)
        view_pager.offscreenPageLimit = 4  // 解决viewpager滑动卡顿

        tab_layout = findViewById(R.id.tab_layout)
        setupViewPager(view_pager)
        tab_layout.setupWithViewPager(view_pager)

        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_filter_list)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_near_me)
        tab_layout.getTabAt(2)?.setIcon(R.drawable.ic_add)
        tab_layout.getTabAt(3)?.setIcon(R.drawable.ic_person)

        // set icon color pre-selected
        tab_layout.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
        tab_layout.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN)
        tab_layout.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN)
        tab_layout.getTabAt(3)?.icon?.setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN)


        fab = findViewById(R.id.fabInMain)
        fab.setOnClickListener {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

            if (!mapFragment.isHidden) {
                MyToast.show(this, "step 1")
//                if (supportFragmentManager.findFragmentByTag(fragmentTagsArray[0]) == null) {
//                    fragmentTransaction.add(R.id.container, fragmentArray[0], fragmentTagsArray[0])
//                }
//                for (i in 0 until fragmentArray.size) {
//                    if (i == 0) {
//                        fragmentTransaction.show(fragmentArray[i])
//                    } else {
//                        if (supportFragmentManager.findFragmentByTag(fragmentTagsArray[i]) != null) {
//                            fragmentTransaction.hide(fragmentArray[i])
//                        }
//                    }
//                }
//                fragmentTransaction.commit()

//                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_map))

            } else {
                MyToast.show(this, "step 2")
//                if (supportFragmentManager.findFragmentByTag(fragmentTagsArray[3]) == null) {
//                    fragmentTransaction.add(R.id.container, fragmentArray[3], fragmentTagsArray[3])
//                }
//                for (i in 0 until fragmentArray.size) {
//                    if (i == 3) {
//                        fragmentTransaction.show(fragmentArray[i])
//                    } else {
//                        if (supportFragmentManager.findFragmentByTag(fragmentTagsArray[i]) != null) {
//                            fragmentTransaction.hide(fragmentArray[i])
//                        }
//                    }
//                }
//                fragmentTransaction.commit()

//                fabInHome.setImageDrawable(resources.getDrawable(R.drawable.ic_dialog_dialer));
            }
        }

    }

    private fun initListeners() {

        homeFragment.setOnScrollChangeListener(this)

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                supportActionBar?.title = viewPagerAdapter.getTitle(tab.position)
                tab.icon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onScrollChangeListener(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        if (scrollY >= oldScrollY) { // down
            if (hide) return
            ViewAnimationUtils.hideFab(fab)
            hide = true
        } else {    // up
            if (!hide) return
            ViewAnimationUtils.showFab(fab)
            hide = false
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        viewPagerAdapter = MyViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(homeFragment, "Home")    // index 0
        viewPagerAdapter.addFragment(nearByFragment, "Near By")   // index 1
        viewPagerAdapter.addFragment(newEventFragment, "Add Event")    // index 2
        viewPagerAdapter.addFragment(profileFragment, "Profile")    // index 3
        viewPager.adapter = viewPagerAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.grey_60))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

    /**
     * 按返回键不退出应用。
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                MyToast.show(this@MainActivity, "Press again to exit")
                mExitTime = System.currentTimeMillis()
            } else {
                MyToast.cancel()
                moveTaskToBack(true)
                // finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
