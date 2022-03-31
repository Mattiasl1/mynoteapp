package com.liljenbergmattias.supernoteapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.liljenbergmattias.supernoteapp.databinding.FragmentEditNoteDetailBinding
import com.liljenbergmattias.supernoteapp.databinding.FragmentLoginBinding


class EditNoteDetailFragment : Fragment(), PressOnBack {
    private var _binding : FragmentEditNoteDetailBinding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_edit_note_detail, container, false)
        _binding = FragmentEditNoteDetailBinding.inflate(inflater, container, false)
        return binding.root


            /*
            if(email existerar på firebase)
            {
            skicka återställningslänk
            snackbar.make En återställningslänk har skickats till din email
            } else {
            snacknar.make Felaktig email, försök igen!
            }

             */


        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun vibrateOnClick() {
            val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(50)
            }
        }


        val resetEmail = binding.editTextTextEmailReset

        binding.resetPasswordButton.setOnClickListener {
            vibrateOnClick()



            if (resetEmail.text.isEmpty()) {
                Snackbar.make(requireView(), (resources.getString(R.string.EnterEmail)), Snackbar.LENGTH_SHORT).show()
            } else if (resetEmail.text.isNotEmpty()) {

                FirebaseAuth.getInstance().sendPasswordResetEmail(resetEmail.text.toString()).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Snackbar.make(requireView(), (resources.getString(R.string.ResetEmailsuccess)), Snackbar.LENGTH_SHORT).show()

                    } else {

                        Snackbar.make(requireView(), task.exception!!.message.toString(), Snackbar.LENGTH_SHORT).show()

                    }
                }
            }
    }
        /*

         */


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun pressBack(): Boolean {

        Log.i("NOTEDEBUG", "Back pressed, password not forgotten")

        return true

    }


}