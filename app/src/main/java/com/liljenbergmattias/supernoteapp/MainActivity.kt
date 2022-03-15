package com.liljenbergmattias.supernoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/*
ATT GÖRA LISTA:
                 ta bort rad
                kan ej spara utan en titel
                visa att anteckningen är redigerad

                Bättra på UI
                anpassa anteckningar, t.ex textstorlek, färg.



 */


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model : NoteViewmodel by viewModels()



        model.checkLogin()

        val loginObserver = Observer<Boolean> { loginStatus ->

            if(loginStatus == true)
            {

                supportFragmentManager.beginTransaction().replace(R.id.fragContainer, StartFragment()).commit()
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.fragContainer, LoginFragment()).commit()
            }
        }

        model.loginOK.observe(this, loginObserver)








    }
}