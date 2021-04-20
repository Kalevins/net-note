package com.unicauca.netnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class InformacionActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth //Autenticacion
    private lateinit var loginButton: Button //Boton "Saltar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion) //Asigna el layout

        //Asignacion
        loginButton = findViewById(R.id.informacion_bienvenida_button)

        auth = FirebaseAuth.getInstance() //Autenticacion
        loginButton.setOnClickListener {
            auth.signInAnonymously() //Inicio de sesion como anonimo (Offline)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) { //Si salio bien
                        //Log.d("Info", "signInAnonymously:success")
                        val user = auth.currentUser //Carga el usuario actual
                        updateUI(user)
                    } else {
                        //Log.w("Info", "signInAnonymously:failure", task.exception)
                        updateUI(null)
                    }
                }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, PrincipalActivity::class.java)) //Direge a la pantalla principal
            finish()
        }
        else {
            Toast.makeText(baseContext, resources.getString(R.string.error_iniciar), Toast.LENGTH_SHORT).show()
        }
    }
}