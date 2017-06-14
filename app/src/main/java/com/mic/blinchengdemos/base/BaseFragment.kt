package com.mic.blinchengdemos.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yiguo.toast.Toast

/**
 * Author: Blincheng.
 * Date: 2017/6/7.
 * Description:
 */

abstract class BaseFragment: Fragment(){
    var mActivity: BaseActivity? = null
    var contentView: View? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater?.inflate(getLayoutId(),container,false)!!
        return contentView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = activity as BaseActivity
        initView()
        initEvent()
    }
    abstract @LayoutRes fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initEvent()
    /**
     * 显示一个短文本提示（短）
     * */
    fun showToast(text: String){
        Toast.makeText(mActivity,text, Toast.LENGTH_SHORT).show()
    }
    /**
     * 显示一个短文本提示（长）
     * */
    fun showLongToast(text: String){
        Toast.makeText(mActivity,text, Toast.LENGTH_LONG).show()
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
     * 如果需要对键盘做操作，可以重写这个方法，返回对应EditText即可
     * @return 返回对应的EditText的Id
     * */
    fun hideKeyBoardByEditViewIds(): IntArray?{
        return null
    }
    /**
     * 当操作某个控件时不想隐藏键盘的时候，可以重写
     * @return 排除的控件数组
     * */
    fun filterViewByIdsForKeyBoard(): Array<View>?{
        return null
    }
    //----------------------------------------键盘的处理工作end---------------------------------------
}
