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
//import kotlinx.android.synthetic.main.notas_lista.*
import models.Document

class NotasListaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        Log.d("Info","************************Esta creando el listDocument**********************")
        setupRecyclerView()

    }

    private fun setupRecyclerView(){

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    }
}