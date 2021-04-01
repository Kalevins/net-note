package com.unicauca.netnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val initSesion: Button = findViewById(R.id.Iniciar_btn)


        initSesion.setOnClickListener(){
        setContentView(R.layout.activity_iniciar_sesion)
        }
        val regist: Button = findViewById(R.id.Registrarse_btn)

        regist.setOnClickListener(){
            setContentView(R.layout.activity_registrarse)
        }

        val saltar: Button = findViewById(R.id.Saltar_btn)

        saltar.setOnClickListener(){
            setContentView(R.layout.activity_informacion)
        }



    }


}