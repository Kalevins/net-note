package com.unicauca.netnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var refDatabase: DatabaseReference
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var skipButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        //Auth
        auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser?.uid

        //Database
        val firebase: FirebaseDatabase = FirebaseDatabase.getInstance()
        refDatabase = firebase.getReference("$userID")

        auth.addAuthStateListener {
            if(auth.currentUser != null){
                startActivity(Intent(this, PrincipalActivity::class.java))
            }
        }

        loginButton = findViewById(R.id.main_iniciar_button)
        registerButton = findViewById(R.id.main_registrarse_button)
        skipButton = findViewById(R.id.main_saltar_button)

        loginButton.setOnClickListener {
            startActivity(Intent(this, IniciarSesionActivity::class.java))
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrarseActivity::class.java))
        }

        skipButton.setOnClickListener {
            startActivity(Intent(this, InformacionActivity::class.java))
        }
    }
}