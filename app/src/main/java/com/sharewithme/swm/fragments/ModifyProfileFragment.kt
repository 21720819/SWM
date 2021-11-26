package com.sharewithme.swm.fragments
import android.graphics.Color
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
import com.sharewithme.swm.R
import com.sharewithme.swm.databinding.FragmentModifyProfileBinding
import kotlinx.android.synthetic.main.fragment_modify_profile.*


class ModifyProfileFragment : Fragment() {
    private lateinit var binding: FragmentModifyProfileBinding

    private var auth: FirebaseAuth? = null
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val docRef = db.collection("users").document(user!!.email.toString())
    var nick: String = ""
    var nicks = mutableListOf("")
    var chS : Boolean= false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_profile, container, false)

        getNickname()

        binding.modifyDoneBtn.setOnClickListener { // 버튼 클릭시 할 행동
            chS=false
            nick = binding.EtModName.text.toString()
            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        nicks.add("${document["nickname"]}")
                    }
                    for (i in 1..nicks.size) {
                        if (i != nicks.size) {
                            if (nicks.get(i).equals(nick)) {

                                modifyTv.text = " 사용할 수 없는 닉네임입니다."
                                modifyTv.setTextColor(Color.RED)
                                break
                            }
                        } else {
                            if (nick.length == 0) {
                                modifyTv.text = " 닉네임을 입력해주세요."
                                modifyTv.setTextColor(Color.RED)
                            } else {
                                chS=true
                                docRef
                                    .update("nickname", nick)
                                it.findNavController()
                                    .navigate(R.id.action_modifyProfileFragment_to_profileFragment)
                            }
                        }

                    }
                }

        }
        binding.imageButton.setOnClickListener{
        }

       //하단바
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_modifyProfileFragment_to_homeFragment)
        }

        binding.declareTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_modifyProfileFragment_to_declareFragment)
        }

        binding.boardTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_modifyProfileFragment_to_boardFragment)
        }

        //binding.profileTap.setOnClickListener {}

        binding.mapTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_modifyProfileFragment_to_mapFragment)
        }

        return binding.root
    }



    private fun getNickname() {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val nick = "${document["nickname"]}"

                    binding.EtModName.setText(nick)
                }
            }

    }

}
