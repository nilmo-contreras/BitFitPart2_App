package com.example.bitfit

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bitfit.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {
    private val sleeps = mutableListOf<DisplaySleep>()
    private lateinit var sleepRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun closeKeyboard() {
            val view = this.currentFocus
            if(view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }

        val date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("MM.dd.yyyy", Locale.US)
        val formattedDate = sdf.format(date)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sleepLengthEntry: SeekBar = findViewById(R.id.sb_length)
        val sleepRatingEntry: SeekBar = findViewById(R.id.sb_rating)
        val sleepNotesEntry: EditText = findViewById(R.id.et_notes)
        val saveButton: Button = findViewById(R.id.button_save)

        saveButton.setOnClickListener {
            closeKeyboard()
            lifecycleScope.launch(IO) {
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
            }
            // Toast.makeText(this, "button press", Toast.LENGTH_SHORT).show()
            sleepNotesEntry.text.clear()
        }

        sleepRecyclerView = findViewById(R.id.rv_previous)
        val sleepAdapter = SleepAdapter(this, sleeps)
        sleepRecyclerView.adapter = sleepAdapter
        lifecycleScope.launch {
            (application as SleepApplication).db.sleepDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    DisplaySleep(
                        entity.sleepDate,
                        entity.sleepLength,
                        entity.sleepRating,
                        entity.sleepNotes
                    )
                }.also { mappedList ->
                    sleeps.clear()
                    sleeps.addAll(mappedList)
                    sleepAdapter.notifyDataSetChanged()
                }
            }
        }
        Log.d("sleeps:", sleeps.toString())

        sleepRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            sleepRecyclerView.addItemDecoration(dividerItemDecoration)
        }
    }
}