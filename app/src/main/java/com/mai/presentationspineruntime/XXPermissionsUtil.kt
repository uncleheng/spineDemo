package com.mai.presentationspineruntime

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 * @Description: 判断是否有权限
 * @author ziheng
 * @date 2023/3/17 16:43
 */
object XXPermissionsUtil {

    private const val TAG = "XXPermissionsUtil"

    fun checkPermissions(mContext: Activity?) {
        if (mContext == null) {
            Log.e(TAG, "showVirtualIp: 暂未申请权限,传入的context为null")
            return
        }
        XXPermissions.with(mContext)
            // 申请单个权限
            .permission(Permission.SYSTEM_ALERT_WINDOW)
            .request(object : OnPermissionCallback {

                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    Log.i(TAG, "onGranted: 悬浮窗 权限获取成功")
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    if (doNotAskAgain) {
                        Log.i(TAG, "onGranted: 被永久拒绝授权，请手动")
                     // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        // XXPermissions.startPermissionActivity(this@SelectVirtualActivity, permissions)
                    } else {
                        Log.i(TAG, "onDenied: 悬浮窗 权限失败")
                    }
                }
            })
    }


}