package com.sharewithme.swm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.sharewithme.swm.R
import com.sharewithme.swm.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

//    private val TAG = HomeFragment::class.java.simpleName
//
//    val bookmarkIdList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("HomeFragment", "onCreateView")

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)



        binding.declareTap.setOnClickListener {
            Log.d("HomeFragment", "tipTap")
            it.findNavController().navigate(R.id.action_homeFragment_to_declareFragment)
        }

        binding.talkTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment)

        }

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_storeFragment)
        }

        binding.category1.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_declareFragment)
        }

        binding.category2.setOnClickListener {

            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment)

        }

        binding.category3.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.category4.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_storeFragment)
        }

        //rvAdapter = BookmarkRVAdapter(requireContext(), items, itemKeyList, bookmarkIdList)

        //getCategoryData()

        return binding.root
    }

    /*  private fun getCategoryData(){

          val postListener = object : ValueEventListener {
              override fun onDataChange(dataSnapshot: DataSnapshot) {

                  for (dataModel in dataSnapshot.children) {

                      val item = dataModel.getValue(ContentModel::class.java)

                      items.add(item!!)
                      itemKeyList.add(dataModel.key.toString())


                  }
                  rvAdapter.notifyDataSetChanged()

              }

              override fun onCancelled(databaseError: DatabaseError) {
                  // Getting Post failed, log a message
                  Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
              }
          }*/
    //FBRef.category1.addValueEventListener(postListener)
    //FBRef.category2.addValueEventListener(postListener)



}