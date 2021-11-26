package com.sharewithme.swm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main_menu.*
import java.text.SimpleDateFormat

class MainMenu : AppCompatActivity() {
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val docRef = db.collection("users").document(user!!.email.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
//여기서 접속날짜 업데이트
        docRef
            .update("latestDate", SimpleDateFormat("yyyy. MM. dd").format(System.currentTimeMillis()))

    }
}