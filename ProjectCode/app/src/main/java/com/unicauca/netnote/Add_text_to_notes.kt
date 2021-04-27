package com.unicauca.netnote

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import models.Document
import java.text.SimpleDateFormat
import java.util.*

class Add_text_to_notes: AppCompatActivity()  {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: FirebaseDatabase = Firebase.database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("actividad","add to text to notes")
        setContentView(R.layout.add_text_to_notes)
        Log.d("actividad","add to text to notes")
        val contentDocument = intent.getStringArrayListExtra("contentdocument")
        val titulo = intent.getStringExtra("Titulo")
        Log.d("holiwi", "$contentDocument")
        val documentID = intent.getStringExtra("IDdocument").toString()
        findViewById<ActionMenuItemView>(R.id.save).setOnClickListener{
            var texto = addText(it, documentID)
            val intent = Intent(this,EditarNotasActivity::class.java)
            intent.putStringArrayListExtra("returnContent", contentDocument)
            intent.putExtra("Titulo", titulo)
            intent.putExtra("BanderaAddText", true)
            intent.putExtra("DocumentoID",documentID)
            intent.putExtra("Texto", texto)
            startActivity(intent)
        }

    }

    private fun addText(view: View, documentID: String):String{

        val editText = findViewById<EditText>(R.id.input_text)
        val texto: String
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val textID: String = "TEXT_$timeStamp"
        texto = editText.text.toString()

        val userID = auth.currentUser?.uid

        database.getReference("/users/$userID/$documentID/texts/$textID/").setValue(texto)

        Log.d("INFO texto","$texto")
        Log.d("INFO texto","$documentID")
        return "TEXT$texto"
    }

}