package com.mai.presentationspineruntime

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.text.TextUtils


/**
 * @Description:
 * @author ziheng
 * @date 2022/12/8 11:24
 */
object ScreenUtils {
    /**
     * 是否是竖屏
     *
     * @param context
     * @return true:是竖屏；false:是横屏
     */
    fun isPortrait(context: Context): Boolean {
        val configuration = context.resources.configuration //获取设置的配置信息
        return configuration.orientation != Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 竖屏的屏幕宽度
     */
    const val SCREEN_WITH = 393

    /**
     * 横屏的屏幕宽度\高度
     */
    const val SCREEN_WITH_HORIZONTAL = 1920

    const val SCREEN_HEIGHT_HORIZONTAL = 984


    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context?, dpValue: Float): Int {
        if (context == null) {
            return dpValue.toInt()
        }
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context?, pxValue: Float): Int {
        if (context == null) {
            return pxValue.toInt()
        }
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    fun isForeground(activity: Activity): Boolean {
        return isForeground(activity, activity.javaClass.name)
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    fun isForeground(context: Context?, className: String): Boolean {
        if (context == null || TextUtils.isEmpty(className)) return false
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            if (className == cpn!!.className) return true
        }
        return false
    }

    /**
     * get App versionCode
     * @param
     * @return
     */
    fun getVersionCode(context: Context): String{
        val packageManager = context.packageManager
        val packageInfo: PackageInfo
        var versionCode= ""
        try {
            packageInfo= packageManager?.getPackageInfo(context.packageName,0)!!
            versionCode=""+packageInfo.versionCode
        }catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }
        return versionCode
    }
    /**
     * get App versionName
     * @param
     * @return
     */
    fun getVersionName(context: Context): String{
        val packageManager = context.packageManager
        val packageInfo: PackageInfo
        var versionCode= ""
        try {
            packageInfo= packageManager?.getPackageInfo(context.packageName,0)!!
            versionCode=""+packageInfo.versionName
        }catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }
        return versionCode
    }
}