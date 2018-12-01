package idk.metropolia.fi.myapplication.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.ahao9.socialevent.utils.LogUtils
import com.example.ahao9.socialevent.utils.MyToast
import idk.metropolia.fi.myapplication.R
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */
class ProfileFragment : Fragment() {

    private var single_choice_selected: String? = null
    private lateinit var languageArr: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // LocaleManager(context!!).getLocale()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        languageArr = arrayOf(getString(R.string.finnish), getString(R.string.english_us))

        val languages = resources.getStringArray(R.array.language)
        val languagesArray = ArrayAdapter(context, R.layout.simple_spinner_item, languages)
        languagesArray.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_language.adapter = languagesArray
        spn_language.setSelection(0)
    }

    private fun initListeners() {

        spn_language.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val str = adapterView?.getItemAtPosition(position).toString();

                LogUtils.e("position : $position --> $str")
                myOnClickListener?.changeLanguage(position)
            }
        };



        lyt_theme.setOnClickListener {
            MyToast.show(context!!, "theme")
        }
        lyt_about_us.setOnClickListener {
            MyToast.show(context!!, "about us")
        }
        lyt_policy.setOnClickListener {
            MyToast.show(context!!, "policy")
        }

    }

    private var myOnClickListener: MyClickListener? = null

    interface MyClickListener {
        fun changeLanguage(index: Int)
    }

    fun setOnMyClickListener(myOnClickListener: MyClickListener) {
        this.myOnClickListener = myOnClickListener
    }

}