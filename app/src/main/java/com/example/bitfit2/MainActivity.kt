package com.example.bitfit2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputBinding
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    //private val calories = mutableListOf<Calories>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(FeedFragment())
        handleNewEntry()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.feedTab -> fragment = FeedFragment()
                R.id.dashboardTab -> fragment = DashboardFragment()
            }
            Log.i("Fragment Checker", fragment.id.toString())
            replaceFragment(fragment)
            handleNewEntry()
            true
        }
    }

    private fun replaceFragment(newFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, newFragment)
        fragmentTransaction.commit()
    }

    private fun handleNewEntry(){
        val calorie = intent.getSerializableExtra("ENTRY_EXTRA")
        //check if the EXTRA exists:
        if (calorie != null) {
            Log.d("ListActivity", "got an extra")
            Log.d("ListActivity", (calorie as Calories).toString())
            //since there is an EXTRA, let's add it to the database
            lifecycleScope.launch(Dispatchers.IO) {
                (application as BitFitApplication).db.caloriesDao().insert(
                    CaloriesEntity(
                        name = calorie.name,
                        amount = calorie.amount
                    )
                )
            }
            intent.removeExtra("ENTRY_EXTRA")
        }
        else{
            //no EXTRA, so we don't need to do anything
            Log.d("ListActivity", "no extra")
        }

        var addCaloriesButtonView = findViewById<Button>(R.id.newItem_button)
        addCaloriesButtonView.setOnClickListener {
            Log.d("ListActivity", "add new food type clicked")
            val intent = Intent(this, EntryActivity::class.java)
            this.startActivity(intent)
        }
    }
}


