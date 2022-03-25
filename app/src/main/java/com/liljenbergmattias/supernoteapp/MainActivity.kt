package com.liljenbergmattias.supernoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/*
ATT GÖRA LISTA:

                            **Inloggning**
                Kontrollera att email existerar?  -> validera email
                Kontrollera lösenord vid registrering?
                Återställa lösenord?
                            ******************************
                            ******************************


                ta bort rad - klar, Swipe left to delete - fråga Bill

                Kunna välja att ha checklista - fråga bill


                Reorder item
                Ny rad, alltid stor bokstav - Klar
                Respons när man klickr på en anteckning (ONpress -> vibrera) - klar


                Bättra på UI -
                anpassa anteckningar, t.ex textstorlek, färg. -




 */

interface PressOnBack {



    fun pressBack() : Boolean
}




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

        findViewById<TextView>(R.id.loginPasswordedittextTWO)

        model.loginOK.observe(this, loginObserver)


    }

    override fun onBackPressed() {
        //super.onBackPressed()
        Log.i("NOTEDEBUG", supportFragmentManager.fragments.size.toString() )

       var lastfrag = supportFragmentManager.fragments.last() as PressOnBack
       if (lastfrag.pressBack() == true)
       {
           super.onBackPressed()

       }

    }
}