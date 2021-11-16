package com.sharewithme.swm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.sharewithme.swm.R
import com.sharewithme.swm.databinding.FragmentBoardBinding


class BoardFragment : Fragment() {
    private lateinit var binding : FragmentBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false)


        //하단바
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_homeFragment)
        }

        binding.declareTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_declareFragment)
        }

        //binding.boardTap.setOnClickListener {}

        binding.profileTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_profileFragment)
        }

        binding.mapTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_boardFragment_to_mapFragment)
        }

        return binding.root
    }

}