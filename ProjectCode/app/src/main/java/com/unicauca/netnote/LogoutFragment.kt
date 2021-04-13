package com.unicauca.netnote

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class LogoutFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        startActivity(Intent(getActivity(), MainActivity::class.java))
    }
}