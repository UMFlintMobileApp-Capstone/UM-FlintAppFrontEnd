package com.example.um_flintapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.um_flintapplication.databinding.ActivityAnnouncementsBinding
import com.example.um_flintapplication.Announcement
import com.google.android.material.navigation.NavigationView

class AlertsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnnouncementsBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var adapter: AnnouncementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityAnnouncementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Announcements"

        // Initialize DrawerLayout and NavigationView
        setupNavigationDrawer()

        // Initialize the RecyclerView for announcements
        setupRecyclerView()

        // Load announcements into the RecyclerView
        val announcements = listOf(
            Announcement(
                title = "Campus Closed on Nov 24",
                description = "Due to severe weather, the campus will remain closed on November 24.",
                timestamp = "Posted on: 2024-11-17"
            ),
            Announcement(
                title = "Upcoming Event: Career Fair",
                description = "Join us for the career fair on November 30. Meet recruiters from top companies.",
                timestamp = "Posted on: 2024-11-15"
            ),
            Announcement(
                title = "Library Hours Updated",
                description = "The library will now be open from 7 AM to 10 PM on weekdays.",
                timestamp = "Posted on: 2024-11-10"
            )
        )
        adapter.submitList(announcements)
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
//                R.id.nav_resources_academic_calendar -> {
//                    // Navigate to Academic Calendar page
//                    val intent = Intent(this, AcademicCalendarActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
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

    private fun setupRecyclerView() {
        binding.recyclerViewAnnouncements.layoutManager = LinearLayoutManager(this)
        adapter = AnnouncementAdapter()
        binding.recyclerViewAnnouncements.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}