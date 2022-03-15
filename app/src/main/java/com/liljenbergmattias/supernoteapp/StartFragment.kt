package com.liljenbergmattias.supernoteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.liljenbergmattias.supernoteapp.databinding.FragmentStartBinding


class StartFragment : Fragment() {



    private var _binding : FragmentStartBinding? = null
    private val binding get() = _binding!!

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



        binding.notesRV.layoutManager = LinearLayoutManager(requireContext())
        binding.notesRV.adapter = notelistadapter




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

    /*
    fun clicksavebutton(rownumber : Int)
    {
        model.toggleEdited(rownumber)

    }
     */

    fun goNote(rownotes : Note)
    {
        val notedetailfrag = NoteDetailFragment()
        notedetailfrag.currentnote = rownotes

        requireActivity().
        supportFragmentManager.beginTransaction().
        add((R.id.fragContainer), notedetailfrag).addToBackStack(null).commit()
    }



    /*
    fun removeAt(rownotes: Note) {
        val notedetailfrag = NoteDetailFragment()
        notedetailfrag.currentnote = rownotes

        currentnote.removeAt(rownotes)

    }
     */




}