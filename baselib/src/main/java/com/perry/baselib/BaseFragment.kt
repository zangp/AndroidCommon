package com.perry.baselib

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * @Author peng.zang
 * @Date 2023/12/15 17:20
 * @Description
 * @Version 1.0
 **/
abstract class BaseFragment<VB : ViewDataBinding>(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    val TAG: String = this::class.java.simpleName

    val binding: VB? by lazy { DataBindingUtil.bind(requireView()) }

    /**
     * 初始化 view
     */
    protected abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 获取数据
     */
    protected abstract fun initData()

    /**
     * 添加数据监听
     */
    protected abstract fun initObserver()

    protected open fun beforeInit() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            beforeInit()
            initView(view, savedInstanceState)
            initObserver()
            initData()
        } catch (e: Exception) {
            Log.e(TAG, "fragment init failed! e: $e")
        }
    }
}