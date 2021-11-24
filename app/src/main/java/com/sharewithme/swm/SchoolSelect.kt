package com.sharewithme.swm

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_school_select.*

class SchoolSelect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_select)

        val items = arrayOf("경북대학교","계명대학교","대구가톨릭대학교","대구대학교","영남대학교")
        var emails = arrayOf("")
        var email:String = ""

        val schoolAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        sp_SchoolSelect.adapter = schoolAdapter

        sp_SchoolSelect.prompt = "학교를 선택하세요"

        sp_SchoolSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                val selectedSchool = items.get(pos)

                //tv_SchoolSearch_subtitle.text = selectedSchool
                when(selectedSchool) {
                    "경북대학교" -> emails = arrayOf("경대")
                    "계명대학교" -> emails = arrayOf("계대")
                    "대구가톨릭대학교" -> emails = arrayOf("대가대")
                    "대구대학교" -> emails = arrayOf("대구대")
                    "영남대학교" -> emails = arrayOf("ynu.ac.kr","yu.ac.kr")
                }

                var emailAdapter = ArrayAdapter(this@SchoolSelect, android.R.layout.simple_spinner_dropdown_item, emails)
                sp_SchoolSelect_email.adapter = emailAdapter

                sp_SchoolSelect_email.prompt = "학교 이메일을 선택하세요"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }

        }
        /*
        when(sp_SchoolSearch.selectedItem.toString()) {
            "경북대학교" -> emails = arrayOf("경대")
            "계명대학교" -> emails = arrayOf("계대")
            "대구가톨릭대학교" -> emails = arrayOf("대가대")
            "대구대학교" -> emails = arrayOf("대구대")
            "영남대학교" -> emails = arrayOf("ynu.ac.kr","yu.ac.kr")
        }

        val emailAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, emails)
        sp_SchoolSearch_email.adapter = emailAdapter

        sp_SchoolSearch_email.prompt = "학교 이메일을 선택하세요"
        */

        et_SchoolSelect_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(et_SchoolSelect_email.text.toString() == "") {
                    btn_SchoolSelect.visibility = View.INVISIBLE
                } else {
                    btn_SchoolSelect.visibility = View.VISIBLE
                }
            }

        })


        // 다음 버튼
        btn_SchoolSelect.setOnClickListener { // 버튼 클릭시 할 행동
            email = "${et_SchoolSelect_email.text.toString()}@${sp_SchoolSelect_email.selectedItem.toString()}"

            val intent = Intent(this, Info::class.java) // 인텐트를 생성
            intent.putExtra("email", email)
            startActivity(intent) // 화면 전환하기
            //finish()
        }

        bg_SchoolSelect.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_SchoolSelect_email.windowToken, 0)
        }
    }

}
