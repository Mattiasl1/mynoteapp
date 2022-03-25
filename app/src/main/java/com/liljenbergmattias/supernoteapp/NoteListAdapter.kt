package com.liljenbergmattias.supernoteapp



import android.content.Context
import android.icu.util.TimeUnit
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.util.SparseBooleanArray
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.snapshot.Index
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit


class NoteListAdapter() : RecyclerView.Adapter<NoteListViewHolder>() {

    lateinit var startfrag : StartFragment





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val vh = NoteListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false))
        return vh




    }




    override fun getItemCount(): Int {
        startfrag.model.notes.value?.let {  list ->
            return list.size



        }
        return 0
    }







    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        var rownote = startfrag.model.notes.value!![position]
        holder.notetitleText.text = rownote.title
        val rowposition = holder.adapterPosition.toString()




        val scaleUp = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_up)
        val scaleDown = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_down)



        fun deleterowatposition() {
            startfrag.deleterow(key = rownote.fbid!!)
            startfrag.model.loadNotes()
        }

        val trashCan = holder.deleteImage

        trashCan.setOnTouchListener { v, event ->
            val action = event.action
            when(action){

                MotionEvent.ACTION_DOWN -> {
                    trashCan.startAnimation(scaleUp)
                    startfrag.vibrateOnClick()
                    deleterowatposition()

                }


                MotionEvent.ACTION_MOVE -> { }

                MotionEvent.ACTION_UP -> {
                    trashCan.startAnimation(scaleDown)
                }

                MotionEvent.ACTION_CANCEL -> {

                }

                else ->{

                }
            }
            true
        }


        //holder.notetitleText.setTextColor(5)
        holder.deleteImage.setOnClickListener {
            deleterowatposition()
            Log.i("NOTEDEBUG", "detta är" + rowposition)
        }






            holder.itemView.setOnTouchListener { v, event ->

                holder.itemView.setOnClickListener {


                    startfrag.goNote(rownote)

                }


                val action = event.action
                when(action){


                    MotionEvent.ACTION_DOWN -> {
                        Log.i("adapter", "hej" + holder.adapterPosition.toString())
                        startfrag.vibrateOnClick()




                        holder.itemView.startAnimation(scaleUp)

                    }


                    MotionEvent.ACTION_MOVE ->  {

                        startfrag.bounceEdge()
                    }

                    MotionEvent.ACTION_UP -> {

                        holder.itemView.startAnimation(scaleDown)
                        startfrag.goNote(rownote)

                        notifyDataSetChanged()
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        holder.itemView.startAnimation(scaleDown)

                    }

                    else ->{

                    }
                }
                true
            }





        /*
        holder.itemView.setOnClickListener {
            it.animate().apply {
                duration = 800
                rotationXBy(360f)
            }.start()
            startfrag.goNote(rownote)
           //holder.noteItemconstraint.setBackgroundColor()
         */





          //  holder.noteupdated.visibility = View.INVISIBLE



        }



    }







class NoteListViewHolder (view: View) : RecyclerView.ViewHolder(view){

    val notetitleText = view.findViewById<TextView>(R.id.noteItemTitleTextView)
    val deleteImage = view.findViewById<ImageView>(R.id.imageView)
    val noteupdated = view.findViewById<TextView>(R.id.noteUpdatedTextview)
    val noteItemconstraint = view.findViewById<ConstraintLayout>(R.id.noteItemLayout)
    val notedate = view.findViewById<TextView>(R.id.noteDateTextView)





}
