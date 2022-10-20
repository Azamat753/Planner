package com.lawlett.planner.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.AchievementModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.databinding.ActivityMainBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.StringPreference
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var headerImage: ImageView
    private lateinit var headerName: TextView
    private lateinit var headerLevel: TextView
    private val achievementViewModel by inject<AchievementViewModel>()
    private var nowLevel = 0
    private var levelId = 0
    private lateinit var analytics: FirebaseAnalytics
    var appUpdateManager: AppUpdateManager? = null
    private val UPDATE_CODE = 22

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.checkedTheme()
        loadLocale(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        analytics = Firebase.analytics
        initNavigationGraph()
        setupNavigation()
        initNavigationDrawer()
        initListeners()
        getCurrentLevel()
        changeTitleToolbar()
    }

    val listener = InstallStateUpdatedListener { installState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            popUp()
        }
    }

    private fun checkUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    appUpdateManager?.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this@MainActivity,
                        UPDATE_CODE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                    Log.e("ololo", "onSuccess:$e ")
                }
            }
        }?.addOnFailureListener {
            Log.e("ololo", "onFailure: ${it.toString()}")
        }
        appUpdateManager?.registerListener(listener)
    }

    private fun popUp() {
        val snackbar = Snackbar.make(
            findViewById(R.id.content),
            "App Update Almost Done.",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(
            "Reload"
        ) { appUpdateManager?.completeUpdate() }
        snackbar.setTextColor(Color.parseColor("#FF0000"))
        snackbar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("ololo", "onActivityResult: RESULT_OK")
            }
        }
    }

    private fun getCurrentLevel() {
        achievementViewModel.getData().observe(this, { level ->
            if (level.isNotEmpty()) {
                nowLevel = level[0].level
                levelId = level[0].id!!
                headerLevel.text =
                    getString(R.string.level) + " $nowLevel" // TODO: 04.09.2021 Найти другое решение
            } else {
                val model = AchievementModel(level = 0)
                achievementViewModel.insertData(model)
            }
        })
    }

    fun getCurrentLevelFromActivity(): Int {
        return nowLevel
    }

    @SuppressLint("InflateParams")
    private fun showCreateNameDialog() {
        val alertDialog = getDialog(R.layout.create_user_name)
        val title: TextView = alertDialog.findViewById(R.id.title)
        title.text = getString(R.string.name)
        val editText: EditText = alertDialog.findViewById(R.id.editText_create_name)
        alertDialog.findViewById<TextView>(R.id.apply_btn).setOnClickListener {
            if (editText.text.toString().trim().trim().isEmpty()) {
                showToast(getString(R.string.empty))
            } else {
                val name = editText.text.toString().trim()
                headerName.text = name
                StringPreference.getInstance(this)?.saveStringData(Constants.USER_NAME, name)
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            val image = StringPreference.getInstance(this)?.getStringData(Constants.USER_IMAGE)
            if (uri == null && image?.isNotEmpty() == true) {
                Glide.with(this).load(image).circleCrop().placeholder(R.drawable.ic_person_white)
                    .into(headerImage)
            } else {
                StringPreference.getInstance(this)
                    ?.saveStringData(Constants.USER_IMAGE, uri.toString())
            }
        }

    private fun initListeners() {
        val view: View = binding.drawerNavigation.getHeaderView(0)
        headerImage = view.findViewById(R.id.nav_header_iv)
        headerName = view.findViewById(R.id.nav_header_tv)
        headerLevel = view.findViewById(R.id.nav_header_level)

        headerImage.setOnClickListener {
            getContent.launch("image/*")
        }
        headerName.setOnClickListener {
            showCreateNameDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        setUserNameAndImage()
    }

    private fun setUserNameAndImage() {
        val image = StringPreference.getInstance(this)?.getStringData(Constants.USER_IMAGE)
        val name = StringPreference.getInstance(this)?.getStringData(Constants.USER_NAME)
        Glide.with(this).load(image).circleCrop().placeholder(R.drawable.ic_person_white)
            .into(headerImage)
        if (name!!.isNotEmpty()) {
            headerName.text = name
        } else {
            headerName.text = getString(R.string.you_name)
        }
    }

    private fun initNavigationGraph() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.itemIconTintList = null
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

    private fun changeTitleToolbar() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.progress_fragment -> {
                    binding.toolbarMain.title = getString(R.string.main)
                }
                R.id.category_fragment -> {
                    binding.toolbarMain.title = getString(R.string.categories)
                    checkUpdate()
                }
                R.id.timing_fragment -> {
                    binding.toolbarMain.title = getString(R.string.focus)
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
                    binding.toolbarMain.title = getString(R.string.stand_up)
                }
                R.id.financeFragment -> {
                    binding.toolbarMain.title = getString(R.string.finance)
                }
                R.id.timetableFragment -> {
                    binding.toolbarMain.title = getString(R.string.timetable)
                }
                R.id.dreamFragment -> {
                    binding.toolbarMain.title = getString(R.string.dream)
                }
                R.id.settingsFragment -> {
                    binding.toolbarMain.title = getString(R.string.settings)
                }
                R.id.instructionFragment -> {
                    binding.toolbarMain.title = getString(R.string.instruction)
                }
            }
        }
    }

    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(
                    R.id.splash_fragment,
                    R.id.createTasksFragment,
                    R.id.timerFragment,
                    R.id.stopwatchFragment,
                    R.id.createStandUpFragment,
                    R.id.mainCreateStandUpFragment,
                    R.id.introFragment
                )
            ) {
                binding.toolbarMain.gone()
                binding.bottomNavigation.gone()
            } else {
                binding.toolbarMain.visible()
                binding.bottomNavigation.visible()
            }
            if (destination.id in arrayOf(
                    R.id.settingsFragment,
                    R.id.timing_fragment,
                    R.id.standUpFragment,
                    R.id.financeFragment,
                    R.id.dreamFragment,
                    R.id.instructionFragment
                )
            ) {
                binding.bottomNavigation.gone()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @SuppressLint("RestrictedApi")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> navController.navigate(R.id.settingsFragment)
            R.id.nav_timing -> navController.navigate(R.id.timing_fragment)
            R.id.nav_category -> navController.navigate(R.id.category_fragment)
            R.id.nav_standup -> navController.navigate(R.id.standUpFragment)
            R.id.nav_finance -> navController.navigate(R.id.financeFragment)
            R.id.nav_dream -> navController.navigate(R.id.dreamFragment)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return false
    }
}