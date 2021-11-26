package com.sharewithme.swm.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sharewithme.swm.MainActivity
import com.sharewithme.swm.R
import com.sharewithme.swm.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    private var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        showProfile()

        binding.logoutBtn.setOnClickListener {
            logOut()
        }

        //하단바
        binding.btnMyprofileGoModifyPro.setOnClickListener{
            it.findNavController().navigate(R.id.action_profileFragment_to_modifyProfileFragment)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }

        binding.declareTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_declareFragment)
        }

        binding.boardTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_boardFragment)
        }

        //binding.profileTap.setOnClickListener {}

        binding.mapTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_profileFragment_to_mapFragment)
        }

        return binding.root
    }

    private fun logOut() {
        sharedPreferences = this.requireActivity().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()


        FirebaseAuth.getInstance().signOut();
        activity?.let {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        this.requireActivity().finish()
    }

    private fun showProfile() {
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        val docRef = db.collection("users").document(user!!.email.toString())

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    binding.userNameTv.text = "${document["nickname"]}"
                    binding.userJoinDateTv.text = "${document["signUpDate"]} 에 가입함"
                    binding.userLevelTv.text="${document["level"]}"
                    binding.userCntDoneTv.text="${document["cntDone"]}"
                    binding.userLatestDateTv.text="${document["latestDate"]}"

                } else {
                    //Log.d(TAG, "No such document")
                    binding.userNameTv.text = "데이터가 없음"
                    binding.userJoinDateTv.text = "데이터가 없음"
                }
            }
            .addOnFailureListener { exception ->
            }
    }

}