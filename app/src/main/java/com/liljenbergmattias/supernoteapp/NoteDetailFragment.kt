package com.liljenbergmattias.supernoteapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.liljenbergmattias.supernoteapp.databinding.FragmentNoteDetailBinding
import com.liljenbergmattias.supernoteapp.databinding.FragmentStartBinding

class NoteDetailFragment : Fragment() {



    private var _binding : FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    val model : NoteViewmodel by activityViewModels()

    var currentnote = Note()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_note_detail, container, false)
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.noteAddTitleEdittext.setText(currentnote.title)
        binding.noteContentText.setText(currentnote.notecontext)

        binding.noteSaveButton.setOnClickListener {

            currentnote.title = binding.noteAddTitleEdittext.text.toString()
            currentnote.notecontext = binding.noteContentText.text.toString()
            model.saveNote(currentnote)
            Snackbar.make(view, "Sparat anteckning!", Snackbar.LENGTH_SHORT).show()

        }


        val savestatusobserver = Observer<Boolean> {
            if (it == true)
            {

                Log.i("NOTEDEBUG", "Stäng frag " + currentnote.title)
                requireActivity().supportFragmentManager.popBackStack()

            }

        }
        model.saveNoteStatus.observe(viewLifecycleOwner, savestatusobserver)




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}