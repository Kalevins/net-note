package com.unicauca.netnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class informacion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion)

        val vamos: Button=findViewById(R.id.bienvenida_button)

        vamos.setOnClickListener(){
            setContentView(R.layout.activity_principal)
        }
    }
}