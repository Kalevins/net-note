package com.unicauca.netnote

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_editar_notas.*
import models.Document

class EditarNotasActivity : AppCompatActivity() {

    //Botones
    private lateinit var textView: TextView
    private lateinit var documentID: String
    private var auth: FirebaseAuth = FirebaseAuth.getInstance() //Aunteticacion
    private var database: FirebaseDatabase = Firebase.database //Base de datos (Realtime Database)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_notas) //Asigna el layout

        val titleDocument = intent.getStringExtra("Titulo")
        Titulo_notas.hint = titleDocument
      
        findViewById<ActionMenuItemView>(R.id.save).setOnClickListener{
            addTitle(it)
        }

        //Asignacion
        textView = findViewById(R.id.Contenido_notas)

        val userID = auth.currentUser?.uid //Obtiene ID usuario actual
        documentID = obtenerDocumentID() //Obtiene ID documento actual
        val path = database.getReference("/users/$userID/$documentID/") //Direccion documento actual

        addPostEventListener(path) //Obtencion valores de la base de datos (Realtime DataBase)

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

    private fun addPostEventListener(postReference: DatabaseReference) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if (dataSnapshot.getValue()!=null) {
                    val map: Map<String, Any> = dataSnapshot.getValue() as Map<String, Any>
                    val namesImages = map.keys // Vector con los nombres de las imagenes
                    val urlImages = map.values // Vector con las URL de las imagenes
                    Log.d("Info", "$namesImages")
                    textView.text = urlImages.toString()
                }
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.w("Error", "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)
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

    fun obtenerDocumentID(): String {
        val extras: Bundle? = intent.extras
        return extras!!.getString("documentID").toString() //Carga el documentID de la actividad PrincipalActivity
    }
}