package com.perry.utilslib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowInsets
import android.view.WindowManager

/**
 * @Author perry
 * @Date 2025/3/23 11:11
 * @Description
 * @Version 1.0
 **/
object ScreenUtils {

    // 获取屏幕物理总宽高，返回Point
    @JvmStatic
    fun getRealScreenSize(context: Context?): Point? {
        // 之所以不指定默认值，是为了告诉使用方，你传递了一个错误的上下文
        if (context == null) return null
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            val bounds = windowMetrics.bounds
            Point(bounds.width(), bounds.height())
        } else {
            getLegacyScreenSize(windowManager)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @Suppress("DEPRECATION")
    private fun getLegacyScreenSize(windowManager: WindowManager): Point {
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.defaultDisplay.getRealSize(point)
        } else {
            windowManager.defaultDisplay.getSize(point)
        }
        return point
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    fun getAvailableScreenHeight(context: Context?): Int {
        if (context == null) return 0
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return 0
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            val windowInsets = windowMetrics.windowInsets
            val insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars() or WindowInsets.Type.statusBars()
            )
            windowMetrics.bounds.height() - (insets.top + insets.bottom)
        } else {
            val rect = Rect()
            windowManager.defaultDisplay?.getRectSize(rect)
            rect.height()
        }
    }

    // 获取屏幕物理总高度（包含导航栏和状态栏）
    @JvmStatic
    fun getRealHeight(context: Context?): Int {
        return if (context == null) {
            0
        } else {
            getRealScreenSize(context)?.y ?: 0
        }
    }

    // 获取屏幕物理宽度（包含导航栏和状态栏）
    @JvmStatic
    fun getRealWidth(context: Context?): Int {
        return if (context == null) {
            0
        } else {
            getRealScreenSize(context)?.x ?: 0
        }
    }

    // 获取应用显示高度（不包含系统栏）
    @Suppress("DEPRECATION")
    @JvmStatic
    fun getAppHeight(context: Context?): Int {
        if (context == null) return 0
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return 0
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels
    }

    // 获取状态栏高度
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    @JvmStatic
    fun getStatusBarHeight(context: Context?): Int {
        if (context == null || context.resources == null) return 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }

    // 获取导航栏高度
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    @JvmStatic
    fun getNavigationBarHeight(context: Context?): Int {
        if (context == null || context.resources == null) return 0
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }

    // 动态计算可用高度（自动处理导航栏显示状态）
    @JvmStatic
    fun calculateAvailableHeight(context: Context?): Int {
        val totalHeight = getRealHeight(context)
        val appHeight = getAppHeight(context)
        val statusBarHeight = getStatusBarHeight(context)

        return if (totalHeight > appHeight) {
            // 导航栏可见时的可用高度
            appHeight - statusBarHeight
        } else {
            // 导航栏不可见时的可用高度
            totalHeight - statusBarHeight
        }
    }

    // dp 转成 px
    @JvmStatic
    fun dpToPx(context: Context?, dpValue: Float): Int {
        return if (context == null || context.resources == null) {
            dpValue.toInt()
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                context.resources.displayMetrics
            ).toInt()
        }
    }

    @JvmStatic
    fun dpToPx(context: Context?, dpValue: Int): Int {
        return if (context == null || context.resources == null) {
            dpValue
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpValue.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }
    }

    @JvmStatic
    fun spToPx(context: Context?, spValue: Int): Int {
        return if (context == null || context.resources == null) {
            spValue
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                spValue.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }
    }
}

/**
 * 以下为扩展方法，方便 kotlin 中进行使用
 */

fun Int.dp2px(context: Context?): Int {
    return if (context == null || context.resources == null) {
        this
    } else {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}

fun Int.sp2px(context: Context?): Int {
    return if (context == null || context.resources == null) {
        this
    } else {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}

fun Float.dp2px(context: Context?): Int {
    return if (context == null || context.resources == null) {
        this.toInt()
    } else {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            context.resources.displayMetrics
        ).toInt()
    }
}