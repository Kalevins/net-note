package com.unicauca.netnote

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_editar_notas.*
import kotlinx.android.synthetic.main.activity_main.*
import models.Document
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditarNotasActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_notas)

        val titleDocument = intent.getStringExtra("Titulo")
        Titulodelasnotas.hint = titleDocument

        findViewById<ActionMenuItemView>(R.id.save).setOnClickListener{
            addTitle(it)
        }

    }

    private fun addTitle(view: View){

        val editText = findViewById<EditText>(R.id.Titulodelasnotas)
        val titulo: String
        val documento: Document



        titulo = editText.text.toString()

        Log.d("INFO",titulo)
    }
}