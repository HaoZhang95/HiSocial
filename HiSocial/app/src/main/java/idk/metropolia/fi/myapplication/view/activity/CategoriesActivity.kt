package idk.metropolia.fi.myapplication.view.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import com.example.ahao9.socialevent.utils.LogUtils
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.activity_categories.*
import org.jetbrains.anko.startActivity
import java.util.*

class CategoriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        initToolbar()
        initListeners()
    }

    private fun initListeners() {
        category01.setOnClickListener { startActivity<ListOfEventActivity>("text" to "Play") }
        category02.setOnClickListener { startActivity<ListOfEventActivity>("text" to "Tahto") }
        category03.setOnClickListener { startActivity<ListOfEventActivity>("text" to "Art") }
        category04.setOnClickListener { startActivity<ListOfEventActivity>("text" to "Music") }

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
    }

    private fun searchAction(text: String) {
        et_search.text.clear()
        et_search.clearFocus()

        startActivity<ListOfEventActivity>("text" to text)
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

}
