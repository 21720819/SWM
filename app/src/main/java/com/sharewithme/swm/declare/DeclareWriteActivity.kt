package com.sharewithme.swm.declare

import com.sharewithme.swm.utils.FireBaseAuth
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sharewithme.swm.R
import com.sharewithme.swm.databinding.ActivityDeclareWriteBinding
import com.sharewithme.swm.utils.FireBaseRef
import java.io.ByteArrayOutputStream

class DeclareWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDeclareWriteBinding
    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_declare_write)

        binding.writeBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FireBaseAuth.getUid()
            val key = FireBaseRef.DeclareRef.push().key.toString()

            // 파이어베이스 storage에 이미지를 저장하고 싶을 때
            // 게시글을 클릭했을 때, 게시글에 대한 정보를 받아와야 하는데
            // 이미지 이름에 대한 정보를 모르기 때문에
            // 이미지 이름을 문서의 key값으로 해줘서 이미지에 대한 정보를 찾기 쉽게 해놓음

            if(title.isNotEmpty() && content.isNotEmpty()){
                FireBaseRef.DeclareRef
                    .child(key)
                    .setValue(DeclareModel(title,content,uid))

                if(isImageUpload) {
                    imageUpload(key)
                }
                Toast.makeText(baseContext, "작성 완료", Toast.LENGTH_SHORT).show()
                finish()
            }
            else {
                Toast.makeText(baseContext, "내용 또는 제목 입력을 완료해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }

    private fun imageUpload(key : String){

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")
        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.imageArea.setImageURI(data?.data)
        }
    }
}