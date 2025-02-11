package com.perry.androidcommon

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.perry.androidcommon.databinding.ActivityMainBinding
import com.perry.baselib.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
}