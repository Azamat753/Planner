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


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
//        setupToolbar()
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
                    binding.toolbarMain.title = getString(R.string.progress)
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
            }
        }
    }

    private fun setupToolbar() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.createEventFragment,
                R.id.stopwatchFragment,
                R.id.timerFragment,
                R.id.settingsFragment,
                R.id.idea_fragment
            )
        )
        setSupportActionBar(binding.toolbarMain)
        binding.toolbarMain.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(
                    R.id.splash_fragment,
                    R.id.board_fragment,
                    R.id.recordIdeaFragment,
                    R.id.settingsFragment,
                    R.id.createTasksFragment,
                    R.id.timerFragment,
                    R.id.stopwatchFragment,
                    R.id.createEventFragment
                )
            ) {
                binding.toolbarMain.gone()
                binding.bottomNavigation.gone()
            } else {
                binding.toolbarMain.visible()
                binding.bottomNavigation.visible()
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
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return false
    }
}