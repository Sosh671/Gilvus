package com.soshdev.gilvus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.soshdev.gilvus.databinding.ActivityMainBinding
import com.soshdev.gilvus.ui.base.SharedViewModel
import com.soshdev.gilvus.util.Constants
import com.soshdev.gilvus.util.showSnackbar
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel by viewModel<SharedViewModel>()

    private val navController: NavController by lazy {
        findNavController(R.id.fragment_container)
    }

    private val topDestinations = setOf(
        R.id.home
    )

    private val appBarConfiguration by lazy {
        AppBarConfiguration(topDestinations, binding.drawerLayout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Sets up a [NavigationView] for use with a [NavController]. This will call
         * [android.view.MenuItem.onNavDestinationSelected] when a menu item is selected.
         *
         * The selected item in the NavigationView will automatically be updated when the destination
         * changes.
         */
        binding.navView.setupWithNavController(navController)

        setupDrawer()

        // todo for testing remove later
        binding.switchLayout.setOnClickListener { binding.drawerSwitch.toggle() }
//        binding.drawerSwitch.isChecked = sharedViewModel.host == Constants.emulatorLocalHost
//        binding.drawerSwitch.setOnCheckedChangeListener { _, _ -> sharedViewModel.toggleHostAndReconnect() }
        binding.drawerBtn.setOnClickListener { sharedViewModel.reconnect() }
    }

    fun showSnackbar(resId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
        binding.drawerSwitch.showSnackbar(resId, duration)
    }

    fun setupToolbar(toolbar: Toolbar, title: String?) {
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(
            navController,
            AppBarConfiguration.Builder(topDestinations)
                .setDrawerLayout(binding.drawerLayout)
                .build()
        )
        title?.let { toolbar.title = it }

        // override only if not at the top destination (toolbar icon is back arrow and not menu)
        if (!topDestinations.contains(navController.currentDestination?.id)) {
            toolbar.setNavigationOnClickListener {
                navController.navigateUp()
            }
        }
    }

    private fun setupDrawer() {
        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            return@setNavigationItemSelectedListener when (menuItem.itemId) {
//                R.id.exit -> {
//                    setDrawerState(open = false)
//                    viewModel.logout()
//                    true
//                }
                else -> {
                    if (!menuItem.isChecked) {
                        val options = NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setPopUpTo(R.id.home, false)
                        navController.navigate(menuItem.itemId, null, options.build())
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }
        binding.navView.getHeaderView(0).setOnClickListener {
            navController.currentDestination?.let {
                if (it.id != R.id.profile) {
                    navController.navigate(R.id.profile)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
