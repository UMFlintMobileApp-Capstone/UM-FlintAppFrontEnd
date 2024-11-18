package com.example.um_flintapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.um_flintapplication.databinding.ActivityMainBinding
import com.example.um_flintapplication.apiRequests.Retrofit
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.toolbar.title = "University of Michigan - Flint" // Set the toolbar title here

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Initialize AppBarConfiguration with top-level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

        // Set up ActionBar with NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Initialize ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.appBarMain.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        // Add DrawerListener to DrawerLayout and sync state
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        CoroutineScope(Dispatchers.IO).launch {
                val items = Retrofit.api.getNews(3)

                withContext(Dispatchers.Main) {
                    val layout = findViewById<LinearLayout>(R.id.NewsSection)

                    items.forEach { item ->
                        val textView = TextView(this@MainActivity)

                        textView.text = item.title
                        textView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
                        textView.setPadding(0, 8, 0, 0)

                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                            LinearLayout.LayoutParams.WRAP_CONTENT  // Height
                        )
                        textView.layoutParams = layoutParams

                        layout.addView(textView)
                    }
                }
            }
        }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Override onOptionsItemSelected to toggle the drawer when the hamburger icon is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun openAlertsPage(view: View) {
        val intent = Intent(this, AlertsActivity::class.java)
        startActivity(intent)
    }

    // Function to open the Sign In page
    fun openSignInPage(view: View) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    // Function to open the Events page
    fun openEventsPage(view: View) {
        val intent = Intent(this, EventsActivity::class.java)
        startActivity(intent)
    }

    // Function to open the News page
    fun openNewsPage(view: View) {
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
    }

    // Function to open the Maps page
    fun openMapsPage(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}
