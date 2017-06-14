package com.mic.blinchengdemos.activity

import android.os.Bundle
import com.mic.blinchengdemos.R
import com.mic.blinchengdemos.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun initView() {
    }

    override fun initEvent() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
