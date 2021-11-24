package com.sharewithme.swm.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sharewithme.swm.databinding.FragmentDeclareBinding
import com.sharewithme.swm.R
import com.sharewithme.swm.declare.DeclareInsideActivity
import com.sharewithme.swm.declare.DeclareListAdapter
import com.sharewithme.swm.declare.DeclareModel
import com.sharewithme.swm.declare.DeclareWriteActivity
import com.sharewithme.swm.utils.FireBaseRef

// 고객센터 게시판 Fragment
class DeclareFragment : Fragment() {

    private lateinit var binding : FragmentDeclareBinding

    private val declareDataList = mutableListOf<DeclareModel>()
    private val declareKeyList = mutableListOf<String>()

    // 어댑터 이름
    private lateinit var declareAdapter : DeclareListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_declare, container, false)
        declareAdapter = DeclareListAdapter(declareDataList)
        binding.declareListView.adapter = declareAdapter
        binding.declareListView.setOnItemClickListener { parent, view, position, id ->

            // Firebase에 있는 board에 대한 데이터의 id를 기반으로 다시 데이터를 받아오는 방법
            val intent = Intent(context, DeclareInsideActivity::class.java)
            intent.putExtra("key", declareKeyList[position])
            startActivity(intent)
        }

        // 고객센터 게시판 글쓰기 화면
        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, DeclareWriteActivity::class.java)
            startActivity(intent)
        }
         binding.homeTap.setOnClickListener {
             it.findNavController().navigate(R.id.action_declareFragment_to_homeFragment)
         }
        //하단바
        binding.boardTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_declareFragment_to_boardFragment)
        }

        binding.profileTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_declareFragment_to_profileFragment)
        }

        binding.mapTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_declareFragment_to_mapFragment)
        }

        getFBDeclareData()
        return binding.root
    }

    private fun getFBDeclareData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                declareDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(DeclareModel::class.java)
                    declareDataList.add(item!!)
                    declareKeyList.add(dataModel.key.toString()) // key을 넣는 부분
                }
                // 최신글이 위로 오도록
                declareKeyList.reverse()
                declareDataList.reverse()
                // 어댑터 동기화 시키는 곳
                declareAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FireBaseRef.DeclareRef.addValueEventListener(postListener)
    }
}