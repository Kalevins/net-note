package com.unicauca.netnote

import com.google.firebase.database.FirebaseDatabase

class MyFirebase : android.app.Application() {

    override fun onCreate()
    {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}