package com.liljenbergmattias.supernoteapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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


        val rownote = startfrag.model.notes.value!![position]

        holder.notetitleText.text = rownote.title



        holder.itemView.setOnClickListener {
            startfrag.goNote(rownote)

        }



    }




}

class NoteListViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    val notetitleText = view.findViewById<TextView>(R.id.noteItemTitleTextView)
    val deleteImage = view.findViewById<ImageView>(R.id.imageView)
    val noteupdated = view.findViewById<TextView>(R.id.noteUpdatedTextview)




}