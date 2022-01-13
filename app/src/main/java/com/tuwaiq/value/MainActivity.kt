package com.tuwaiq.value

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.*
import android.widget.TableLayout
import androidx.datastore.core.DataStore
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.tuwaiq.value.authentication.logIn.LoginFragment
import com.tuwaiq.value.dataStore.DatastorePreferences
import java.util.prefs.Preferences

class MainActivity : AppCompatActivity() {

    companion object{

        fun intent(context: Context):Intent{
            return Intent(context, MainActivity::class.java)
        }
    }


//


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

//            val viewPager = findViewById<ViewPager>(R.id.viewPager)
//            viewPager.adapter = PageAdapter(supportFragmentManager)
//
//            val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
//            tabLayout.setupWithViewPager(viewPager)
//


        }


}