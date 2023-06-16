package com.mai.presentationspineruntime

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.GdxNativesLoader
import com.esotericsoftware.spine.AnimationStateData
import com.esotericsoftware.spine.SkeletonData
import com.esotericsoftware.spine.SkeletonJson
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.mai.presentationspineruntime.databinding.ActivityMainBinding
import com.mai.spine.SpineBaseAdapter


class MainActivity : AppCompatActivity(), SpineBaseAdapter.OnSpineCreatedListener,
    View.OnClickListener {

    private val TAG = "MainActivity"

    companion object {
        init {
            GdxNativesLoader.load()
        }
    }

    private lateinit var boyAdapter: BoyAdapter

    private lateinit var binding: ActivityMainBinding

    private var pendantView: PendantView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.donghuaOne.setOnClickListener(this)
        binding.donghuaTwo.setOnClickListener(this)
        binding.showPresentationBtn.setOnClickListener(this)
        val scale = resources.displayMetrics.density

        val height = ScreenUtils.px2dip(this, 327f)
        val width = ScreenUtils.px2dip(this, 320f)

        Log.i(TAG, "onCreate: height: $height")
        Log.i(TAG, "onCreate: width: $width")




//        val playerAtlas = TextureAtlas(Gdx.files.getFileHandle("spineboy/spineboy.atlas", Files.FileType.Internal))
//        val json = SkeletonJson(playerAtlas)
//        val playerSkeletonData: SkeletonData =
//            json.readSkeletonData(Gdx.files.getFileHandle("G_ab_spine01/G_ab_spine01.json",Files.FileType.Internal))
//        Log.i(TAG, "onCreate: 获取到的  animations ：${playerSkeletonData.animations}")
//        Log.i(TAG, "onCreate: 获取到的  defaultSkin ：${playerSkeletonData.defaultSkin}")
//        Log.i(TAG, "onCreate: 获取到的  skins ：${playerSkeletonData.skins}")
//        Log.i(TAG, "onCreate: 获取到的  height ：${playerSkeletonData.height}")
//        Log.i(TAG, "onCreate: 获取到的  width ：${playerSkeletonData.width}")
//        Log.i(TAG, "onCreate: 获取到的  width ：${playerSkeletonData.name}")
//        playerSkeletonData.animations
//        val playerAnimationData = AnimationStateData(playerSkeletonData)
//        playerAnimationData.toString()
    }


    private var mHandler = object : Handler(Looper.getMainLooper()) {}

    override fun onCreated(tag: String) {
        mHandler.post {
            if (tag == "Boy1") {
//                val glSurfaceView = generateBoyGLSurfaceView("Boy2")
//                showSpine(glSurfaceView, binding.frameLay)
            }
            if (tag == "Boy2") {
//                binding.surfaceBtn.setOnClickListener {
//                    if (bool)
//                        return@setOnClickListener
//                    bool = true
//
//                    val overlyRoot = SpineOverlayBinding.inflate(layoutInflater).root
//
//                    val newPU = PUSurfaceView(outerContext)
//                    val layoutParams = FrameLayout.LayoutParams(180, 180)
//                    layoutParams.gravity = Gravity.CENTER_VERTICAL or Gravity.END
//                    newPU.layoutParams = layoutParams
//                    newPU.drawImage(R.mipmap.octocat)
//                    overlyRoot.addView(newPU)
//
//                    overlyRoot.layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT
//                    )
//                    binding.root.addView(overlyRoot)
//
//                    newPU.postDelayed({
//                        overlyRoot.removeView(newPU)
//                        binding.root.removeView(overlyRoot)
//                        bool = false
//                    }, 3000)
//                }
            }
        }

    }

    private fun showSpine(glSurfaceView: View, container: ViewGroup) {
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        glSurfaceView.layoutParams = params

        container.addView(glSurfaceView)
    }

    override fun onClick(v: View?) {
        if (v == binding.showPresentationBtn) {
            if (XXPermissions.isGranted(this@MainActivity, Permission.SYSTEM_ALERT_WINDOW)) {
                if (pendantView == null) {
                    pendantView = PendantView(this)
                    pendantView?.initWindow()
                    Log.i(TAG, "onCreate: 执行了悬浮窗")
                }
            } else {
                XXPermissionsUtil.checkPermissions(this)
            }
        }
    }


//    private fun generateBoyGLSurfaceView(tag: String): View {
//        boyAdapter = BoyAdapter(0, 50, 0, 20)
//        boyAdapter.skinName = "default"
//        boyAdapter.animationName = "walk"
//        boyAdapter.debugMode = tag == "Boy1"
//        boyAdapter.tag = tag
//        boyAdapter.setOnCreatedListener(this)
//        boyAdapter.setOnSpineClickListener(object : OnSpineClickListener {
//            override fun onClick() {
//                Log.e("SpineTest", "on spine:${boyAdapter.tag} clicked")
//            }
//        })
//
//
//        return boyAdapter.create(this)
//    }

}