package idk.metropolia.fi.myapplication.view.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.example.ahao9.socialevent.utils.LogUtils
import com.example.ahao9.socialevent.utils.MyToast
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.adapter.MyPostAdapter
import idk.metropolia.fi.myapplication.httpsService.Networking
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.activity_my_post.*
import org.jetbrains.anko.startActivity
import java.io.Serializable

class MyPostActivity : AppCompatActivity() {

    private var mListAdapter: MyPostAdapter? = null
    private val mDatas = ArrayList<SearchEventsResultObject.SingleBeanData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_post)

        initToolbar()
        initComponent()
        loadData()
    }

    // page: String, page_size: String, type: String, input: String, start: String
    private lateinit var call: retrofit2.Call<SearchEventsResultObject>
    private fun loadData() {

        val mDialog = ProgressDialog(this)
        mDialog.setProgressStyle(0)
        mDialog.setCancelable(false)
        mDialog.setMessage(getString(R.string.loading))
        mDialog.show()

        call = Networking.testService.getMyPostEvent()

        val value = object : retrofit2.Callback<SearchEventsResultObject> {
            override fun onResponse(call: retrofit2.Call<SearchEventsResultObject>, response: retrofit2.Response<SearchEventsResultObject>) {

                if (response.isSuccessful) {
                    val dataList = response.body().data

                    mDatas.clear()
                    mDatas.addAll(dataList)

                    LogUtils.e("mListAdapter is nulll: --> ${mListAdapter == null}")

                    mListAdapter?.notifyDataSetChanged()

                    mDialog.dismiss()
                    LogUtils.e("size: --> ${dataList.size}")

                } else {
                    MyToast.show(this@MyPostActivity, getString(R.string.check_network))
                    mDialog.dismiss()
                }
            }

            // this method gets called if the http call fails (no internet etc)
            override fun onFailure(call: retrofit2.Call<SearchEventsResultObject>, t: Throwable) {
                LogUtils.e("onFailure: " + t.toString())
                MyToast.show(this@MyPostActivity, getString(R.string.check_network))
                mDialog.dismiss()
            }
        }
        call.enqueue(value)
    }

    private fun initComponent() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        mListAdapter = MyPostAdapter(this, mDatas)
        recyclerView.adapter = mListAdapter

        mListAdapter?.setOnItemClickListener(object : MyPostAdapter.OnItemClickListener {
            override fun onItemClick(view: View, obj: SearchEventsResultObject.SingleBeanData, position: Int) {
                startActivity<DetailsActivity>("obj" to (mDatas[position] as Serializable))
            }
        })
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.my_event)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
