package idk.metropolia.fi.myapplication

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.ahao9.socialevent.utils.MyToast
import idk.metropolia.fi.myapplication.adapter.MyViewPagerAdapter
import idk.metropolia.fi.myapplication.view.fragment.HomeFragment
import idk.metropolia.fi.myapplication.utils.Tools
import idk.metropolia.fi.myapplication.view.fragment.NearByFragment

class MainActivity : AppCompatActivity() {

    private lateinit var view_pager: ViewPager
    private lateinit var viewPagerAdapter: MyViewPagerAdapter
    private lateinit var tab_layout: TabLayout
    private var mExitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initComponents()
        initListeners()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Music"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)
    }

    private fun initComponents() {
        view_pager = findViewById(R.id.view_pager)
        view_pager.offscreenPageLimit = 4  // 解决viewpager滑动卡顿

        tab_layout = findViewById(R.id.tab_layout)
        setupViewPager(view_pager)
        tab_layout.setupWithViewPager(view_pager)

        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_music)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_movie)
        tab_layout.getTabAt(2)?.setIcon(R.drawable.ic_book)
        tab_layout.getTabAt(3)?.setIcon(R.drawable.ic_games)

        // set icon color pre-selected
        tab_layout.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
        tab_layout.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN)
        tab_layout.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN)
        tab_layout.getTabAt(3)?.icon?.setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN)
    }

    private fun initListeners() {

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

    private fun setupViewPager(viewPager: ViewPager) {
        viewPagerAdapter = MyViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(HomeFragment(), "Music")    // index 0
        viewPagerAdapter.addFragment(NearByFragment(), "Movies")   // index 1
        viewPagerAdapter.addFragment(HomeFragment(), "Books")    // index 2
        viewPagerAdapter.addFragment(HomeFragment(), "Games")    // index 3
        viewPager.adapter = viewPagerAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.grey_60))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }
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
