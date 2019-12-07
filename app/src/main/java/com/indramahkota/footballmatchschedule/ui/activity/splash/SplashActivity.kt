package com.indramahkota.footballmatchschedule.ui.activity.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.indramahkota.footballmatchschedule.ui.activity.main.MainActivity
import org.jetbrains.anko.intentFor

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(intentFor<MainActivity>())
        finish()
    }
}