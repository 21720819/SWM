package com.sharewithme.swm.fragments

import com.sharewithme.swm.board.BoardInsideActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sharewithme.swm.R
import com.sharewithme.swm.board.BoardListAdapter
import com.sharewithme.swm.board.BoardModel
import com.sharewithme.swm.board.BoardWriteActivity
import com.sharewithme.swm.databinding.FragmentBoardBinding
import com.sharewithme.swm.utils.FireBaseRef


class BoardFragment : Fragment() {
    private lateinit var binding : FragmentBoardBinding

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()

    private val TAG = BoardFragment::class.java.simpleName

    private lateinit var BoardListAdapter : BoardListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false)

        BoardListAdapter = BoardListAdapter(boardDataList)
        binding.boardListView.adapter = BoardListAdapter



        binding.boardListView.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key", boardKeyList[position])
            startActivity(intent)

        }

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        //하단바
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_homeFragment)
        }

        binding.declareTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_declareFragment)
        }

        binding.profileTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_profileFragment)
        }

        binding.mapTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_boardFragment_to_mapFragment)
        }

        getFBBoardData()

        return binding.root
    }

    private fun getFBBoardData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                boardKeyList.reverse()
                boardDataList.reverse()
                BoardListAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 가져오기 오류시
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FireBaseRef.boardRef.addValueEventListener(postListener)

    }


}