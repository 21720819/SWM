package com.sharewithme.swm

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import kotlinx.android.synthetic.main.activity_success.*

class Success : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        //var name:String = ""
        //var nick:String = ""
        var email:String = ""

        /*
        if(intent.hasExtra("name") && intent.hasExtra("nick")) {
            name = intent.getStringExtra("name").toString()
            nick = intent.getStringExtra("nick").toString()
            email = intent.getStringExtra("email").toString()
        }
        */

        if(intent.hasExtra("email")) {
            email = intent.getStringExtra("email").toString()
        }

        val spannable = SpannableStringBuilder("회원가입에 성공했습니다!\n지금부터 쉐어윗미를 이용해보세요!")
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#4311C6")),
            19, // start
            23, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        tv_Success.setText(spannable)


        // 다음 버튼
        btn_Success.setOnClickListener { // 버튼 클릭시 할 행동
            val intent = Intent(this, MainActivity::class.java) // 인텐트를 생성
            //intent.putExtra("name", name)
            //intent.putExtra("nick", nick)
            intent.putExtra("email", email)
            startActivity(intent) // 화면 전환하기
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}