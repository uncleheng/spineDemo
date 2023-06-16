package com.mai.spine

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.View
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class SpineBaseAnimView : AndroidViewApplication {

    lateinit var adapter: SpineBaseAdapter

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun initializeForView(listener: ApplicationListener): View {
        val config = AndroidApplicationConfiguration()
        config.a = 8
        config.b = config.a
        config.g = config.b
        config.r = config.g
        val spineView = initializeForView(listener, config)
        if (spineView is GLSurfaceView) {
            // 透明背景
            spineView.holder.setFormat(PixelFormat.TRANSLUCENT)
            // 设置到最上层
            spineView.setZOrderOnTop(true)
        }
        //不处理touch事件
        spineView.setOnTouchListener { _, _ ->
            return@setOnTouchListener false
        }
        return spineView
    }

}