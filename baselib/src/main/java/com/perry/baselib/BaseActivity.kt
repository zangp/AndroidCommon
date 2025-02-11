package com.perry.baselib

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @Author peng.zang
 * @Date 2023/12/15 17:18
 * @Description
 * @Version 1.0
 **/
abstract class BaseActivity<VB : ViewDataBinding>(@LayoutRes layoutId: Int = -1) : AppCompatActivity(layoutId) {
    val TAG: String = this::class.java.simpleName

    var binding: VB? = null

    override fun setContentView(layoutResID: Int) {
        val root = layoutInflater.inflate(layoutResID, null)
        setContentView(root)
        binding = DataBindingUtil.bind(root)
        afterSetContentView()
    }

    open fun afterSetContentView() {

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}