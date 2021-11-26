package com.sharewithme.swm

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_school_select.*
import kotlinx.android.synthetic.main.activity_success.*
import java.text.SimpleDateFormat

class Info : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        auth = Firebase.auth
        val db = Firebase.firestore

        var pw:String = ""
        var pwCheck:String = ""
        var isNameFilled:Boolean = false
        var isNickChecked:Boolean = false
        var isPwSame:Boolean = false

        // 이름
        et_Info_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(et_Info_name.length() > 0) {
                    isNameFilled = true
                    tv_Info_nameDblCheck.text = ""
                } else {
                    isNameFilled = false
                    // 안내 메세지
                    tv_Info_nameDblCheck.text = " 이름을 입력해주세요."
                    tv_Info_nameDblCheck.setTextColor(Color.RED)
                }

                // 다 true면 다음버튼 활성화
                if(isNameFilled && isNickChecked && isPwSame) {
                    btn_Info_next.visibility = View.VISIBLE
                } else {
                    btn_Info_next.visibility = View.INVISIBLE
                }
            }
        })

        // 닉넴
        et_Info_nick.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isNickChecked = false

                btn_Info_next.visibility = View.INVISIBLE
            }
        })

        // 중복확인
        btn_Info_dblCheck.setOnClickListener { // 버튼 클릭시 할 행동
            val db = Firebase.firestore
            var nicks = mutableListOf("")
            val nick = et_Info_nick.text.toString()

            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        //Log.d(TAG, "${document.id} => ${document.data}")
                        nicks.add("${document["nickname"]}")

                    }

                    for(i in 1..nicks.size) {
                        if(i != nicks.size) {
                            if(nicks.get(i).equals(nick)) {
                                btn_Info_next.visibility = View.INVISIBLE

                                tv_Info_nickDblCheck.text = " 사용할 수 없는 닉네임입니다."
                                tv_Info_nickDblCheck.setTextColor(Color.RED)
                                break
                            }
                        } else {
                            if (nick.length == 0) {
                                tv_Info_nickDblCheck.text = " 닉네임을 입력해주세요."
                                tv_Info_nickDblCheck.setTextColor(Color.RED)
                            } else {
                                isNickChecked = true

                                tv_Info_nickDblCheck.text = " 사용 가능한 닉네임입니다."
                                tv_Info_nickDblCheck.setTextColor(Color.parseColor("#4311C6"))

                                // 다 true면 다음버튼 활성화
                                if(isNameFilled && isNickChecked && isPwSame) {
                                    btn_Info_next.visibility = View.VISIBLE
                                } else {
                                    btn_Info_next.visibility = View.INVISIBLE
                                }
                            }
                        }

                    }
                }
                .addOnFailureListener { exception ->
                    //Log.d(TAG, "Error getting documents: ", exception)
                    Toast.makeText(applicationContext, "실패", Toast.LENGTH_LONG).show()
                }

            //[빨강]다시 입력하세요
            //tv_school_alarm_jh.text = " 비밀번호가 일치하지 않아요"
            //tv_school_alarm_jh.setTextColor(Color.RED)
        }


        // 비번
        et_Info_pw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pw = et_Info_pw.text.toString()
                pwCheck = et_Info_pwCheck.text.toString()

                if(pwCheck.length > 0 && pw.length > 0 && pwCheck.equals(pw)) {
                    isPwSame = true

                    tv_Info_pwDblCheck.text = " 비밀번호가 일치합니다."
                    tv_Info_pwDblCheck.setTextColor(Color.parseColor("#4311C6"))

                    // 다 true면 다음버튼 활성화
                    if(isNameFilled && isNickChecked && isPwSame) {
                        btn_Info_next.visibility = View.VISIBLE
                    }
                }
                else {
                    btn_Info_next.visibility = View.INVISIBLE

                    tv_Info_pwDblCheck.text = " 비밀번호가 일치하지 않습니다."
                    tv_Info_pwDblCheck.setTextColor(Color.RED)
                }
            }

        })

        et_Info_pwCheck.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pw = et_Info_pw.text.toString()
                pwCheck = et_Info_pwCheck.text.toString()

                if (pwCheck.length > 0 && pw.length > 0 && pwCheck.equals(pw)) {
                    isPwSame = true

                    tv_Info_pwDblCheck.text = " 비밀번호가 일치합니다."
                    tv_Info_pwDblCheck.setTextColor(Color.parseColor("#4311C6"))

                    // 다 true면 다음버튼 활성화
                    if(isNameFilled && isNickChecked && isPwSame) {
                        btn_Info_next.visibility = View.VISIBLE
                    }
                }
                else {
                    btn_Info_next.visibility = View.INVISIBLE

                    tv_Info_pwDblCheck.text = " 비밀번호가 일치하지 않습니다."
                    tv_Info_pwDblCheck.setTextColor(Color.RED)
                }
            }

        })


        // 다음 버튼
        btn_Info_next.setOnClickListener { // 버튼 클릭시 할 행동
            if(intent.hasExtra("email")) {
                val email = intent.getStringExtra("email").toString()
                val password = et_Info_pwCheck.text.toString()
                val name = et_Info_name.text.toString()
                val nick = et_Info_nick.text.toString()

                // 정보 입력
                val mem = UserInfo(name, nick, SimpleDateFormat("yyyy. MM. dd").format(System.currentTimeMillis()))
                db.collection("users").document(email).set(mem)
                    //.addOnSuccessListener { Toast.makeText(applicationContext, "정보 입력 성공", Toast.LENGTH_LONG).show() }
                    .addOnFailureListener { e -> Toast.makeText(applicationContext, "정보 입력 실패", Toast.LENGTH_LONG).show() }

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        //Toast.makeText(baseContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Success::class.java) // 인텐트를 생성

                        intent.putExtra("email", email)
                        startActivity(intent) // 화면 전환하기
                        //finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }

        cl_Info.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_Info_name.windowToken, 0)
        }
    }
}