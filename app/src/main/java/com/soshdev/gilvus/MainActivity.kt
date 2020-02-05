package com.soshdev.gilvus

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.soshdev.gilvus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy {
        findNavController(R.id.fragment_container)
    }

    private val topDestinations = setOf(
        R.id.nav_home
    )

    private val appBarConfiguration by lazy {
        AppBarConfiguration(topDestinations, binding.drawerLayout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        binding.navView.setupWithNavController(navController)
        setupDrawer()
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
        if (!topDestinations.contains(findNavController(R.id.fragment_container).currentDestination?.id)) {
            toolbar.setNavigationOnClickListener {
                findNavController(R.id.fragment_container).navigateUp()
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
                            .setPopUpTo(R.id.nav_home, false)
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
        val navController = findNavController(R.id.nav_view)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
