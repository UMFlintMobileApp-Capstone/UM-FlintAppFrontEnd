package com.example.um_flintapplication

import android.os.Bundle
import android.view.Menu
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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class DepartmentInformationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDepartmentInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDepartmentInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDepartmentInformation.toolbar)

        binding.appBarDepartmentInformation.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_department_information)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        // Start the scraping task
        fetchDepartmentData()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.department_information, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_department_information)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}