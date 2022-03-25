package com.liljenbergmattias.supernoteapp


import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.SocketKeepalive
import android.os.*
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Adapter
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size

import androidx.fragment.app.activityViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.liljenbergmattias.supernoteapp.databinding.FragmentStartBinding
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import javax.security.auth.callback.Callback



class StartFragment() : Fragment(), PressOnBack {

    private var _binding : FragmentStartBinding? = null
    private val binding get() = _binding!!


    var allnotes = Note()
    val model : NoteViewmodel by activityViewModels()
    val notelistadapter = NoteListAdapter()
    var noteedited = false



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

    //@SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val mynoteRV = binding.notesRV
        mynoteRV.apply {
            binding.notesRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            binding.notesRV.adapter = notelistadapter

            edgeEffectFactory = BounceEdgeEffectFactory()

        }
   






        var simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
            DEFAULT_SWIPE_ANIMATION_DURATION)
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val database = Firebase.database.reference
                val auth = Firebase.auth
/*
                 if positionindex >= oldpositionindex
                 index -1
                 else
                 index +1
                  */


                val startPosition = viewHolder.adapterPosition
                val endposition = target.adapterPosition


                Collections.swap(model.notes.value!!, startPosition, endposition)

                notelistadapter.notifyItemMoved(startPosition, endposition)




                return true


            }



            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                //model.notes.value!!
                notelistadapter.notifyDataSetChanged()
                return
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(mynoteRV)




        val reloadImage = binding.noteStartReloadRvImage




        fun reloadlist(){
            reloadImage.animate().apply {

                duration = 500

                rotationXBy(180f)
            }.start()
            Log.i("NOTEDEBUG", "LADDA OM RECYCLERVIEW")
            model.loadNotes()
        /*
            binding.noteStartprogressBar.visibility = View.VISIBLE
            val noteobserver = Observer<List<Note>> {

                notelistadapter.notifyDataSetChanged()
            }

            model.notes.observe(viewLifecycleOwner, noteobserver)


            binding.noteStartprogressBar.visibility = View.INVISIBLE

             */
        }
        val scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
        val scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)
        val addnoteimageView = binding.startAddButtonImage
        val logOutImage = binding.noteStartLogoutImage
        fun pressaddButton()
        {




              var delaytrans = requireActivity().supportFragmentManager.beginTransaction()

                delaytrans.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                delaytrans.add((R.id.fragContainer), NoteDetailFragment())
                delaytrans.addToBackStack(null)
                delaytrans.commit()

            notelistadapter.notifyDataSetChanged()
        }

        logOutImage.setOnTouchListener { v, event ->


            val action = event.action
            when(action){

                MotionEvent.ACTION_DOWN -> {
                   vibrateOnClick()

                    logOutImage.startAnimation(scaleUp)
                    model.logout()

                }


                MotionEvent.ACTION_MOVE -> { }

                MotionEvent.ACTION_UP -> {
                    logOutImage.startAnimation(scaleDown)
                }

                MotionEvent.ACTION_CANCEL -> {

                }

                else ->{

                }
            }
            true
        }

        reloadImage.setOnTouchListener { _, event ->


            val action = event.action
            when(action){

                MotionEvent.ACTION_DOWN -> {
                    vibrateOnClick()

                    reloadImage.startAnimation(scaleUp)
                    reloadlist()

                }


                MotionEvent.ACTION_MOVE -> { }

                MotionEvent.ACTION_UP -> {
                    reloadImage.startAnimation(scaleDown)
                }

                MotionEvent.ACTION_CANCEL -> {

                }

                else ->{

                }
            }
            true
        }

        addnoteimageView.setOnTouchListener { v, event ->


            val action = event.action
            when(action){

                MotionEvent.ACTION_DOWN -> {
                    vibrateOnClick()

                    addnoteimageView.startAnimation(scaleUp)
                    pressaddButton()

                }


                MotionEvent.ACTION_MOVE -> { }

                MotionEvent.ACTION_UP -> {
                    addnoteimageView.startAnimation(scaleDown)
                }

                MotionEvent.ACTION_CANCEL -> {

                }

                else ->{

                }
            }
            true
        }




        val noteobserver = Observer<List<Note>> {

            notelistadapter.notifyDataSetChanged()
        }

        model.notes.observe(viewLifecycleOwner, noteobserver)

        model.loadNotes()






        /*
        binding.startAddButtonImage.setOnClickListener {


            notelistadapter.notifyDataSetChanged()
            requireActivity().
            supportFragmentManager.beginTransaction().
            add((R.id.fragContainer), NoteDetailFragment()).addToBackStack(null).commit()
        }
         */

        /*
        binding.addNoteButton.setOnClickListener {
            requireActivity().
            supportFragmentManager.beginTransaction().
            add((R.id.fragContainer), NoteDetailFragment()).addToBackStack(null).commit()

        }
         */




    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    fun goNote(rownotes : Note)
    {
        val notedetailfrag = NoteDetailFragment()
        notedetailfrag.currentnote = rownotes

        val goChoosenNote = requireActivity().supportFragmentManager.beginTransaction()

        goChoosenNote.setCustomAnimations(R.anim.slide_in, R.anim.scale_down)
        goChoosenNote.add((R.id.fragContainer), notedetailfrag)
        goChoosenNote.addToBackStack(null)
        goChoosenNote.commit()


    }

    fun toggleEdited(key: String)
    {
        val database = Firebase.database.reference
        val auth = Firebase.auth
        database.child("myNotesapp").child(auth.currentUser!!.uid).
        child("notese").child(key)
            notelistadapter.notifyDataSetChanged()
    }

    fun vibrateOnClick() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }



    fun bounceEdge(){
        val edgeEffectFactory = BounceEdgeEffectFactory()
    }


    fun deleterow(key : String)
    {
        val database = Firebase.database.reference
        val auth = Firebase.auth

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Radera anteckning")
        builder.setMessage("Vill du verkligen radera din anteckning?")


        builder.setPositiveButton("Radera") { dialog, which ->
            vibrateOnClick()

            database.child("myNotesapp").child(auth.currentUser!!.uid).
            child("notese").child(key).removeValue()
            requireActivity().supportFragmentManager.beginTransaction().
            add((R.id.fragContainer), StartFragment()).replace(R.id.fragContainer, StartFragment()).commit()
            notelistadapter.notifyDataSetChanged()
            Snackbar.make(requireView(), "Raderat anteckning!", Snackbar.LENGTH_SHORT).show()


        }

        builder.setNegativeButton("Ångra") { dialog, which ->
            vibrateOnClick()
            notelistadapter.notifyDataSetChanged()

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