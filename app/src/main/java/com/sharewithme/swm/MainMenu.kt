package com.sharewithme.swm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class MainMenu : AppCompatActivity() {
    private val user = Firebase.auth.currentUser
    private val db = Firebase.firestore
    private val docRef = db.collection("users").document(user!!.email.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
//여기서 접속날짜 업데이트
        docRef
            .update("latestDate", SimpleDateFormat("yyyy. MM. dd").format(System.currentTimeMillis()))

    }
}