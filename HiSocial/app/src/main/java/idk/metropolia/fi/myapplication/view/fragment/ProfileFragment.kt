package idk.metropolia.fi.myapplication.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahao9.socialevent.utils.MyToast
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.view.activity.MyPostActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */
class ProfileFragment : Fragment() {

    private lateinit var languageArr: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        languageArr = arrayOf(getString(R.string.finnish), getString(R.string.english_us))
    }

    private fun initListeners() {

        lyt_my_post.setOnClickListener { startActivity<MyPostActivity>() }

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