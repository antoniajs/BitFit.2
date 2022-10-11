package com.example.bitfit2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max


class DashboardFragment : Fragment() {

    private lateinit var avgView: TextView
    private lateinit var maxView: TextView
    private lateinit var minView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        avgView = view.findViewById(R.id.avgTextview)
        minView = view.findViewById(R.id.minTextview)
        maxView = view.findViewById(R.id.maxTextview)

        //using Flow, update the recycler view whenever the db is updated
        //taken from the provided lab 5 code
        lifecycleScope.launch {
            (activity?.application as BitFitApplication).db.caloriesDao().getAll().collect{databaseList ->
                databaseList.map { entity ->
                    Calories(
                        entity.name,
                        entity.amount
                    )
                }.also {mappedList ->
                    update(mappedList)
                }
            }
        }

        var clearbuttonView : Button = view.findViewById<Button>(R.id.clearButton)
        clearbuttonView.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                (activity?.application as BitFitApplication).db.caloriesDao().deleteAll()
            }
        }

        return view
    }

    private fun update(calories: List<Calories>) {
        if (calories.isEmpty()) {
            avgView.text = "No Data"
            minView.text = "No Data"
            maxView.text = " No Data"
            return
        }

        var min : Long = Long.MAX_VALUE
        var max : Long = Long.MIN_VALUE
        var sum : Long = 0

        for (calories in calories) {
            calories.amount?.let {
                sum += it
                if (it < min) min = it
                if (it > max) max = it
            }
        }

        avgView.text = (sum/calories.size).toString()
        minView.text = min.toString()
        maxView.text = max.toString()
    }




}