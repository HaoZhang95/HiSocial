package idk.metropolia.fi.myapplication.view.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import com.example.ahao9.socialevent.utils.LogUtils
import com.example.ahao9.socialevent.utils.MyToast
import com.google.android.flexbox.FlexboxLayout
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.httpsService.Networking
import idk.metropolia.fi.myapplication.model.SearchPlacesResultObject
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var locationBox: FlexboxLayout
    private var selectedLocationButtons: MutableList<Button> = mutableListOf()
    private var selectedLocation: String? = null
    private var startDate: String? = null
    private var endDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar()
        initData()
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        locationBox = findViewById(R.id.flex_box)
    }

    private fun initData() {
        getLocations()
    }

    private fun initListeners() {

        addFilterBtn.setOnClickListener { showFilterDialog() }

        et_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                val text = textView.text.trim().toString()
                searchAction(text)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        btn_clear.setOnClickListener {
            resetLocation()
        }
    }

    private fun searchAction(text: String) {
        et_search.text.clear()
        et_search.clearFocus()

        val bundle = Bundle()
        bundle.putString("text",text)
        bundle.putString("start",startDate)
        bundle.putString("end",endDate)
        bundle.putString("location",selectedLocation)

        startActivity<ListOfEventActivity>("obj" to bundle)
    }

    // 设置搜索
    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
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
            startDate = startDateStr
            endDate = endDateStr
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

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Tools.setSystemBarColor(this, R.color.colorPrimary)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    // ===========================
    companion object {
        val LOCATION_NAME_LIST = mutableMapOf<String, String>()
    }

    private fun getLocations() {

        val call = Networking.service.searchPlacesResult()

        val value = object : Callback<SearchPlacesResultObject> {
            // this method gets called after a http call, no matter the http code
            override fun onResponse(call: Call<SearchPlacesResultObject>, response: Response<SearchPlacesResultObject>) {

                if (response.isSuccessful) {
                    val dataList = response.body().data

                    for (temp in dataList) {
                        temp.name?.let {
                            if (it.fi != null) {
                                LOCATION_NAME_LIST.put(temp.id, it.fi)

                                generateLocationChip(temp.id, it.fi)
                            }
                        }
                    }

                    LogUtils.e("size: --> ${dataList.size}")
                } else {
                    MyToast.show(this@SearchActivity, "Can not get any areas")
                }
            }

            // this method gets called if the http call fails (no internet etc)
            override fun onFailure(call: Call<SearchPlacesResultObject>, t: Throwable) {
                LogUtils.e("onFailure: " + t.toString())
            }
        }
        call.enqueue(value)
    }


    private fun generateLocationChip(id: String, name: String) {

        val locationChip = LayoutInflater.from(this)
                .inflate(R.layout.location_small_item, locationBox, false)

        val chip = locationChip.findViewById<Button>(R.id.btn_chip)
        chip.text = name
        locationBox.addView(locationChip)

        chip.setOnClickListener {
            selectLocation(id, it as Button)
        }
    }

    private fun selectLocation(id: String, button: Button) {
        if (! selectedLocationButtons.contains(button)) {
            selectedLocationButtons.add(button)
        }
        selectedLocation = null
        for ( button in selectedLocationButtons) {
            if (button.isSelected) {
                button.setTextColor(resources.getColor(R.color.orange_500))
                button.isSelected = !button.isSelected
            }
        }

        button.isSelected = true
        button.setTextColor(Color.WHITE)
        selectedLocation = id
    }

    private fun resetLocation() {
        for ( button in selectedLocationButtons) {
            if (button.isSelected) {
                button.setTextColor(resources.getColor(R.color.orange_500))
                button.isSelected = !button.isSelected
            }
        }
        selectedLocation = null

        MyToast.show(this, "Reset successfully")
    }


}
