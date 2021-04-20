package com.unicauca.netnote

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        auth = FirebaseAuth.getInstance()
        textView = findViewById(R.id.Contenido_notas)

        val database = Firebase.database
        val userID = auth.currentUser?.uid
        val imagesPath = database.getReference("/users/$userID/imageURI/")

        addPostEventListener(imagesPath)

    }

    private fun addPostEventListener(postReference: DatabaseReference) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val map: Map<String, Any> = dataSnapshot.getValue() as Map<String, Any>
                val namesImages = map.keys // Vector con los nombres de las imagenes
                val urlImages = map.values // Vector con las URL de las imagenes
                Log.d("Info","$namesImages")
                textView.text = urlImages.toString()
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Error", "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)
    }
}