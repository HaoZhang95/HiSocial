package idk.metropolia.fi.myapplication.view.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.ahao9.socialevent.utils.MyToast
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.utils.Tools
import idk.metropolia.fi.myapplication.utils.ViewAnimationUtils
import kotlinx.android.synthetic.main.fragment_new_event.*
import java.util.*

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */
class NewEventFragment : Fragment() {

    private val view_list = ArrayList<View>()
    private val step_view_list = ArrayList<RelativeLayout>()
    private var success_step = 0
    private var current_step = 0
    private lateinit var parent_view: View
    private var date: Date? = null
    private var time: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parent_view = view

        initComponents(view)
        initListeners()
    }

    private fun initComponents(view: View) {
        // populate layout field
        view_list.add(view.findViewById(R.id.lyt_title))
        view_list.add(view.findViewById(R.id.lyt_description))
        view_list.add(view.findViewById(R.id.lyt_time))
        view_list.add(view.findViewById(R.id.lyt_date))
        view_list.add(view.findViewById(R.id.lyt_confirmation))

        // populate view step (circle in left)
        step_view_list.add(view.findViewById(R.id.step_title) as RelativeLayout)
        step_view_list.add(view.findViewById(R.id.step_description) as RelativeLayout)
        step_view_list.add(view.findViewById(R.id.step_time) as RelativeLayout)
        step_view_list.add(view.findViewById(R.id.step_date) as RelativeLayout)
        step_view_list.add(view.findViewById(R.id.step_confirmation) as RelativeLayout)

        for (v in view_list) {
            v.visibility = View.GONE
        }

        view_list[0].visibility = View.VISIBLE
        hideSoftKeyboard()

        (view.findViewById(R.id.tv_time) as TextView).setOnClickListener { v -> dialogTimePickerLight(v as TextView) }

        (view.findViewById(R.id.tv_date) as TextView).setOnClickListener { v -> dialogDatePickerLight(v as TextView) }
    }

    private fun initListeners() {

        bt_continue_title.setOnClickListener {
            // validate input user here
            if ((view!!.findViewById(R.id.et_title) as EditText).text.toString().trim { it <= ' ' } == "") {
                Snackbar.make(parent_view, "Title cannot empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            collapseAndContinue(0)
        }

        bt_continue_description.setOnClickListener {
            // validate input user here
            if ((view!!.findViewById(R.id.et_description) as EditText).text.toString().trim { it <= ' ' } == "") {
                Snackbar.make(parent_view, "Description cannot empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            collapseAndContinue(1)
        }

        bt_continue_time.setOnClickListener {
            // validate input user here
            if (time == null) {
                Snackbar.make(parent_view, "Please set event time", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            collapseAndContinue(2)
        }

        bt_continue_date.setOnClickListener {
            // validate input user here
            if (date == null) {
                Snackbar.make(parent_view, "Please set event date", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            collapseAndContinue(3)
        }

        bt_add_event.setOnClickListener {
            MyToast.show(context!!, "添加活动.")
        }

        // ==========================================
        tv_label_title.setOnClickListener {
            if (success_step >= 0 && current_step != 0) {
                current_step = 0
                collapseAll()
                ViewAnimationUtils.expand(view_list[0])
            }
        }

        tv_label_description.setOnClickListener {
            if (success_step >= 1 && current_step != 1) {
                current_step = 1
                collapseAll()
                ViewAnimationUtils.expand(view_list[1])
            }
        }

        tv_label_time.setOnClickListener {
            if (success_step >= 2 && current_step != 2) {
                current_step = 2
                collapseAll()
                ViewAnimationUtils.expand(view_list[2])
            }
        }

        tv_label_date.setOnClickListener {
            if (success_step >= 3 && current_step != 3) {
                current_step = 3
                collapseAll()
                ViewAnimationUtils.expand(view_list[3])
            }
        }

        tv_label_confirmation.setOnClickListener {
            if (success_step >= 4 && current_step != 4) {
                current_step = 4
                collapseAll()
                ViewAnimationUtils.expand(view_list[4])
            }
        }

    }

    fun clickAction(view: View) {
        val id = view.id
        when (id) {
            R.id.bt_continue_title -> {
                // validate input user here
                if ((view.findViewById(R.id.et_title) as EditText).text.toString().trim { it <= ' ' } == "") {
                    Snackbar.make(parent_view, "Title cannot empty", Snackbar.LENGTH_SHORT).show()
                    return
                }

                collapseAndContinue(0)
            }
            R.id.bt_continue_description -> {
                // validate input user here
                if ((view.findViewById(R.id.et_description) as EditText).text.toString().trim { it <= ' ' } == "") {
                    Snackbar.make(parent_view, "Description cannot empty", Snackbar.LENGTH_SHORT).show()
                    return
                }

                collapseAndContinue(1)
            }
            R.id.bt_continue_time -> {
                // validate input user here
                if (time == null) {
                    Snackbar.make(parent_view, "Please set event time", Snackbar.LENGTH_SHORT).show()
                    return
                }
                collapseAndContinue(2)
            }
            R.id.bt_continue_date -> {
                // validate input user here
                if (date == null) {
                    Snackbar.make(parent_view, "Please set event date", Snackbar.LENGTH_SHORT).show()
                    return
                }

                collapseAndContinue(3)
            }
            R.id.bt_add_event -> {
                MyToast.show(context!!, "添加活动.")
            }

        }
    }

    fun clickLabel(view: View) {
        val id = view.id
        when (id) {
            R.id.tv_label_title -> if (success_step >= 0 && current_step != 0) {
                current_step = 0
                collapseAll()
                ViewAnimationUtils.expand(view_list[0])
            }
            R.id.tv_label_description -> if (success_step >= 1 && current_step != 1) {
                current_step = 1
                collapseAll()
                ViewAnimationUtils.expand(view_list[1])
            }
            R.id.tv_label_time -> if (success_step >= 2 && current_step != 2) {
                current_step = 2
                collapseAll()
                ViewAnimationUtils.expand(view_list[2])
            }
            R.id.tv_label_date -> if (success_step >= 3 && current_step != 3) {
                current_step = 3
                collapseAll()
                ViewAnimationUtils.expand(view_list[3])
            }
            R.id.tv_label_confirmation -> if (success_step >= 4 && current_step != 4) {
                current_step = 4
                collapseAll()
                ViewAnimationUtils.expand(view_list[4])
            }
        }
    }

    private fun collapseAndContinue(index: Int) {
        var index = index
        ViewAnimationUtils.collapse(view_list[index])
        setCheckedStep(index)
        index++
        current_step = index
        success_step = if (index > success_step) index else success_step
        ViewAnimationUtils.expand(view_list[index])
    }

    private fun collapseAll() {
        for (v in view_list) {
            ViewAnimationUtils.collapse(v)
        }
    }

    private fun setCheckedStep(index: Int) {
        val relative = step_view_list[index]
        relative.removeAllViews()
        val img = ImageButton(context)
        img.setImageResource(R.drawable.ic_done)
        img.setBackgroundColor(Color.TRANSPARENT)
        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        relative.addView(img)
    }


    private fun dialogDatePickerLight(textView: TextView) {
        val cur_calender = Calendar.getInstance()
        val datePicker = DatePickerDialog.newInstance(
                { view, year, monthOfYear, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date_ship_millis = calendar.timeInMillis
                    date = Date(date_ship_millis)
                    textView.text = Tools.getFormattedDateSimple(date_ship_millis)
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        )
        //set dark light
        datePicker.isThemeDark = false
        datePicker.accentColor = resources.getColor(R.color.colorPrimary)
        datePicker.minDate = cur_calender
        datePicker.show(activity!!.fragmentManager, "Datepickerdialog")
    }

    private fun dialogTimePickerLight(textView: TextView) {
        val cur_calender = Calendar.getInstance()
        val datePicker = TimePickerDialog.newInstance({ view, hourOfDay, minute, second ->
            time = (if (hourOfDay > 9) hourOfDay.toString() + "" else "0$hourOfDay") + ":" + minute
            textView.setText(time)
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true)
        //set dark light
        datePicker.isThemeDark = false
        datePicker.accentColor = resources.getColor(R.color.colorPrimary)
        datePicker.show(activity!!.fragmentManager, "Timepickerdialog")
    }

    private fun hideSoftKeyboard() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

}