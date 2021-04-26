package com.unicauca.netnote

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class PrincipalActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth //Autenticacion
    private lateinit var toolbar: Toolbar //Barra superior
    private lateinit var fab: FloatingActionButton //Boton nuevo
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private var documentID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal) //Asigne el layout

        //Autenticacion
        auth = FirebaseAuth.getInstance()

        //Asignacion
        toolbar = findViewById(R.id.toolbar)
        fab = findViewById(R.id.edit_buttom)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            documentID = SimpleDateFormat("yyyyMMddHHmmss").format(Date()) //Formato unico (Fecha) del nuevo documento
            val intent = Intent(this, EditarNotasActivity::class.java) //Intent actividad EditarNotasActivity
            intent.putExtra("documentID", documentID) //Envia "documentID"
            startActivity(intent) //Inicia la actividad
        }

        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration( //Menu de navegacion
            setOf(
                R.id.nav_home,
                R.id.nav_share,
                R.id.nav_paper_bin,
                R.id.nav_logout,
                R.id.nav_creditos
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser //Usuario actual
        updateUI(currentUser)
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java)) //Direge a la pantalla de inicio
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean { //Informacion de usuario
        val navController = findNavController(R.id.nav_host_fragment) //Carga el layout

        //Autenticacion
        val user = auth.currentUser

        //Variable auxiliar
        var textView: TextView

        if (user != null) { //Si el usuario esta registrado
            //Nombre
            textView = findViewById(R.id.name_textView)
            textView.text = user.displayName
            //Correo
            textView = findViewById(R.id.email_textView)
            textView.text = user.email
        }
        else {
            //Nombre
            textView = findViewById(R.id.name_textView)
            textView.text = "NetNote"
            //Correo
            textView = findViewById(R.id.email_textView)
            textView.text = "Registrate para mas"

        }

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)

        val search = menu?.findItem(R.id.search)
        val searchView = search?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}
