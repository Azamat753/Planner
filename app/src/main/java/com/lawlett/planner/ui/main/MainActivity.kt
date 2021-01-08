package com.lawlett.planner.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.lawlett.planner.R
import com.lawlett.planner.extensions.checkedTheme
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.loadLocale
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.widget.AppWidget
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        this.checkedTheme()
        loadLocale(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
        setupBottomNavigation()
        setupNavBarColor()
        setupToolbar()
        changeTitleToolbar()
        getWidgetIntent()

    }
    private fun getWidgetIntent(){
        val id = intent.getIntExtra(AppWidget.BUTTON_KEY,0)
        Log.d("pop", "getWidgetIntent $id")
        when(id){
            100 -> navController.navigate(R.id.category_fragment)
        }

    }


    private fun changeTitleToolbar() {
        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.progress_fragment -> {
                    toolbar_title.text = getString(R.string.progress)
                }
                R.id.category_fragment -> {
                    toolbar_title.text = getString(R.string.tasks)
                }
                R.id.timing_fragment -> {
                    toolbar_title.text = getString(R.string.timing)
                }
                R.id.events_fragment -> {
                    toolbar_title.text = getString(R.string.events)
                }
                R.id.idea_fragment -> {
                    toolbar_title.text = getString(R.string.ideas)
                }
            }
        }
    }

    private fun setupToolbar() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.progress_fragment,
                R.id.category_fragment,
                R.id.timing_fragment,
                R.id.events_fragment,
                R.id.idea_fragment
            )
        )
        setSupportActionBar(toolbar_main)
        toolbar_main.setupWithNavController(navController, appBarConfiguration)

        settings_view.setOnClickListener {
            navController.navigate(R.id.settingsFragment)
        }
    }

    private fun setupNavBarColor() {
        window.navigationBarColor = resources.getColor(R.color.statusBarC)
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setupWithNavController(navController)
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(
                    R.id.splash_fragment,
                    R.id.board_fragment,
                    R.id.recordIdeaFragment,
                    R.id.settingsFragment,
                    R.id.createTasksFragment,
                    R.id.timerFragment,
                    R.id.stopwatchFragment
                )
            ) {
                toolbar_main.gone()
                bottomNavigation.gone()
            } else {
                toolbar_main.visible()
                bottomNavigation.visible()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}