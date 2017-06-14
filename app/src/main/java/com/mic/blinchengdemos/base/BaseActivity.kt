package com.mic.blinchengdemos.base

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import com.mic.blinchengdemos.utils.KeyBoardUtils
import com.yiguo.toast.Toast

/**
 * Author: Blincheng.
 * Date: 2017/6/7.
 * Description:所有Activity的基类
 */
abstract class BaseActivity : AppCompatActivity(){
    var mActivity: Activity ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        initView()
        initEvent()
    }
    abstract fun initView()
    abstract fun initEvent()
    /**
     * 显示一个短文本提示（短）
     * */
    fun showToast(text: String){
        Toast.makeText(mActivity,text,Toast.LENGTH_SHORT).show()
    }
    /**
     * 显示一个短文本提示（长）
     * */
    fun showLongToast(text: String){
        Toast.makeText(mActivity,text,Toast.LENGTH_LONG).show()
    }
    /**
     * 显示一个加载动画
     * */
    fun showLoading(){

    }
    /**
     * 关闭加载动画
     * */
    fun stopLoading(){

    }
    //----------------------------------------键盘的处理工作start-------------------------------------
    /**
     *  清除editText的焦点
     *  @param v 焦点所在的View
     *  @param ids 输入框集合
     * */
    fun clearViewFocus(v: View, ids: IntArray){
        if(ids.isNotEmpty())
            for (id: Int in ids){
                if(id == v.id){
                    v.clearFocus()
                    break
                }
            }
    }
    /**
     * 是否触摸在指定view上面,对某个控件过滤
     * */
    fun isTouchView(views: Array<View>, ev: MotionEvent): Boolean{
        if(views.isEmpty()) return false
        var location = IntArray(2)
        for(view: View in views){
            view.getLocationOnScreen(location)
            var x = location[0]
            var y = location[1]
            if(ev.x > x && ev.x < (x + view.width) && ev.y > y && ev.y <(y + view.height))
                return true
        }
        return false
    }
    /**
     * 焦点是否在输入框上
     * */
    fun isFocusEditText(view: View, ids: IntArray): Boolean{
        return ids.any { it == view.id }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev?.action == MotionEvent.ACTION_DOWN ){
            if(filterViewByIdsForKeyBoard() != null && isTouchView(filterViewByIdsForKeyBoard()!!,ev))
                return super.dispatchTouchEvent(ev)
            if(hideKeyBoardByEditViewIds() != null && hideKeyBoardByEditViewIds()?.size != 0){
                var view: View = currentFocus
                if(isFocusEditText(view,hideKeyBoardByEditViewIds()!!)){
                    KeyBoardUtils.hideInputForce(mActivity)
                    clearViewFocus(view,hideKeyBoardByEditViewIds()!!)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
    /**
     * 如果需要对键盘做操作，可以重写这个方法，返回对应EditText即可
     * @return 返回对应的EditText的Id
     * 在Fragment中如果实现这个方法，需要在对应的Activity中去调用该接口
     * */
    fun hideKeyBoardByEditViewIds(): IntArray?{
        return null
    }
    /**
     * 当操作某个控件时不想隐藏键盘的时候，可以重写
     * @return 排除的控件数组
     * 在Fragment中如果实现这个方法，需要在对应的Activity中去调用该接口
     * */
    fun filterViewByIdsForKeyBoard(): Array<View>?{
        return null
    }
    //----------------------------------------键盘的处理工作end---------------------------------------
}