package idk.metropolia.fi.myapplication.view.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.ahao9.socialevent.utils.MyToast
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.utils.Tools
import idk.metropolia.fi.myapplication.view.activity.AboutUsActivity
import idk.metropolia.fi.myapplication.view.activity.MyPostActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.support.v4.startActivity
import skin.support.SkinCompatManager

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

        lyt_about_us.setOnClickListener { startActivity<AboutUsActivity>() }

        lyt_policy.setOnClickListener { showDialogAbout() }

        darkTheme.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN)
            } else {
                SkinCompatManager.getInstance().restoreDefaultTheme()
            }
        }
    }

    private fun showDialogAbout() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_policy)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        (dialog.findViewById(R.id.bt_getcode) as View).setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://www.hel.fi/helsinki/en")
            startActivity(i)
        }

        (dialog.findViewById(R.id.bt_close) as ImageButton).setOnClickListener { dialog.dismiss() }

        (dialog.findViewById(R.id.bt_school) as Button).setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://www.metropolia.fi/")
            startActivity(i)
        }

        dialog.show()
        dialog.window!!.attributes = lp
    }

    private var myOnClickListener: MyClickListener? = null

    interface MyClickListener {
        fun changeLanguage(index: Int)
    }

    fun setOnMyClickListener(myOnClickListener: MyClickListener) {
        this.myOnClickListener = myOnClickListener
    }

}