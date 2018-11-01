package idk.metropolia.fi.myapplication.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject.SingleBeanInSearch
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.activity_details.*

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 22:21 2018/10/30
 * @ Description：Build for Metropolia project
 */
class DetailsActivity: AppCompatActivity() {

    private var parent_view: View? = null
    private var iv_details: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        parent_view = findViewById(R.id.parent_view)

        iv_details = findViewById(R.id.iv_details)
        initToolbar()
        initComponent()
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initComponent() {
        val obj = intent.extras.get("obj") as SingleBeanInSearch
        if (obj.images.isNotEmpty()) {
            Tools.displayImageOriginal(this, iv_details, obj.images.get(0).url)
        } else {
            Tools.displayImageOriginal(this, iv_details, R.drawable.not_found)
        }
        tv_title.text = obj.name.fi

        tv_location.text = "Helsinki"
        tv_price.text = "Free"

        tv_subtitle.text = obj.shortDescription.fi
        if (obj.score != 0.0) {
            tv_score.text = "${obj.score.toString().slice(0..3)} / 10"
            rb_score.rating = obj.score.toFloat() / 2
        } else {
            tv_score.text = "un-known"
            rb_score.rating = 0.0f
        }
        tv_desc.text = obj.description.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
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

}