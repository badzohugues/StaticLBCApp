package com.badzohugues.staticlbcapp.ui.home

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.badzohugues.staticlbcapp.common.BaseActivity
import com.badzohugues.staticlbcapp.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private lateinit var homeNavController: NavController

    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeNavController = (supportFragmentManager.findFragmentById(binding.homeNavHost.id) as NavHostFragment).navController
        setupActionBarWithNavController(homeNavController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return homeNavController.navigateUp() || super.onSupportNavigateUp()
    }
}