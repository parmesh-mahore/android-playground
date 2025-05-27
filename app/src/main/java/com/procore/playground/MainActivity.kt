package com.procore.playground

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.procore.playground.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var splitInstallManager: SplitInstallManager
    private val featureProfile = "profile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        splitInstallManager = SplitInstallManagerFactory.create(this)

        binding.appBarMain.fab.setOnClickListener {
            launchDynamicFeature()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun launchDynamicFeature() {
        val intent =
            Intent().setClassName(packageName, "com.procore.profile.ProfileActivity")
        startActivity(intent)
    }

    private fun isModuleInstalled(moduleName: String): Boolean {
        return splitInstallManager.installedModules.contains(moduleName)
    }

    private fun downloadDynamicFeature(moduleName: String) {
        val request = SplitInstallRequest
            .newBuilder()
            .addModule(moduleName)
            .build()
        splitInstallManager.registerListener(installListener)
        splitInstallManager.startInstall(request).addOnSuccessListener { sessionId ->
            // Installation started successfully
            Log.d(
                "TAG-PARAM",
                "Dynamic feature module installation started with session ID: $sessionId"
            )
        }.addOnFailureListener { exception ->
            // Installation failed
            Log.e(
                "TAG-PARAM",
                "Dynamic feature module installation failed: ${exception.message}"
            )
        }
    }

    private val installListener = SplitInstallStateUpdatedListener { state ->
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                // Module installed successfully
                Log.d("TAG-PARAM", "Dynamic feature module installed")
            }

            SplitInstallSessionStatus.INSTALLING -> {
                // Module is downloading
                Log.d("TAG-PARAM", "Dynamic feature module downloading")
            }

            SplitInstallSessionStatus.INSTALLED -> {
                // Module installation failed
                Log.e(
                    "TAG-PARAM",
                    "Dynamic feature module installation failed: ${state.errorCode()}"
                )
            }

            SplitInstallSessionStatus.FAILED -> {
                // Module installation failed
                Log.e(
                    "TAG-PARAM",
                    "Dynamic feature module installation failed: ${state.errorCode()}"
                )
            }

            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                // User confirmation is required for installation
                Log.d("TAG-PARAM", "Dynamic feature module requires user confirmation")
            }

            SplitInstallSessionStatus.CANCELED -> {
                // Module installation was canceled
                Log.d("TAG-PARAM", "Dynamic feature module installation canceled")
            }

            else -> {
                // Other states can be handled as needed
            }

        }
    }
}