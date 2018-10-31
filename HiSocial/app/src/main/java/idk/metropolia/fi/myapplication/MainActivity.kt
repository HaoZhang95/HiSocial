package idk.metropolia.fi.myapplication

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.ahao9.socialevent.utils.LogUtils
import com.example.ahao9.socialevent.utils.MyToast
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import idk.metropolia.fi.myapplication.fragment.HomeFragment
import idk.metropolia.fi.myapplication.fragment.MapFragment
import idk.metropolia.fi.myapplication.fragment.SettingsFragment
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_search_bar.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var search_bar: View
    private lateinit var bottom_sheet: View
    private lateinit var mBehavior: BottomSheetBehavior<View>
    private var mBottomSheetDialog: BottomSheetDialog? = null

    private lateinit var homeFragment: HomeFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var tempFragment: Fragment
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment = HomeFragment()
        mapFragment = MapFragment()
        settingsFragment = SettingsFragment()

        supportFragmentManager
                .beginTransaction()
                .add(R.id.central_content, homeFragment)
                .commit()
        tempFragment = homeFragment

        initComponent()
    }

    private fun switchFragment(fragment: Fragment) {
        if (fragment != tempFragment) {
            if (!fragment.isAdded) {
                supportFragmentManager
                        .beginTransaction()
                        .hide(tempFragment)
                        .add(R.id.central_content, fragment).commit();
            } else {
                supportFragmentManager
                        .beginTransaction()
                        .hide(tempFragment)
                        .show(fragment).commit();
            }
            tempFragment = fragment;
        }
    }

    private fun initComponent() {

        search_bar = findViewById(R.id.search_bar)
        bottom_sheet = findViewById(R.id.bottom_sheet)
        mBehavior = BottomSheetBehavior.from<View>(bottom_sheet)

        nested_scroll_view.setOnScrollChangeListener { v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY < oldScrollY) { // up
                animateSearchBar(false)
            }
            if (scrollY > oldScrollY) { // down
                animateSearchBar(true)
            }
        }

        bt_menu.setOnClickListener {
            showBottomSheetDialog()
        }

        et_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                val text = textView.text.trim()
                searchAction(text)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)

        addFilterBtn.setOnClickListener { showFilterDialog() }
    }

    private fun showFilterDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_event)
        dialog.setCancelable(true)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        val startDateBtn = dialog.findViewById<Button>(R.id.startDateBtn)
        val endDateBtn = dialog.findViewById<Button>(R.id.endDateBtn)

        startDateBtn.text = startDateStr
        endDateBtn.text = endDateStr

        startDateBtn.setOnClickListener { showDatePicker(startDateBtn, 1) }
        endDateBtn.setOnClickListener { showDatePicker(endDateBtn, 2) }

        (dialog.findViewById(R.id.bt_close) as ImageButton).setOnClickListener { dialog.dismiss() }
        (dialog.findViewById(R.id.bt_save) as Button).setOnClickListener {
            LogUtils.e("$startDateStr --> $endDateStr")
            dialog.dismiss()
        }

        dialog.show()
        dialog.window!!.attributes = lp
    }

    private var startDateStr: String = Tools.getFormattedDateSimple2(Date().time)
    private var endDateStr: String = ""

    private fun showDatePicker(button: Button, flag: Int) {
        val cur_calender = Calendar.getInstance()
        val datePicker = DatePickerDialog.newInstance(
                { view, year, monthOfYear, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date_ship_millis = calendar.timeInMillis

                    button.text = Tools.getFormattedDateSimple(date_ship_millis)
                    if (flag == 1) {
                        startDateStr = Tools.getFormattedDateSimple2(date_ship_millis)
                    } else {
                        endDateStr = Tools.getFormattedDateSimple2(date_ship_millis)
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        )
        //set dark light
        datePicker.isThemeDark = false
        datePicker.accentColor = resources.getColor(R.color.colorPrimary)
        datePicker.minDate = cur_calender
        datePicker.show(fragmentManager, "Datepickerdialog")
    }


    private fun searchAction(text: CharSequence) {
        MyToast.show(this, "Begin search: $text")
        et_search.text.clear()
        et_search.clearFocus()
    }

    // 设置搜索
    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    internal var isSearchBarHide = false
    private fun animateSearchBar(hide: Boolean) {
        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return
        isSearchBarHide = hide
        val moveY = if (hide) -(2 * search_bar.height) else 0
        search_bar.animate().translationY(moveY.toFloat()).setStartDelay(100).setDuration(300).start()
    }

    private fun showBottomSheetDialog() {
        if (mBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val view = layoutInflater.inflate(R.layout.sheet_floating, null)

        mBottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetDialog?.setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog?.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        // set background transparent
        (view.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        mBottomSheetDialog?.show()
        mBottomSheetDialog?.setOnDismissListener { mBottomSheetDialog = null }

        view.findViewById<LinearLayout>(R.id.lyt_home).setOnClickListener {
            currentIndex = 0
            switchFragment(homeFragment)
            mBottomSheetDialog?.hide()
        }
        view.findViewById<ImageView>(R.id.iv_home).setOnClickListener {
            currentIndex = 0
            switchFragment(homeFragment)
            mBottomSheetDialog?.hide()
        }
        view.findViewById<ImageButton>(R.id.bt_close).setOnClickListener {
            mBottomSheetDialog?.hide()
        }
        view.findViewById<View>(R.id.lyt_map).setOnClickListener {
            currentIndex = 1
            switchFragment(settingsFragment)
            mBottomSheetDialog?.hide()
        }
        view.findViewById<View>(R.id.lyt_share).setOnClickListener {
            currentIndex = 2
            switchFragment(mapFragment)
            mBottomSheetDialog?.hide()
        }
        view.findViewById<View>(R.id.lyt_settings).setOnClickListener {
            Toast.makeText(applicationContext, "Settings not implemented yet", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<View>(R.id.lyt_about_us).setOnClickListener {
            Toast.makeText(applicationContext, "About not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }
}
