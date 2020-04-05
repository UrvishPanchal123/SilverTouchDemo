package com.silvertouch.demo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.silvertouch.demo.R
import com.silvertouch.demo.databinding.ActivitySplashBinding
import com.silvertouch.demo.ui.BaseActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_splash)

        init()
    }

    private fun init() {
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}
