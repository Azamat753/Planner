package com.lawlett.planner.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.lawlett.planner.R
import com.lawlett.planner.databinding.ActivityMainBinding
import com.lawlett.planner.extensions.checkedTheme
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.loadLocale
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.standup.CreateStandUpFragment
import com.lawlett.planner.ui.standup.MainCreateStandUpFragment


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale(this)
        this.checkedTheme()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigationGraph()
        setupNavigation()
        initNavigationDrawer()
        changeTitleToolbar()
        lockProgressFragment()
    }

    private fun initNavigationGraph() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

    }

    private fun initNavigationDrawer() {
        setSupportActionBar(binding.toolbarMain)
        val actionBar = supportActionBar
        if (actionBar != null) {
            binding.drawerNavigation.getHeaderView(0)
            binding.drawerNavigation.setNavigationItemSelectedListener(this)
            actionBarDrawerToggle = ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbarMain,
                R.string.nav_open,
                R.string.nav_close
            )
            actionBarDrawerToggle.onDrawerOpened(binding.drawerLayout)
            actionBarDrawerToggle.onDrawerClosed(binding.drawerLayout)
            actionBarDrawerToggle.isDrawerIndicatorEnabled = true
            binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
            actionBarDrawerToggle.syncState()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerLayout.isOpen) {
            binding.drawerLayout.close()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        actionBarDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    private fun lockProgressFragment() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.menu.getItem(0).isEnabled = destination.id !in arrayOf(
                R.id.progress_fragment,
            )
        }
    }

    private fun changeTitleToolbar() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.progress_fragment -> {
                    binding.toolbarMain.title = getString(R.string.main)
                }
                R.id.category_fragment -> {
                    binding.toolbarMain.title = getString(R.string.tasks)
                }
                R.id.timing_fragment -> {
                    binding.toolbarMain.title = getString(R.string.timing)
                }
                R.id.events_fragment -> {
                    binding.toolbarMain.title = getString(R.string.events)
                }
                R.id.idea_fragment -> {
                    binding.toolbarMain.title = getString(R.string.ideas)
                }
                R.id.habitFragment -> {
                    binding.toolbarMain.title = getString(R.string.habit)
                }
                R.id.standUpFragment -> {
                    binding.toolbarMain.title = getString(R.string.standup)
                }
            }
        }
    }

    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(
                    R.id.splash_fragment,
                    R.id.board_fragment,
                    R.id.settingsFragment,
                    R.id.createTasksFragment,
                    R.id.timerFragment,
                    R.id.stopwatchFragment,
                    R.id.createStandUpFragment,
                    R.id.mainCreateStandUpFragment
                )
            ) {
                binding.toolbarMain.gone()
                binding.bottomNavigation.gone()
            } else {
                binding.toolbarMain.visible()
                binding.bottomNavigation.visible()
            }
            if (destination.id in arrayOf(
                    R.id.timing_fragment,
                    R.id.standUpFragment
                )
            ) {
                binding.bottomNavigation.gone()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> navController.navigate(R.id.settingsFragment)
            R.id.nav_timing -> navController.navigate(R.id.timing_fragment)
            R.id.nav_main -> navController.navigate(R.id.progress_fragment)
            R.id.nav_standup -> navController.navigate(R.id.standUpFragment)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return false
    }

}