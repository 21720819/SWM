package com.sharewithme.swm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Entrance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)

        Handler().postDelayed({

            startActivity(Intent(this, MainActivity::class.java))

            // close this activity
            finish()
        }, 1500)
    }
}