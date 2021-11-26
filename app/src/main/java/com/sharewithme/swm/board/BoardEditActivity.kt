package com.sharewithme.swm.board

import com.sharewithme.swm.utils.FireBaseAuth
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sharewithme.swm.R
import com.sharewithme.swm.databinding.ActivityBoardEditBinding
import com.sharewithme.swm.utils.FireBaseRef
import java.io.ByteArrayOutputStream

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key:String

    private lateinit var binding : ActivityBoardEditBinding
    private var isImageUpload = false

    private val TAG = BoardEditActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.imgBtnCompleted.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val totalNum = binding.etTotalNum.text.toString()
            val price = binding.etPrice.text.toString()


            if(title.isEmpty() || content.isEmpty() ||  totalNum.isEmpty() || price.isEmpty()) {
                Toast.makeText(applicationContext,"제목, 내용, 모집 인원 수, 가격은 필수로 입력하셔야 합니다.", Toast.LENGTH_LONG).show();
            }

            else {
                editBoardData(key)
                if(isImageUpload) {
                    imageUpload(key)
                }
            }

        }
        binding.imgInput.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.imgInput.setImageURI(data?.data)
        }

    }


    private fun imageUpload(key : String){

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".png")

        val imageView = binding.imgInput
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            //업로드 실패시
        }.addOnSuccessListener { taskSnapshot ->

        }
    }

    private fun editBoardData(key : String){
        //title,content,datetime,place,nickName,time,numberOfPeople,price,uid

        var place =  binding.etPlace.text.toString()
        var dateTime = binding.etDateTime.text.toString()

        if(place.isEmpty() ){
            place = "무관"
        }

        if(dateTime.isEmpty() ){
            dateTime = "무관"
        }


        FireBaseRef.boardRef
            .child(key)
            .setValue(
                BoardModel(
                    binding.etTitle.text.toString(),
                    binding.etContent.text.toString(),
                    dateTime,
                    place,
                    "Hello",
                    FireBaseAuth.getTime(),
                    binding.etTotalNum.text.toString(),
                    binding.etPrice.text.toString(),
                    FireBaseAuth.getUid()
                )
            )
        Toast.makeText(this, "글 수정 완료", Toast.LENGTH_LONG).show()

        finish()
    }

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFireBase = binding.imgInput

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFireBase)
            }
        })

    }

    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(BoardModel::class.java)

                binding.etTitle.setText(dataModel?.title)
                binding.etContent.setText(dataModel?.content)
                binding.etDateTime.setText(dataModel?.datetime)
                binding.etPlace.setText(dataModel?.place)
                binding.etTotalNum.setText(dataModel?.totalNum)
                binding.etPrice.setText(dataModel?.price)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        FireBaseRef.boardRef.child(key).addValueEventListener(postListener)

    }

}