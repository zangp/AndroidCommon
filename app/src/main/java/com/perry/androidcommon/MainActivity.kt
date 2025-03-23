package com.perry.androidcommon

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.perry.androidcommon.databinding.ActivityMainBinding
import com.perry.baselib.BaseActivity
import com.perry.utilslib.dp2px

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        7f.dp2px(applicationContext)
    }
}