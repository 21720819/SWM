package com.sharewithme.swm.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sharewithme.swm.R
import com.sharewithme.swm.databinding.ActivityBoardWriteBinding
import com.sharewithme.swm.utils.FireBaseAuth
import com.sharewithme.swm.utils.FireBaseRef
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private lateinit var binding : ActivityBoardWriteBinding

    private var isImageUpload = false

    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val docRef = db.collection("users").document(user!!.email.toString())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        auth = FirebaseAuth.getInstance()

        var nickname : String = ""

        docRef.get()
            .addOnSuccessListener { document  ->
                if (document != null)  {
                    nickname = "${document["nickname"]}"

                }
            }

        var preCntDone:String=""
        docRef.get()
            .addOnSuccessListener { document  ->
                if (document != null)  {
                    preCntDone = "${document["cntDone"]}"

                }
            }


        binding.imgBtnCompleted.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            var place = binding.etPlace.text.toString()
            var datetime = binding.etDateTime.text.toString()
            val totalNum = binding.etTotalNum.text.toString()
            val price = binding.etPrice.text.toString()
            val uid = FireBaseAuth.getUid()
            val time = FireBaseAuth.getTime()



            if(title.isEmpty() || content.isEmpty() ||  totalNum.isEmpty() || price.isEmpty()) {
                Toast.makeText(applicationContext,"제목, 내용, 모집 인원 수, 가격은 필수로 입력하셔야 합니다.", Toast.LENGTH_LONG).show();
            }

            else{
//여기서 글 수 +1
                docRef
                    .update("cntDone", preCntDone.toInt()+1)

                if (preCntDone.toInt()+1 != null) {
                    when(preCntDone.toInt()+1) {

                        0, 1 -> { docRef
                            .update("level", 0)
                        }

                        in 2..5 -> { docRef
                            .update("level", 1) }
                        in 6..10 -> { docRef
                            .update("level", 2) }

                        !in 0..10 -> { docRef
                            .update("level", 3) }

                    }

                }

                if(datetime.isEmpty()) {
                    datetime = "무관"
                }

                if(place.isEmpty()) {
                    place = "무관"
                }

                val key = FireBaseRef.boardRef.push().key.toString()

                FireBaseRef.boardRef
                    .child(key)
                    .setValue(BoardModel(title,content,datetime,place,nickname,time,totalNum,price,uid))

                Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_LONG).show()

                if(isImageUpload) {
                    imageUpload(key)
                }
                finish()
            }
        }

        binding.imgInput.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }

    }

    private fun imageUpload(key : String){

        val storage = Firebase.storage
        val storageReference = storage.reference
        val mountainsReference = storageReference.child(key + ".png")

        val imageView = binding.imgInput
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsReference.putBytes(data)
        uploadTask.addOnFailureListener {
            //업로드 실패시
        }.addOnSuccessListener { taskSnapshot ->

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.imgInput.setImageURI(data?.data)
        }

    }
}