package com.sharewithme.swm.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.sharewithme.swm.R
import com.sharewithme.swm.databinding.FragmentMapBinding


class MapFragment : Fragment() {
    private lateinit var binding : FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)


        val webView = binding.mapView
        webView.loadUrl("https://www.google.co.kr/maps/place/%EC%98%81%EB%82%A8%EB%8C%80%ED%95%99%EA%B5%90+%EC%A0%95%EB%AC%B8/@35.8362965,128.7505607,15.96z/data=!4m5!3m4!1s0x35660c169e2cefd1:0x1cdae38e5894e57e!8m2!3d35.8360578!4d128.7529371?hl=ko")

        //하단바
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_homeFragment)
        }

        binding.declareTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_declareFragment)
        }

        binding.boardTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_boardFragment)
        }

        binding.profileTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_profileFragment)
        }

       // binding.mapTap.setOnClickListener{}

        return binding.root
    }

}