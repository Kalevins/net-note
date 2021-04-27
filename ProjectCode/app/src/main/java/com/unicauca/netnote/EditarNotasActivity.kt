package com.unicauca.netnote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_editar_notas.*
import kotlinx.android.synthetic.main.activity_editar_notas.recyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import models.Document

class EditarNotasActivity : AppCompatActivity() {

    //Botones
    //private lateinit var textView: TextView
    private lateinit var documentID: String
    private var auth: FirebaseAuth = FirebaseAuth.getInstance() //Aunteticacion
    private var database: FirebaseDatabase = Firebase.database //Base de datos (Realtime Database)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_notas) //Asigna el layout

        val titleDocument = intent.getStringExtra("Titulo")
        val contentDocument = intent.getStringArrayListExtra("Contenido")
        val mutableContentDocument = contentDocument?.toMutableList<String>()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = mutableContentDocument?.let { RecyAdapterEditNotas(this, it) }

        Titulo_notas.hint = titleDocument
      
        findViewById<ActionMenuItemView>(R.id.save).setOnClickListener{
            addTitle(it)
        }
        findViewById<ImageButton>(R.id.paper_image).setOnClickListener{
            deleteDocument(it)
            startActivity(Intent(this, PrincipalActivity::class.java))
        }

        documentID = obtenerDocumentID() //Obtiene ID documento actual
    }

    override fun onDestroy() {
        super.onDestroy()
        val userID = auth.currentUser?.uid //Obtiene ID usuario actual
        val path = database.getReference("/users/$userID/$documentID/") //Direccion documento actual
        path.child("title").get().addOnSuccessListener { //Revisa si hay un titulo
            if (it.value == null) { //Si es nulo
                path.removeValue() //Elimina el documento de la base de datos (RealTime DataBase)
            }
        }
    }


    private fun addTitle(view: View){

        val editText = findViewById<EditText>(R.id.Titulo_notas)
        val titulo: String
        val documento: Document

        titulo = editText.text.toString()

        val userID = auth.currentUser?.uid
        database.getReference("/users/$userID/$documentID/title/").setValue(titulo)

        Log.d("INFO",titulo)
    }
    private fun deleteDocument(view: View){
        val userID = auth.currentUser?.uid
        database.getReference("/users/$userID/$documentID/").setValue(null)

        Log.d("INFO","$documentID")
    }

    fun obtenerDocumentID(): String {
        val extras: Bundle? = intent.extras
        //Carga el documentID de la actividad PrincipalActivity
        var idDocument = extras!!.getString("documentID").toString()
        if (idDocument == "null") {
            idDocument = intent.getStringExtra("ID").toString()
        }
        return idDocument
    }
}