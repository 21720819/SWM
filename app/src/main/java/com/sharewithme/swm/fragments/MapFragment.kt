package com.sharewithme.swm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sharewithme.swm.R
import kotlin.text.split as textSplit

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        val user = Firebase.auth.currentUser
        //val db = Firebase.firestore
        val schoolEmail = user!!.email.toString()
        val splitArray = schoolEmail.textSplit("@")
        if (splitArray[1].equals("ynu.ac.kr")  || splitArray[1].equals("yu.ac.kr")) {
            val yeoungnam = LatLng(35.836223, 128.752913)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(yeoungnam))
            googleMap?.moveCamera(CameraUpdateFactory.zoomTo(15f))

            val marker = MarkerOptions()
                .position(yeoungnam)
                .title("정문")
                .snippet("영남대입니다.")
            googleMap?.addMarker(marker)
        }
        if (splitArray[1].equals("knu.ac.kr")) {
            val keungbok = LatLng(35.885202, 128.614638)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(keungbok))
            googleMap?.moveCamera(CameraUpdateFactory.zoomTo(15f))

            val marker = MarkerOptions()
                .position(keungbok)
                .title("정문")
                .snippet("경북대입니다.")
            googleMap?.addMarker(marker)
        }
        if (splitArray[1].equals("kmu.ac.kr")) {
            val keimyung = LatLng(35.852497, 128.487125)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(keimyung))
            googleMap?.moveCamera(CameraUpdateFactory.zoomTo(15f))

            val marker = MarkerOptions()
                .position(keimyung)
                .title("정문")
                .snippet("계명대입니다.")
            googleMap?.addMarker(marker)
        }
        if (splitArray[1].equals("cu.ac.kr")){
            val daega = LatLng(35.907751, 128.811937)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(daega))
            googleMap?.moveCamera(CameraUpdateFactory.zoomTo(15f))

            val marker = MarkerOptions()
                .position(daega)
                .title("정문")
                .snippet("대구카톨릭대입니다.")
            googleMap?.addMarker(marker)
        }
        if (splitArray[1].equals("daegu.ac.kr")){
            val daegu = LatLng(35.897312, 128.848217)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(daegu))
            googleMap?.moveCamera(CameraUpdateFactory.zoomTo(15f))

            val marker = MarkerOptions()
                .position(daegu)
                .title("정문")
                .snippet("대구대입니다.")
            googleMap?.addMarker(marker)
        }
    }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }
}