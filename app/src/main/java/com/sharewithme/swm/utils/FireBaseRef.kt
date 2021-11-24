package com.sharewithme.swm.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FireBaseRef {

    companion object {

        private val database = Firebase.database
        val boardRef = database.getReference("board")
        val commentRef = database.getReference("comment")
        val DeclareRef = database.getReference("declare")
    }
}