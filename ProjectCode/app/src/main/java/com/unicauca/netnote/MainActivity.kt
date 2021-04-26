package com.unicauca.netnote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth //Autenticacion
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var skipButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        //Autenticacion
        auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener {
            if(auth.currentUser != null){ //Usuario logeado
                startActivity(Intent(this, PrincipalActivity::class.java)) //Direge a la pantalla principal
            }
        }

        //Asigancion
        loginButton = findViewById(R.id.main_iniciar_button)
        registerButton = findViewById(R.id.main_registrarse_button)
        skipButton = findViewById(R.id.main_saltar_button)

        //Listener botones
        loginButton.setOnClickListener {
            startActivity(Intent(this, IniciarSesionActivity::class.java)) //Direge a la pantalla de inicio de sesion
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrarseActivity::class.java)) //Direge a la pantalla de registro
        }

        skipButton.setOnClickListener {
            startActivity(Intent(this, InformacionActivity::class.java)) //Direge a la pantalla de informacion
        }
    }
}