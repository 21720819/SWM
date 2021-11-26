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

        /*
        if(intent.hasExtra("id")) {
            textView.text = "${intent.getStringExtra("id")} 로그인 성공"
        }
        */

        /*
        val user = Firebase.auth.currentUser

        if (user != null) {
            // User is signed in
            textView.text = user.email
        } else {
            // No user is signed in
        }
        */

        // 여기서 부터
        /*
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        val docRef = db.collection("users").document(user!!.email.toString())

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    textView.text = "${document["nickname"]}님의 메인화면"
                } else {
                    //Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                //Log.d(TAG, "get failed with ", exception)
            }

        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut();

            val intent = Intent(this, MainActivity::class.java) // 인텐트를 생성
            startActivity(intent) // 화면 전환하기
            finish()
        }
        */
    }
}