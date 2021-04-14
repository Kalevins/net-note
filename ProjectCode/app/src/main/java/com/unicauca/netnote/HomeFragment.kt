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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.unicauca.netnote.databinding.ActivityEditarNotasBinding.inflate
import kotlinx.android.synthetic.main.fragment_home.*
import models.Document


class HomeFragment : Fragment() , RecyclerAdapter.onDocumentClickListener{


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    private fun Intent(homeFragment: HomeFragment, java: Class<PrincipalActivity>) {



    }


    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {


        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        val listDocuments = listOf(
            Document("Teoria Electromagnetica", 2, false, true, true, "Contenido de Teoria Electromag" ),
            Document("Aplicaciones Móviles", 2,true, false, true, "Contenido de Apli moviles" ),
            Document("Medios de transmisión", 8, true, true, false, "Contenido de medios de transmision" )
        )
        Log.d("Info","*******************Esta creando el listDocument")
        recyclerView.adapter = RecyclerAdapter(itemView.context, listDocuments, this)

    }

    override fun onItemClick(title: String) {
        val intent = Intent(context, EditarNotasActivity::class.java)
        intent.putExtra("Titulo", title)
        startActivity(intent)
    }
}