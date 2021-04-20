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
    lateinit var documentID: String
    var database: FirebaseDatabase = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_notas)

        val titleDocument = intent.getStringExtra("Titulo")
        Titulodelasnotas.hint = titleDocument
      
        findViewById<ActionMenuItemView>(R.id.save).setOnClickListener{
            addTitle(it)
        }

        val userID = auth.currentUser?.uid
        documentID = obtenerDocumentID()
        val imagesPath = database.getReference("/users/$userID/$documentID/")

        addPostEventListener(imagesPath)

    }

    override fun onStop() {
        super.onStop()
        val userID = auth.currentUser?.uid
        val path = database.getReference("/users/$userID/$documentID/")
        path.child("title").get().addOnSuccessListener {
            if (it.value == null) {
                path.removeValue()
            }
            //Log.i("firebase", "Got value ${it.value}")
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
                Log.w("Error", "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)
    }

    private fun addTitle(view: View){

        val editText = findViewById<EditText>(R.id.Titulodelasnotas)
        val titulo: String
        val documento: Document



        titulo = editText.text.toString()

        Log.d("INFO",titulo)
    }

    fun obtenerDocumentID(): String {
        val extras: Bundle? = intent.extras
        return extras!!.getString("documentID").toString()
    }
}