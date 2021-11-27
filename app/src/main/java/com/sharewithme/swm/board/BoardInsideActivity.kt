package com.sharewithme.swm.board

import com.sharewithme.swm.utils.FireBaseAuth
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sharewithme.swm.R
import com.sharewithme.swm.comment.CommentAdapter
import com.sharewithme.swm.comment.CommentModel
import com.sharewithme.swm.databinding.ActivityBoardInsideBinding
import com.sharewithme.swm.utils.FireBaseRef

class BoardInsideActivity  : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private var auth: FirebaseAuth? = null
    private lateinit var binding : ActivityBoardInsideBinding

    private lateinit var key:String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentAdapter
    var nickname : String = ""
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val docRef = db.collection("users").document(user!!.email.toString())
    var preCntDone:String=""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)


        auth = FirebaseAuth.getInstance()

        val user = Firebase.auth.currentUser
        val db = Firebase.firestore


        val docRef = db.collection("users").document(user!!.email.toString())
        docRef.get()
            .addOnSuccessListener { document  ->
                if (document != null)  {
                    nickname = "${document["nickname"]}"

                }
            }


        docRef.get()
            .addOnSuccessListener { document  ->
                if (document != null)  {
                    preCntDone = "${document["cntDone"]}"

                }
            }

        binding.imgSettingIcon.setOnClickListener {
            showDialog()
        }

        // key값(=UID)을 받아와서 각각의 데이터를 가져온다.
        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)


        binding.btnComment.setOnClickListener {

            if (binding.tvComment.text.toString().isEmpty()) {
                Toast.makeText(applicationContext,"댓글을 입력해 주세요.", Toast.LENGTH_LONG).show();
            }
            else {
                insertComment(key)
            }
        }

        getCommentData(key)

        commentAdapter = CommentAdapter(commentDataList)
        binding.comment.adapter = commentAdapter

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun insertComment(key : String){

        val uid = FireBaseAuth.getUid()


        FireBaseRef.commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.tvComment.text.toString(),
                    FireBaseAuth.getTime(),
                    nickname,
                    uid
                )
            )

        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        binding.tvComment.setText("")

    }

    fun getCommentData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()

                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }
                commentAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FireBaseRef.commentRef.child(key).addValueEventListener(postListener)
    }

    private fun showDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_LONG).show()

            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
            finish()
        }

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            FireBaseRef.boardRef.child(key).removeValue()

            docRef
                .update("cntDone", preCntDone.toInt()-1)
            if (preCntDone.toInt()-1 != null) {
                when(preCntDone.toInt()-1) {

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
            Toast.makeText(this, "글 삭제완료", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFireBase = binding.imgWatch

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFireBase)
            } else {

                binding.imgWatch.isVisible = false
            }
        })
    }

    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)

                    val title = binding.tvPostTitle
                    val content = binding.tvContent
                    val price = binding.tvPrice
                    val writtenTime = binding.tvWrittenTime
                    val dateTime = binding.tvDatetime
                    val nickName = binding.tvNickName
                    val place = binding.tvPlace
                    val totalNum = binding.tvTotalNum

                    val schoolname = binding.tvSchoolname // 수정

                    title.text = dataModel!!.title
                    content.text = dataModel!!.content
                    price.text = "${dataModel!!.price}원"
                    writtenTime.text = dataModel!!.time
                    dateTime.text = dataModel!!.datetime
                    nickName.text = dataModel!!.nickname
                    place.text  = dataModel!!.place
                    totalNum.text=  "전체 ${dataModel!!.totalNum}명"
                    dateTime.text = dataModel!!.datetime

                    schoolname.text = dataModel!!.schoolname // 수정

                    val myUid = FireBaseAuth.getUid()
                    val writerUid = dataModel.uid

                    if(myUid.equals(writerUid)){
                        binding.imgSettingIcon.isVisible = true
                    }

                } catch (e : Exception){

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FireBaseRef.boardRef.child(key).addValueEventListener(postListener)

    }

}