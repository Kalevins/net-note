package com.unicauca.netnote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_editar_notas.*
import kotlinx.android.synthetic.main.activity_main.*

class EditarNotasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_notas)

        val titleDocument = intent.getStringExtra("Titulo")
        Titulodelasnotas.text = titleDocument
    }
}