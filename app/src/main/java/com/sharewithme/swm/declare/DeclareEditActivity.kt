package com.sharewithme.swm.declare

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sharewithme.swm.R
import com.sharewithme.swm.databinding.ActivityDeclareEditBinding
import com.sharewithme.swm.utils.FireBaseRef
import java.io.ByteArrayOutputStream

class DeclareEditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDeclareEditBinding
    private lateinit var key:String // key값
    private lateinit var writerUid : String // uid값
    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_declare_edit)

        key = intent.getStringExtra("key").toString()
        getDeclareData(key)
        getImageData(key)

        binding.editBtn.setOnClickListener {
            editDeclareData(key)
            if(isImageUpload){
                imageUpload(key)
            }
            finish()
        }
        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.imageArea.setImageURI(data?.data)
        }
    }
    private fun editDeclareData(key : String){
        val title = binding.titleArea.text.toString()
        val content = binding.contentArea.text.toString()
        if(title.length > 0 && content.length > 0) {
            FireBaseRef.DeclareRef
                .child(key)
                .setValue(
                    DeclareModel(
                        binding.titleArea.text.toString(),
                        binding.contentArea.text.toString(),
                        writerUid
                    )
                )

            finish()
        }
        else {
            Toast.makeText(baseContext, "내용 입력을 완료해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getImageData(key : String) {

        val storageReference = Firebase.storage.reference.child("$key.png")
        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }
            else{

            }

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
    private fun getDeclareData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val dataModel = dataSnapshot.getValue(DeclareModel::class.java)

                // editText이기 때문에 setText로 해야한다
                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.content)
                writerUid = dataModel!!.uid

            }

            override fun onCancelled(error: DatabaseError) {
                // Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        FireBaseRef.DeclareRef.child(key).addValueEventListener(postListener) // uid안의 데이터를 쓰기때문에 child(key)라고 적는다
    }
}


