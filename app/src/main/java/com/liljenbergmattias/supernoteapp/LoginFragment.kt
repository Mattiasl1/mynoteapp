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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.liljenbergmattias.supernoteapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val FirstLogin = Boolean

    private val model : NoteViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_login, container, false)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val passwordControll = binding.loginPasswordedittextTWO.text.toString()
        val passwordOne = binding.loginPasswordedittext.text.toString()

        binding.loginPasswordedittextTWO.visibility = View.INVISIBLE

        fun vibrateOnClick() {
            val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(50)
            }
        }








        binding.loginButton.setOnClickListener {
            vibrateOnClick()
            val email = binding.loginEmailEdittext.text.toString()
            val password = binding.loginPasswordedittext.text.toString()
            model.login(email, password)

        }


        binding.registerButton.setOnClickListener {
            vibrateOnClick()


                val email = binding.loginEmailEdittext.text.toString()
                val password = binding.loginPasswordedittext.text.toString()
                model.signup(email, password)


        }



        val loginobserver = Observer<LoginResult> {
            Log.i("NOTEDEBUG", "LOGIN STATUS")

            if (it == LoginResult.LOGINOK)
            {
                Snackbar.make(view, "Välkommen!", Snackbar.LENGTH_SHORT).show()
            }

            if(it == LoginResult.LOGINFAIL)
            {
                Log.i("NOTEDEBUG", "LOGIN FAIL")
                Snackbar.make(view, "Felaktig inloggning", Snackbar.LENGTH_SHORT).show()
            }
            if(it == LoginResult.REGISTERFAIL)
            {
                Log.i("NOTEDEBUG", "REGISTER FAIL")
                Snackbar.make(view, "Felaktig registrering", Snackbar.LENGTH_SHORT).show()
            }


        }
        model.loginStatus.observe(viewLifecycleOwner, loginobserver)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}

