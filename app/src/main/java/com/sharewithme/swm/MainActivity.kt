package com.sharewithme.swm


import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

// 메인화면
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var lastTimeBackPressed : Long = 0

    // 뷰가 생성되었을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 그릴 xml 뷰 파일 연결(설정)
        setContentView(R.layout.activity_main)
        //val intent = Intent(this, MainActivity2::class.java) // 인텐트를 생성
        //startActivity(intent)  // 화면 전환하기

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Success에서 가져옴
        //var name:String = ""
        //var nick:String = ""
        var email:String = ""

        /*
        if(intent.hasExtra("name") && intent.hasExtra("nick") && intent.hasExtra("email")) {
            name = intent.getStringExtra("name").toString()
            nick = intent.getStringExtra("nick").toString()
            email = intent.getStringExtra("email").toString()

            et_Login_id.setText(email)
        }
        */


        if(intent.hasExtra("email")) {
            email = intent.getStringExtra("email").toString()
            et_Login_id.setText(email)
        }

        sharedPreferences = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        //editor.putString("id", et_Login_id.toString())
        //editor.putString("pw", et_Login_pw.toString())
        //editor.putBoolean("autoLogin", cb_Login.isChecked)
        cb_Login.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                editor.putBoolean("autoLogin", true)
            } else {
                editor.putBoolean("autoLogin", false)
            }
            editor.commit()
            //tv_Login_q.text = sharedPreferences.getBoolean("autoLogin", false).toString()
        }



        // 로그인 버튼
        btn_Login.setOnClickListener {
            /*
            var id:String = ""

            if(intent.hasExtra("email")) {
                id = email
            } else {
                id = et_Login_id.text.toString()
            }
            */
            val id = et_Login_id.text.toString()
            val password = et_Login_pw.text.toString()

            if(id.length > 0 && password.length > 0) {
                auth.signInWithEmailAndPassword(id, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        //val db = Firebase.firestore

                        /*
                        if(intent.hasExtra("name") && intent.hasExtra("nick")) {
                            val mem = MemberInfo(name, nick)
                            if (user != null) {
                                db.collection("users").document(user.email.toString()).set(mem)
                                    .addOnSuccessListener { Toast.makeText(applicationContext, "정보 성공", Toast.LENGTH_LONG).show() }
                                    .addOnFailureListener { e -> Toast.makeText(applicationContext, "정보 실패", Toast.LENGTH_LONG).show() }
                            }
                        }
                        */


                        //updateUI(user)
                        val intent = Intent(this, MainMenu::class.java) // 인텐트를 생성
                        //intent.putExtra("id", id)
                        startActivity(intent) // 화면 전환하기
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "로그인에 실패했습니다.\n아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                        //val intent = Intent(this, Banned::class.java) // 인텐트를 생성
                        //startActivity(intent) // 화면 전환하기
                    }
                }
            } else {
                Toast.makeText(baseContext, "입력칸에 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

            //val user = Firebase.auth.currentUser

            /*
            val profileUpdates = userProfileChangeRequest {
                displayName = "et_Login_pw.text.toString()"
                photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Log.d(TAG, "User profile updated.")
                        Toast.makeText(applicationContext, "성공", Toast.LENGTH_LONG).show()
                    }
                }
            */



        }



        // 회원가입 버튼
        tv_Login_new.setOnClickListener { // 버튼 클릭시 할 행동
            val intent = Intent(this, SchoolSelect::class.java) // 인텐트를 생성
            startActivity(intent)  // 화면 전환하기
            /*
            val email = et_Login_id.text.toString()
            val password = et_Login_pw.text.toString()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
            */
        }


        //tv_Login_new.setOnClickListener { // 버튼 클릭시 할 행동
        //    val intent = Intent(this, SchoolSearch::class.java) // 인텐트를 생성
        //    startActivity(intent)  // 화면 전환하기
        //}

        bg_Login.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_Login_id.windowToken, 0)
        }

    }

    // 로그인 유지
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(sharedPreferences.getBoolean("autoLogin", false) && !intent.hasExtra("email") && currentUser != null){
            val intent = Intent(this, MainMenu::class.java) // 인텐트를 생성
            startActivity(intent) // 화면 전환하기
            finish()
        }
    }

    // 뒤로가기 2번 종료
    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed >= 1500) {
            lastTimeBackPressed = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show()
        } else {
            finish()
        }
    }

}