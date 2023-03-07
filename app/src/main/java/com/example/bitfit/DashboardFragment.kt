package com.example.bitfit

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DashboardFragment : Fragment() {
    private lateinit var application: Application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = requireActivity().application
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_dashboard, container, false)

        val inputCard: CardView = view.findViewById(R.id.cv_sleepInput)
        val hiddenGroup: Group = view.findViewById(R.id.group)
        val arrow: ImageView = view.findViewById(R.id.arrow)
        val sleepLengthEntry: SeekBar = view.findViewById(R.id.sb_length)
        val sleepRatingEntry: SeekBar = view.findViewById(R.id.sb_rating)
        val sleepNotesEntry: EditText = view.findViewById(R.id.et_notes)
        val averageSleepLength: TextView = view.findViewById(R.id.tv_sleepLengthAverage)
        val averageSleepRating: TextView = view.findViewById(R.id.tv_sleepRatingAverage)
        val saveButton: Button = view.findViewById(R.id.button_save)

        lifecycleScope.launch(Dispatchers.IO) {
            val sleepLengthAverage =
                (application as SleepApplication).db.sleepDao().getAverageLength()
            val sleepLengthFormat = "Average sleep length: %.2f hours".format(sleepLengthAverage)
            val sleepRatingAverage =
                (application as SleepApplication).db.sleepDao().getAverageRating()
            val sleepRatingFormat = "Average sleep rating: %.0f/10".format(sleepRatingAverage)
            averageSleepLength.text = sleepLengthFormat
            averageSleepRating.text = sleepRatingFormat
        }

        val date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val formattedDate = sdf.format(date)

        saveButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                Log.v("database", sleepLengthEntry.progress.toString())
                Log.v("database", formattedDate)
                Log.v("database", sleepRatingEntry.progress.toString())
                Log.v("database", sleepNotesEntry.text.toString())
                (application as SleepApplication).db.sleepDao().insert( SleepEntity(
                    sleepDate = formattedDate,
                    sleepLength = sleepLengthEntry.progress,
                    sleepRating = sleepRatingEntry.progress,
                    sleepNotes = sleepNotesEntry.text.toString()
                ))
                sleepNotesEntry.text.clear()
                val sleepAverage =
                    (application as SleepApplication).db.sleepDao().getAverageLength()
                val formatSleepAverage = "Average Sleep Duration: %.2f".format(sleepAverage)
                // averageSleepLength.text = formatSleepAverage
                val sleepRatingAverage =
                    (application as SleepApplication).db.sleepDao().getAverageRating()
                val sleepRatingFormat = "Average sleep rating: %.2f".format(sleepRatingAverage)
                // averageSleepRating.text = sleepRatingFormat
            }
        }

        arrow.setOnClickListener {
            if (hiddenGroup.isVisible) {
                TransitionManager.beginDelayedTransition(inputCard, AutoTransition())
                hiddenGroup.visibility = View.GONE
                arrow.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                TransitionManager.beginDelayedTransition(inputCard, AutoTransition())
                hiddenGroup.visibility = View.VISIBLE
                arrow.setImageResource(android.R.drawable.arrow_up_float)
            }
        }

        return view
    }

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }
}