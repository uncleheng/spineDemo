package com.mai.presentationspineruntime

import com.badlogic.gdx.Files
import com.mai.spine.SpineBaseAdapter

class BoyAdapter : SpineBaseAdapter {

    override var skinName: String = "default"
    override var animationName: String = "Q_ab_idle1"

    constructor() : super()
    constructor(padding: Int) : super(padding)
    constructor(paddingStart: Int, paddingTop: Int, paddingEnd: Int, paddingBottom: Int) : super(
        paddingStart,
        paddingTop,
        paddingEnd,
        paddingBottom
    )

    override fun onCreateImpl() {
        setAltasPath("G_ab_spine02/G_ab_spine02.atlas", Files.FileType.Internal)
        setSkeletonPath("G_ab_spine02/G_ab_spine02.json", Files.FileType.Internal)
    }

    override fun onCreatedImpl(){
        //默认皮肤
        setSkin(skinName)
        //默认动作
        setAnimation(0, "Q_ab_idle1", true)
    }

    override fun doClick() {
        mAnimationState.setAnimation(0, "jump", false)
        mAnimationState.addAnimation(0, "walk", true, 1.5f)
    }

    fun setAction(string: String){
        mAnimationState.setAnimation(0, string, false)
        mAnimationState.addAnimation(0, "walk", true, 1.5f)
    }
}