package com.unicauca.netnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class IniciarSesionActivity : AppCompatActivity() {

    //Botones
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    //Cajas de texto
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var auth: FirebaseAuth //Autenticacion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion) //Asigna el layout

        auth = FirebaseAuth.getInstance() //Autenticacion

        //Asignacion
        loginButton = findViewById(R.id.iniciar_iniciar_button)
        registerButton = findViewById(R.id.iniciar_registrarse_button)
        emailEditText = findViewById(R.id.iniciar_correo_editText)
        passwordEditText = findViewById(R.id.iniciar_contrasena_editText)

        //Listener botones
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrarseActivity::class.java)) //Direge a la pantalla de registro
        }
        loginButton.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        //Condiciones de ingreso
        if (emailEditText.text.toString().isEmpty()) {
            emailEditText.error = resources.getString(R.string.error_ingresarCorreo)
            emailEditText.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
            emailEditText.error = resources.getString(R.string.error_ingresarCorreoInvalido)
            emailEditText.requestFocus()
            return
        }
        if (passwordEditText.text.toString().isEmpty()) {
            passwordEditText.error = resources.getString(R.string.error_ingresarContrasena)
            passwordEditText.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString()) //Inicio de sesion con email
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser //Carga el usuario actual
                        updateUI(user)
                    } else {
                        updateUI(null)
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