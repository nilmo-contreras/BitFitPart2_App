package com.example.bitfit

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SleepAdapter (private val context: Context, private val sleeps: List<DisplaySleep>) :
    RecyclerView.Adapter<SleepAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_sleep, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SleepAdapter.ViewHolder, position: Int) {
        val sleep = sleeps[position]
        holder.bind(sleep)
    }

    override fun getItemCount(): Int = sleeps.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val dateTextView = itemView.findViewById<TextView>(R.id.sleepDate)
        private val hoursTextView = itemView.findViewById<TextView>(R.id.sleepLength)
        private val ratingTextView = itemView.findViewById<TextView>(R.id.sleepRating)
        private val notesTextView = itemView.findViewById<TextView>(R.id.sleepNotes)

        // helper method to help set up the onBindViewHolder method
        fun bind(sleep: DisplaySleep) {
            dateTextView.text = sleep.sleepDate
            hoursTextView.text = sleep.sleepLength.toString()
            ratingTextView.text = sleep.sleepRating.toString()
            notesTextView.text = sleep.sleepNotes
        }

        override fun onClick(p0: View?) {
            Log.d("Item Clicked", p0.toString())
        }
    }
}