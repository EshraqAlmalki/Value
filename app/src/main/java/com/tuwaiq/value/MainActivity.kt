package com.tuwaiq.value

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuwaiq.value.authentication.logIn.LoginFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navController = findNavController(R.id.fragment_container)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> bottomNavigationView.visibility = View.GONE
                R.id.registerFragment -> bottomNavigationView.visibility = View.GONE
                R.id.nextRegisterFragment -> bottomNavigationView.visibility = View.GONE
               R.id.homePageFragment -> bottomNavigationView.visibility = View.VISIBLE
            }



            bottomNavigationView.setupWithNavController(navController)


        }

    }
}