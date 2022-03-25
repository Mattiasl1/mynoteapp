package com.liljenbergmattias.supernoteapp

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.marginTop
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




class NoteDetailFragment : Fragment(), PressOnBack {

    private var _binding : FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    val notelistadapter = NoteListAdapter()

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

       // postponeEnterTransition()



        binding.noteAddTitleEdittext.setText(currentnote.title)
        binding.noteContentText.setText(currentnote.notecontext)


        val titletextview = binding.noteAddTitleEdittext
        val notecontextrule = binding.noteContentText
        titletextview.setOnClickListener {
            /*
            val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(20)
            vibrator.cancel()
             */
        }

        titletextview.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        notecontextrule.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        notecontextrule.setSingleLine(false)

        val scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
        val scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)

        val homeButton = binding.noteDetailsHomeImage

        binding.noteDetailBlueColor.setOnClickListener {
            vibrateOnClick()
            notecontextrule.setTextColor(Color.parseColor("#4C86B3"))
            titletextview.setTextColor(Color.parseColor("#4C86B3"))
        }
        binding.noteDetailRedColor.setOnClickListener {
            vibrateOnClick()
            notecontextrule.setTextColor(Color.parseColor("#9E5252"))
            titletextview.setTextColor(Color.parseColor("#9E5252"))
        }
        binding.noteDetailBlackColor.setOnClickListener {
            vibrateOnClick()
            notecontextrule.setTextColor(Color.parseColor("#000000"))
            titletextview.setTextColor(Color.parseColor("#000000"))
        }



        fun pressedSave()
        {

            if (titletextview.text.isNotEmpty()){
                currentnote.title = binding.noteAddTitleEdittext.text.toString()
                currentnote.notecontext = binding.noteContentText.text.toString()

                model.saveNote(currentnote)


                /* // *****NÅGON HAR REDIGERAT NOTE*****
                 notelistadapter.startfrag.toggleEdited(key = currentnote.fbid!!)
                 currentnote.noteedited = true
                 notelistadapter.notifyDataSetChanged()
                 */


                Snackbar.make(view, "Sparat anteckning!", Snackbar.LENGTH_SHORT).show()
            } else{
                Snackbar.make(view, "Du behöver ange en titel!", Snackbar.LENGTH_SHORT).show()
            }

        }
       val savenoteimagebutton = binding.noteDetailSaveImage



        savenoteimagebutton.setOnTouchListener { v, event ->

            val action = event.action
            when(action){

                MotionEvent.ACTION_DOWN -> {
                    vibrateOnClick()

                    savenoteimagebutton.startAnimation(scaleUp)
                    pressedSave()

                }


                MotionEvent.ACTION_MOVE -> { }

                MotionEvent.ACTION_UP -> {
                    savenoteimagebutton.startAnimation(scaleDown)
                }

                MotionEvent.ACTION_CANCEL -> {

                }

                else ->{

                }
            }
            true
        }


        homeButton.setOnTouchListener { v, event ->

            val action = event.action
            when(action){

                MotionEvent.ACTION_DOWN -> {
                    vibrateOnClick()

                    homeButton.startAnimation(scaleUp)


                }


                MotionEvent.ACTION_MOVE -> { }

                MotionEvent.ACTION_UP -> {
                    homeButton.startAnimation(scaleDown)
                    Log.i("NOTEDEBUG", "Stäng frag " + currentnote.title)
                    requireActivity().supportFragmentManager.popBackStack()
                }

                MotionEvent.ACTION_CANCEL -> {
                    homeButton.startAnimation(scaleDown)

                }

                else ->{

                }
            }
            true
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

    fun vibrateOnClick() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





    override fun pressBack(): Boolean {

        Log.i("NOTEDEBUG", "Back pressed, note not edited")


        notelistadapter.notifyDataSetChanged()

            return true

    }


}