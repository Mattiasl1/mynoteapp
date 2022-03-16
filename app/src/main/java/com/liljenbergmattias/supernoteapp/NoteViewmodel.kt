package com.liljenbergmattias.supernoteapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

enum class LoginResult {
    LOGINOK, LOGINFAIL, REGISTERFAIL
}

enum class NoteEdited {
    EDITED, NOTEDITED
}

class NoteViewmodel : ViewModel() {
    lateinit var database: DatabaseReference


    val saveNoteStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val notes : MutableLiveData<List<Note>> by lazy {
        MutableLiveData<List<Note>>()
    }

    val loginOK: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val loginStatus: MutableLiveData<LoginResult> by lazy {
        MutableLiveData<LoginResult>()
    }



    fun checkLogin()
    {
        if (Firebase.auth.currentUser == null)
        {
            loginOK.value = false
        } else {
            loginOK.value = true
        }
    }



    fun login(email : String, password : String)
    {
        val auth = Firebase.auth

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginOK.value = true
                loginStatus.value = LoginResult.LOGINOK
            } else {
                loginStatus.value = LoginResult.LOGINFAIL

            }
            loginStatus.value = null

        }


    }

    fun signup(email : String, password : String)
    {
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password). addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginOK.value = true
                loginStatus.value = LoginResult.LOGINOK
            } else {
                loginStatus.value = LoginResult.LOGINFAIL

            }
            loginStatus.value = null

        }
    }

    fun toggleEdited(rownumber : Int) {
        var rownote = notes.value!![rownumber]

        if (rownote.noteedited == true) {
            rownote.noteedited = false
        } else {
            rownote.noteedited = true
        }
    }


    fun logout()
    {
        val auth = Firebase.auth
        auth.signOut()
        loginOK.value = false
    }

    fun saveNote(mynotes : Note)
    {
        val database = Firebase.database.reference
        val auth = Firebase.auth

        val notespath = database.child("myNotesapp").child(auth.currentUser!!.uid).
        child("notese")


        if(mynotes.fbid == null)
        {

            notespath.push().setValue(mynotes)
        } else {


            notespath.child(mynotes.fbid!!).setValue(mynotes)
        }

        saveNoteStatus.value = true
        saveNoteStatus.value = null
        loadNotes()

    }



    fun loadNotes()
    {
        val database = Firebase.database.reference
        val auth = Firebase.auth

        if(auth.currentUser == null)
        {
            return
        }


        database.child("myNotesapp").child(auth.currentUser!!.uid).
        child("notese").get().addOnSuccessListener {

            val tempnotelist = mutableListOf<Note>()
            for (snap in it.children){
                val tempnote = snap.getValue<Note>()!!
                tempnote.fbid = snap.key
                tempnotelist.add(tempnote)
            }
            notes.value = tempnotelist

        }.addOnFailureListener {

        }



    }







}