package com.perry.baselib.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.perry.baselib.app.BaseApplication
import java.lang.ref.WeakReference

object AppStackManager {
    private val activityList = ArrayDeque<WeakReference<Activity>>()
    private var isActivityVisibility = false
    private const val TAG = "AppStackManager"

    fun getTopActivityInstance(): Activity? {
        val topReference = activityList.firstOrNull()
        return if (topReference == null) {
            val reference = activityList.find { refer ->
                refer.get()?.isFinishing == false
            }
            reference?.get()
        } else {
            topReference.get()
        }
    }

    fun registerActivityLifecycle() {
        BaseApplication.get().registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityList.addFirst(WeakReference(activity))
                isActivityVisibility = false
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
                isActivityVisibility = true
                Log.d(TAG, "onActivityResumed ${activity.javaClass.simpleName}")
            }

            override fun onActivityPaused(activity: Activity) {
                Log.d(TAG, "onActivityPaused ${activity.javaClass.simpleName}")
            }

            override fun onActivityStopped(activity: Activity) {
                isActivityVisibility = false
                Log.d(TAG, "onActivityStopped ${activity.javaClass.simpleName}")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                isActivityVisibility = false
                val a = activityList.find { act ->
                    act.get() == activity
                }
                if (a?.get() != null) {
                    activityList.remove(a)
                }
            }
        })
    }

    fun containerActivity(activity: Class<out Activity>): Boolean {
        for (v in activityList) {
            if (TextUtils.equals(activity.simpleName, v.get()?.javaClass?.simpleName)) {
                return true
            }
        }
        return false
    }

    fun isActivityVisibility(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing && isActivityVisibility
    }


    fun isActivityAvailable(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing
    }

    fun getActivityList(): ArrayDeque<WeakReference<Activity>> {
        return activityList
    }

    fun finishAllActivity() {
        for (v in activityList) {
            v.get()?.finish()
        }
    }

}