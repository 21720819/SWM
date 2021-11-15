package com.sharewithme.swm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.sharewithme.swm.R

class StoreFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val webView : WebView = view.findViewById(R.id.storeWebView)
        webView.loadUrl("https://www.google.co.kr/maps/place/%EC%98%81%EB%82%A8%EB%8C%80%ED%95%99%EA%B5%90+%EC%A0%95%EB%AC%B8/@35.8362965,128.7505607,15.96z/data=!4m5!3m4!1s0x35660c169e2cefd1:0x1cdae38e5894e57e!8m2!3d35.8360578!4d128.7529371?hl=ko")


        return view
    }

}