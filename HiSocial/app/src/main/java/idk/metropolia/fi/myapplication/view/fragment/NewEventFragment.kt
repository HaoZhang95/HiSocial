package idk.metropolia.fi.myapplication.view.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.ahao9.socialevent.utils.LogUtils
import com.example.ahao9.socialevent.utils.MyToast
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.utils.PhotoUtils
import idk.metropolia.fi.myapplication.utils.PhotoUtils.hasSdcard
import idk.metropolia.fi.myapplication.utils.Tools
import idk.metropolia.fi.myapplication.utils.ViewAnimationUtils
import kotlinx.android.synthetic.main.fragment_new_event.*
import java.io.File
import java.util.*

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */

class NewEventFragment : BaseFragment() {

    private val fileProviderPath = "idk.metropolia.fi.myapplication.fileprovider"
    private val requestPermissionsList = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)

    private val step_view_list = ArrayList<RelativeLayout>()
    private val view_list = ArrayList<View>()
    private var success_step = 0
    private var current_step = 0
    private lateinit var parent_view: View
    private var date: Date? = null
    private var time: String? = null
    private lateinit var mBehavior: BottomSheetBehavior<View>
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private lateinit var bottom_sheet: View
    private var hasImage = false

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

       // view_list.add(view.findViewById(R.id.lyt_title))
        view_list.add(view.findViewById(R.id.lyt_description))
        view_list.add(view.findViewById(R.id.lyt_time))
        view_list.add(view.findViewById(R.id.lyt_date))
        view_list.add(view.findViewById(R.id.lyt_confirmation))
        view_list.add(view.findViewById(R.id.lyt_competition))

        // populate view step (circle in left)
        step_view_list.add(view.findViewById(R.id.step_title) as RelativeLayout)
        step_view_list.add(view.findViewById(R.id.step_description) as RelativeLayout)
        step_view_list.add(view.findViewById(R.id.step_time) as RelativeLayout)
        step_view_list.add(view.findViewById(R.id.step_date) as RelativeLayout)
        step_view_list.add(view.findViewById(R.id.step_confirmation) as RelativeLayout)
        step_view_list.add(view.findViewById(R.id.step_competition) as RelativeLayout)


        bottom_sheet = view.findViewById(R.id.bottom_sheet_list)
        mBehavior = BottomSheetBehavior.from(bottom_sheet)
        for (v in view_list) {
            v.visibility = View.GONE
        }

        view_list[0].visibility = View.VISIBLE
        hideSoftKeyboard()
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
            // createMyEvent()
            collapseAndContinue(4)
        }

        bt_competition.setOnClickListener {
            MyToast.show(context!!,"Competition is clicked")
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

        tv_label_competition.setOnClickListener {
            if (success_step >= 5 && current_step != 5) {
                current_step = 5
                collapseAll()
                ViewAnimationUtils.expand(view_list[5])
            }
        }

        fab_take_photo.setOnClickListener { showBottomSheetDialog() }

        tv_time.setOnClickListener { dialogTimePickerLight(it as TextView) }

        tv_date.setOnClickListener { dialogDatePickerLight(it as TextView) }
    }

    private fun createMyEvent() {

        val title = et_title.text.trim().toString()
        val desc = et_description.text.trim().toString()
        val time = tv_time.text.trim().toString()
        val date = tv_date.text.trim().toString()

        if (hasImage) {
            val image = iv_event.drawable
            LogUtils.e("$image $title --> $desc --> $time --> $date")
        } else {
            LogUtils.e("$title --> $desc --> $time --> $date")
        }

    }

    private fun takePhoto() {
        myRequestPermissions(context!!, requestPermissionsList, object : RequestPermissionCallBack {
            override fun granted() {
                if (hasSdcard()) {
                    imageUri = Uri.fromFile(fileUri)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //通过FileProvider创建一个content类型的UriF
                        imageUri = FileProvider.getUriForFile(context!!, fileProviderPath, fileUri)
                    }
                    PhotoUtils.takePicture(activity, imageUri, CODE_CAMERA_REQUEST)
                } else {
                    MyToast.show(context!!, "No sdcard on the device！")
                }
            }

            override fun denied() {
                MyToast.show(context!!, "Some permission acquisition failed, normal function is affected！")
            }
        })
    }

    private fun showBottomSheetDialog() {
        if (mBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val view = layoutInflater.inflate(R.layout.sheet_list, null)

        (view.findViewById(R.id.lyt_camera) as View).setOnClickListener {
            takePhoto()
            mBottomSheetDialog?.dismiss()
        }

        (view.findViewById(R.id.lyt_gallery) as View).setOnClickListener {
            openGallery()
            mBottomSheetDialog?.dismiss()
        }

        mBottomSheetDialog = BottomSheetDialog(context!!)
        mBottomSheetDialog?.setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog?.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        mBottomSheetDialog?.show()
        mBottomSheetDialog?.setOnDismissListener {
            mBottomSheetDialog = null
        }
    }

    private fun openGallery() {

        myRequestPermissions(context!!, requestPermissionsList, object : RequestPermissionCallBack {
            override fun granted() {
                PhotoUtils.openPic(activity, CODE_GALLERY_REQUEST)
            }

            override fun denied() {
                MyToast.show(context!!, "Some permission acquisition failed, normal function is affected！")
            }
        })
    }

    private val CODE_GALLERY_REQUEST = 0xa0
    private val CODE_CAMERA_REQUEST = 0xa1
    private val CODE_RESULT_REQUEST = 0xa2
    private val fileUri = File(Environment.getExternalStorageDirectory().path + "/photo.jpg")
    private val fileCropUri = File(Environment.getExternalStorageDirectory().path + "/crop_photo.jpg")
    private var imageUri: Uri? = null
    private var cropImageUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CODE_CAMERA_REQUEST -> {     //拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri)
                    // PhotoUtils.cropImageUri(activity, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST)
                    PhotoUtils.notCropImageUri(activity, imageUri, cropImageUri, CODE_RESULT_REQUEST)
                }
                CODE_GALLERY_REQUEST -> //访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri)
                        var newUri = Uri.parse(PhotoUtils.getPath(context!!, data?.data))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(context!!, fileProviderPath, File(newUri.path!!))
                        PhotoUtils.notCropImageUri(activity, newUri, cropImageUri, CODE_RESULT_REQUEST)
                    } else {
                        MyToast.show(context!!, "No sdcard on the device！")
                    }
                CODE_RESULT_REQUEST -> {
                    val bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, context)
                    if (bitmap != null) {
                        showImages(bitmap)
                    }
                }
            }
        }
    }

    private fun showImages(bitmap: Bitmap) {
        iv_event.setImageBitmap(bitmap)
        hasImage = true
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
                { _, year, monthOfYear, dayOfMonth ->
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
            textView.text = time
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