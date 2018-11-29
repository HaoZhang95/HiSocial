package idk.metropolia.fi.myapplication.view.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.utils.Tools
import idk.metropolia.fi.myapplication.utils.ViewAnimationUtils
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivity
import java.util.*

class SearchActivity : AppCompatActivity() {

    private var startDateStr: String? = null
    private var endDateStr: String? = null

    private lateinit var lyt_expand_price: View
    private lateinit var btnStartFrom: Button
    private lateinit var btnEndBefore: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar()
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        btnStartFrom = findViewById(R.id.btnStartFrom)
        btnEndBefore = findViewById(R.id.btnEndBefore)
        lyt_expand_price = findViewById(R.id.lyt_price)
        lyt_expand_price.visibility = View.GONE

        free_checkbox.isChecked = true
        val categorys = resources.getStringArray(R.array.category)
        val categorysArray = ArrayAdapter(this, R.layout.simple_spinner_item, categorys)
        categorysArray.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_category.adapter = categorysArray
        spn_category.setSelection(0)

        val postcodes = resources.getStringArray(R.array.postcode)
        val postcodesArray = ArrayAdapter(this, R.layout.simple_spinner_item, postcodes)
        postcodesArray.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_postcode.adapter = postcodesArray
        spn_postcode.setSelection(0)
    }

    private fun initListeners() {

        clearBtn.setOnClickListener { showFilterDialog() }

        et_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                val text = textView.text.trim().toString()
                searchAction(text)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        free_checkbox.setOnCheckedChangeListener { button, checked ->
            if (!checked) {
                ViewAnimationUtils.expand(lyt_expand_price) { /*Tools.nestedScrollTo(nested_scroll_view, lyt_expand_price) */ }
            } else {
                ViewAnimationUtils.collapse(lyt_expand_price)
            }
        }

        btnStartFrom.setOnClickListener {
            showDatePicker(btnStartFrom, 1)
        }

        btnEndBefore.setOnClickListener {
            showDatePicker(btnEndBefore, 2)
        }

        clearBtn.setOnClickListener { showResetDialog() }

        range_seek_bar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            price_min.text = "$minValue €"
            price_max.text = "$maxValue €"
        }

        ageGroup01.setOnClickListener { ageSelect(it as Button) }
        ageGroup02.setOnClickListener { ageSelect(it as Button) }
        ageGroup03.setOnClickListener { ageSelect(it as Button) }
        ageGroup04.setOnClickListener { ageSelect(it as Button) }
        ageGroup05.setOnClickListener { ageSelect(it as Button) }
    }

    private fun showResetDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.reset_selctions))
        builder.setPositiveButton(R.string.okay) { dialogInterface, i ->
            free_checkbox.isSelected = true
            btnStartFrom.text = getString(R.string.start_from)
            btnEndBefore.text = getString(R.string.end_before)
            startDateStr = null
            endDateStr = null

            spn_postcode.setSelection(0)
            spn_category.setSelection(0)

            for (button in ageButtons) {
                if (button.isSelected) {
                    button.setTextColor(resources.getColor(R.color.grey_40))
                    button.isSelected = !button.isSelected
                }
            }
        }
        builder.setNegativeButton(R.string.cancel, null)
        builder.show()
    }

    private var ageButtons = mutableListOf<Button>()
    private fun ageSelect(button: Button) {
        if (!ageButtons.contains(button)) {
            ageButtons.add(button)
        }
        for (button in ageButtons) {
            if (button.isSelected) {
                button.setTextColor(resources.getColor(R.color.grey_40))
                button.isSelected = !button.isSelected
            }
        }

        button.isSelected = true
        button.setTextColor(Color.WHITE)
    }

    private fun searchAction(text: String) {
        et_search.clearFocus()

        val bundle = Bundle()
        bundle.putString("text", text)
        bundle.putString("start", startDateStr)
        bundle.putString("end", endDateStr)
        startActivity<ListOfEventActivity>("obj" to bundle)
    }

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
        (dialog.findViewById(R.id.bt_save) as Button).setOnClickListener { dialog.dismiss() }

        dialog.show()
        dialog.window!!.attributes = lp
    }

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
}
