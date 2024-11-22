package com.example.um_flintapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.um_flintapplication.databinding.ActivityDepartmentInformationBinding
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup




class DepartmentInformationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityDepartmentInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDepartmentInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Academic Calendar"

        setupNavigationDrawer()
    }

    private fun setupNavigationDrawer() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Initialize ActionBarDrawerToggle for the navigation drawer
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up NavigationView to work with navigation destinations
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Navigate to the Home page
                    val intent =
                        Intent(this, MainActivity::class.java) // Replace with your Home activity
                    startActivity(intent)
                    true
                }
                R.id.nav_resources_academic_calendar -> {
                    // Navigate to Academic Calendar page
                    val intent = Intent(this, AcademicCalendar::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_resources_departments -> {
                    // Navigate to Departments page
                    val intent = Intent(this, DepartmentInformationActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_resources_maps -> {
                    // Navigate to Maps page
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
//                R.id.nav_scheduling_reserve_room -> {
//                    // Navigate to Reserve Room page
//                    val intent = Intent(this, ReserveRoomActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
                R.id.nav_announcements -> {
                    // Navigate to Announcements page
                    val intent = Intent(this, AlertsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun fetchDepartmentData() {

        lifecycleScope.launch {
            val departmentData = scrapeDepartmentData() // Fetch data with scraping function
            displayData(departmentData) // Display the fetched data in the UI
        }
    }

    private suspend fun scrapeDepartmentData(): List<String> = withContext(Dispatchers.IO) {
        val departmentList = mutableListOf<String>()
        try {
            // Connect to the web page and parse content
            val doc = Jsoup.connect("https://www.umflint.edu/departments/").get()
            val elements = doc.select("CSS_SELECTOR_OF_DEPARTMENT_INFO") // Select elements

            for (element in elements) {
                departmentList.add(element.text()) // Add text to list
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        departmentList
    }

    private fun displayData(departmentData: List<String>) {
        // Access your LinearLayout where you want to add dynamic data
        val containerLayout = findViewById<LinearLayout>(R.id.containerLayout) // Replace with actual ID

        departmentData.forEach { data ->
            // Create a new TextView for each piece of data
            val textView = TextView(this)
            textView.text = data
            textView.setTextColor(resources.getColor(R.color.white, theme))
            textView.setTextSize(18f)
            textView.setPadding(16, 16, 16, 16)

            // Optional: add background, margins, etc., similar to XML design
            textView.setBackgroundColor(resources.getColor(R.color.blue, theme))
            textView.elevation = 4f

            // Add TextView to the layout
            containerLayout.addView(textView)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}