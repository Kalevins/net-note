package com.unicauca.netnote

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.inflate
import android.widget.SearchView
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import models.Document


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)

    }




    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {


        recyclerView.layoutManager = LinearLayoutManager(context)
        val listDocuments = listOf(
            Document("Teoria Electromagnetica", 2, "https://raw.githubusercontent.com/kevinmuz55/NetNote/Kevin/Imagenes/Micro.png", "https://raw.githubusercontent.com/kevinmuz55/NetNote/Kevin/Imagenes/Imagen.png", "Esto es teoria de campos electromagneticos" ),
            Document("Aplicaciones Móviles", 2, "https://raw.githubusercontent.com/kevinmuz55/NetNote/Kevin/Imagenes/Micro.png", "https://raw.githubusercontent.com/kevinmuz55/NetNote/Kevin/Imagenes/Imagen.png", "Esto es desarrollo de aplicaciones moviles" ),
            Document("Medios de transmisión", 8, "https://raw.githubusercontent.com/kevinmuz55/NetNote/Kevin/Imagenes/Micro.png", "https://raw.githubusercontent.com/kevinmuz55/NetNote/Kevin/Imagenes/Imagen.png", "Esto es medios de transmision" )
        )
        Log.d("Info","*******************Esta creando el listDocument")
        recyclerView.adapter = RecyclerAdapter(itemView.context, listDocuments)

    }





}