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
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logoutButton = view.findViewById(R.id.button_logout)
        auth = FirebaseAuth.getInstance()

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(getActivity(), MainActivity::class.java))
        }
    }

}