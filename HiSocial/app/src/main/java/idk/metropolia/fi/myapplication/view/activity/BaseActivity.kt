package idk.metropolia.fi.myapplication.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    private val mRequestCode = 1024
    private var mRequestPermissionCallBack: RequestPermissionCallBack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * 权限请求结果回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var hasAllGranted = true
        var permissionNames = StringBuilder()
        for (s in permissions) {
            permissionNames = permissionNames.append(s + "\r\n")
        }
        when (requestCode) {
            mRequestCode -> {
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        hasAllGranted = false
                        //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                        // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            AlertDialog.Builder(this).setTitle("Permission Request")//设置对话框标题
                                    .setMessage("Hi there，You need this permission：" + permissionNames +
                                            " Please allow, otherwise normal function will be affected。")//设置显示的内容
                                    .setPositiveButton("Allow") { dialog, which ->
                                        //添加确定按钮
                                        //确定按钮的响应事件
                                        //TODO Auto-generated method stub
                                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        val uri = Uri.fromParts("package", this.applicationContext.packageName, null)
                                        intent.data = uri
                                        startActivity(intent)
                                        dialog.dismiss()
                                    }.setNegativeButton("Cancel") { dialog, which ->
                                        //添加返回按钮
                                        //响应事件
                                        // TODO Auto-generated method stub
                                        dialog.dismiss()
                                    }.setOnCancelListener { mRequestPermissionCallBack!!.denied() }.show()//在按键响应事件中显示此对话框
                        } else {
                            //用户拒绝权限请求，但未选中“不再提示”选项
                            mRequestPermissionCallBack!!.denied()
                        }
                        break
                    }
                }
                if (hasAllGranted) {
                    mRequestPermissionCallBack!!.granted()
                }
            }
        }
    }

    /**
     * 发起权限请求
     */
    fun myRequestPermissions(context: Context, permissions: Array<String>,
                             callback: RequestPermissionCallBack) {
        this.mRequestPermissionCallBack = callback
        var permissionNames = StringBuilder()
        for (s in permissions) {
            permissionNames = permissionNames.append(s + "\r\n")
        }
        //如果所有权限都已授权，则直接返回授权成功,只要有一项未授权，则发起权限请求
        var isAllGranted = true
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                    AlertDialog.Builder(context).setTitle("Permission Request")//设置对话框标题
                            .setMessage("Hi there，You need this permission：" + permissionNames +
                                    " Please allow, otherwise normal function will be affected。")//设置显示的内容
                            .setPositiveButton("Okay") { dialog, which ->
                                //添加确定按钮
                                //确定按钮的响应事件
                                //TODO Auto-generated method stub
                                ActivityCompat.requestPermissions(context, permissions, mRequestCode)
                            }.show()//在按键响应事件中显示此对话框
                } else {
                    ActivityCompat.requestPermissions(context, permissions, mRequestCode)
                }
                break
            }
        }
        if (isAllGranted) {
            mRequestPermissionCallBack!!.granted()
            return
        }
    }

    /**
     * 权限请求结果回调接口
     */
    interface RequestPermissionCallBack {
        fun granted()
        fun denied()
    }
}
