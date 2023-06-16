package com.mai.presentationspineruntime

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import com.mai.spine.OnSpineClickListener
import com.mai.spine.SpineBaseAdapter

/**
 * @Description:
 * @author ziheng
 * @date 2023/6/13 14:05
 */
class PendantView : View.OnTouchListener, View.OnClickListener,
    SpineBaseAdapter.OnSpineCreatedListener {

    private val TAG = "PendantView"

    private var mContext: Context

    private lateinit var winManager: WindowManager
    //动画层
    private lateinit var pendantViewParams: WindowManager.LayoutParams
    //处理事件层
    private lateinit var touchViewParams: WindowManager.LayoutParams
    private lateinit var inflater: LayoutInflater
    //挂件层View
    private lateinit var pendantView: View
    //处理时间层view
    private lateinit var touchView: View

    //开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
    private var mTouchStartX = 0
    private var mTouchStartY = 0
    private var mTouchCurrentX = 0
    private var mTouchCurrentY = 0

    //开始时的坐标和结束时的坐标（相对于自身控件的坐标）
    private var mStartX = 0
    private var mStartY = 0
    private var mStopX = 0
    private var mStopY = 0

    //判断悬浮窗口是否移动，这里做个标记，防止移动后松手触发了点击事件
    private var isMove = false

    private var spineView: FrameLayout? = null

    constructor(mContext: Context) {
        this.mContext = mContext
//        initWindow()
    }

    /**
     * 初始化窗口
     */
    @SuppressLint("ClickableViewAccessibility")
    fun initWindow() {
        winManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //添加动画层
        addPendantView()
        addTouchView()

    }

    /**
     * 动画显示层
     */
    fun addPendantView(){
        pendantViewParams = getWinParams(false)
        // 悬浮窗默认显示以左上角为起始坐标
        pendantViewParams.gravity = Gravity.LEFT or Gravity.TOP
        //悬浮窗的开始位置，因为设置的是从左上角开始，所以屏幕左上角是x=0;y=0
        pendantViewParams.x = winManager.defaultDisplay.width - 260
        pendantViewParams.y = 210
        //得到容器，通过这个inflater来获得悬浮窗控件
        inflater = LayoutInflater.from(mContext)
        // 获取浮动窗口视图所在布局
        pendantView = inflater.inflate(R.layout.pendant_layout, null)
        spineView = pendantView.findViewById(R.id.spine_view)
        val generateBoyGLSurfaceView = generateBoyGLSurfaceView("Boy2", mContext as MainActivity)
        spineView?.addView(generateBoyGLSurfaceView)
        winManager.addView(pendantView, pendantViewParams)
    }

    /**
     * 事件处理层
     */
    fun addTouchView(){
        touchViewParams = getWinParams(true)
        // 悬浮窗默认显示以左上角为起始坐标
        touchViewParams.gravity = Gravity.LEFT or Gravity.TOP
        //悬浮窗的开始位置，因为设置的是从左上角开始，所以屏幕左上角是x=0;y=0
        val xzhou = winManager.defaultDisplay.width - 260
        Log.i(TAG, "获取到的x轴: ${xzhou}")
        touchViewParams.x = xzhou
        touchViewParams.y = 210
        //得到容器，通过这个inflater来获得悬浮窗控件
        inflater = LayoutInflater.from(mContext)
        // 获取浮动窗口视图所在布局
        touchView = inflater.inflate(R.layout.pendant_touch_view, null)
        touchView.setOnTouchListener(this)
        winManager.addView(touchView, touchViewParams)
    }

    private fun getWinParams(isTouch: Boolean): WindowManager.LayoutParams {
        val params = WindowManager.LayoutParams()
        //设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        if(isTouch){
            //事件处理层
            params.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        }else{
            //挂件视图事件可以穿透
            params.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or //事件可以穿透
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS// 视图可以超出屏幕
        }
        //设置悬浮窗口长宽数据
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.format = PixelFormat.TRANSPARENT
        return params
    }


    //按下和移动时的时间，用于判断是否是长按事件
    var timeDown: Long = 0
    var timeMove: Long = 0

    //是否是长按事件
    var isLongClick = false

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                timeDown = System.currentTimeMillis()
                isLongClick = false
                isMove = false
                mTouchStartX = event.rawX.toInt()
                mTouchStartY = event.rawY.toInt()
                mStartX = event.x.toInt()
                mStartY = event.y.toInt()
                Log.i(TAG, "onTouch: ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                timeMove = System.currentTimeMillis()
                val durationMs = timeMove - timeDown
                if (durationMs > 100) {
                    isLongClick = true
                }
                mTouchCurrentX = event.rawX.toInt()
                mTouchCurrentY = event.rawY.toInt()
                //更新动画层布局
                pendantViewParams.x += mTouchCurrentX - mTouchStartX
                pendantViewParams.y += mTouchCurrentY - mTouchStartY
                winManager.updateViewLayout(pendantView, pendantViewParams)
                mTouchStartX = mTouchCurrentX
                mTouchStartY = mTouchCurrentY
                Log.i(TAG, "onTouch: ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                if (!isLongClick) {
                    boyAdapter.setAction("jump")
                }
                mStopX = event.x.toInt()
                mStopY = event.y.toInt()
                Log.i("TAG", "ACTION_UP - onTouch: ${mStopX}")
                Log.i("TAG", "ACTION_UP - onTouch: ${mStopY}")
                Log.i("TAG", "ACTION_UP - onTouch: ${pendantViewParams.x}")
                Log.i("TAG", "ACTION_UP - onTouch: ${pendantViewParams.y}")
                //更新事件接收层布局
                touchViewParams.x = pendantViewParams.x
                touchViewParams.y = pendantViewParams.y
                winManager.updateViewLayout(touchView,touchViewParams)
                if (kotlin.math.abs(mStartX - mStopX) >= 1 || kotlin.math.abs(mStartY - mStopY) >= 1) {
                    isMove = true
                }
                Log.i(TAG, "onTouch: ACTION_UP")
            }
        }
        //如果是移动事件不触发OnClick事件，防止移动的时候一放手形成点击事件
        return isMove
    }

    /**
     * 销毁视图
     */
    fun onDestory() {
        if (this::pendantView.isInitialized) {
            try {
                winManager.removeView(pendantView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private lateinit var boyAdapter: BoyAdapter
    private var mHandler = object : Handler(Looper.getMainLooper()) {}


    private fun generateBoyGLSurfaceView(tag: String, activity: MainActivity): View {
        boyAdapter = BoyAdapter(0, 50, 0, 20)
        boyAdapter.skinName = "default"
        boyAdapter.animationName = "Q_ab_tb01"
//        boyAdapter.debugMode = tag == "Boy1"
        //        boyAdapter.debugMode = tag == "Boy1"
//        boyAdapter.tag = tag
        boyAdapter.setOnCreatedListener(this)
        boyAdapter.setOnSpineClickListener(object : OnSpineClickListener {
            override fun onClick() {
                Log.e("SpineTest", "on spine:${boyAdapter.tag} clicked")
            }
        })
        return boyAdapter.create(activity)
    }


    override fun onCreated(tag: String) {
        mHandler.post {

        }
    }

    override fun onClick(v: View?) {

    }
}