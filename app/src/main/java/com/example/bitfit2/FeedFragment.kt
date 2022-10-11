package com.example.bitfit2


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

//basically a copy of MainActivity
class FeedFragment : Fragment() {

    private val calories = mutableListOf<Calories>()
    private lateinit var rvFeed: RecyclerView
    private lateinit var addCaloriesButtonView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        //grab the recycler view
        rvFeed = view.findViewById<RecyclerView>(R.id.rvFeed)


        //grab the adapter
        val bitFitAdapter = BitFitAdapter(view.context, calories)

        //set up the recycler view
        rvFeed.adapter = bitFitAdapter
        rvFeed.layoutManager = LinearLayoutManager(view.context).also {
            val dividerItemDecoration = DividerItemDecoration(view.context, it.orientation)
            rvFeed.addItemDecoration(dividerItemDecoration)
        }

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
                    calories.clear()
                    calories.addAll(mappedList)
                    bitFitAdapter.notifyDataSetChanged()
                }
            }
        }
        return view

    }
    fun getAdapter(): BitFitAdapter{
        return rvFeed.adapter as BitFitAdapter
    }

    fun getCalories(): MutableList<Calories>{
        return calories
    }

    companion object{
        fun newInstance(): FeedFragment{
            return FeedFragment()
        }
    }

}