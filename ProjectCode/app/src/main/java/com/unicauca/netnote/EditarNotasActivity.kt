package com.unicauca.netnote

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_editar.*
import kotlinx.android.synthetic.main.fragment_home.*
import models.Document

class EditarNotasActivity : AppCompatActivity() {

    //Botones
    //private lateinit var textView: TextView
    private lateinit var documentID: String
    private var auth: FirebaseAuth = FirebaseAuth.getInstance() //Aunteticacion
    private var database: FirebaseDatabase = Firebase.database //Base de datos (Realtime Database)
    private var globalmutablecontentdocument = arrayListOf<String>()
    private var titulo: String=""
    private var globalDocID=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_notas) //Asigna el layout

        var titleDocument = intent.getStringExtra("Titulo") // Obtengo el titulo del documento desde el Homefragment onItemClick
        val contentDocument = intent.getStringArrayListExtra("Contenido") // Obtengo el contenido del documento desde el Homefragment onItemClick
        var mutableContentDocument = contentDocument?.toMutableList<String>() //convierto de Array a Mutable List de Strings
        documentID = obtenerDocumentID() //Obtiene ID documento actual

        if (contentDocument != null) {
            globalmutablecontentdocument = contentDocument // Guardo el contenido del documento en una variable global para recuperarlo en el EditarFragment

        }
        if (titleDocument != null) {
            titulo = titleDocument // Guardo el titulo del documento en una variable global para recuperarlo en el EditarFragment
        }
        if (documentID != null){
            globalDocID = documentID
        }
        var bandera = intent.getBooleanExtra("BanderaAddText",false) //obtengo la bandera de que entro al boton de editar texto
        if(bandera){
            val texto = intent.getStringExtra("Texto").toString() //obtengo el texto que se agrego en editar texto
            titleDocument = intent.getStringExtra("Titulo") //traigo de vuelta el titulo del documento para no perderlo al cambiar de actividad
            mutableContentDocument = intent.getStringArrayListExtra("returnContent")  //traigo de vuelta el contenido del documento para no perderlo al cambiar de actividad
            mutableContentDocument?.add(texto)  //agrego el ultimo texto agregado al contenido
            globalmutablecontentdocument = mutableContentDocument!! // Se actualiza la variable global para que no se pierda la info
            globalDocID = intent.getStringExtra("DocumentoID").toString()
            !bandera
            Log.d("PRUEBA", "ENTRA AL IF: $mutableContentDocument")

        }



        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = mutableContentDocument?.let { RecyAdapterEditNotas(this, it) }

        Titulo_notas.hint = titleDocument
      
        findViewById<ActionMenuItemView>(R.id.save).setOnClickListener{
            addTitle(it)
            startActivity(Intent(this, PrincipalActivity::class.java))
        }
        findViewById<ImageButton>(R.id.paper_image).setOnClickListener{
            deleteDocument(it)
            startActivity(Intent(this, PrincipalActivity::class.java))
        }


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


        titulo = editText.text.toString()
        if(titulo != "") {
            val userID = auth.currentUser?.uid
            database.getReference("/users/$userID/$documentID/title/").setValue(titulo)

            Log.d("INFO", titulo)
        }
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

    fun obtenerContent(): ArrayList<String>{
        return globalmutablecontentdocument
    }
    fun obtenerTitulo(): String{
        return titulo
    }
    fun obtenerDocuID(): String{
        return globalDocID
    }
}