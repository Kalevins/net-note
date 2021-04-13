package com.unicauca.netnote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.notas_lista.*
import models.Document

class NotasListaActivity : AppCompatActivity(), RecyclerAdapter.onDocumentClickListener {
    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        Log.d("Info","************************Esta creando el listDocument**********************")
        setupRecyclerView()

    }

    private fun setupRecyclerView(){

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val listDocuments = listOf( Document("Teoria Electromagnetica", 2, true, true, true, "Contenido de Teoria Electromag" ),
                Document("Aplicaciones Móviles", 2,true, true, true, "Contenido de Apli moviles" ),
                Document("Medios de transmisión", 8, true, true, true, "Contenido de medios de transmision" ))
        Log.d("Info","*******************Esta creando el listDocument")
        recyclerView.adapter = RecyclerAdapter(this, listDocuments, this)

    }

    override fun onItemClick(title: String) {
        val intent = Intent(this, EditarNotasActivity::class.java)
        intent.putExtra("Titulo", title)
        startActivity(intent)
    }
}