package com.unicauca.netnote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

class EditarFragment : Fragment() {

    private lateinit var cam_buttom: ImageButton
    private lateinit var gallery_buttom: ImageButton
    private lateinit var mic_buttom: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cam_buttom = view.findViewById(R.id.cam_image)
        gallery_buttom = view.findViewById(R.id.gallery_image)
        mic_buttom = view.findViewById(R.id.mic_image)

        cam_buttom.setOnClickListener {

        }
    }

}