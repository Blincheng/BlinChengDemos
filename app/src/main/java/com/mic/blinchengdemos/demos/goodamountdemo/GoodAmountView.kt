package com.mic.blinchengdemos.demos.goodamountdemo

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.mic.blinchengdemos.R
import com.mic.blinchengdemos.utils.UIUtils
import java.util.*


/**
 * Author: Blincheng.
 * Date: 2017/6/9.
 * Description:一个有趣的商品数量加减交互控件
 */

class GoodAmountView : RelativeLayout, View.OnTouchListener, View.OnClickListener {
    var actionBack: OnActionBack?= null
    val ONCE_TIME = 160L //加减的时间间隔
    val ANIMATION_DURATION = 200L//恢复动画时间
    var startX = 0f
    var countView: LinearLayout?= null
    var onActionTime = 0L
    var isAction = false//是否正在被拖动(触发加减操作)
    var amount_tv: TextView?= null
    var add_img: ImageView?= null
    var sub_img: ImageView?= null
    var amount = 1//文本数量
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init(){
        LayoutInflater.from(context).inflate(R.layout.layout_good_amount_view, this, true)
        countView = findViewById(R.id.good_amount_linear) as LinearLayout
        countView?.setOnTouchListener(this)
        amount_tv = findViewById(R.id.good_amount_tv) as TextView
        add_img = findViewById(R.id.good_amount_add_btn) as ImageView
        sub_img = findViewById(R.id.good_amount_sub_btn) as ImageView
        add_img?.setOnClickListener(this)
        sub_img?.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.good_amount_add_btn -> {
                amount ++
                amount_tv?.text = amount.toString()
                actionBack?.onAddAction()
            }
            R.id.good_amount_sub_btn -> {
                if(amount > 1)
                    amount --
                amount_tv?.text = amount.toString()
                actionBack?.onSubAction()
            }
        }
    }
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                startX = event?.rawX
            }
            MotionEvent.ACTION_MOVE -> {
                var moveX = event?.rawX - startX
                moveAnimation(moveX.toInt())
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                backAnimation()
            }
        }
        return true
    }
    fun moveAnimation(x: Int){
        if (x in -UIUtils.dip2px(context,30f)..UIUtils.dip2px(context,30f)){
            var layout: LayoutParams = countView?.layoutParams as LayoutParams
            layout?.leftMargin = x+ UIUtils.dip2px(context,30f)
            isAction = false
            countView?.layoutParams = layout
            return
        }
        //一些回调
        if(isAction){
            if(x < -UIUtils.dip2px(context,30f)){//减
                actionCallBack(false)
            }else if(x > UIUtils.dip2px(context,30f)){//加
                actionCallBack(true)
            }
        }else{
            isAction = true
            onActionTime = Date().time
        }
    }
    fun backAnimation(){
        var layout: LayoutParams = countView?.layoutParams as LayoutParams
        var valueAnimator = ValueAnimator.ofFloat(layout.leftMargin.toFloat(), UIUtils.dip2px(context,30f).toFloat())
        valueAnimator.duration = ANIMATION_DURATION
        valueAnimator.interpolator = AccelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            var layout: LayoutParams = countView?.layoutParams as LayoutParams
            layout.leftMargin = (animation.animatedValue as Float).toInt()
            countView?.layoutParams = layout
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                actionBack?.onResult(amount)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
        valueAnimator.start()
    }
    fun actionCallBack(isAdd: Boolean){
        var now =  Date().time
        if(now - onActionTime > ONCE_TIME){
            if(isAdd){
                amount ++
                amount_tv?.text = amount.toString()
                actionBack?.onAddAction()
            }else{
                if(amount > 1)
                    amount --
                amount_tv?.text = amount.toString()
                actionBack?.onSubAction()
            }
            onActionTime = now
        }
    }
    fun setOnActionBackListener(onActionBack: OnActionBack){
        actionBack = onActionBack
    }
    interface OnActionBack{
        fun onAddAction()//加事件
        fun onSubAction()//减事件
        fun onResult(amount: Int)//最终回弹后回调事件
    }
}
