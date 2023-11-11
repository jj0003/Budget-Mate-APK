package com.janstudios.budgit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var transactions : ArrayList<Transaction>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactions = arrayListOf(
            Transaction("Pay", 400.00))



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Handle home navigation
                    true
                }
                R.id.nav_savings -> {
                    // Handle favorites navigation
                    true
                }
                R.id.nav_wallet -> {
                    // Handle search navigation
                    true
                }
                else -> false
            }
        }
    }



}