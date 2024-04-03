package com.janstudios.budgit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.janstudios.budgit.database.BudgetDatabase
import com.janstudios.budgit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var db: BudgetDatabase

    // Called when the activity is starting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout and set the content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Navigation Controller with the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_layout) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavigationView, navController)

        // Initialize the database
        initializeDatabase()

        setupDestinationChangeListener()

    }

    private fun setupDestinationChangeListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val menu = binding.bottomNavigationView.menu
            // Reset all icons to default
            menu.findItem(R.id.homeFragment).setIcon(R.drawable.ic_home)
            menu.findItem(R.id.transactionFragment).setIcon(R.drawable.ic_bullet_list)
            menu.findItem(R.id.budgetFragment).setIcon(R.drawable.ic_savings)
            menu.findItem(R.id.settingsFragment).setIcon(R.drawable.ic_gear)

            // Set the active icon based on the destination
            when (destination.id) {
                R.id.homeFragment -> menu.findItem(R.id.homeFragment).setIcon(R.drawable.ic_home_active)
                R.id.transactionFragment -> menu.findItem(R.id.transactionFragment).setIcon(R.drawable.ic_bullet_list_active)
                R.id.budgetFragment -> menu.findItem(R.id.budgetFragment).setIcon(R.drawable.ic_pig_active)
                R.id.settingsFragment -> menu.findItem(R.id.settingsFragment).setIcon(R.drawable.ic_gear_active)
            }
        }
    }


    // Initialize the Room database
    private fun initializeDatabase() {
        db = BudgetDatabase.getInstance(this)
    }
}
