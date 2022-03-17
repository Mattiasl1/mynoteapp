package com.liljenbergmattias.supernoteapp

import android.app.AlertDialog
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.liljenbergmattias.supernoteapp.databinding.FragmentStartBinding


class StartFragment : Fragment(), PressOnBack {


    private var _binding : FragmentStartBinding? = null
    private val binding get() = _binding!!

    var allnotes = Note()

    val model : NoteViewmodel by activityViewModels()

    val notelistadapter = NoteListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notelistadapter.startfrag = this



    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_start, container, false)
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mynoteRV = binding.notesRV
        mynoteRV.apply {
            binding.notesRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            binding.notesRV.adapter = notelistadapter

            edgeEffectFactory = BounceEdgeEffectFactory()
        }






        val noteobserver = Observer<List<Note>> {

            notelistadapter.notifyDataSetChanged()
        }

        model.notes.observe(viewLifecycleOwner, noteobserver)

        model.loadNotes()



        binding.addNoteButton.setOnClickListener {
            requireActivity().
            supportFragmentManager.beginTransaction().
            add((R.id.fragContainer), NoteDetailFragment()).addToBackStack(null).commit()

        }

        binding.logOutButton.setOnClickListener {
            model.logout()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    fun goNote(rownotes : Note)
    {
        val notedetailfrag = NoteDetailFragment()
        notedetailfrag.currentnote = rownotes

        requireActivity().
        supportFragmentManager.beginTransaction().
        add((R.id.fragContainer), notedetailfrag).addToBackStack(null).commit()
    }

    fun toggleEdited(key: String)
    {
        val database = Firebase.database.reference
        val auth = Firebase.auth
        database.child("myNotesapp").child(auth.currentUser!!.uid).
        child("notese").child(key)
            notelistadapter.notifyDataSetChanged()
    }


    fun deleterow(key : String)
    {
        val database = Firebase.database.reference
        val auth = Firebase.auth

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Radera anteckning")
        builder.setMessage("Vill du verkligen radera din anteckning?")


        builder.setPositiveButton("Radera") { dialog, which ->

            database.child("myNotesapp").child(auth.currentUser!!.uid).
            child("notese").child(key).removeValue()
            requireActivity().supportFragmentManager.beginTransaction().
            add((R.id.fragContainer), StartFragment()).replace(R.id.fragContainer, StartFragment()).commit()
            notelistadapter.notifyDataSetChanged()


        }

        builder.setNegativeButton("Ångra") { dialog, which ->

        }
        builder.show()





    }

    override fun pressBack(): Boolean {
        return false
    }


    /*
    database.child("myNotesapp").child(auth.currentUser!!.uid).
        child("notese").removeValue()
     */



    /*
    fun removeAt(rownotes: Note) {
        val notedetailfrag = NoteDetailFragment()
        notedetailfrag.currentnote = rownotes

        currentnote.removeAt(rownotes)

    }
     */




}