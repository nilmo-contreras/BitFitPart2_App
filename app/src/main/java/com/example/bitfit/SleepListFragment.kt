package com.example.bitfit

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class SleepListFragment : Fragment() {
    private val sleeps = mutableListOf<DisplaySleep>()
    private lateinit var sleepRecyclerView: RecyclerView
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
        val view = inflater.inflate(R.layout.fragment_sleep_list, container, false)

        sleepRecyclerView = view.findViewById(R.id.rv_previous)
        val sleepAdapter = SleepAdapter(view.context, sleeps)
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

        sleepRecyclerView.layoutManager = LinearLayoutManager(context).also {
            val dividerItemDecoration = DividerItemDecoration(context, it.orientation)
            sleepRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        return view
    }

    companion object {
        fun newInstance(): SleepListFragment {
            return SleepListFragment()
        }
    }
}