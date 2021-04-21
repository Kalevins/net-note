package com.unicauca.netnote

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class RegistrarseActivity : AppCompatActivity() {

    //Autenticacion
    private lateinit var auth: FirebaseAuth
    //Botones
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    //Cajas de texto
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse) //Carga el layout

        //Autenticacion
        auth = FirebaseAuth.getInstance()

        //Asigancion
        registerButton = findViewById(R.id.registrarse_registrarse_button)
        loginButton = findViewById(R.id.registrarse_iniciar_button)
        emailEditText = findViewById(R.id.registrarse_correo_editText)
        passwordEditText = findViewById(R.id.registrarse_contrasena_editText)
        nameEditText = findViewById(R.id.registrarse_nombre_editText)

        //Listener botones
        registerButton.setOnClickListener {
            registerUser()
        }
        loginButton.setOnClickListener {
            startActivity(Intent(this, IniciarSesionActivity::class.java)) //Direge a la pantalla de inicio de sesion
        }


    }
    private fun registerUser() {
        //Condiciones de registro
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
        if (passwordEditText.text.length <= 5) {
            passwordEditText.error = resources.getString(R.string.error_ingresarContrasenaInvalida)
            passwordEditText.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword( //Creacion usuario (Email, contrasena)
                emailEditText.text.toString(),
                passwordEditText.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser //Usuario actual
                    user?.sendEmailVerification() //Envio email de verificacion
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(baseContext, resources.getString(R.string.acierto_registrarse), Toast.LENGTH_SHORT).show()
                                //Agregar nombre
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(nameEditText.text.toString())
                                    //.setPhotoUri(Uri.parse("https://www.flaticon.com/svg/vstatic/svg/1077/1077012.svg?token=exp=1617572628~hmac=7e65ad7926dbda0c0e9141eb3ac6331a"))
                                    .build()
                                user.updateProfile(profileUpdates)

                                auth.signOut() //Cerrar sesion
                                startActivity(Intent(this, IniciarSesionActivity::class.java)) //Direge a la pantalla de inicio de sesion
                                finish()
                            }
                        }

                } else {
                    Toast.makeText(baseContext, resources.getString(R.string.error_registrarse), Toast.LENGTH_SHORT ).show()
                }
            }
    }
}